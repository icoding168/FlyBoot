package xyz.icoding168.flyboot.controller;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import xyz.icoding168.flyboot.dao.BlogDao;
import xyz.icoding168.flyboot.model.Blog;
import xyz.icoding168.flyboot.request.BlogRequest;
import xyz.icoding168.flyboot.service.BlogService;

@Controller
@RequestMapping("blog")
public class BlogController {

    private static final Logger logger = LogManager.getLogger();

    @Autowired
    private BlogService blogService;

    @Autowired
    private BlogDao blogDao;

    @RequestMapping("save")
    @ResponseBody
    public Object save(@RequestBody Blog blog) {
        if(StringUtils.isBlank(blog.getBlogId())){
            Blog data = blogService.create(blog);
            return data.getBlogId();
        }else {
            blogService.update(blog);
            return blog.getBlogId();
        }
    }

    @RequestMapping("findByPage")
    @ResponseBody
    public Object findByPage(@RequestBody(required = false) BlogRequest blogRequest) {
        if(blogRequest == null){
            blogRequest = new BlogRequest();
        }

        return blogService.findByPage(blogRequest);
    }

    @RequestMapping("updateState")
    @ResponseBody
    public void updateState(@RequestBody Blog blog) {
        blogDao.updateState(blog);
    }





}
