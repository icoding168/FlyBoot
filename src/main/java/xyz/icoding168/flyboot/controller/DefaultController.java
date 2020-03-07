package xyz.icoding168.flyboot.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import xyz.icoding168.flyboot.common.PaginationResponse;
import xyz.icoding168.flyboot.model.Blog;
import xyz.icoding168.flyboot.request.BlogRequest;
import xyz.icoding168.flyboot.service.BlogService;

@Controller
public class DefaultController {

    private static final Logger logger = LogManager.getLogger();

    @Autowired
    private BlogService blogService;

    @RequestMapping("index")
    public String index() {

        return "index";

    }

    @RequestMapping("blog-list.html")
    public String list(@RequestBody(required = false) BlogRequest blogRequest, Model model){
        if(blogRequest == null){
            blogRequest = new BlogRequest();
        }

        PaginationResponse<Blog> paginationResponse = blogService.findByPage(blogRequest);

        model.addAttribute("body",paginationResponse);

        return "blog-list";
    }

    @RequestMapping("blog-create.html")
    public String create(String blogId,Model model){
        Blog blog = blogService.findByBlogId(blogId);

        if(blog == null){
            blog = new Blog();
        }

        model.addAttribute("blog",blog);

        return "blog-create";
    }


    @RequestMapping("{path}.html")
    public String path(@PathVariable("path")String path, Model model){

        return path;
    }
}
