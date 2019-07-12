package com.tanmesh.splatter.scrachpad.Redis;

import redis.clients.jedis.Jedis;

/**
 * Created by tanmesh
 * Date: 2019-07-12
 * Time: 01:39
 */
public class TestJesisNew {
    public static void main(String[] args) {
        Jedis jedis = new Jedis("localhost");
        //adding a new key
        jedis.set("counter", "2345");
        //getting the key value
        System.out.println(jedis.get("counter"));
    }
}
