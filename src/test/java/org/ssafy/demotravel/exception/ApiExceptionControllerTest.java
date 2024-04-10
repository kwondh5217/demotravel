package org.ssafy.demotravel.exception;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
class ApiExceptionControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Test
    void customError() throws Exception {
        this.mockMvc.perform(get("/customError/{id}", "user-ex"))
                .andDo(print())
                .andExpect(status().isBadRequest());


    }

}