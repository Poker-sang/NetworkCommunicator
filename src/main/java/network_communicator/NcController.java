package network_communicator;

import javafx.application.*;
import javafx.event.*;
import javafx.fxml.*;
import javafx.scene.control.*;
import javafx.scene.paint.*;
import javafx.scene.text.*;
import network_communicator.services.*;
import network_communicator.utils.*;

import java.io.*;
import java.net.*;
import java.time.*;
import java.time.format.*;
import java.util.*;

public class NcController implements Initializable
{
    @Override
    public void initialize(URL location, ResourceBundle resources)
    {
        _helper = new TextFlowHelper(_textFlow);
        var textDate = new Text();
        textDate.setText("当前日期：" + DateTimeFormatter.ofPattern("yyyy-MM-dd").format(LocalDate.now()) + "\n");
        _textFlow.getChildren().add(textDate);
    }

    private TextFlowHelper _helper;

    @FXML
    private CheckBox _cbAsServer;
    @FXML
    private TextField _tfPort;
    @FXML
    private TextField _tfServerIp;
    @FXML
    private Button _bStart;

    @FXML
    private TextFlow _textFlow;

    @FXML
    private TextField _tfSend;
    @FXML
    private Button _bSend;

    @FXML
    public void OnCheckChanged(ActionEvent actionEvent)
    {
        if (_cbAsServer.isSelected())
        {
            _bStart.setText("开启服务器");
            _tfServerIp.setDisable(true);
            _tfPort.setPromptText("Listening Port");
        }
        else
        {
            _bStart.setText("连接服务器");
            _tfServerIp.setDisable(false);
            _tfPort.setPromptText("Server Port");
        }
    }

    @FXML
    public void onStartClicked(ActionEvent actionEvent)
    {
        if (_cbAsServer.isDisable())
        {
            if (_cbAsServer.isSelected())
            {
                Server.close();
                _helper.newLine("已关闭服务器");
                _bStart.setText("开启服务器");
            }
            else
            {
                Client.close();
                _helper.newLine("已断开服务器");
                _bStart.setText("连接服务器");
                _tfServerIp.setDisable(false);
            }
            _bSend.setDisable(true);
            _tfPort.setDisable(false);
            _cbAsServer.setDisable(false);
        }
        else
        {
            _bStart.setDisable(true);
            _tfServerIp.setDisable(true);
            _tfPort.setDisable(true);
            _cbAsServer.setDisable(true);
            if (_cbAsServer.isSelected())
            {
                _helper.newLine("正在等待连接...");
                try
                {
                    Server.start(
                            Integer.parseInt(_tfPort.getText()),
                            (message) -> Platform.runLater(() -> _helper.newLine("对方（客户端）", message)),
                            (user) -> Platform.runLater(() -> _helper.newLine("已成功连接用户：" + user, Color.GREEN)),
                            () -> Platform.runLater(() ->
                            {
                                _helper.newLine("对方（客户端）已关闭连接");
                                _helper.newLine("已关闭服务器");
                                _bStart.setText("开启服务器");
                                _bSend.setDisable(true);
                                _tfPort.setDisable(false);
                                _cbAsServer.setDisable(false);
                            }));
                }
                catch (IOException e)
                {
                    _helper.newLine("开启失败", Color.RED);
                    _bStart.setDisable(false);
                    _tfPort.setDisable(false);
                    _cbAsServer.setDisable(false);
                    return;
                }
                _bStart.setText("关闭服务器");
            }
            else
            {
                _helper.newLine("正在连接服务器...");
                try
                {
                    Client.Connect(InetAddress.getByName(_tfServerIp.getText()), Integer.parseInt(_tfPort.getText()),
                            (message) -> Platform.runLater(() -> _helper.newLine("对方（服务器）", message)),
                            () -> Platform.runLater(() ->
                            {
                                _helper.newLine("对方（服务器）已关闭连接");
                                _helper.newLine("已断开服务器");
                                _bStart.setText("连接服务器");
                                _bSend.setDisable(true);
                                _tfPort.setDisable(false);
                                _cbAsServer.setDisable(false);
                            }));
                }
                catch (IOException e)
                {
                    _helper.newLine("连接失败", Color.RED);
                    _bStart.setDisable(false);
                    _tfServerIp.setDisable(false);
                    _tfPort.setDisable(false);
                    _cbAsServer.setDisable(false);
                    return;
                }
                _helper.newLine("已连接服务器", Color.GREEN);
                _bStart.setText("断开服务器");
            }
            _bSend.setDisable(false);
            _bStart.setDisable(false);
        }
    }

    @FXML
    public void onSendClick(ActionEvent actionEvent) throws IOException
    {
        var message = _tfSend.getText();
        if (message.matches("\\s*"))
            return;
        if (_cbAsServer.isSelected())
            Server.send(message);
        else
            Client.send(message);
        _helper.newLine("自己", message);
        _tfSend.setText("");
    }
}