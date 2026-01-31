package lk.tourism.tourism_api_2026.repository;

import lk.tourism.tourism_api_2026.model.ReservationRequest;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReservationRequestRepository extends JpaRepository<ReservationRequest, Long> {
}
