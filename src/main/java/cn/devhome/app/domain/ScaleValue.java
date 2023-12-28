package cn.devhome.app.domain;

import lombok.Data;

/**
 * 电子秤值
 * @author lishiying
 * @date 2022/8/2 002
 */
@Data
public class ScaleValue {
    /**
     * 电子秤读数
     */
    private String value;
    /**
     * 单位
     */
    private String unit;
}
