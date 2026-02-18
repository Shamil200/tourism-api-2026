package lk.tourism.tourism_api_2026.controller.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import tools.jackson.databind.PropertyNamingStrategies;
import tools.jackson.databind.annotation.JsonNaming;

import java.util.List;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class CreateTravelPackageRequest {

    @NotBlank(message = "name cannot be empty")
    private String name;

    @NotBlank(message = "people count cannot be empty")
    private Integer peopleCount;

    @NotBlank(message = "duration cannot be empty")
    private String duration;

    @NotBlank(message = "total price cannot be empty")
    private Double totalPrice;

    @NotBlank(message = "admission percentage cannot be empty")
    private Float admissionPercentage;

    @NotEmpty(message = "visiting locations cannot be empty")
    @Valid
    private List<TravelPackageDetailItem> visitingLocations;

}
