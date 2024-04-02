package org.ssafy.demotravel.travels;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Getter @Setter @Builder
@Entity(name = "travels")
public class Travel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "travel_id")
    private Long id;
    @Column(name = "travel_title", length = 100)
    private String travelTitle;
    @Column(name = "travel_addr", length = 100)
    private String travelAddr;
    @Column(name = "travel_zipcode", length = 50)
    private String travelZipcode;
    @Column(name = "travel_first_image", length = 200)
    private String travelFirstImage;
    @Column(name = "travel_first_image2", length = 200)
    private String travelFirstImage2;
    @Column(name = "travel_sido_code")
    private Integer travelSidoCode;
    @Column(name = "travel_gugun_code")
    private Integer travelGugunCode;
    @Column(name = "travel_latitude", precision = 20, scale = 17)
    private BigDecimal travelLatitude;
    @Column(name = "travel_longitude", precision = 20, scale = 17)
    private BigDecimal travelLongitude;
}
