CREATE TABLE users (
                       id BIGINT AUTO_INCREMENT PRIMARY KEY,
                       username VARCHAR(255),
                       hashed_password VARCHAR(255),
                       user_type VARCHAR(50),
                       credentials_state VARCHAR(50)
);

/**/

CREATE TABLE sessions (
                          id BIGINT AUTO_INCREMENT PRIMARY KEY,
                          user_id BIGINT,
                          session_created_date DATE,
                          session_created_time TIME,
                          session_code VARCHAR(32),
                          session_state VARCHAR(50)
);

/**/

CREATE TABLE travel_packages (
                                 id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                 name VARCHAR(255),
                                 member_count INTEGER,
                                 estimated_duration VARCHAR(255),
                                 total_price DECIMAL(38,2),
                                 reservation_admission_percentage FLOAT,
                                 travel_package_status VARCHAR(50)
);

/**/

CREATE TABLE travel_package_details (
                                        id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                        destination_title VARCHAR(255),
                                        destination_description TEXT,
                                        google_map_url VARCHAR(500),
                                        travel_package_id BIGINT,
                                        FOREIGN KEY (travel_package_id) REFERENCES travel_packages(id)
);

/**/

CREATE TABLE reservation_requests (
                                      id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                      check_in_date DATE,
                                      check_out_date DATE,
                                      reservation_added_date DATE,
                                      reservation_added_time TIME,
                                      reservation_admission_payment DECIMAL(38,2),
                                      reservation_remaining_payment DECIMAL(38,2),
                                      reservation_payment_status VARCHAR(50),
                                      tour_guide_response_status VARCHAR(50),
                                      tour_status VARCHAR(50),
                                      reservation_status VARCHAR(50),
                                      package_id BIGINT,
                                      FOREIGN KEY (package_id) REFERENCES travel_packages(id)
);

/**/