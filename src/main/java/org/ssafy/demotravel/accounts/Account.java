package org.ssafy.demotravel.accounts;

import jakarta.persistence.*;
import lombok.*;

@NoArgsConstructor @AllArgsConstructor
@Getter @Setter @Builder
@Entity
public class Account {

    @Id @GeneratedValue
    private Long id;

    private String name;

    private String password;

    @Enumerated(EnumType.STRING)
    private AccountRole role;
}
