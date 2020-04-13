package pl.agh.product.catalog.application.controller.book.update;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import pl.agh.product.catalog.mysql.entity.Book;
import pl.agh.product.catalog.mysql.repository.BookRepository;

import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest()
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class UpdateBookImageControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private BookRepository bookRepository;

    @Test
    public void successTest() throws Exception {
        Book bookBefore = bookRepository.findById(1L).orElseThrow(null);

        mvc.perform(MockMvcRequestBuilders.patch("/books/1")
                .param("photoUrl", "newUrl"))
                .andExpect(status().is(200))
                .andExpect(jsonPath("id").value("1"))
                .andExpect(jsonPath("title").value("Nineteen Eighty-Four"))
                .andExpect(jsonPath("author").value("George Orwell"))
                .andExpect(jsonPath("category.id").value("1"))
                .andExpect(jsonPath("category.name").value("Novel"))
                .andExpect(jsonPath("year").value("1949"))
                .andExpect(jsonPath("photoUrl").value("newUrl"))
                .andExpect(jsonPath("description").value(nullValue()))
                .andExpect(jsonPath("available").value("true"))
                .andExpect(jsonPath("price").value("21.37"));

        Book book = bookRepository.findById(1L).orElse(null);
        assertNotNull(book);
        assertEquals(book.getPhotoUrl(), "newUrl");

        bookRepository.save(bookBefore);
    }

    @Test
    public void bookWithSpecifiedIdDoesNotExistTest() throws Exception {
        mvc.perform(MockMvcRequestBuilders.patch("/books/10")
                .param("photoUrl", "newUrl"))
                .andExpect(status().is(404));
    }
}
