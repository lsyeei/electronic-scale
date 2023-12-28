package cn.devhome.app.view;

import javafx.util.StringConverter;
import cn.devhome.app.domain.PairData;

/**
 * @author lishiying
 * @date 2022/8/4 004
 */
public class ComboDataConverter extends StringConverter<PairData> {
    @Override
    public String toString(PairData object) {
        return object.getName();
    }

    @Override
    public PairData fromString(String string) {
        return null;
    }
}
