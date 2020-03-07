package xyz.icoding168.flyboot.service;


import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import xyz.icoding168.flyboot.common.PaginationResponse;
import xyz.icoding168.flyboot.common.PaginationResponseBuilder;
import xyz.icoding168.flyboot.common.UncheckedException;
import xyz.icoding168.flyboot.common.helper.IdGenerator;
import xyz.icoding168.flyboot.dao.BlogDao;
import xyz.icoding168.flyboot.model.Blog;
import xyz.icoding168.flyboot.request.BlogRequest;

import java.util.Date;
import java.util.List;

@Service
public class BlogService {

    private static final Logger logger = LogManager.getLogger();

    @Autowired
    private BlogDao blogDao;

    @Autowired
    private JwtService jwtService;

    public Blog findByBlogId(String blogId){
        return blogDao.findByBlogId(blogId);
    }

    public PaginationResponse<Blog> findByPage(BlogRequest blogRequest){
        try{
            return new PaginationResponseBuilder(){
                @Override
                public int count() {
                    return blogDao.count(blogRequest);
                }

                @Override
                public List find() {
                    return blogDao.find(blogRequest);
                }
            }.handlePaginationRequest(blogRequest);

        }catch(UncheckedException e){
            throw e;
        }
        catch(Exception e){
            throw new UncheckedException(e);
        }
    }

    private void validate(Blog blog){
        if(StringUtils.isBlank(blog.getTitle())){
            throw new UncheckedException("title 不能为空");
        }

        if(StringUtils.isBlank(blog.getContent())){
            throw new UncheckedException("content 不能为空");
        }
    }

    public Blog create(Blog blog){
        validate(blog);

        Date now = DateTime.now().toDate();

        blog.setCreateTime(now);
        blog.setUpdateTime(now);

        String blogId = IdGenerator.generateId();
        blog.setBlogId(blogId);

        blog.setSort(0);

        blogDao.create(blog);

        return blog;
    }

    public void update(Blog blog){
        validate(blog);

        blog.setUpdateTime(new Date());

        if(StringUtils.isBlank(blog.getBlogId())){
            throw new UncheckedException("blogId 不能为空");
        }

        blogDao.update(blog);
    }

}
