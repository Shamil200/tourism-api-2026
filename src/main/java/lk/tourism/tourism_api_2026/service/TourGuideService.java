package lk.tourism.tourism_api_2026.service;

import lk.tourism.tourism_api_2026.controller.request.TourGuideSignInRequest;
import lk.tourism.tourism_api_2026.controller.request.TourGuideSignOutRequest;
import lk.tourism.tourism_api_2026.model.Session;
import lk.tourism.tourism_api_2026.model.User;

public interface TourGuideService {

    Boolean isExistByUsername(String username);

    Boolean isExistByUsernamePasswordAndUserType(String username, String password, String userType);

    User findUserByUsernameAndUserType(String username, String userType);

    User findUserByUsername(String username);

    Session findSessionBySessionCodeSessionStateAndUserType(String sessionCode, String sessionState, String userType);

    Session signIn(TourGuideSignInRequest rq);

    Boolean signOut(TourGuideSignOutRequest rq);

}
