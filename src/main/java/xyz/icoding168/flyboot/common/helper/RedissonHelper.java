package xyz.icoding168.flyboot.common.helper;

import org.redisson.Redisson;
import org.redisson.api.RMap;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.redisson.config.SingleServerConfig;

public class RedissonHelper {

    private static RedissonClient redisson;

    static {
        Config config = new Config();

        SingleServerConfig singleServerConfig = config.useSingleServer();
        String schema = "redis://";
        singleServerConfig.setAddress(schema + "localhost" + ":" + "6379");
        singleServerConfig.setDatabase(0);
        singleServerConfig.setPassword("6379");

        // 其他配置项都先采用默认值
        redisson = Redisson.create(config);
    }

    public static RedissonClient getRedissonClient(){
        return redisson;
    }

    public static void main(String[] args){
        RedissonClient client = RedissonHelper.getRedissonClient();

        RMap rm = client.getMap("test");
        rm.put("foo","bar");

        System.out.println(rm.get("foo"));
    }



}
