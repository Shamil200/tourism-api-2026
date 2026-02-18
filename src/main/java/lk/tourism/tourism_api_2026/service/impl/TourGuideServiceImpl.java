package lk.tourism.tourism_api_2026.service.impl;

import lk.tourism.tourism_api_2026.controller.request.TourGuideSignInRequest;
import lk.tourism.tourism_api_2026.controller.request.TourGuideSignOutRequest;
import lk.tourism.tourism_api_2026.exception.TourGuideSignInFailedException;
import lk.tourism.tourism_api_2026.exception.TourGuideSignOutFailedException;
import lk.tourism.tourism_api_2026.model.Session;
import lk.tourism.tourism_api_2026.model.User;
import lk.tourism.tourism_api_2026.repository.SessionRepository;
import lk.tourism.tourism_api_2026.repository.UserRepository;
import lk.tourism.tourism_api_2026.service.TourGuideService;
import lk.tourism.tourism_api_2026.utilities.GeneralUtilities;
import lk.tourism.tourism_api_2026.utilities.Sha256HashUtility;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@Primary
@RequiredArgsConstructor
public class TourGuideServiceImpl implements TourGuideService {

    private final UserRepository userRepository;
    private final SessionRepository sessionRepository;

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
    public Session signIn(TourGuideSignInRequest rq) {

        if(rq.getUsername().trim().isEmpty()) {
            log.debug("username cannot be empty");
            throw new TourGuideSignInFailedException("username cannot be empty");
        }

        if(rq.getPassword().trim().isEmpty()) {
            log.debug("password cannot be empty");
            throw new TourGuideSignInFailedException("password cannot be empty");
        }

        if(!GeneralUtilities.isValidUsernameFormat(rq.getUsername())) {
            log.debug("invalid username format provided , username : {}", rq.getUsername());
            throw new TourGuideSignInFailedException("invalid username format provided");
        }

        if( !isExistByUsernamePasswordAndUserType(rq.getUsername(), rq.getPassword(), "TOUR_GUIDE") ) {
            log.debug("provided credentials are invalid");
            throw new TourGuideSignInFailedException("provided credentials are invalid");
        }

        User fetchedUser = findUserByUsernameAndUserType(rq.getUsername(), "TOUR_GUIDE");

        if(fetchedUser == null) {
            log.debug("tour guide does not exist by given username");
            throw new TourGuideSignInFailedException("admin does not exist by given username");
        }

        Session savedSession;

        try {
            savedSession = sessionRepository.save(new Session(fetchedUser.getId()));
        } catch (RuntimeException e) {
            log.debug(e.getMessage());
            throw new TourGuideSignInFailedException(e.getMessage());
        }

        return savedSession;

    }

    @Override
    public Boolean signOut(TourGuideSignOutRequest rq) {

        if(rq.getSessionCode().trim().isEmpty()) {
            log.debug("session code cannot be empty");
            throw new TourGuideSignOutFailedException("session code cannot be empty");
        }

        Session fetchedSession = sessionRepository.findBySessionCode(rq.getSessionCode());

        if(fetchedSession == null) {
            log.debug("session does not exist in the database");
            throw new TourGuideSignOutFailedException("session does not exist in the database");
        }

        if(fetchedSession.getSessionState() == GeneralUtilities.getSessionState("INACTIVE")){
            log.debug("provided session is inactive");
            throw new TourGuideSignOutFailedException("provided session is inactive");
        }

        User fetchedUser = userRepository.getUserById(fetchedSession.getUserId());

        if(fetchedUser == null) {
            log.debug("the user associated with the session does not exist in the database");
            throw new TourGuideSignOutFailedException("the user associated with the session does not exist in the database");
        }

        try {
            sessionRepository.setAllSessionsInactiveByUserId(fetchedUser.getId());
        } catch (RuntimeException e) {
            log.debug(e.getMessage());
            throw new TourGuideSignOutFailedException(e.getMessage());
        }

        return (Boolean) true;

    }

}
