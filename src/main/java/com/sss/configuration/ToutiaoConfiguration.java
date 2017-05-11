package com.sss.configuration;

import com.sss.interceptor.LoginRequiredInterceptor;
import com.sss.interceptor.PassportInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * Created by Shusheng Shi on 2017/5/10 22:28.
 */

/**
 * 在MVC Java编程配置方式下，若想对默认配置进行定制，可以自己实
 * 现 WebMvcConfigurer 接口，或者继承 WebMvcConfigurerAdapter 类并覆写需要定制的方法
 *
 * 可以配置处理器拦截器 HandlerInterceptors ，并配置它拦截所有进入容器的请求
 */
@Component
public class ToutiaoConfiguration extends WebMvcConfigurerAdapter {

    @Autowired
    private PassportInterceptor passportInterceptor;

    @Autowired
    private LoginRequiredInterceptor loginRequiredInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //TODO:回调设计模式研究
        registry.addInterceptor(passportInterceptor);
//      访问setting*页面时才调用此拦截器
        registry.addInterceptor(loginRequiredInterceptor).addPathPatterns("/setting*");
        super.addInterceptors(registry);
    }
}
