package pl.agh.product.catalog.application.controller.category.delete;

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
import pl.agh.product.catalog.mysql.entity.Category;
import pl.agh.product.catalog.mysql.repository.CategoryRepository;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static pl.agh.product.catalog.application.config.TestUtils.getIdFromResponse;

@RunWith(SpringRunner.class)
@SpringBootTest()
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class DeleteCategoryControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private CategoryRepository categoryRepository;

    @WithMockUser(username = "john", roles = {"ADMIN"})
    @Test
    public void createAndDeleteCategorySuccessTest() throws Exception {
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.post("/categories")
                .param("name", "newCategory"))
                .andExpect(status().is(201))
                .andExpect(jsonPath("id").isNumber())
                .andReturn();

        Long categoryId = getIdFromResponse(mvcResult);
        Category categoryBeforeDeleting = categoryRepository.findById(categoryId).orElse(null);
        assertNotNull(categoryBeforeDeleting);

        mvc.perform(MockMvcRequestBuilders.delete("/categories/" + categoryId))
                .andExpect(status().is(204));

        Category categoryAfterDeleting = categoryRepository.findById(categoryId).orElse(null);
        assertNull(categoryAfterDeleting);
    }

    @WithMockUser(username = "john", roles = {"ADMIN"})
    @Test
    public void notFoundTest() throws Exception {
        mvc.perform(MockMvcRequestBuilders.delete("/categories/10"))
                .andExpect(status().is(404));
    }
}
