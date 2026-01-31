package lk.tourism.tourism_api_2026.repository;

import lk.tourism.tourism_api_2026.model.User;
import lk.tourism.tourism_api_2026.model.enums.UserType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    Boolean existsByUsername(String username);

    Boolean existsByUsernameAndHashedPasswordAndUserType(String username, String hashedPassword, UserType userType);

    User findByUsernameAndUserType(String username, UserType userType);

    User getUserById(Long id);

    User findByIdAndUserType(Long id, UserType userType);

    User findByUsername(String username);
}
