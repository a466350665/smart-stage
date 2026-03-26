package openjoe.smart.stage.exception;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.context.annotation.Import;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = GlobalExceptionHandlerTest.TestApplication.class)
@AutoConfigureMockMvc
class GlobalExceptionHandlerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void shouldHandleCustomException() throws Exception {
        mockMvc.perform(get("/test/custom"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("E001"))
                .andExpect(jsonPath("$.message").value("custom error"));
    }

    @Test
    void shouldHandleMissingRequestParameter() throws Exception {
        mockMvc.perform(get("/test/missing"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("000002"))
                .andExpect(jsonPath("$.message").isNotEmpty());
    }

    @Test
    void shouldHandleConstraintViolation() throws Exception {
        mockMvc.perform(get("/test/constraint").param("page", "0"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("000002"))
                .andExpect(jsonPath("$.message").value(org.hamcrest.Matchers.containsString("must be greater than or equal to 1")));
    }

    @Test
    void shouldHandleMethodArgumentValidation() throws Exception {
        mockMvc.perform(post("/test/body")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new DemoRequest())))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("000002"))
                .andExpect(jsonPath("$.message").value(org.hamcrest.Matchers.containsString("name")));
    }

    @Test
    void shouldHandleUnreadableBody() throws Exception {
        mockMvc.perform(post("/test/body")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{invalid-json}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("000002"))
                .andExpect(jsonPath("$.message").isNotEmpty());
    }

    @SpringBootApplication
    @Import(TestController.class)
    @ImportAutoConfiguration(ExceptionAutoConfiguration.class)
    static class TestApplication {
    }

    @RestController
    @Validated
    @RequestMapping("/test")
    static class TestController {

        @GetMapping("/custom")
        String custom() {
            throw new CommonException("E001", "custom error");
        }

        @GetMapping("/missing")
        String missing(@RequestParam("name") String name) {
            return name;
        }

        @GetMapping("/constraint")
        String constraint(@RequestParam("page") @Min(1) Integer page) {
            return String.valueOf(page);
        }

        @PostMapping("/body")
        String body(@Valid @RequestBody DemoRequest request) {
            return request.getName();
        }
    }

    static class DemoRequest {

        @NotBlank
        private String name;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}
