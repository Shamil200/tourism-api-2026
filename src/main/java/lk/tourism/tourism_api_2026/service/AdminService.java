package lk.tourism.tourism_api_2026.service;

import lk.tourism.tourism_api_2026.controller.request.*;
import lk.tourism.tourism_api_2026.model.Session;
import lk.tourism.tourism_api_2026.model.User;

public interface AdminService {

    Boolean isExistByUsername(String username);

    Boolean isExistByUsernamePasswordAndUserType(String username, String password, String userType);

    User findUserByUsernameAndUserType(String username, String userType);

    User findUserByUsername(String username);

    Session findSessionBySessionCodeSessionStateAndUserType(String sessionCode, String sessionState, String userType);

    void create(CreateAdminRequest rq);

    Session signIn(AdminSignInRequest rq);

    Boolean signOut(AdminSignOutRequest rq);

    void createTourGuide(String adminSessionCode, CreateTourGuideRequest rq);

    Boolean makeTourGuideInactive(String adminSessionCode, MakeTourGuideInactiveRequest rq);

}
