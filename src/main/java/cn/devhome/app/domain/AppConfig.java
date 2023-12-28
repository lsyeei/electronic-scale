package cn.devhome.app.domain;

import lombok.Data;


/**
 * 程序配置
 * @author lishiying
 * @date 2022/8/3 003
 */
@Data
public class AppConfig extends Persistable {
    /**
     * 电子秤信息
     */
    private ScaleInfo scale;
    /**
     * 串口配置
     */
    private ComPortConfig portConfig;

    /**
     * 更新电子秤信息
     * @param info 秤信息
     */
    public void updateScaleInfo(ScaleInfo info){
        if (info == null){
            return;
        }
        scale = info;
    }
}
