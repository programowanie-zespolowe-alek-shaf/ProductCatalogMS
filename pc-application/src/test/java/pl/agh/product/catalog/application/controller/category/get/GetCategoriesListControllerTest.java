package pl.agh.product.catalog.application.controller.category.get;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest()
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class GetCategoriesListControllerTest {

    @Autowired
    private MockMvc mvc;

    @Test
    public void successTest() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/categories"))
                .andExpect(status().is(200))
                .andExpect(jsonPath("list[0].id").value("1"))
                .andExpect(jsonPath("list[0].name").value("Novel"))
                .andExpect(jsonPath("list[1].id").value("2"))
                .andExpect(jsonPath("list[1].name").value("Religious"))
                .andExpect(jsonPath("count").value("2"));
    }
}
