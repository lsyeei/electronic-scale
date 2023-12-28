package cn.devhome.app.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Objects;

/**
 * @author lishiying
 * @date 2022/8/4 004
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PairData {
    /**
     * 显示的名称
     */
    private String name;
    /**
     * 值
     */
    private Object value;

    @Override
    public boolean equals(Object o) {
        if (this == o){
            return true;
        }
        if (o == null || getClass() != o.getClass()){
            return false;
        }
        PairData pairData = (PairData) o;
        return Objects.equals(name, pairData.name) &&
                Objects.equals(value, pairData.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, value);
    }

}
