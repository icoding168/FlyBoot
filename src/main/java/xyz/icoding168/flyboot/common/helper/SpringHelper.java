package xyz.icoding168.flyboot.common.helper;

import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.http.HttpServletRequest;

public class SpringHelper {


    public static <T> T getBean(Class<T> clazz, HttpServletRequest request){
        //通过该方法获得的applicationContext 已经是初始化之后的applicationContext 容器
        WebApplicationContext applicationContext = WebApplicationContextUtils.getRequiredWebApplicationContext(request.getServletContext());
        return applicationContext.getBean(clazz);
    }
}
