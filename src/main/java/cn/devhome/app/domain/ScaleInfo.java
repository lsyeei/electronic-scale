package cn.devhome.app.domain;

import cn.devhome.app.service.ScaleProxy;
import lombok.Data;

/**
 * ScaleInfo
 *
 * @author lishiying
 * @version 1.0.0
 * @date 2023-12-25 16:09
 **/
@Data
public class ScaleInfo {
    /**
     * 电子秤标识代码
     */
    private String code;
    /**
     * 电子秤名称
     */
    private String name;
    /**
     * 解析脚本文件路径
     */
    private String script;
    /**
     * 代理
     */
    private transient ScaleProxy proxy;
}
