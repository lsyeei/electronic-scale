package cn.devhome.app;
import de.felixroske.jfxsupport.AbstractJavaFxApplicationSupport;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import cn.devhome.app.view.CustomSplash;
import cn.devhome.app.view.ModelView;
import cn.devhome.app.view.MySystemTray;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author lishiying
 * @date 2022/8/2 002
 */
@SpringBootApplication
public class ElectronicScaleApplication extends AbstractJavaFxApplicationSupport {

    public static void main(String[] args) {
        launch(ElectronicScaleApplication.class, ModelView.class, new CustomSplash(), args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        super.start(stage);
        stage.getIcons().add(new Image("/images/tray1.png"));
        stage.setTitle("电子秤服务配置");
        stage.setResizable(false);
        stage.initStyle(StageStyle.UNDECORATED);
        stage.setOnCloseRequest(e->{
            stage.hide();
        });
        // 创建托盘图标
        MySystemTray.getInstance(stage);
    }

}
