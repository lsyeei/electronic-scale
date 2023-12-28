package cn.devhome.app.utils;

import cn.devhome.app.domain.EventData;
import cn.devhome.app.domain.ScaleData;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * @author lishiying
 * @date 2022/8/5 005
 */
@Component
public class EventPublisher implements ApplicationContextAware {
    private static ApplicationContext context;

    /**
     * 发布事件数据
     * @param data 事件数据
     */
    public static void publish(EventData data){
        context.publishEvent(data);
    }
    /**
     * 发布事件数据
     * @param data 事件数据
     */
    public static void publish(ScaleData data){
        context.publishEvent(data);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        EventPublisher.context = applicationContext;
    }
}
