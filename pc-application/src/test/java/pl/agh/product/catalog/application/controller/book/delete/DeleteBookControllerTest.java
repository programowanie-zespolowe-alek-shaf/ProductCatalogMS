package pl.agh.product.catalog.application.controller.book.delete;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import pl.agh.product.catalog.application.dto.BookRequestDTO;
import pl.agh.product.catalog.mysql.entity.Book;
import pl.agh.product.catalog.mysql.repository.BookRepository;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static pl.agh.product.catalog.application.config.TestUtils.*;

@RunWith(SpringRunner.class)
@SpringBootTest()
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class DeleteBookControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @WithMockUser(username = "john", roles = {"ADMIN"})
    @Test
    public void createAndDeleteSuccessTest() throws Exception {
        BookRequestDTO bookRequestDTO = new BookRequestDTO();
        bookRequestDTO.setTitle("A");
        bookRequestDTO.setAuthor("A");
        bookRequestDTO.setCategoryId(1);
        bookRequestDTO.setAvailable(true);
        bookRequestDTO.setPrice(20.3464f);
        bookRequestDTO.setRecommended(true);
        String requestJson = mapObjectToStringJson(bookRequestDTO, objectMapper);

        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.post("/books").contentType(APPLICATION_JSON_UTF8)
                .content(requestJson))
                .andExpect(status().is(201))
                .andExpect(jsonPath("id").isNumber())
                .andReturn();

        Long bookId = getIdFromResponse(mvcResult);
        Book bookBeforeDeleting = bookRepository.findById(bookId).orElse(null);
        assertNotNull(bookBeforeDeleting);

        mvc.perform(MockMvcRequestBuilders.delete("/books/" + bookId))
                .andExpect(status().is(204));

        Book bookAfterDeleting = bookRepository.findById(bookId).orElse(null);
        assertNull(bookAfterDeleting);
    }

    @WithMockUser(username = "john", roles = {"ADMIN"})
    @Test
    public void notFoundTest() throws Exception {
        mvc.perform(MockMvcRequestBuilders.delete("/books/10"))
                .andExpect(status().is(404));
    }
}
