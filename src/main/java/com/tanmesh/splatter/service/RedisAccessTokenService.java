package com.tanmesh.splatter.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tanmesh.splatter.authentication.UserSession;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * Created by tanmesh
 * Date: 2019-07-14
 * Time: 07:21
 */
public class RedisAccessTokenService implements AccessTokenService {
    /*
        un --> userName
        at --> accessToken
    */

    private ObjectMapper objectMapper;
    private JedisPool jedisPool;

    public RedisAccessTokenService() {
        this.objectMapper = new ObjectMapper();
        this.jedisPool = new JedisPool(new JedisPoolConfig(), "localhost");
    }

    @Override
    public boolean saveAccessToken(UserSession userSession) {
        boolean flag = false;
        try (Jedis jedis = jedisPool.getResource()) {
            String emailId = String.valueOf(userSession.getEmailId());
            String emailIdKey = getUserNameKey(emailId);
            String agentJson = objectMapper.writeValueAsString(userSession);
            jedis.set(emailIdKey, agentJson);                           // {emailId, json}

            String accessToken = userSession.getAccessToken();
            jedis.set(getAccessTokenKey(accessToken), agentJson);       // {token, json}
            flag = true;
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return flag;
    }

    private String getUserNameKey(String emailId) {
        return "unToAt:" + emailId;
    }

    private String getAccessTokenKey(String accessToken) {
        return "atToUn:" + accessToken;
    }

    @Override
    public UserSession getUserFromAccessToken(String accessToken) {
        UserSession userSession = null;
        try (Jedis jedis = jedisPool.getResource()) {
            String agentJson = jedis.get(getAccessTokenKey(accessToken));
            userSession = objectMapper.readValue(agentJson, UserSession.class);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return userSession;
    }

    @Override
    public boolean isValidToken(String accessToken) {
        UserSession userSession = getUserFromAccessToken(accessToken);
        return userSession != null;
    }

    @Override
    public boolean removeAccessToken(String accessToken) {
        boolean flag = false;
        try (Jedis jedis = jedisPool.getResource()) {
            jedis.del(getAccessTokenKey(accessToken));
            flag = true;
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return flag;
    }

    @Override
    public void removeUser(String userName) {

    }
}
