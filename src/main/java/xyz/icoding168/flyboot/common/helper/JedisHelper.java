package xyz.icoding168.flyboot.common.helper;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

public class JedisHelper {

    private static JedisPool pool = new JedisPool(new JedisPoolConfig(), "localhost");

    public static Jedis getJedis(){
        Jedis jedis = pool.getResource();

        jedis.auth("6379");

        return jedis;
    }

    public static void main(String[] args){
        Jedis jedis = JedisHelper.getJedis();

        jedis.set("foo", "bar");
        String foobar = jedis.get("foo");

        System.out.println(foobar);

        jedis.close();
    }

}
