package cn.devhome.app.view;

import javafx.scene.control.Alert;

/**
 * 提示对话框
 * @author lishiying
 * @date 2022/8/5 005
 */
public class AlertDlg {
    private static final Alert dialog = new Alert(Alert.AlertType.NONE);
    public static void message(Alert.AlertType type, String message){
        dialog.setAlertType(type);
//        dialog.setContentText(message);
        dialog.setHeaderText(message);
        dialog.show();
    }
    public static void message(String message){
        message(Alert.AlertType.INFORMATION, message);
    }
}
