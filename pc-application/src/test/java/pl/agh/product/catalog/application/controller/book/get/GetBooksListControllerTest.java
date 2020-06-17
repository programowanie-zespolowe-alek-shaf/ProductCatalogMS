package pl.agh.product.catalog.application.controller.book.get;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import pl.agh.product.catalog.mysql.entity.Book;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.nullValue;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest()
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class GetBooksListControllerTest {

    @Autowired
    private MockMvc mvc;

    @Test
    public void noLimitAndOffsetTest() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/books"))
                .andExpect(status().is(400))
                .andExpect(status().reason("Required int parameter 'limit' is not present"));
    }

    @Test
    public void noOffsetTest() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/books")
                .param("limit", "0"))
                .andExpect(status().is(400))
                .andExpect(status().reason("Required int parameter 'offset' is not present"));
    }

    @Test
    public void onlyLimitAndOffsetTest() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/books")
                .param("offset", "0")
                .param("limit", "10"))
                .andExpect(status().is(200))
                .andExpect(jsonPath("list[0].id").value("1"))
                .andExpect(jsonPath("list[0].title").value("Nineteen Eighty-Four"))
                .andExpect(jsonPath("list[0].author").value("George Orwell"))
                .andExpect(jsonPath("list[0].category.id").value("1"))
                .andExpect(jsonPath("list[0].category.name").value("Novel"))
                .andExpect(jsonPath("list[0].year").value("1949"))
                .andExpect(jsonPath("list[0].photoUrl").value(nullValue()))
                .andExpect(jsonPath("list[0].description").value(nullValue()))
                .andExpect(jsonPath("list[0].available").value("true"))
                .andExpect(jsonPath("list[0].price").value("21.37"))
                .andExpect(jsonPath("list[1].id").value("2"))
                .andExpect(jsonPath("list[2].id").value("3"))
                .andExpect(jsonPath("count").value("3"));
    }


    @Test
    public void listReturnsOnlyRecommendedTest() throws Exception {
       mvc.perform(MockMvcRequestBuilders.get("/books")
                .param("offset", "0")
                .param("limit", "10")
                .param("recommended", "true"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("list[0].id").value("1"))
                .andExpect(jsonPath("count").value("1"));
    }

    @Test
    public void listReturnsSortedResultsTest() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/books")
                .param("offset", "0")
                .param("limit", "10")
                .param("sort", "title;asc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("list[0].id").value("2"))
                .andExpect(jsonPath("list[0].title").value("Holy Bible"))
                .andExpect(jsonPath("list[0].author").value("Unknown"))
                .andExpect(jsonPath("list[0].category.id").value("2"))
                .andExpect(jsonPath("list[0].category.name").value("Religious"))
                .andExpect(jsonPath("list[0].year").value(nullValue()))
                .andExpect(jsonPath("list[0].photoUrl").value(nullValue()))
                .andExpect(jsonPath("list[0].description").value(nullValue()))
                .andExpect(jsonPath("list[0].available").value("true"))
                .andExpect(jsonPath("list[0].price").value("6.66"))
                .andExpect(jsonPath("list[1].id").value("3"))
                .andExpect(jsonPath("list[2].id").value("1"))
                .andExpect(jsonPath("count").value("3"));

        mvc.perform(MockMvcRequestBuilders.get("/books")
                .param("offset", "0")
                .param("limit", "10")
                .param("sort", "title;desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("list[0].id").value("1"))
                .andExpect(jsonPath("list[1].id").value("3"))
                .andExpect(jsonPath("list[2].id").value("2"))
                .andExpect(jsonPath("count").value("3"));
    }

    @Test
    public void phraseHolyaaaTest() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/books")
                .param("offset", "0")
                .param("limit", "10")
                .param("phrases", "holyaaa"))
                .andExpect(status().is(200))
                .andExpect(jsonPath("list[0].id").value("3"))
                .andExpect(jsonPath("list[0].title").value("Holyaaa Bible"))
                .andExpect(jsonPath("count").value("1"));
    }

    @Test
    public void phraseHolyTest() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/books")
                .param("offset", "0")
                .param("limit", "10")
                .param("phrases", "holY"))
                .andExpect(status().is(200))
                .andExpect(jsonPath("list[0].id").value("2"))
                .andExpect(jsonPath("list[0].title").value("Holy Bible"))
                .andExpect(jsonPath("list[1].id").value("3"))
                .andExpect(jsonPath("list[1].title").value("Holyaaa Bible"))
                .andExpect(jsonPath("count").value("2"));
    }

    @Test
    public void phraseUnknownFromAuthorTest() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/books")
                .param("offset", "0")
                .param("limit", "10")
                .param("phrases", "known"))
                .andExpect(status().is(200))
                .andExpect(jsonPath("list[0].id").value("2"))
                .andExpect(jsonPath("list[0].title").value("Holy Bible"))
                .andExpect(jsonPath("list[0].author").value("Unknown"))
                .andExpect(jsonPath("list[1].id").value("3"))
                .andExpect(jsonPath("list[1].title").value("Holyaaa Bible"))
                .andExpect(jsonPath("list[1].author").value("Unknown"))
                .andExpect(jsonPath("count").value("2"));
    }

    @Test
    public void phraseHolyOrNineTest() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/books")
                .param("offset", "0")
                .param("limit", "10")
                .param("phrases", "holY,Nine"))
                .andExpect(status().is(200))
                .andExpect(jsonPath("list[0].id").value("1"))
                .andExpect(jsonPath("list[0].title").value("Nineteen Eighty-Four"))
                .andExpect(jsonPath("list[1].id").value("2"))
                .andExpect(jsonPath("list[1].title").value("Holy Bible"))
                .andExpect(jsonPath("list[2].id").value("3"))
                .andExpect(jsonPath("list[2].title").value("Holyaaa Bible"))
                .andExpect(jsonPath("count").value("3"));
    }

    @Test
    public void phraseHolyOrNineAndCategoryNovelTest() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/books")
                .param("offset", "0")
                .param("limit", "10")
                .param("phrases", "holY,Nine")
                .param("category", "1"))
                .andExpect(status().is(200))
                .andExpect(jsonPath("list[0].id").value("1"))
                .andExpect(jsonPath("list[0].category.id").value("1"))
                .andExpect(jsonPath("list[0].category.name").value("Novel"))
                .andExpect(jsonPath("count").value("1"));
    }

    @Test
    public void offset1Test() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/books")
                .param("offset", "1")
                .param("limit", "10"))
                .andExpect(status().is(200))
                .andExpect(jsonPath("list[0].id").value("2"))
                .andExpect(jsonPath("list[1].id").value("3"))
                .andExpect(jsonPath("list[2].id").doesNotExist())
                .andExpect(jsonPath("count").value("3"));
    }

    @Test
    public void limit0Test() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/books")
                .param("offset", "0")
                .param("limit", "0"))
                .andExpect(status().is(200))
                .andExpect(jsonPath("list").isEmpty())
                .andExpect(jsonPath("count").value("3"));
    }
}
