package lk.tourism.tourism_api_2026.controller;

import lk.tourism.tourism_api_2026.controller.request.CreateTourGuideRequest;
import lk.tourism.tourism_api_2026.controller.request.CreateTravelPackageRequest;
import lk.tourism.tourism_api_2026.controller.request.TourGuideSignInRequest;
import lk.tourism.tourism_api_2026.controller.request.TourGuideSignOutRequest;
import lk.tourism.tourism_api_2026.controller.response.TourGuideSignInResponse;
import lk.tourism.tourism_api_2026.controller.response.TourGuideSignOutResponse;
import lk.tourism.tourism_api_2026.service.TourGuideService;
import lk.tourism.tourism_api_2026.service.TravelPackageService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/tour-guides")
@AllArgsConstructor
public class TourGuideController {

    private TourGuideService tourGuideService;
    private TravelPackageService travelPackageService;

    @PostMapping(value = "/sign-in", headers = "X-Api-Version=v1")
    TourGuideSignInResponse tourGuideSignInV1(@RequestBody TourGuideSignInRequest rq){
        log.trace("received request : {}", rq);

        return TourGuideSignInResponse
                .builder()
                .sessionCode(tourGuideService.signIn(rq).getSessionCode())
                .build();

    }

    @PutMapping(value = "/sign-out", headers = "X-Api-Version=v1")
    TourGuideSignOutResponse tourGuideSignOutV1(@RequestBody TourGuideSignOutRequest rq){
        log.trace("received request : {}", rq);

        return TourGuideSignOutResponse
                .builder()
                .isSignedOut(tourGuideService.signOut(rq))
                .build();

    }

    @PostMapping(value = "/{tour-guide-session-code}/create-travel-package", headers = "X-Api-Version=v1")
    void addTravelPackageV1(@PathVariable("tour-guide-session-code") String tourGuideSessionCode, @RequestBody CreateTravelPackageRequest rq){
        log.trace("received path variable : {}", tourGuideSessionCode);
        log.trace("received request : {}", rq);
        travelPackageService.create(tourGuideSessionCode, rq);

    }

}
