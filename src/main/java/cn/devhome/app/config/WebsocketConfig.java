package cn.devhome.app.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

/**
 * @author lishiying
 * @date 2022/8/4 004
 */
@Configuration
public class WebsocketConfig {
    /**
     * 自动注册@ServerEndpoint注解
     * @return websocket端口
     */
    @Bean
    public ServerEndpointExporter serverEndpointExporter(){
        return new ServerEndpointExporter();
    }
}
