package xyz.icoding168.flyboot.service;


import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import redis.clients.jedis.Jedis;
import xyz.icoding168.flyboot.common.PaginationResponse;
import xyz.icoding168.flyboot.common.PaginationResponseBuilder;
import xyz.icoding168.flyboot.common.UncheckedException;
import xyz.icoding168.flyboot.common.helper.IdGenerator;
import xyz.icoding168.flyboot.common.helper.JedisHelper;
import xyz.icoding168.flyboot.common.helper.StringHelper;
import xyz.icoding168.flyboot.dao.UserDao;
import xyz.icoding168.flyboot.model.User;
import xyz.icoding168.flyboot.request.UserRequest;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class UserService {

    private static final Logger logger = LogManager.getLogger();

    @Autowired
    private UserDao userDao;

    @Autowired
    private JwtService jwtService;

    public PaginationResponse<User> findByPage(UserRequest userRequest){
        try{
            return new PaginationResponseBuilder(){
                @Override
                public int count() {
                    return userDao.count(userRequest);
                }

                @Override
                public List find() {
                    return userDao.find(userRequest);
                }
            }.handlePaginationRequest(userRequest);

        }catch(UncheckedException e){
            throw e;
        }
        catch(Exception e){
            throw new UncheckedException(e);
        }
    }

    private void validateFormForRegister(UserRequest userRequest){
        String username = userRequest.getUsername();
        if(StringHelper.isNullOrEmptyOrBlank(username)){
            throw new UncheckedException("参数不能为空：username");
        }

        String password = userRequest.getPassword();
        if(StringHelper.isNullOrEmptyOrBlank(password)){
            throw new UncheckedException("参数不能为空：password");
        }
    }

    private void validateFormForLogin(UserRequest userRequest){
        String username = userRequest.getUsername();
        if(StringHelper.isNullOrEmptyOrBlank(username)){
            throw new UncheckedException("参数不能为空：username");
        }

        String password = userRequest.getPassword();
        if(StringHelper.isNullOrEmptyOrBlank(password)){
            throw new UncheckedException("参数不能为空：password");
        }

    }

    @Transactional
    public Object register(UserRequest userRequest){
        validateFormForRegister(userRequest);

        User user = userDao.findUserWithPasswordByUsername(userRequest.getUsername());
        if(user != null){
            throw new UncheckedException("用户名已存在");
        }

        user = new User();

        String userId = IdGenerator.generateId();
        user.setUserId(userId);
        user.setUsername(userRequest.getUsername());

        Integer salt = RandomUtils.nextInt(100000,999999);
        String passwordWithSalt = userRequest.getPassword() + salt.toString();
        String password = DigestUtils.sha512Hex(DigestUtils.sha512Hex(passwordWithSalt));
        user.setPassword(password);
        user.setPasswordSalt(salt.toString());

        DateTime now = DateTime.now();
        user.setCreateTime(now.toDate());
        user.setUpdateTime(now.toDate());
        userDao.create(user);

        return login(userRequest);
    }

    public Object login(UserRequest userRequest){
        validateFormForLogin(userRequest);

        User user = userDao.findUserWithPasswordByUsername(userRequest.getUsername());
        if(user == null){
            throw new UncheckedException("该账号不存在");
        }

        String passwordWithSalt = userRequest.getPassword() + user.getPasswordSalt();
        String password = DigestUtils.sha512Hex(DigestUtils.sha512Hex(passwordWithSalt));
        if(StringUtils.equals(password,user.getPassword()) == false){
            throw new UncheckedException("密码不正确");
        }

        String token = jwtService.createToken(user.getUserId());

        DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
        userDao.updateLastLoginTime(user.getUserId(), DateTime.now().toString(formatter));

        Map map = new HashMap();
        map.put("token",token);

        Jedis jedis = JedisHelper.getJedis();
        String key = "user-token-" + user.getUserId();
        jedis.set(key,token);

        // 过期时间一个月
        jedis.expire(key,60 * 60 * 24 * 30);
        jedis.close();

        return map;
    }
}
