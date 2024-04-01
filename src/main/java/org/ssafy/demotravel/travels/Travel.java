package org.ssafy.demotravel.travels;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Getter @Setter @Builder
@Entity
public class Travel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "travel_id")
    private Long id;
    private String travelTitle;
    private String travelAddr;
    private String travelZipcode;
    private String travelFirstImage;
    private String travelFirstImage2;
    private Integer travelSidoCode;
    private Integer travelGugunCode;
    private BigDecimal travelLatitude;
    private BigDecimal travelLongitude;
}
