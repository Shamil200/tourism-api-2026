package lk.tourism.tourism_api_2026.model;

import jakarta.persistence.*;
import lk.tourism.tourism_api_2026.model.enums.ReservationPaymentStatus;
import lk.tourism.tourism_api_2026.model.enums.ReservationStatus;
import lk.tourism.tourism_api_2026.model.enums.ReservationTourGuideResponseStatus;
import lk.tourism.tourism_api_2026.model.enums.ReservationTourStatus;
import lk.tourism.tourism_api_2026.utilities.CurrentDateTimeUtility;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Data
@NoArgsConstructor
@Table(name = "reservation_requests")
public class ReservationRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate checkInDate;
    private LocalDate checkOutDate;
    private LocalDate reservationAddedDate;
    private LocalTime reservationAddedTime;
    private Double reservationAdmissionPayment;
    private Double reservationRemainingPayment;

    @Enumerated(EnumType.STRING)
    private ReservationPaymentStatus reservationPaymentStatus;

    @Enumerated(EnumType.STRING)
    private ReservationTourGuideResponseStatus tourGuideResponseStatus;

    @Enumerated(EnumType.STRING)
    private ReservationTourStatus tourStatus;

    @Enumerated(EnumType.STRING)
    private ReservationStatus reservationStatus;

    @ManyToOne
    @JoinColumn(name = "package_id")
    private TravelPackage travelPackage;

    public ReservationRequest(LocalDate checkInDate, LocalDate checkOutDate, Double reservationAdmissionPayment, Double reservationRemainingPayment, TravelPackage travelPackage) {
        this.checkInDate = checkInDate;
        this.checkOutDate = checkOutDate;
        this.reservationAdmissionPayment = reservationAdmissionPayment;
        this.reservationRemainingPayment = reservationRemainingPayment;
        this.travelPackage = travelPackage;

        this.reservationPaymentStatus = ReservationPaymentStatus.PARTIALLY_PAID;
        this.tourGuideResponseStatus = ReservationTourGuideResponseStatus.WAITING;
        this.tourStatus = ReservationTourStatus.INCOMPLETE;
        this.reservationStatus = ReservationStatus.NOT_ARCHIVED;

        CurrentDateTimeUtility currentDateTimeUtility = new CurrentDateTimeUtility();
        this.reservationAddedDate = currentDateTimeUtility.getCurrentDate();
        this.reservationAddedTime = currentDateTimeUtility.getCurrentTime();

    }

}
