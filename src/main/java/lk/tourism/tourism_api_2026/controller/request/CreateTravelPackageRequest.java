package lk.tourism.tourism_api_2026.controller.request;

import lombok.Data;
import tools.jackson.databind.PropertyNamingStrategies;
import tools.jackson.databind.annotation.JsonNaming;

import java.util.List;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class CreateTravelPackageRequest {

    private String name;
    private Integer peopleCount;
    private String duration;
    private Double totalPrice;
    private Float admissionPercentage;
    private List<TravelPackageDetailForRequest> visitingLocations;

}
