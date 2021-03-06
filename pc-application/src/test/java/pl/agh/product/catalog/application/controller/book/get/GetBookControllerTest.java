package pl.agh.product.catalog.application.controller.book.get;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.hamcrest.Matchers.nullValue;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest()
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class GetBookControllerTest {

    @Autowired
    private MockMvc mvc;

    @Test
    public void successTest() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/books/1"))
                .andExpect(status().is(200))
                .andExpect(jsonPath("id").value("1"))
                .andExpect(jsonPath("title").value("Nineteen Eighty-Four"))
                .andExpect(jsonPath("author").value("George Orwell"))
                .andExpect(jsonPath("category.id").value("1"))
                .andExpect(jsonPath("category.name").value("Novel"))
                .andExpect(jsonPath("year").value("1949"))
                .andExpect(jsonPath("photoUrl").value(nullValue()))
                .andExpect(jsonPath("description").value(nullValue()))
                .andExpect(jsonPath("available").value("true"))
                .andExpect(jsonPath("price").value("21.37"))
                .andExpect(jsonPath("recommended").value(true))
                .andExpect(jsonPath("numPages").value("345"))
                .andExpect(jsonPath("coverType").value("HARDCOVER"));
    }

    @Test
    public void notFoundTest() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/books/10"))
                .andExpect(status().is(404));
    }

    @Test
    public void invalidIdFormatTest() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/books/10ds"))
                .andExpect(status().is(400));
    }
}
