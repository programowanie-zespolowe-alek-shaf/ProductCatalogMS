package pl.agh.product.catalog.application.controller.book.update;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import pl.agh.product.catalog.application.dto.BookRequestDTO;
import pl.agh.product.catalog.mysql.entity.Book;
import pl.agh.product.catalog.mysql.entity.Category;
import pl.agh.product.catalog.mysql.repository.BookRepository;

import java.nio.charset.Charset;

import static junit.framework.TestCase.assertTrue;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static pl.agh.product.catalog.application.config.TestUtils.mapObjectToStringJson;

@RunWith(SpringRunner.class)
@SpringBootTest()
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class UpdateBookControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private BookRepository bookRepository;

    private static final MediaType APPLICATION_JSON_UTF8 = new MediaType(MediaType.APPLICATION_JSON.getType(),
            MediaType.APPLICATION_JSON.getSubtype(), Charset.forName("utf8"));

    @Test
    public void successUpdateBookTest() throws Exception {
        Book bookBefore = bookRepository.findById(1L).orElseThrow(null);

        BookRequestDTO bookRequestDTO = new BookRequestDTO();
        bookRequestDTO.setTitle("A");
        bookRequestDTO.setAuthor("A");
        bookRequestDTO.setCategory(new Category(1L, "someName")); //only id is important
        bookRequestDTO.setYear(2000);
        bookRequestDTO.setPhotoUrl("url");
        bookRequestDTO.setDescription("desc");
        bookRequestDTO.setAvailable(true);
        bookRequestDTO.setPrice(20.3f);
        bookRequestDTO.setRecommended(false);
        bookRequestDTO.setNumPages(321);
        bookRequestDTO.setCoverType(Book.CoverType.PAPERBACK);

        String requestJson = mapObjectToStringJson(bookRequestDTO);

        mvc.perform(MockMvcRequestBuilders.put("/books/1").contentType(APPLICATION_JSON_UTF8)
                .content(requestJson))
                .andExpect(status().is(200))
                .andExpect(jsonPath("id").value("1"))
                .andExpect(jsonPath("title").value("A"))
                .andExpect(jsonPath("author").value("A"))
                .andExpect(jsonPath("category.id").value("1"))
                .andExpect(jsonPath("category.name").value("Novel"))
                .andExpect(jsonPath("year").value("2000"))
                .andExpect(jsonPath("photoUrl").value("url"))
                .andExpect(jsonPath("description").value("desc"))
                .andExpect(jsonPath("available").value("true"))
                .andExpect(jsonPath("price").value("20.3"))
                .andExpect(jsonPath("numPages").value("321"))
                .andExpect(jsonPath("coverType").value("PAPERBACK"));

        Book book = bookRepository.findById(1L).orElse(null);
        assertNotNull(book);
        assertEquals(book.getId(), 1L, 0.01);
        assertEquals(book.getTitle(), "A");
        assertEquals(book.getAuthor(), "A");
        assertEquals(book.getCategory().getId(), 1L, 0.01);
        assertEquals(book.getCategory().getName(), "Novel");
        assertEquals(book.getYear(), 2000, 0.01);
        assertEquals(book.getPhotoUrl(), "url");
        assertEquals(book.getDescription(), "desc");
        assertTrue(book.getAvailable());
        assertEquals(book.getPrice(), 20.3f, 0.01);
        assertEquals(book.getNumPages(), 321, 0.01);
        assertEquals(book.getCoverType(), Book.CoverType.PAPERBACK);

        bookRepository.save(bookBefore);
    }

    @Test
    public void successMinArgsTest() throws Exception {
        Book bookBefore = bookRepository.findById(1L).orElseThrow(null);

        BookRequestDTO bookRequestDTO = new BookRequestDTO();
        bookRequestDTO.setTitle("A");
        bookRequestDTO.setAuthor("A");
        bookRequestDTO.setCategory(new Category(1L, "someName")); //only id is important
        bookRequestDTO.setAvailable(true);
        bookRequestDTO.setPrice(20.3464f);
        bookRequestDTO.setRecommended(false);

        String requestJson = mapObjectToStringJson(bookRequestDTO);

        mvc.perform(MockMvcRequestBuilders.put("/books/1").contentType(APPLICATION_JSON_UTF8)
                .content(requestJson))
                .andExpect(status().is(200))
                .andExpect(jsonPath("id").value("1"))
                .andExpect(jsonPath("title").value("A"))
                .andExpect(jsonPath("author").value("A"))
                .andExpect(jsonPath("category.id").value("1"))
                .andExpect(jsonPath("category.name").value("Novel"))
                .andExpect(jsonPath("year").value(nullValue()))
                .andExpect(jsonPath("photoUrl").value(nullValue()))
                .andExpect(jsonPath("description").value(nullValue()))
                .andExpect(jsonPath("available").value("true"))
                .andExpect(jsonPath("price").value("20.35"));

        Book book = bookRepository.findById(1L).orElse(null);
        assertNotNull(book);
        assertEquals(book.getId(), 1L, 0.01);
        assertEquals(book.getTitle(), "A");
        assertEquals(book.getAuthor(), "A");
        assertEquals(book.getCategory().getId(), 1L, 0.01);
        assertEquals(book.getCategory().getName(), "Novel");
        assertNull(book.getYear());
        assertNull(book.getPhotoUrl());
        assertNull(book.getDescription());
        assertTrue(book.getAvailable());
        assertEquals(book.getPrice(), 20.35f, 0.01);

        bookRepository.save(bookBefore);
    }


    @Test
    public void noTitleFailedTest() throws Exception {
        BookRequestDTO bookRequestDTO = new BookRequestDTO();
        bookRequestDTO.setAuthor("A");
        bookRequestDTO.setCategory(new Category(1L, "someName")); //only id is important
        bookRequestDTO.setAvailable(true);
        bookRequestDTO.setPrice(20.3464f);
        bookRequestDTO.setRecommended(false);

        String requestJson = mapObjectToStringJson(bookRequestDTO);

        mvc.perform(MockMvcRequestBuilders.put("/books/2").contentType(APPLICATION_JSON_UTF8)
                .content(requestJson))
                .andExpect(status().is(400))
                .andExpect(jsonPath("error").value("title cannot be null"));
    }

    @Test
    public void noPriceFailedTest() throws Exception {
        BookRequestDTO bookRequestDTO = new BookRequestDTO();
        bookRequestDTO.setTitle("A");
        bookRequestDTO.setAuthor("A");
        bookRequestDTO.setCategory(new Category(1L, "someName")); //only id is important
        bookRequestDTO.setAvailable(true);
        bookRequestDTO.setRecommended(false);

        String requestJson = mapObjectToStringJson(bookRequestDTO);

        mvc.perform(MockMvcRequestBuilders.put("/books/3").contentType(APPLICATION_JSON_UTF8)
                .content(requestJson))
                .andExpect(status().is(400))
                .andExpect(jsonPath("error").value("price cannot be null"));
    }

    @Test
    public void priceBelowZeroFailedTest() throws Exception {
        BookRequestDTO bookRequestDTO = new BookRequestDTO();
        bookRequestDTO.setTitle("A");
        bookRequestDTO.setAuthor("A");
        bookRequestDTO.setCategory(new Category(1L, "someName")); //only id is important
        bookRequestDTO.setAvailable(true);
        bookRequestDTO.setPrice(-20.3464f);
        bookRequestDTO.setRecommended(false);

        String requestJson = mapObjectToStringJson(bookRequestDTO);

        mvc.perform(MockMvcRequestBuilders.put("/books/1").contentType(APPLICATION_JSON_UTF8)
                .content(requestJson))
                .andExpect(status().is(400))
                .andExpect(jsonPath("error").value("price must be greater than zero"));
    }

    @Test
    public void categoryDoesNotExistFailedTest() throws Exception {
        BookRequestDTO bookRequestDTO = new BookRequestDTO();
        bookRequestDTO.setTitle("A");
        bookRequestDTO.setAuthor("A");
        bookRequestDTO.setCategory(new Category(7L, "someName")); //only id is important
        bookRequestDTO.setAvailable(true);
        bookRequestDTO.setPrice(20.3464f);
        bookRequestDTO.setRecommended(false);

        String requestJson = mapObjectToStringJson(bookRequestDTO);

        mvc.perform(MockMvcRequestBuilders.put("/books/3").contentType(APPLICATION_JSON_UTF8)
                .content(requestJson))
                .andExpect(status().is(400))
                .andExpect(jsonPath("error").value("category not found"));
    }

    @Test
    public void bookWithSpecifiedIdDoesNotExistTest() throws Exception {
        BookRequestDTO bookRequestDTO = new BookRequestDTO();
        bookRequestDTO.setTitle("A");
        bookRequestDTO.setAuthor("A");
        bookRequestDTO.setCategory(new Category(1L, "someName")); //only id is important
        bookRequestDTO.setYear(2000);
        bookRequestDTO.setPhotoUrl("url");
        bookRequestDTO.setDescription("desc");
        bookRequestDTO.setAvailable(true);
        bookRequestDTO.setPrice(20.3f);
        bookRequestDTO.setRecommended(false);

        String requestJson = mapObjectToStringJson(bookRequestDTO);

        mvc.perform(MockMvcRequestBuilders.put("/books/10").contentType(APPLICATION_JSON_UTF8)
                .content(requestJson))
                .andExpect(status().is(404));
    }

    @Test
    public void recommendedNotProvidedFailedTest() throws Exception {
        BookRequestDTO bookRequestDTO = new BookRequestDTO();
        bookRequestDTO.setTitle("A");
        bookRequestDTO.setAuthor("A");
        bookRequestDTO.setCategory(new Category(7L, "someName")); //only id is important
        bookRequestDTO.setAvailable(true);
        bookRequestDTO.setPrice(20.3464f);

        String requestJson = mapObjectToStringJson(bookRequestDTO);

        mvc.perform(MockMvcRequestBuilders.post("/books").contentType(APPLICATION_JSON_UTF8)
                .content(requestJson))
                .andExpect(status().is(400))
                .andExpect(jsonPath("error").value("recommended cannot be null"));
    }
}
