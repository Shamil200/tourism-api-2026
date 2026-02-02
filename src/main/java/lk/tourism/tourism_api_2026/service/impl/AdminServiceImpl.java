package lk.tourism.tourism_api_2026.service.impl;

import lk.tourism.tourism_api_2026.controller.request.*;
import lk.tourism.tourism_api_2026.exception.*;
import lk.tourism.tourism_api_2026.model.Session;
import lk.tourism.tourism_api_2026.model.User;
import lk.tourism.tourism_api_2026.repository.SessionRepository;
import lk.tourism.tourism_api_2026.repository.UserRepository;
import lk.tourism.tourism_api_2026.service.AdminService;
import lk.tourism.tourism_api_2026.utilities.GeneralUtilities;
import lk.tourism.tourism_api_2026.utilities.Sha256HashUtility;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Primary
@AllArgsConstructor
public class AdminServiceImpl implements AdminService {

    private UserRepository userRepository;
    private SessionRepository sessionRepository;

    @Override
    public Boolean isExistByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    @Override
    public Boolean isExistByUsernamePasswordAndUserType(String username, String password, String userType) {

        String hashedPassword = Sha256HashUtility.hash(password);

        return userRepository.existsByUsernameAndHashedPasswordAndUserType(username, hashedPassword, GeneralUtilities.getUserType(userType));

    }

    @Override
    public User findUserByUsernameAndUserType(String username, String userType) {
        return userRepository.findByUsernameAndUserType(username, GeneralUtilities.getUserType(userType));
    }

    @Override
    public User findUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public Session findSessionBySessionCodeSessionStateAndUserType(String sessionCode, String sessionState, String userType) {
        Session session = sessionRepository.findBySessionCodeAndSessionState(sessionCode, GeneralUtilities.getSessionState(sessionState));

        User user = userRepository.findByIdAndUserType(session.getUserId(), GeneralUtilities.getUserType(userType));

        if(user.getUserType() == GeneralUtilities.getUserType(userType)) {
            return session;
        }

        return null;

    }

    @Override
    public void create(CreateAdminRequest rq) {

        try {
            if(rq.getUsername().trim().isEmpty()) {
                log.debug("username cannot be empty , username : {}", rq.getUsername());
                throw new AdminNotCreatedException("username cannot be empty");
            }
        } catch (NullPointerException e) {
            log.debug("username cannot be empty , username : {}", rq.getUsername());
            throw new AdminNotCreatedException("username cannot be empty");
        }

        try {
            if(rq.getPassword().trim().isEmpty()) {
                log.debug("password cannot be empty , password : {}", rq.getPassword());
                throw new AdminNotCreatedException("password cannot be empty");
            }
        } catch (NullPointerException e) {
            log.debug("password cannot be empty , password : {}", rq.getPassword());
            throw new AdminNotCreatedException("password cannot be empty");
        }

        if(!GeneralUtilities.isValidUsernameFormat(rq.getUsername())) {
            log.debug("invalid username format provided , username : {}", rq.getUsername());
            throw new AdminNotCreatedException("invalid username format provided");
        }

        if(isExistByUsername(rq.getUsername())) {
            log.debug("username already exist in the database , username : {}", rq.getUsername());
            throw new AdminNotCreatedException("username already exist in the database");
        }

        User user = new User(
                rq.getUsername(),
                Sha256HashUtility.hash(rq.getPassword()),
                GeneralUtilities.getUserType("ADMIN"),
                GeneralUtilities.getCredentialsState("ACTIVE")
        );

        try {
            userRepository.save(user);
        } catch (RuntimeException e) {
            log.debug(e.getMessage());
            throw new AdminNotCreatedException(e.getMessage());
        }

        log.debug("admin created successfully");

    }

    @Override
    public Session signIn(AdminSignInRequest rq) {

        try {
            if(rq.getUsername().trim().isEmpty()) {
                log.debug("username cannot be empty");
                throw new AdminSignInFailedException("username cannot be empty");
            }
        } catch (NullPointerException e) {
            log.debug("username cannot be empty");
            throw new AdminSignInFailedException("username cannot be empty");
        }

        try {
            if(rq.getPassword().trim().isEmpty()) {
                log.debug("password cannot be empty");
                throw new AdminSignInFailedException("password cannot be empty");
            }
        } catch (NullPointerException e) {
            log.debug("password cannot be empty");
            throw new AdminSignInFailedException("password cannot be empty");
        }

        if(!GeneralUtilities.isValidUsernameFormat(rq.getUsername())) {
            log.debug("invalid username format provided , username : {}", rq.getUsername());
            throw new AdminSignInFailedException("invalid username format provided");
        }

        if( !isExistByUsernamePasswordAndUserType(rq.getUsername(), rq.getPassword(), "ADMIN") ) {
            log.debug("provided credentials are invalid");
            throw new AdminSignInFailedException("provided credentials are invalid");
        }

        User fetchedUser = findUserByUsernameAndUserType(rq.getUsername(), "ADMIN");

        if(fetchedUser == null) {
            log.debug("admin does not exist by given username");
            throw new AdminSignInFailedException("admin does not exist by given username");
        }

        Session savedSession;

        try {
            savedSession = sessionRepository.save(new Session(fetchedUser.getId()));
        } catch (RuntimeException e) {
            log.debug(e.getMessage());
            throw new AdminSignInFailedException(e.getMessage());
        }

        return savedSession;

    }


    @Override
    @Transactional(rollbackFor = {Exception.class})
    public Boolean signOut(AdminSignOutRequest rq) {

        try {
            if(rq.getSessionCode().trim().isEmpty()) {
                log.debug("session code cannot be empty");
                throw new AdminSignOutFailedException("session code cannot be empty");
            }
        } catch (NullPointerException e) {
            log.debug("session code cannot be empty");
            throw new AdminSignOutFailedException("session code cannot be empty");
        }

        Session fetchedSession = sessionRepository.findBySessionCode(rq.getSessionCode());

        if(fetchedSession == null) {
            log.debug("session does not exist in the database");
            throw new AdminSignOutFailedException("session does not exist in the database");
        }

        if(fetchedSession.getSessionState() == GeneralUtilities.getSessionState("INACTIVE")){
            log.debug("provided session is inactive");
            throw new AdminSignOutFailedException("provided session is inactive");
        }

        User fetchedUser = userRepository.getUserById(fetchedSession.getUserId());

        if(fetchedUser == null) {
            log.debug("the user associated with the session does not exist in the database");
            throw new AdminSignOutFailedException("the user associated with the session does not exist in the database");
        }

        try {
            sessionRepository.setAllSessionsInactiveByUserId(fetchedUser.getId());
        } catch (RuntimeException e) {
            log.debug(e.getMessage());
            throw new AdminSignOutFailedException(e.getMessage());
        }

        return (Boolean) true;

    }

    @Override
    public void createTourGuide(String adminSessionCode, CreateTourGuideRequest rq) {

        try {
            if(adminSessionCode.trim().isEmpty()) {
                log.debug("admin session code cannot be empty");
                throw new AdminSignOutFailedException("admin session code cannot be empty");
            }
        } catch (NullPointerException e) {
            log.debug("admin session code cannot be empty");
            throw new AdminSignOutFailedException("admin session code cannot be empty");
        }

        try {
            if(rq.getUsername().trim().isEmpty()) {
                log.debug("username cannot be empty");
                throw new TourGuideNotCreatedException("username cannot be empty");
            }
        } catch (NullPointerException e) {
            log.debug("username cannot be empty");
            throw new TourGuideNotCreatedException("username cannot be empty");
        }

        try {
            if(rq.getPassword().trim().isEmpty()) {
                log.debug("password cannot be empty");
                throw new TourGuideNotCreatedException("password cannot be empty");
            }
        } catch (NullPointerException e) {
            log.debug("password cannot be empty");
            throw new TourGuideNotCreatedException("password cannot be empty");
        }

        try {
            if(findSessionBySessionCodeSessionStateAndUserType(adminSessionCode, "ACTIVE", "ADMIN") == null){
                log.debug("provided admin session code is invalid");
                throw new TourGuideNotCreatedException("provided admin session code is invalid");
            }
        } catch (NullPointerException e) {
            log.debug("provided admin session code is invalid");
            throw new TourGuideNotCreatedException("provided admin session code is invalid");
        }

        if(!GeneralUtilities.isValidUsernameFormat(rq.getUsername())) {
            log.debug("invalid username format provided , username : {}", rq.getUsername());
            throw new TourGuideNotCreatedException("invalid username format provided");
        }

        if(isExistByUsername(rq.getUsername())) {
            log.debug("username already exist in the database , username : {}", rq.getUsername());
            throw new TourGuideNotCreatedException("username already exist in the database");
        }

        User user = new User(
                rq.getUsername(),
                Sha256HashUtility.hash(rq.getPassword()),
                GeneralUtilities.getUserType("TOUR_GUIDE"),
                GeneralUtilities.getCredentialsState("ACTIVE")
        );

        try {
            userRepository.save(user);
        } catch (RuntimeException e) {
            log.debug(e.getMessage());
            throw new TourGuideNotCreatedException(e.getMessage());
        }

        log.debug("tour guide created successfully");

    }

    @Override
    public Boolean makeTourGuideInactive(String adminSessionCode, MakeTourGuideInactiveRequest rq) {

        try {
            if(rq.getUsername().trim().isEmpty()) {
                log.debug("username cannot be empty");
                throw new MakeTourGuideInactiveException("username cannot be empty");
            }
        } catch (NullPointerException e) {
            log.debug("username cannot be empty");
            throw new MakeTourGuideInactiveException("username cannot be empty");
        }

        try {
            if(findSessionBySessionCodeSessionStateAndUserType(adminSessionCode, "ACTIVE", "ADMIN") == null){
                log.debug("provided admin session code is invalid");
                throw new MakeTourGuideInactiveException("provided admin session code is invalid");
            }
        } catch (NullPointerException e) {
            log.debug("provided admin session code is invalid");
            throw new MakeTourGuideInactiveException("provided admin session code is invalid");
        }

        User fetchedUser = findUserByUsername(rq.getUsername());

        if(fetchedUser == null){
            log.debug("provided username is invalid");
            throw new MakeTourGuideInactiveException("provided username is invalid");
        }

        if(fetchedUser.getCredentialsState() == GeneralUtilities.getCredentialsState("INACTIVE")){
            log.debug("the account associated with username is already inactive");
            throw new MakeTourGuideInactiveException("the account associated with username is already inactive");
        }

        Long deactivatedSessionCount = sessionRepository.setAllSessionsInactiveByUserId(fetchedUser.getId());

        if(deactivatedSessionCount == 0) {
            log.debug("there are no sessions to be deactivated");
        }

        fetchedUser.setCredentialsState(GeneralUtilities.getCredentialsState("INACTIVE"));

        try {
            userRepository.save(fetchedUser);
        } catch (RuntimeException e) {
            log.debug(e.getMessage());
            throw new MakeTourGuideInactiveException(e.getMessage());
        }

        return (Boolean) true;

    }
}
