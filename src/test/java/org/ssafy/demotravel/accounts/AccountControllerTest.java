package org.ssafy.demotravel.accounts;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.modifyUris;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@ExtendWith(RestDocumentationExtension.class)
@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc
class AccountControllerTest {

    @Autowired
    MockMvc mockMvc;
    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    AccountRepository accountRepository;


    @BeforeEach
    void setup(WebApplicationContext webApplicationContext,
               RestDocumentationContextProvider restDocumentationContextProvider){
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .addFilter(new CharacterEncodingFilter("UTF-8", true))
                .apply(documentationConfiguration(restDocumentationContextProvider)
                        .operationPreprocessors()
                        .withRequestDefaults(modifyUris().host("localhost").removePort(), prettyPrint())
                        .withResponseDefaults(modifyUris().host("localhost").removePort(), prettyPrint()))
                .apply(springSecurity())
                .build();
    }

    @DisplayName("회원 가입 테스트")
    @Test
    void getRequestJoin() throws Exception {
        // given
        Long userId = 1L;
        String userEmail = "trewq231@naver.com";
        String password = "pass";
        AccountRole userRole = AccountRole.USER;
        var requestDto = createAccountRequestJoinDto(userEmail, password, userRole);
        var responseDto = createAccountResponseDto(userId,userEmail,password,userRole);

        // when
        ResultActions resultActions = this.mockMvc.perform(post("/accounts")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDto)));

        // then
        resultActions.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("email").exists());
    }


    @DisplayName("중복된 이메일 회원 정보가 있어 회원 가입이 실패하는 테스트")
    @Test
    void getRequestJoin_fail() throws Exception {
        // given
        String userEmail = "trewq231@naver.com";
        String password = "pass";
        AccountRole userRole = AccountRole.USER;
        Account account = Account.builder()
                .email(userEmail)
                .password(password)
                .role(userRole)
                .build();
        this.accountRepository.save(account);
        var joinDto = createAccountRequestJoinDto(userEmail, password, userRole);

        // when
        ResultActions resultActions = this.mockMvc.perform(post("/accounts")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(joinDto)));

        // then
        resultActions.andDo(print())
                .andExpect(status().isBadRequest());
    }

    @DisplayName("필수 입력을 하지 않아서 실패하는 회원가입 테스트")
    @Test
    void getRequestJoin_notNull() throws Exception {
        // given
        String userEmail = "trewq231@naver.com";
        AccountRole userRole = AccountRole.USER;
        AccountJoinRequestDto accountJoinRequestDto = AccountJoinRequestDto.builder()
                .email(userEmail)
                .role(userRole)
                .build();

        // when
        ResultActions resultActions = this.mockMvc.perform(post("/accounts")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(accountJoinRequestDto)));

        // then
        resultActions.andDo(print())
                .andExpect(status().isBadRequest());
    }






    private static AccountJoinRequestDto createAccountRequestJoinDto(String userEmail,
                                                                     String password,
                                                                     AccountRole userRole) {
        AccountJoinRequestDto accountJoinRequestDto = AccountJoinRequestDto.builder()
                .email(userEmail)
                .password(password)
                .role(userRole)
                .build();
        return accountJoinRequestDto;
    }

    private static AccountResponseDto createAccountResponseDto(Long id,String userEmail,
                                                               String password,
                                                               AccountRole userRole) {
        AccountResponseDto accountResponseDto = AccountResponseDto.builder()
                .id(id)
                .email(userEmail)
                .password(password)
                .role(userRole)
                .build();
        return accountResponseDto;
    }


}