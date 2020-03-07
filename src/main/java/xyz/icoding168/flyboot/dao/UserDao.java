package xyz.icoding168.flyboot.dao;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;
import xyz.icoding168.flyboot.model.User;
import xyz.icoding168.flyboot.request.UserRequest;

import java.util.List;

@Repository
public interface UserDao {

    int count(UserRequest userRequest);

    List<User> find(UserRequest userRequest);

    @Select("select user_id userId,username,password,password_salt passwordSalt from user where username=#{username}")
    User findUserWithPasswordByUsername(String username);

    @Update("update user set last_login_time=#{time} where user_id=#{userId}")
    void updateLastLoginTime(@Param("userId") String userId, @Param("time") String time);

    @Insert("insert into user(user_id,username,password,password_salt,create_time,update_time) " +
            "values(#{userId},#{username},#{password},#{passwordSalt},#{createTime},#{updateTime})")
    void create(User user);

    @Select("select user_id userId,username from user where user_id=#{userId}")
    User findByUserId(String userId);
}
