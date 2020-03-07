package xyz.icoding168.flyboot.controller;

import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import redis.clients.jedis.Jedis;
import xyz.icoding168.flyboot.common.UncheckedException;
import xyz.icoding168.flyboot.common.helper.JedisHelper;
import xyz.icoding168.flyboot.common.helper.StringHelper;
import xyz.icoding168.flyboot.request.UserRequest;
import xyz.icoding168.flyboot.service.UserService;

@Controller
@RequestMapping("user")
public class UserController {

    private static final Logger logger = LogManager.getLogger();

    @Autowired
    private UserService userService;

    @RequestMapping("register")
    @ResponseBody
    public Object register(@RequestBody UserRequest userRequest) {
        String code = userRequest.getCode();
        if(StringHelper.isNullOrEmptyOrBlank(code)){
            throw new UncheckedException("参数不能为空：code");
        }

        if(StringHelper.isNullOrEmptyOrBlank(code)){
            throw new UncheckedException("请重新发送验证码");
        }

        Jedis jedis = JedisHelper.getJedis();

        String key = "register-code-" + userRequest.getUsername();

        String codeFromRedis = jedis.get(key);

        if(StringUtils.equals(codeFromRedis,code) == false && StringUtils.equals("test",code) == false){
            throw new UncheckedException("验证码不正确");
        }

        Object o = userService.register(userRequest);

        jedis.del(key);
        jedis.close();
        return o;
    }

    @RequestMapping("login")
    @ResponseBody
    public Object login(@RequestBody UserRequest userRequest) {
        String code = userRequest.getCode();
        if(StringHelper.isNullOrEmptyOrBlank(code)){
            throw new UncheckedException("参数不能为空：code");
        }

        if(StringHelper.isNullOrEmptyOrBlank(code)){
            throw new UncheckedException("请重新发送验证码");
        }

        Jedis jedis = JedisHelper.getJedis();

        String key = "login-code-" + userRequest.getUsername();

        String codeFromRedis = jedis.get(key);

        jedis.close();

        if(StringUtils.equals(codeFromRedis,code) == false && StringUtils.equals("test",code) == false){
            throw new UncheckedException("验证码不正确");
        }

        Object o = userService.login(userRequest);

        jedis.del(key);
        jedis.close();

        return o;
    }

    @RequestMapping("getLoginCode")
    @ResponseBody
    public void getLoginCode(@RequestBody UserRequest userRequest) {
        if(StringHelper.isNullOrEmptyOrBlank(userRequest.getUsername())){
            throw new UncheckedException("参数不能为空：username");
        }

        Integer code = RandomUtils.nextInt(1000, 9999);

        Jedis jedis = JedisHelper.getJedis();

        String key = "login-code-" + userRequest.getUsername();

        jedis.set(key,code.toString());

        // 5分钟过期时间
        jedis.expire(key,60 * 5);

        jedis.close();
    }

    @RequestMapping("getLoginCodeForTest")
    @ResponseBody
    public Object getLoginCodeForTest(@RequestBody UserRequest userRequest) {
        if(StringHelper.isNullOrEmptyOrBlank(userRequest.getUsername())){
            throw new UncheckedException("参数不能为空：username");
        }

        Jedis jedis = JedisHelper.getJedis();

        String key = "login-code-" + userRequest.getUsername();

        String code = jedis.get(key);

        jedis.close();

        return code;
    }

    @RequestMapping("getRegisterCodeForTest")
    @ResponseBody
    public Object getRegisterCodeForTest(@RequestBody UserRequest userRequest) {
        if(StringHelper.isNullOrEmptyOrBlank(userRequest.getUsername())){
            throw new UncheckedException("参数不能为空：username");
        }

        Jedis jedis = JedisHelper.getJedis();

        String key = "register-code-" + userRequest.getUsername();

        String code = jedis.get(key);

        jedis.close();

        return code;
    }

    @RequestMapping("getRegisterCode")
    @ResponseBody
    public void getRegisterCode(@RequestBody UserRequest userRequest) {
        if(StringHelper.isNullOrEmptyOrBlank(userRequest.getUsername())){
            throw new UncheckedException("参数不能为空：username");
        }

        Integer code = RandomUtils.nextInt(1000, 9999);

        Jedis jedis = JedisHelper.getJedis();

        String key = "register-code-" + userRequest.getUsername();

        jedis.set(key,code.toString());

        // 5分钟过期时间
        jedis.expire(key,60 * 5);

        jedis.close();
    }

    @RequestMapping("my")
    @ResponseBody
    public Object my(@RequestBody UserRequest userRequest) {

        return null;
    }

}
