package cn.devhome.app.config;

import org.springframework.boot.SpringBootConfiguration;
import org.springframework.web.servlet.config.annotation.*;


/**
 *处理跨域请求
 * @author lishiying
 * @version 1.0
 * @date 2022/08/04 17:03
 */

@SpringBootConfiguration
public class WebMvcConfig implements WebMvcConfigurer  {
    @Override
    public void addCorsMappings(CorsRegistry corsRegistry){
        /**
         * 所有请求都允许跨域，使用这种配置就不需要
         * 在interceptor中配置header了
         */
        corsRegistry.addMapping("/**")
                .allowCredentials(true)
//                .allowedOrigins("*")
                .allowedOriginPatterns("*")
                .allowedMethods("POST", "GET", "PUT", "PATCH", "DELETE","OPTIONS")
                .maxAge(3600);
    }

    /**
     * 拦截器加载的时间点在springcontext之前，
     * 需要在此将拦截器加入Spring的容器，
     * 拦截器中使用的其它Service才能自动注入
     * return日志拦截器
    Bean
    public LogInterceptor logInterceptor(){
        return new LogInterceptor();
    }
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //添加拦截器，记录日志
//        registry.addInterceptor(logInterceptor()).order(1);

    }


}
