package pl.agh.product.catalog.application.controller.book.create;

import com.jayway.jsonpath.JsonPath;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import pl.agh.product.catalog.application.dto.BookRequestDTO;
import pl.agh.product.catalog.common.util.StringUtils;
import pl.agh.product.catalog.mysql.entity.Book;
import pl.agh.product.catalog.mysql.entity.Category;
import pl.agh.product.catalog.mysql.repository.BookRepository;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;

import static junit.framework.TestCase.assertTrue;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest()
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class AddBookControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private BookRepository bookRepository;

    private static final MediaType APPLICATION_JSON_UTF8 = new MediaType(MediaType.APPLICATION_JSON.getType(),
            MediaType.APPLICATION_JSON.getSubtype(), Charset.forName("utf8"));

    @Test
    public void successAllArgsTest() throws Exception {
        BookRequestDTO bookRequestDTO = new BookRequestDTO();
        bookRequestDTO.setTitle("A");
        bookRequestDTO.setAuthor("A");
        bookRequestDTO.setCategory(new Category(1L, "someName")); //only id is important
        bookRequestDTO.setYear(2000);
        bookRequestDTO.setPhotoUrl("url");
        bookRequestDTO.setDescription("desc");
        bookRequestDTO.setAvailable(true);
        bookRequestDTO.setPrice(20.3f);

        String requestJson = StringUtils.mapObjectToStringJson(bookRequestDTO);

        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.post("/books").contentType(APPLICATION_JSON_UTF8)
                .content(requestJson))
                .andExpect(status().is(201))
                .andExpect(jsonPath("id").isNumber())
                .andExpect(jsonPath("title").value("A"))
                .andExpect(jsonPath("author").value("A"))
                .andExpect(jsonPath("category.id").value("1"))
                .andExpect(jsonPath("category.name").value("someName"))
                .andExpect(jsonPath("year").value("2000"))
                .andExpect(jsonPath("photoUrl").value("url"))
                .andExpect(jsonPath("description").value("desc"))
                .andExpect(jsonPath("available").value("true"))
                .andExpect(jsonPath("price").value("20.3"))
                .andReturn();

        Long bookId = getIdFromResponse(mvcResult);
        Book book = bookRepository.findById(bookId).orElse(null);
        assertNotNull(book);
        assertEquals(book.getId(), bookId);
        assertEquals(book.getTitle(), "A");
        assertEquals(book.getAuthor(), "A");
        assertEquals(book.getCategory().getId(), 1L, 0.01);
        assertEquals(book.getCategory().getName(), "Novel");
        assertEquals(book.getYear(), 2000, 0.01);
        assertEquals(book.getPhotoUrl(), "url");
        assertEquals(book.getDescription(), "desc");
        assertTrue(book.getAvailable());
        assertEquals(book.getPrice(), 20.3f, 0.01);

        bookRepository.delete(book);
    }

    @Test
    public void successMinArgsTest() throws Exception {
        BookRequestDTO bookRequestDTO = new BookRequestDTO();
        bookRequestDTO.setTitle("A");
        bookRequestDTO.setAuthor("A");
        bookRequestDTO.setCategory(new Category(1L, "someName")); //only id is important
        bookRequestDTO.setAvailable(true);
        bookRequestDTO.setPrice(20.3464f);

        String requestJson = StringUtils.mapObjectToStringJson(bookRequestDTO);

        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.post("/books").contentType(APPLICATION_JSON_UTF8)
                .content(requestJson))
                .andExpect(status().is(201))
                .andExpect(jsonPath("id").isNumber())
                .andExpect(jsonPath("title").value("A"))
                .andExpect(jsonPath("author").value("A"))
                .andExpect(jsonPath("category.id").value("1"))
                .andExpect(jsonPath("category.name").value("someName"))
                .andExpect(jsonPath("year").value(nullValue()))
                .andExpect(jsonPath("photoUrl").value(nullValue()))
                .andExpect(jsonPath("description").value(nullValue()))
                .andExpect(jsonPath("available").value("true"))
                .andExpect(jsonPath("price").value("20.35"))
                .andReturn();

        Long bookId = getIdFromResponse(mvcResult);
        Book book = bookRepository.findById(bookId).orElse(null);
        assertNotNull(book);
        assertEquals(book.getId(), bookId);
        assertEquals(book.getTitle(), "A");
        assertEquals(book.getAuthor(), "A");
        assertEquals(book.getCategory().getId(), 1L, 0.01);
        assertEquals(book.getCategory().getName(), "Novel");
        assertNull(book.getYear());
        assertNull(book.getPhotoUrl());
        assertNull(book.getDescription());
        assertTrue(book.getAvailable());
        assertEquals(book.getPrice(), 20.35f, 0.01);

        bookRepository.delete(book);
    }


    @Test
    public void noTitleFailedTest() throws Exception {
        BookRequestDTO bookRequestDTO = new BookRequestDTO();
        bookRequestDTO.setAuthor("A");
        bookRequestDTO.setCategory(new Category(1L, "someName")); //only id is important
        bookRequestDTO.setAvailable(true);
        bookRequestDTO.setPrice(20.3464f);

        String requestJson = StringUtils.mapObjectToStringJson(bookRequestDTO);

        mvc.perform(MockMvcRequestBuilders.post("/books").contentType(APPLICATION_JSON_UTF8)
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

        String requestJson = StringUtils.mapObjectToStringJson(bookRequestDTO);

        mvc.perform(MockMvcRequestBuilders.post("/books").contentType(APPLICATION_JSON_UTF8)
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

        String requestJson = StringUtils.mapObjectToStringJson(bookRequestDTO);

        mvc.perform(MockMvcRequestBuilders.post("/books").contentType(APPLICATION_JSON_UTF8)
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

        String requestJson = StringUtils.mapObjectToStringJson(bookRequestDTO);

        mvc.perform(MockMvcRequestBuilders.post("/books").contentType(APPLICATION_JSON_UTF8)
                .content(requestJson))
                .andExpect(status().is(400))
                .andExpect(jsonPath("error").value("category not found"));
    }


    private Long getIdFromResponse(MvcResult mvcResult) throws UnsupportedEncodingException {
        String response = mvcResult.getResponse().getContentAsString();
        Integer id = JsonPath.parse(response).read("id");
        return id.longValue();
    }
}
