package com.example.bookstore.controllers.imagecontrollers;

import com.example.bookstore.model.Publisher;
import com.example.bookstore.service.ImageService;
import com.example.bookstore.service.PublisherService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class PublisherImageControllerTest {

    private final static String errorPage = "error/400error";

    @Mock
    ImageService imageService;

    @Mock
    PublisherService publisherService;

    @InjectMocks
    PublisherImageController controller;

    MockMvc mockMvc;

    Publisher publisherMock;

    MockMultipartFile multipartFile =
            new MockMultipartFile("imagefile", "testing.txt", "text/plain",
                    "BookImage".getBytes());

    @BeforeEach
    void setUp() {
        publisherMock = Publisher.builder().build();

        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    void showUploadForm() throws Exception{
        when(publisherService.findById(anyLong())).thenReturn(publisherMock);

        mockMvc.perform(get("/publisher/1/newImage"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("publisher"))
                .andExpect(view().name("publisher/imageuploadform"));

        verify(publisherService, times(1)).findById(anyLong());
    }

    @Test
    public void getImageFormWithBrokenPublisherId() throws Exception {
        mockMvc.perform(get("/publisher/1/newImage"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("exception"))
                .andExpect(view().name(errorPage));

        verify(publisherService, times(1)).findById(anyLong());
    }

    @Test
    void handleImagePost() throws Exception{
        when(publisherService.findById(anyLong())).thenReturn(publisherMock);

        mockMvc.perform(multipart("/publisher/1/newImage").file(multipartFile))
                .andExpect(status().is3xxRedirection())
                .andExpect(header().string("Location", "/publisher/1/show"));

        verify(imageService, times(1)).savePublisherImage(any(), any());
    }

    @Test
    public void handleImagePostWithoutCustomerId() throws Exception {
        mockMvc.perform(multipart("/publisher/1/newImage").file(multipartFile))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("exception"))
                .andExpect(view().name(errorPage));

        verify(publisherService, times(1)).findById(anyLong());
    }

    @Test
    void renderImageFromDB() throws Exception{
        String s = "fake image text";
        Byte[] bytesBoxed = new Byte[s.getBytes().length];

        int i = 0;
        for (byte primByte : s.getBytes()){
            bytesBoxed[i++] = primByte;
        }

        publisherMock.setImage(bytesBoxed);
        when(publisherService.findById(anyLong())).thenReturn(publisherMock);

        //when
        MockHttpServletResponse response = mockMvc.perform(get("/publisher/1/image"))
                .andExpect(status().isOk())
                .andReturn().getResponse();

        assertEquals(s.getBytes().length, response.getContentAsByteArray().length);
    }
}