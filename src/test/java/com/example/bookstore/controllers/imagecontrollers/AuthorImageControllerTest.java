package com.example.bookstore.controllers.imagecontrollers;

import com.example.bookstore.controllers.ControllerExceptionHandler;
import com.example.bookstore.model.Author;
import com.example.bookstore.service.AuthorService;
import com.example.bookstore.service.ImageService;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class AuthorImageControllerTest {

    private final static String errorPage = "error/400error";

    @Mock
    ImageService imageService;

    @Mock
    AuthorService authorService;

    @InjectMocks
    AuthorImageController controller;

    MockMvc mockMvc;

    Author authorMock;

    MockMultipartFile multipartFile =
            new MockMultipartFile("imagefile", "testing.txt", "text/plain",
                    "AuthorImage".getBytes());

    @BeforeEach
    public void setUp() throws Exception {
        authorMock = Author.builder().build();

        mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .setControllerAdvice(new ControllerExceptionHandler())
                .build();
    }

    @Test
    public void showImageForm() throws Exception {
        when(authorService.findById(anyLong())).thenReturn(authorMock);

        mockMvc.perform(get("/author/1/newImage"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("author"))
                .andExpect(view().name("author/imageuploadform"));

        verify(authorService, times(1)).findById(anyLong());
    }

    @Test
    public void getImageFormWithBrokenAuthorId() throws Exception {
        mockMvc.perform(get("/author/1/newImage"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("exception"))
                .andExpect(view().name(errorPage));

        verify(authorService, times(1)).findById(anyLong());
    }

    @Test
    public void handleImagePost() throws Exception {
        when(authorService.findById(anyLong())).thenReturn(authorMock);

        mockMvc.perform(multipart("/author/1/newImage").file(multipartFile))
                .andExpect(status().is3xxRedirection())
                .andExpect(header().string("Location", "/author/1/show"));

        verify(imageService, times(1)).saveAuthorImage(any(), any());
    }

    @Test
    public void handleImagePostWithoutAuthorId() throws Exception {
        mockMvc.perform(multipart("/author/1/newImage").file(multipartFile))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("exception"))
                .andExpect(view().name(errorPage));

        verify(authorService, times(1)).findById(anyLong());
    }


    @Test
    public void renderImageFromDB() throws Exception {
        String s = "fake image text";
        Byte[] bytesBoxed = new Byte[s.getBytes().length];

        int i = 0;
        for (byte primByte : s.getBytes()){
            bytesBoxed[i++] = primByte;
        }

        authorMock.setImage(bytesBoxed);
        when(authorService.findById(anyLong())).thenReturn(authorMock);

        //when
        MockHttpServletResponse response = mockMvc.perform(get("/author/1/image"))
                .andExpect(status().isOk())
                .andReturn().getResponse();

        assertEquals(s.getBytes().length, response.getContentAsByteArray().length);
    }

    @Test
    public void testGetImageNumberFormatException() throws Exception{
        mockMvc.perform(get("/author/asdf/image"))
                .andExpect(status().isBadRequest())
                .andExpect(view().name( errorPage));
    }
}