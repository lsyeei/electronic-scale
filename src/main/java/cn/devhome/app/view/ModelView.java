package cn.devhome.app.view;


import de.felixroske.jfxsupport.AbstractFxmlView;
import de.felixroske.jfxsupport.FXMLView;
import javafx.scene.Parent;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;

/**
 * @author lishiying
 * @date 2022/8/4 004
 */
@FXMLView(value = "/ModelView.fxml", title = "电子秤服务配置")
public class ModelView extends AbstractFxmlView {
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        super.setApplicationContext(applicationContext);
        System.out.println("--------setApplicationContext--------");
    }

    @Override
    public Parent getView() {
        return super.getView();
    }
}
