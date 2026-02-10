package lk.tourism.tourism_api_2026.model;

import jakarta.persistence.*;
import lk.tourism.tourism_api_2026.model.enums.TravelPackageStatus;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@Table(name = "travel_packages")
public class TravelPackage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private Integer memberCount;
    private String estimatedDuration;
    private BigDecimal totalPrice;
    private Float reservationAdmissionPercentage;

    @Enumerated(EnumType.STRING)
    private TravelPackageStatus travelPackageStatus;

    @OneToMany(mappedBy = "travelPackage")
    private List<ReservationRequest> reservationRequestList;

    @OneToMany(mappedBy = "travelPackage")
    private List<TravelPackageDetail> travelPackageDetailList;

    public TravelPackage(String name, Integer memberCount, String estimatedDuration, BigDecimal totalPrice, Float reservationAdmissionPercentage) {
        this.name = name;
        this.memberCount = memberCount;
        this.estimatedDuration = estimatedDuration;
        this.totalPrice = totalPrice;
        this.reservationAdmissionPercentage = reservationAdmissionPercentage;

        this.travelPackageStatus = TravelPackageStatus.ACTIVE;

    }

}
