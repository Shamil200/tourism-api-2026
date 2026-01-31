package lk.tourism.tourism_api_2026.utilities;

import lk.tourism.tourism_api_2026.model.enums.CredentialsState;
import lk.tourism.tourism_api_2026.model.enums.SessionState;
import lk.tourism.tourism_api_2026.model.enums.UserType;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GeneralUtilities {

    private static final Map<String, UserType> VALID_USER_TYPES = Map.of(
            UserType.ADMIN.toString(), UserType.ADMIN,
            UserType.TOUR_GUIDE.toString(), UserType.TOUR_GUIDE
    );

    private static final Map<String, CredentialsState> VALID_CREDENTIALS_STATES = Map.of(
            CredentialsState.ACTIVE.toString(), CredentialsState.ACTIVE,
            CredentialsState.INACTIVE.toString(), CredentialsState.INACTIVE
    );

    private static final Map<String, SessionState> VALID_SESSION_STATES = Map.of(
            SessionState.ACTIVE.toString(), SessionState.ACTIVE,
            SessionState.INACTIVE.toString(), SessionState.INACTIVE
    );

    private static final String PATTERN = "^[a-z0-9._%+-]+@[a-z0-9.-]+$" ;
    private static final Pattern compiledPattern = Pattern.compile(PATTERN);

    public static Boolean isValidUserType(String userType){
        return VALID_USER_TYPES.containsKey(userType);
    }

    public static UserType getUserType(String userType){
        return VALID_USER_TYPES.get(userType);
    }

    public static Boolean isValidCredentialsState(String credentialsState){
        return VALID_CREDENTIALS_STATES.containsKey(credentialsState);
    }

    public static CredentialsState getCredentialsState(String credentialsState){
        return VALID_CREDENTIALS_STATES.get(credentialsState);
    }

    public static Boolean isValidSessionState(String sessionState){
        return VALID_SESSION_STATES.containsKey(sessionState);
    }

    public static SessionState getSessionState(String sessionState){
        return VALID_SESSION_STATES.get(sessionState);
    }

    public static boolean isValidUsernameFormat(String username) {
        if (username == null || username.isEmpty()) {
            return false;
        }
        Matcher matcher = compiledPattern.matcher(username);
        return matcher.matches();
    }

}
