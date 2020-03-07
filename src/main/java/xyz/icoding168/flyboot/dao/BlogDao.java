package xyz.icoding168.flyboot.dao;

import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;
import xyz.icoding168.flyboot.model.Blog;
import xyz.icoding168.flyboot.model.User;
import xyz.icoding168.flyboot.request.BlogRequest;

import java.util.List;

@Repository
public interface BlogDao {

    int count(BlogRequest blogRequest);

    List<User> find(BlogRequest blogRequest);

    void create(Blog blog);

    void update(Blog blog);

    Blog findByBlogId(String blogId);

    @Update("update blog set state=#{state} where blog_id=#{blogId}")
    void updateState(Blog blog);
}
