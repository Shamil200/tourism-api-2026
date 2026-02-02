package lk.tourism.tourism_api_2026.service;

import lk.tourism.tourism_api_2026.controller.request.CreateTravelPackageRequest;

public interface TravelPackageService {

    Boolean sessionExistBySessionCodeSessionStateUserTypeAndCredentialsState(String sessionCode, String sessionState, String userType, String credentialsState);

    void create(String tourGuideSessionCode, CreateTravelPackageRequest rq);

}
