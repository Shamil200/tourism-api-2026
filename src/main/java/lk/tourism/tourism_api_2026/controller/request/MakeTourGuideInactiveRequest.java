package lk.tourism.tourism_api_2026.controller.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import tools.jackson.databind.PropertyNamingStrategies;
import tools.jackson.databind.annotation.JsonNaming;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class MakeTourGuideInactiveRequest {

    @NotBlank(message = "username cannot be empty")
    private String username;

}
