package lk.tourism.tourism_api_2026.model;

import jakarta.persistence.*;
import lk.tourism.tourism_api_2026.model.enums.CredentialsState;
import lk.tourism.tourism_api_2026.model.enums.UserType;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;
    private String hashedPassword;

    @Enumerated(EnumType.STRING)
    private UserType userType;

    @Enumerated(EnumType.STRING)
    private CredentialsState credentialsState;

    public User(String username, String hashedPassword, UserType userType, CredentialsState credentialsState) {
        this.username = username;
        this.hashedPassword = hashedPassword;
        this.userType = userType;
        this.credentialsState = credentialsState;
    }

}
