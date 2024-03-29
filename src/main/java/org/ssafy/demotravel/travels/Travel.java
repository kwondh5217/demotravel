package org.ssafy.demotravel.travels;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter @Setter @Builder
@Entity
public class Travel {

    @Id @GeneratedValue
    private Long id;

    private String name;
}
