package lk.tourism.tourism_api_2026.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@Table(name = "travel_package_details")
public class TravelPackageDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String destinationTitle;
    private String destinationDescription;

    @Column(name = "google_map_url")
    private String googleMapURL;

    @ManyToOne
    private TravelPackage travelPackage;

    public TravelPackageDetail(String destinationTitle, String destinationDescription, String googleMapURL, TravelPackage travelPackage) {
        this.destinationTitle = destinationTitle;
        this.destinationDescription = destinationDescription;
        this.googleMapURL = googleMapURL;
        this.travelPackage = travelPackage;
    }

}
