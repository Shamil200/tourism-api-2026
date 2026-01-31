package lk.tourism.tourism_api_2026.model;

import jakarta.persistence.*;
import lk.tourism.tourism_api_2026.model.enums.SessionState;
import lk.tourism.tourism_api_2026.utilities.CurrentDateTimeUtility;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
@Table(name = "sessions")
public class Session {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;
    private LocalDate sessionCreatedDate;
    private LocalTime sessionCreatedTime;
    private String sessionCode;

    @Enumerated(EnumType.STRING)
    private SessionState sessionState;


    public Session(Long userId) {
        this.userId = userId;

        CurrentDateTimeUtility currentDateTimeUtility = new CurrentDateTimeUtility();
        this.sessionCreatedDate = currentDateTimeUtility.getCurrentDate();
        this.sessionCreatedTime = currentDateTimeUtility.getCurrentTime();

        this.sessionCode =  UUID.randomUUID().toString().replace("-", "");
        this.sessionState = SessionState.ACTIVE;

    }
}
