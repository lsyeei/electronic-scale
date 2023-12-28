package cn.devhome.app.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import cn.devhome.app.enums.EventDataType;

/**
 * Spring 事件数据
 * @author lishiying
 * @date 2022/8/5 005
 */
@Data
@AllArgsConstructor
public class EventData {
    /**
     * 事件类型
     */
    private EventDataType type;
    /**
     * 数据
     */
    private Object data;
}
