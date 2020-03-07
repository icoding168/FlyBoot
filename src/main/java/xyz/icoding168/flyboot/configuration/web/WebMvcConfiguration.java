package xyz.icoding168.flyboot.configuration.web;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.http.HttpHeaders;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.web.servlet.config.annotation.*;
import org.springframework.web.servlet.view.freemarker.FreeMarkerViewResolver;
import xyz.icoding168.flyboot.common.helper.HttpHelper;

import javax.servlet.Servlet;
import java.util.ArrayList;
import java.util.List;


@Configuration
@ConditionalOnClass({Servlet.class})
public class WebMvcConfiguration extends WebMvcConfigurerAdapter {

    @Autowired
    private JwtInterceptor jwtInterceptor;

    // 必须传 token
    public static final List<String> loginRequiredPathList = new ArrayList<>();

    // 不传 token，传了也不解析
    public static final List<String> loginExcludedPathList = new ArrayList<>();

    // 可以传 token，传了就会解析
    public static final List<String> loginOptionalPathList = new ArrayList<>();

    static{

        loginExcludedPathList.add("/**");


    }


    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        InterceptorRegistration ir = registry.addInterceptor(jwtInterceptor);

        if(CollectionUtils.isNotEmpty(loginRequiredPathList)){
            for(String path:loginRequiredPathList){
                ir.addPathPatterns(path);
            }
        }

        if(CollectionUtils.isNotEmpty(loginExcludedPathList)){
            for(String path:loginExcludedPathList){
                ir.excludePathPatterns(path);
            }
        }

        super.addInterceptors(registry);
    }

    @Bean
    public FreeMarkerViewResolver freemarkerViewResolver() {
        FreeMarkerViewResolver resolver = new FreeMarkerViewResolver();
        resolver.setCache(false);
        resolver.setPrefix("");
        resolver.setSuffix(".html");
        resolver.setContentType("text/html;charset=utf-8");
        return resolver;
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/resources/**").setCachePeriod(0);
    }



    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurerAdapter() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**").allowedOrigins("*")
                        .allowedMethods("*").allowedHeaders("*")
                        .allowCredentials(true)
                        .exposedHeaders(HttpHeaders.SET_COOKIE).maxAge(3600L);
            }
        };
    }

    /**
     * 替换掉 Spring 的 Jackson ObjectMapper
     * @param builder
     * @return
     */
    @Bean
    @Primary
    @ConditionalOnMissingBean(ObjectMapper.class)
    public ObjectMapper jacksonObjectMapper(Jackson2ObjectMapperBuilder builder)
    {
        return HttpHelper.objectMapper;
    }










}
