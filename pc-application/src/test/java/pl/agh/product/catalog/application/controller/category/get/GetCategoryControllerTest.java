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
public class GetCategoryControllerTest {

    @Autowired
    private MockMvc mvc;

    @Test
    public void onlyLimitAndOffsetTest() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/categories/1"))
                .andExpect(status().is(200))
                .andExpect(jsonPath("id").value("1"))
                .andExpect(jsonPath("name").value("Novel"));
    }


    @Test
    public void notFoundTest() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/categories/10"))
                .andExpect(status().is(404));
    }

    @Test
    public void invalidIdFormatTest() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/categories/10ds"))
                .andExpect(status().is(400));
    }
}
