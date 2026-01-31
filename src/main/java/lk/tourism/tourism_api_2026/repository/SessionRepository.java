package lk.tourism.tourism_api_2026.repository;

import jakarta.transaction.Transactional;
import lk.tourism.tourism_api_2026.model.Session;
import lk.tourism.tourism_api_2026.model.enums.SessionState;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface SessionRepository extends JpaRepository<Session, Long> {
    Session findBySessionCode(String sessionCode);

    @Modifying
    @Transactional
    @Query(value = """
    UPDATE sessions 
    SET session_state = 'INACTIVE' 
    WHERE user_id = :userId 
    AND session_state = 'ACTIVE'
""", nativeQuery = true)
    Long setAllSessionsInactiveByUserId(@Param("userId") Long userId);

    Session findBySessionCodeAndSessionState(String sessionCode, SessionState sessionState);
}
