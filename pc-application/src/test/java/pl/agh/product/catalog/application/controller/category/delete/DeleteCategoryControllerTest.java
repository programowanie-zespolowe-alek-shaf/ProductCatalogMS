package pl.agh.product.catalog.application.controller.category.delete;

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
import pl.agh.product.catalog.application.dto.CategoryRequestDTO;
import pl.agh.product.catalog.mysql.entity.Category;
import pl.agh.product.catalog.mysql.repository.CategoryRepository;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static pl.agh.product.catalog.application.config.TestUtils.*;

@RunWith(SpringRunner.class)
@SpringBootTest()
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class DeleteCategoryControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @WithMockUser(username = "john", roles = {"ADMIN"})
    @Test
    public void createAndDeleteCategorySuccessTest() throws Exception {
        CategoryRequestDTO categoryRequestDTO = new CategoryRequestDTO();
        categoryRequestDTO.setName("newCategory");

        String requestJson = mapObjectToStringJson(categoryRequestDTO, objectMapper);

        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.post("/categories")
                .contentType(APPLICATION_JSON_UTF8)
                .content(requestJson))
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
