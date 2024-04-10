package org.ssafy.demotravel.exception.resolver;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class MyHandlerExceptionResolverTest {

    @Autowired
    MockMvc mockMvc;

    @WithMockUser
    @DisplayName("exception resolver가 동작한다")
    @Test
    void exceptionResolve_400() throws Exception{
        this.mockMvc.perform(get("/customError"))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }


}