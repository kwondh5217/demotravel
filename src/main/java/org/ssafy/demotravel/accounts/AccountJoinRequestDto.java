package org.ssafy.demotravel.accounts;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@AllArgsConstructor @NoArgsConstructor
@Builder @Setter @Getter
public class AccountJoinRequestDto {

    @NotNull
    private String email;
    @NotNull
    private String password;
    private AccountRole role;
}
