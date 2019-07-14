package com.tanmesh.splatter.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tanmesh.splatter.authentication.UserSession;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.UUID;

/**
 * Created by tanmesh
 * Date: 2019-07-14
 * Time: 07:21
 */
public class RedisAccessTokenService implements AccessTokenService{

    private ObjectMapper objectMapper = new ObjectMapper();
    private JedisPool jedisPool;

    @Override
    public UserSession createAccessToken(UserSession userSession) {
        try(Jedis jedis = jedisPool.getResource()) {
            String accessToken = UUID.randomUUID().toString();
            System.out.println("AccessToken is " + accessToken);
            userSession.setAccessToken(accessToken);

//            String emailIdKey = getUserNameKey(String.valueOf(userSession.getEmailId()));
//            String agentJson = objectMapper.writeValueAsString(userSession);
//            jedis.set(emailIdKey, agentJson);

            userSession = saveAccessToken(userSession);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return userSession;
    }

    @Override
    //why it is returning?
    public UserSession saveAccessToken(UserSession userSession) {
        try(Jedis jedis = jedisPool.getResource()) {
            String emailIdKey = getUserNameKey(String.valueOf(userSession.getEmailId()));
            String agentJson = objectMapper.writeValueAsString(userSession);
            jedis.set(emailIdKey, agentJson);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return userSession;
    }

    @Override
    public UserSession getUserFromAccessToken(String accessToken) {
        try(Jedis jedis = jedisPool.getResource()) {
            String emailId = "";
            if (jedis.exists(getAccessTokenKey(accessToken))) {
                emailId = jedis.get(getAccessTokenKey(accessToken));
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    @Override
    public boolean isValidToken(String accessToken) {
        return false;
    }

    @Override
    public void removeAccessToken(String accessToken) {

    }

    @Override
    public void removeUser(String userName) {

    }

    private String getUserNameKey(String emailId) {
        return "unToAt:" + emailId;
    }

    private String getAccessTokenKey(String newAccessToken) {
        return "atToUn:" + newAccessToken;
    }

    public static void main(String[] args) {
        Jedis jedis = new Jedis("localhost");
        AccessTokenService accessTokenService = null;
        UserSession agent = accessTokenService.createAccessToken(new UserSession("tanm@gmail.com"));
        agent = accessTokenService.saveAccessToken(agent);
    }
}
