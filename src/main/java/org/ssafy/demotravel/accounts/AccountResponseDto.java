package org.ssafy.demotravel.accounts;

import lombok.*;

@NoArgsConstructor @AllArgsConstructor
@Builder @Getter @Setter
public class AccountResponseDto {
    private Long id;
    private String email;
    private String password;
    private AccountRole role;
}
