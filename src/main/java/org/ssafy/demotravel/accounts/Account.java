package org.ssafy.demotravel.accounts;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.security.crypto.password.PasswordEncoder;

@NoArgsConstructor @AllArgsConstructor
@Getter @Setter @Builder
@Entity
public class Account {

    @Id @GeneratedValue
    private Long id;

    @Column(unique = true)
    private String email;
    @NotNull
    private String password;

    @Enumerated(EnumType.STRING)
    private AccountRole role;


    public void encodePassword(PasswordEncoder passwordEncoder, String password){
        this.password = passwordEncoder.encode(password);
    }

}
