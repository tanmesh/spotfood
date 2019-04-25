package com.tanmesh.splatter.authentication;
import com.tanmesh.splatter.dao.AuthDAO;
import com.tanmesh.splatter.entity.Auth;
import com.tanmesh.splatter.entity.User;
import java.util.UUID;

public class AuthService {
    private AuthDAO authDao;

    public AuthService() {
    }

    public AuthService(AuthDAO authDao) {
        this.authDao = authDao;
    }

    public void addNewAccessToken(User user) {
        String emailId = user.getEmailId();
        String token =  UUID.randomUUID().toString();

        // add in local cache
        AccessTokenService.addNewAccessToken(emailId, token);

        // add in database
        Auth auth  = new Auth(token, emailId);
        authDao.save(auth);
    }

    public String getAccessToken(User user) {
        String emailId = user.getEmailId();
        // first get from local cache
        String token = AccessTokenService.getAccessToken(emailId);
        if (token.isEmpty()) {
            token = authDao.getAccessToken(emailId);
            AccessTokenService.addNewAccessToken(emailId, token);
        }
        return token;
    }

    public String getUserEmailId(String accessToken) {
        String emailId = AccessTokenService.getUserEmailId(accessToken);
        if (emailId.isEmpty()) {
            emailId = authDao.getUserId(accessToken);
            AccessTokenService.addNewAccessToken(emailId, accessToken);
        }
        return emailId;
    }

    public boolean isValidToken(String accessToken) {
        String emailId = this.getUserEmailId(accessToken);
        if (!emailId.isEmpty()) {
            return true;
        }
        return false;
    }

    public boolean removeAccessToken(String emailId, String accessToken) {
        AccessTokenService.removeAccessToken(emailId,accessToken);
        authDao.removeAccessToken(emailId, accessToken);
        return true;
    }

    public UserSession getUserFromAccessToken(String token) {
        String emailId = this.getUserEmailId(token);
        UserSession userSession = new UserSession(token,emailId);
        return userSession;
    }
}

