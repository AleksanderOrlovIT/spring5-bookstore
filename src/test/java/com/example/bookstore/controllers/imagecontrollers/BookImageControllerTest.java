package com.example.bookstore.controllers.imagecontrollers;

import com.example.bookstore.controllers.ControllerExceptionHandler;
import com.example.bookstore.model.Author;
import com.example.bookstore.model.Book;
import com.example.bookstore.service.AuthorService;
import com.example.bookstore.service.BookService;
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
class BookImageControllerTest {

    private final static String errorPage = "error/400error";

    @Mock
    ImageService imageService;

    @Mock
    BookService bookService;

    @InjectMocks
    BookImageController controller;

    MockMvc mockMvc;

    Book bookMock;

    MockMultipartFile multipartFile =
            new MockMultipartFile("imagefile", "testing.txt", "text/plain",
                    "BookImage".getBytes());

    @BeforeEach
    void setUp() {
        bookMock = Book.builder().build();

        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    void showUploadForm() throws Exception{
        when(bookService.findById(anyLong())).thenReturn(bookMock);

        mockMvc.perform(get("/book/1/newImage"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("book"))
                .andExpect(view().name("book/imageuploadform"));

        verify(bookService, times(1)).findById(anyLong());
    }

    @Test
    public void getImageFormWithBrokenBookId() throws Exception {
        mockMvc.perform(get("/book/1/newImage"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("exception"))
                .andExpect(view().name(errorPage));

        verify(bookService, times(1)).findById(anyLong());
    }

    @Test
    void handleImagePost() throws Exception{
        when(bookService.findById(anyLong())).thenReturn(bookMock);

        mockMvc.perform(multipart("/book/1/newImage").file(multipartFile))
                .andExpect(status().is3xxRedirection())
                .andExpect(header().string("Location", "/book/1/show"));

        verify(imageService, times(1)).saveBookImage(any(), any());
    }

    @Test
    public void handleImagePostWithoutBookId() throws Exception {
        mockMvc.perform(multipart("/book/1/newImage").file(multipartFile))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("exception"))
                .andExpect(view().name(errorPage));

        verify(bookService, times(1)).findById(anyLong());
    }

    @Test
    void renderImageFromDB() throws Exception{
        String s = "fake image text";
        Byte[] bytesBoxed = new Byte[s.getBytes().length];

        int i = 0;
        for (byte primByte : s.getBytes()){
            bytesBoxed[i++] = primByte;
        }

        bookMock.setImage(bytesBoxed);
        when(bookService.findById(anyLong())).thenReturn(bookMock);

        //when
        MockHttpServletResponse response = mockMvc.perform(get("/book/1/image"))
                .andExpect(status().isOk())
                .andReturn().getResponse();

        assertEquals(s.getBytes().length, response.getContentAsByteArray().length);
    }
}