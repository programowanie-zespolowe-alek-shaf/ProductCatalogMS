package pl.agh.product.catalog.application.controller.category.create;

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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static pl.agh.product.catalog.application.config.TestUtils.*;

@RunWith(SpringRunner.class)
@SpringBootTest()
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class AddCategoryControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @WithMockUser(username = "john", roles = {"ADMIN"})
    @Test
    public void successTest() throws Exception {

        CategoryRequestDTO categoryRequestDTO = new CategoryRequestDTO();
        categoryRequestDTO.setName("newCategory");

        String requestJson = mapObjectToStringJson(categoryRequestDTO, objectMapper);

        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.post("/categories")
                .contentType(APPLICATION_JSON_UTF8)
                .content(requestJson))
                .andExpect(status().is(201))
                .andExpect(jsonPath("id").isNumber())
                .andExpect(jsonPath("name").value("newCategory"))
                .andReturn();

        Long categoryId = getIdFromResponse(mvcResult);
        Category category = categoryRepository.findById(categoryId).orElse(null);
        assertNotNull(category);
        assertEquals("newCategory", category.getName());

        categoryRepository.delete(category);
    }

    @WithMockUser(username = "john", roles = {"ADMIN"})
    @Test
    public void categoryAlreadyExistsFailedTest() throws Exception {
        CategoryRequestDTO categoryRequestDTO = new CategoryRequestDTO();
        categoryRequestDTO.setName("noveL");

        String requestJson = mapObjectToStringJson(categoryRequestDTO, objectMapper);

        mvc.perform(MockMvcRequestBuilders.post("/categories")
                .contentType(APPLICATION_JSON_UTF8)
                .content(requestJson))
                .andExpect(status().is(400))
                .andExpect(jsonPath("error").value("category already exists"));
    }
}
