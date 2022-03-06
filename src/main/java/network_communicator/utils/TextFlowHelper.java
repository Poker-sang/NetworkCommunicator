package network_communicator.utils;

import javafx.scene.paint.*;
import javafx.scene.text.*;

import java.time.*;
import java.time.format.*;

/**
 * 显示到文字流
 */
public class TextFlowHelper
{
    public TextFlowHelper(TextFlow textFlow)
    {
        _textFlow = textFlow;
    }

    private final TextFlow _textFlow;

    public void newLine(String string)
    {
        newLine(string, Color.BLACK);
    }

    public void newLine(String string, Color color)
    {
        newLine("系统", string, color);
    }

    public void newLine(String from, String string)
    {
        newLine(from, string, Color.BLACK);
    }

    public void newLine(String from, String string, Color color)
    {
        var textTime = new Text();
        textTime.setText(from + " " + DateTimeFormatter.ofPattern("HH:mm:ss.SS").format(LocalTime.now()) + "\n");
        textTime.setFont(Font.font(10));
        textTime.setFill(Color.BLUE);
        var text = new Text();
        text.setText(string + "\n");
        text.setFill(color);
        _textFlow.getChildren().add(textTime);
        _textFlow.getChildren().add(text);
    }
}
