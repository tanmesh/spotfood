package com.tanmesh.spotfood.scrachpad.Redis;
import redis.clients.jedis.Jedis;
/**
 * Created by tanmesh
 * Date: 2019-07-12
 * Time: 00:59
 */

public class TestJedis {
    public static void main(String[] args) {
        Jedis jedis = new Jedis("localhost");
        System.out.println(jedis.get("fpp"));
        jedis.incr("fpp");
        System.out.println(jedis.get("fpp"));
    }
}

