package com.example.bookstore.controllers.imagecontrollers;

import com.example.bookstore.model.Book;
import com.example.bookstore.model.Customer;
import com.example.bookstore.service.BookService;
import com.example.bookstore.service.CustomerService;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class CustomerImageControllerTest {

    private final static String errorPage = "error/400error";

    @Mock
    ImageService imageService;

    @Mock
    CustomerService customerService;

    @InjectMocks
    CustomerImageController controller;

    MockMvc mockMvc;

    Customer customerMock;

    MockMultipartFile multipartFile =
            new MockMultipartFile("imagefile", "testing.txt", "text/plain",
                    "BookImage".getBytes());

    @BeforeEach
    void setUp() {
        customerMock = Customer.builder().build();

        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    void showUploadForm() throws Exception{
        when(customerService.findById(anyLong())).thenReturn(customerMock);

        mockMvc.perform(get("/customer/1/newImage"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("customer"))
                .andExpect(view().name("customer/imageuploadform"));

        verify(customerService, times(1)).findById(anyLong());
    }

    @Test
    public void getImageFormWithBrokenCustomerId() throws Exception {
        mockMvc.perform(get("/customer/1/newImage"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("exception"))
                .andExpect(view().name(errorPage));

        verify(customerService, times(1)).findById(anyLong());
    }

    @Test
    void handleImagePost() throws Exception{
        when(customerService.findById(anyLong())).thenReturn(customerMock);

        mockMvc.perform(multipart("/customer/1/newImage").file(multipartFile))
                .andExpect(status().is3xxRedirection())
                .andExpect(header().string("Location", "/customer/1/show"));

        verify(imageService, times(1)).saveCustomerImage(any(), any());
    }

    @Test
    public void handleImagePostWithoutCustomerId() throws Exception {
        mockMvc.perform(multipart("/customer/1/newImage").file(multipartFile))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("exception"))
                .andExpect(view().name(errorPage));

        verify(customerService, times(1)).findById(anyLong());
    }

    @Test
    void renderImageFromDB() throws Exception{
        String s = "fake image text";
        Byte[] bytesBoxed = new Byte[s.getBytes().length];

        int i = 0;
        for (byte primByte : s.getBytes()){
            bytesBoxed[i++] = primByte;
        }

        customerMock.setImage(bytesBoxed);
        when(customerService.findById(anyLong())).thenReturn(customerMock);

        //when
        MockHttpServletResponse response = mockMvc.perform(get("/customer/1/image"))
                .andExpect(status().isOk())
                .andReturn().getResponse();

        assertEquals(s.getBytes().length, response.getContentAsByteArray().length);
    }
}