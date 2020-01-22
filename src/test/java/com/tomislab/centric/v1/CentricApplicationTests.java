package com.tomislab.centric.v1;

import com.tomislab.centric.repository.ProductRepository;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class CentricApplicationTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ProductRepository productRepository;

    @BeforeEach
    public void deleteAllBeforeTests() throws Exception {
        productRepository.deleteAll();
    }

    @Test
    public void shouldReturnRepositoryIndex() throws Exception {
        this.mockMvc.perform(get("/v1/products")).andDo(print()).andExpect(status().isOk());
    }

    @Test
    public void shouldCreateEntity() throws Exception {
        mockMvc.perform(post("/v1/products").header("Accept", "application/json").header("Content-Type", "application/json")
            .content("{\"name\": \"Red Shirt\", \"description\":\"Red hugo boss shirt\", \"category\":\"apparel\"}"))
            .andExpect(status().isOk());
    }

    @Test
    public void shouldRetrieveEntity() throws Exception {
        MvcResult mvcResult = mockMvc.perform(post("/v1/products").header("Accept", "application/json").header("Content-Type", "application/json")
            .content("{\"name\": \"Red Shirt\", \"description\":\"Red hugo boss shirt\", \"category\":\"apparel\"}")).andExpect(
            status().isOk()).andReturn();

        final JSONObject json = new JSONObject(mvcResult.getResponse().getContentAsString());
        assertThat(json.toString(), containsString("Red Shirt"));
    }

    @Test
    public void shouldExecuteFindByCategory() throws Exception {
        mockMvc.perform(post("/v1/products").header("Accept", "application/json").header("Content-Type", "application/json")
            .content("{\"name\": \"Red Shirt\", \"description\":\"Red hugo boss shirt\", \"category\":\"apparel\"}"))
            .andExpect(status().isOk());

        MvcResult mvcResult = mockMvc.perform(
            get("/v1/products/findByCategory?category=apparel&page=0&size=1")).andExpect(
            status().isOk()).andReturn();

        final JSONArray result = new JSONArray(mvcResult.getResponse().getContentAsString());
        assertThat(result.length(), is(1));
        assertThat(result.get(0).toString(), containsString("Red Shirt"));
    }

    @Test
    public void shouldExecuteFindByCategoryWithPaginationAndSorting() throws Exception {
        mockMvc.perform(post("/v1/products").header("Accept", "application/json").header("Content-Type", "application/json")
            .content("{\"name\": \"Red Shirt\", \"description\":\"Red hugo boss shirt\", \"category\":\"apparel\"}"))
            .andExpect(status().isOk());

        mockMvc.perform(post("/v1/products").header("Accept", "application/json").header("Content-Type", "application/json")
            .content("{\"name\": \"Blue Shirt\", \"description\":\"Blue hugo boss shirt\", \"category\":\"apparel\"}"))
            .andExpect(status().isOk());

        mockMvc.perform(post("/v1/products").header("Accept", "application/json").header("Content-Type", "application/json")
            .content("{\"name\": \"Green Shirt\", \"description\":\"Green hugo boss shirt\", \"category\":\"apparel\"}"))
            .andExpect(status().isOk());

        mockMvc.perform(post("/v1/products").header("Accept", "application/json").header("Content-Type", "application/json")
            .content("{\"name\": \"Brown Shoe\", \"description\":\"Brown hugo boss shoe\", \"category\":\"footwear\"}"))
            .andExpect(status().isOk());

        MvcResult mvcResult = mockMvc.perform(
            get("/v1/products/findByCategory?category=apparel&page=0&size=2")).andExpect(
            status().isOk()).andReturn();

        final JSONArray result = new JSONArray(mvcResult.getResponse().getContentAsString());
        assertThat(result.length(), is(2));
        // reverse insert order, Green Shirt created last
        assertThat(result.get(0).toString(), containsString("Green Shirt"));
        assertThat(result.get(1).toString(), containsString("Blue Shirt"));
    }

}
