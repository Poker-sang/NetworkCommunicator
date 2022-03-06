package network_communicator;

import javafx.application.*;
import javafx.fxml.*;
import javafx.scene.*;
import javafx.stage.*;
import network_communicator.services.*;
import org.jetbrains.annotations.*;

import java.io.*;

public class NcApp extends Application
{
    @Override
    public void start(@NotNull Stage stage) throws IOException
    {
        var fxmlLoader = new FXMLLoader(NcApp.class.getResource("nc-view.fxml"));
        var scene = new Scene(fxmlLoader.load(), 640, 360);
        stage.setTitle("NetworkCommunicator");
        stage.setScene(scene);
        stage.setOnCloseRequest(event ->
        {
            Client.close();
            Server.close();
            System.exit(0);
        });
        stage.show();
    }

    public static void main(String[] args)
    {
        launch();
    }
}