package lk.tourism.tourism_api_2026.controller.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.Data;
import tools.jackson.databind.PropertyNamingStrategies;
import tools.jackson.databind.annotation.JsonNaming;

import java.util.List;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class CreateTravelPackageRequest {

    @NotBlank(message = "name cannot be empty")
    private String name;

    @NotNull(message = "people count cannot be empty")
    @Positive(message = "people count must be positive")
    private Integer peopleCount;

    @NotBlank(message = "duration cannot be empty")
    private String duration;

    @NotNull(message = "total price cannot be null")
    @Positive(message = "total price must be positive")
    @DecimalMin(value = "0.0", inclusive = false, message = "total price must be greater than 0")
    @DecimalMax(value = "40000.0", inclusive = false, message = "total price must be less 40,000")
    private Double totalPrice;

    @NotNull(message = "admission percentage cannot be empty")
    @Positive(message = "admission percentage must be positive")
    @DecimalMin(value = "0.0", inclusive = false, message = "admission percentage must be greater than 0")
    @DecimalMax(value = "100.0", inclusive = false, message = "admission percentage must be less than 100")
    private Float admissionPercentage;

    @NotEmpty(message = "visiting locations cannot be empty")
    @Valid
    private List<TravelPackageDetailItem> visitingLocations;

}
