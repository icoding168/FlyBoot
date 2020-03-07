package xyz.icoding168.flyboot.configuration.web;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import redis.clients.jedis.Jedis;
import xyz.icoding168.flyboot.common.UncheckedException;
import xyz.icoding168.flyboot.common.helper.JedisHelper;
import xyz.icoding168.flyboot.common.helper.StringHelper;
import xyz.icoding168.flyboot.dao.UserDao;
import xyz.icoding168.flyboot.model.User;
import xyz.icoding168.flyboot.service.JwtService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@Component
public class JwtInterceptor implements HandlerInterceptor {

    @Autowired
    private UserDao userDao;

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        return checkToken(request);
    }

    public boolean checkToken(HttpServletRequest request) {
        try {
            String token = request.getHeader("Authorization");
            String path = request.getRequestURI();

            boolean loginOptional = false;
            for(String item:WebMvcConfiguration.loginOptionalPathList){
                if(path.contains(item)){
                    loginOptional = true;
                    break;
                }
            }

            if(loginOptional == true && StringHelper.isNullOrEmptyOrBlank(token)){
                return true;
            }

            if(loginOptional == false && StringHelper.isNullOrEmptyOrBlank(token)){
                throw new UncheckedException(HttpServletResponse.SC_UNAUTHORIZED, "缺少授权");
            }

            Map<String,String> tokenInfo = JwtService.decodeToken(token);

            String userId = tokenInfo.get("userId");

            Jedis jedis = JedisHelper.getJedis();

            String key = "user-token-" + userId;

            String tokenFromRedis = jedis.get(key);

            if(tokenFromRedis == null){
                throw new UncheckedException(HttpServletResponse.SC_UNAUTHORIZED, "token 已过期");
            }

            if(StringUtils.equals(tokenFromRedis,token) == false){
                throw new UncheckedException(HttpServletResponse.SC_UNAUTHORIZED, "token 不正确");
            }

            User user = userDao.findByUserId(userId);

            if(user == null){
                throw new UncheckedException(HttpServletResponse.SC_UNAUTHORIZED, "账号不存在");
            }

            request.setAttribute("userId", userId);

            return true;
        } catch (UncheckedException e) {
            throw e;
        } catch (Exception e) {
            throw new UncheckedException(HttpServletResponse.SC_UNAUTHORIZED, "解析 token 出现异常", e);
        }
    }


}
