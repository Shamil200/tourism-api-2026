package lk.tourism.tourism_api_2026.controller.request;

import lombok.Data;
import tools.jackson.databind.PropertyNamingStrategies;
import tools.jackson.databind.annotation.JsonNaming;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class AdminSignInRequest {

    private String username;
    private String password;

}
