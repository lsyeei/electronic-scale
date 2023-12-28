package cn.devhome.app.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 电子秤数据
 * @author lishiying
 * @date 2022/8/23 023
 */
@Data
@AllArgsConstructor
@Accessors(chain = true)
public class ScaleData {
    private Integer len;
    private byte[] data;
}
