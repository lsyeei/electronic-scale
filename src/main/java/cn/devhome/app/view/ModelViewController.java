package cn.devhome.app.view;

import cn.devhome.app.domain.*;
import cn.devhome.app.service.IScale;
import cn.devhome.app.service.ScaleMarket;
import cn.devhome.app.service.impl.ScaleService;
import cn.devhome.app.utils.CommPortUtils;
import cn.devhome.app.utils.PathUtils;
import de.felixroske.jfxsupport.FXMLController;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.event.EventListener;
import purejavacomm.SerialPort;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

/**
 * @author lishiying
 * @date 2022/8/4 004
 */
@Slf4j
@FXMLController
public class ModelViewController implements Initializable {
    @Autowired
    private ScaleMarket scaleMarket;
    /**
     * 电子秤
     */
    private IScale scale;
    /**
     * 配置
     */
    private AppConfig config;
    @Value("${config.path:/config.dat}")
    private String configFile;
    /**
     * 电子秤值
     */
    @FXML
    private Label scaleValue;
    /**
     * 电子秤名称
     */
    @FXML
    private Label scaleName;
    /**
     * 提示信息
     */
    @FXML
    private Label tips;
    @FXML
    private ComboBox<String> portName;
    @FXML
    private ComboBox<Integer> baud;
    @FXML
    private ComboBox<Integer> dataBits;
    @FXML
    private ComboBox<PairData> parity;
    @FXML
    private ComboBox<PairData> stopBits;
    @FXML
    private ComboBox<PairData> scaleType;
    @FXML
    private Button connectBtn;
    @FXML
    private Button disconnectBtn;
    /**
     * 电子秤上次的值
     */
    private String lastValue;

    @Override
    public String toString() {
        return super.toString();
    }

    public IScale getScale() {
        return scale;
    }

    /**
     * 初始化
     * @param location url
     * @param resources resources
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // 加载配置文件
        config = AppConfig.load(PathUtils.getCurrentPath() + configFile, AppConfig.class);
        boolean readyFlag = true;
        if (config == null) {
            defaultConfig();
            readyFlag = false;
        }else {
            ScaleInfo info = config.getScale();
            if (info != null){
                config.setScale(scaleMarket.getScaleByName(info.getName()));
            }
        }
        // 初始化UI
        initUI();
        // 自动连接电子秤
        if (readyFlag) {
            connectScale();
        }
    }

    /**
     * 连接电子秤
     */
    private void connectScale(){
        if (scale == null) {
            scale = new ScaleService(config.getScale());
        }else {
            scale.setScaleInfo(config.getScale());
        }
        setConnectStatus(scale.connect(config.getPortConfig()));
        PairData item = scaleType.getSelectionModel().getSelectedItem();
        scaleName.setText(item.getName());
    }

    /**
     * 设置串口连接状态
     * @param flag 状态
     */
    private void setConnectStatus(boolean flag) {
        connectBtn.setDisable(flag);
        disconnectBtn.setDisable(!flag);
    }

    /**
     * 初始化UI
     */
    private void initUI(){
        ComboDataConverter comboDataConverter = new ComboDataConverter();
        // 电子秤类型
        ObservableList<PairData> items = scaleType.getItems();
        for (ScaleInfo scaleInfo : scaleMarket.getAllScale()) {
            items.add(new PairData(scaleInfo.getName(), scaleInfo));
        }
        scaleType.setItems(items);
        scaleType.setConverter(comboDataConverter);
        ScaleInfo scale = config.getScale();
        if (scale != null) {
            scaleType.getSelectionModel().select(new PairData(scale.getName(), scale));
//            scaleName.setText(scale.getName());
        }

        // 波特率
        ObservableList<Integer> bauds = baud.getItems();
        bauds.addAll(ComPortConfig.allBaud());
        baud.setItems(bauds);
        baud.getSelectionModel().select(config.getPortConfig().getBaud());

        // 数据位
        ObservableList<Integer> dataBitList = dataBits.getItems();
        dataBitList.addAll(8,7,6,5);
        dataBits.setItems(dataBitList);
        dataBits.getSelectionModel().select(config.getPortConfig().getDataBits());

        // 校验
        items = parity.getItems();
        items.addAll(ComPortConfig.allParity());
        parity.setItems(items);
        parity.setConverter(comboDataConverter);
        this.parity.getSelectionModel().select(config.getPortConfig().obtainParity());

        // 停止位
        items = stopBits.getItems();
        items.addAll(ComPortConfig.allStopBits());
        stopBits.setItems(items);
        stopBits.setConverter(comboDataConverter);
        stopBits.getSelectionModel().select(config.getPortConfig().obtainStopBits());

        // 当前系统中的串口
        ArrayList<String> allComPort = CommPortUtils.findSystemAllComPort();
        ObservableList<String> ports = portName.getItems();
        ports.addAll(allComPort);
        portName.setItems(ports);
        portName.getSelectionModel().select(config.getPortConfig().getPortName());

        setConnectStatus(false);
    }

    /**
     * 设置默认配置
     */
    private void defaultConfig() {
        config = new AppConfig();
        ComPortConfig portConfig = new ComPortConfig();
        config.setPortConfig(portConfig);
        portConfig.setBaud(9600)
                .setDataBits(SerialPort.DATABITS_8)
                .setParity(SerialPort.PARITY_NONE)
                .setStopBits(SerialPort.STOPBITS_1);
    }

    /**
     * 点击连接按钮
     */
    public void connectBtnClick(){
        // 检查各个控件的值
        if (!checkInput()) {
            return;
        }
        // 保存服务配置
        config.save(PathUtils.getCurrentPath() + configFile);
        // 切换电子秤，并设置按钮状态
        connectScale();
    }

    /**
     * 点击断开连接按钮
     */
    public void disconnectBtnClick(){
        // 断开电子秤，并设置按钮状态
        setConnectStatus(!scale.disconnect());
    }

    /**
     * 校验用户输入
     * @return true 成功， false 失败
     */
    private boolean checkInput() {
        PairData scaleTypeValue = scaleType.getSelectionModel().getSelectedItem();
        if (scaleTypeValue == null){
            AlertDlg.message("请选择电子秤");
            return false;
        }
        Integer baudValue = baud.getSelectionModel().getSelectedItem();
        if (baudValue == null){
            AlertDlg.message("请选择波特率");
            return false;
        }
        Integer dataBitsValue = dataBits.getSelectionModel().getSelectedItem();
        if (dataBitsValue == null){
            AlertDlg.message("请选择数据位");
            return false;
        }
        PairData parityValue = parity.getSelectionModel().getSelectedItem();
        if (parityValue == null){
            AlertDlg.message("请选择校验方式");
            return false;
        }
        PairData stopBitsValue = stopBits.getSelectionModel().getSelectedItem();
        if (stopBitsValue == null){
            AlertDlg.message("请选择停止位");
            return false;
        }
        String portNameValue = portName.getSelectionModel().getSelectedItem();
        if (StringUtils.isEmpty(portNameValue)){
            AlertDlg.message("请选择串口");
            return false;
        }
        // 保存
        config.updateScaleInfo((ScaleInfo) scaleTypeValue.getValue());
        config.getPortConfig().setPortName(portNameValue)
                .setBaud(baudValue)
                .setDataBits(dataBitsValue)
                .setParity((Integer) parityValue.getValue())
                .setStopBits((Integer)stopBitsValue.getValue());
        return true;
    }

    /**
     * 处理通知数据
     * @param event 通知事件
     */
    @EventListener(EventData.class)
    public void receiveTips(EventData event){
        Runnable run = null;
        switch (event.getType()){
            case TIPS:
                run = ()->{tips.setText((String) event.getData());};
                break;
            case SCALE_TYPE:
                run = ()->{scaleName.setText((String) event.getData());};
                break;
            case SCALE_VALUE:
                ScaleValue data = (ScaleValue)event.getData();
                if (data != null && data.getValue() != null) {
                    if (StringUtils.isEmpty(lastValue) || !lastValue.equals(data.getValue())) {
                        run = () -> {
                            scaleValue.setText(data.getValue() + data.getUnit());
                        };
                        lastValue = data.getValue();
                    }
                }
                break;
            default:
        }
        if (run != null) {
            Platform.runLater(run);
        }
    }
}
