package xyz.icoding168.flyboot.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import xyz.icoding168.flyboot.common.UncheckedException;
import xyz.icoding168.flyboot.common.helper.StringHelper;

import java.util.HashMap;
import java.util.Map;

@Service
public class JwtService {

    @Value("${jwt.secret}")
    private String secret;

    public String createToken(String userId){
        try {
            if(StringHelper.isNullOrEmptyOrBlank(secret)){
                throw new UncheckedException("不能为空：secret");
            }

            if(StringHelper.isNullOrEmptyOrBlank(userId)){
                throw new UncheckedException("不能为空：userId");
            }

            Algorithm algorithm = Algorithm.HMAC256(secret);
            String token = JWT.create()
                    .withClaim("userId",userId)
                    .sign(algorithm);

            return token;
        } catch (Exception e){
            throw new UncheckedException(e);
        }
    }

    public static Map<String, String> decodeToken(String token){
        try{
            DecodedJWT jwt = JWT.decode(token);

            Map<String, Claim> claims = jwt.getClaims();

            if(claims == null || claims.size() == 0){
                return new HashMap<>();
            }

            Map<String,String> map = new HashMap<>();
            for(String key:claims.keySet()){
                map.put(key,claims.get(key).asString());
            }

            return map;
        }catch(Exception e){
            throw new UncheckedException("解析 token 出现异常");
        }
    }


    public static void main(String[] args){
        String token = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJ1c2VySWQiOiIyMDE5MTIwNDEyMDIwNTUyODI4NTU3NzYyNTYifQ.Z0IGFiQefCN7phmduDlc7Ranw4ItDl1Fw-pKDK3oFPY";

        System.out.println(decodeToken(token));
    }
}
