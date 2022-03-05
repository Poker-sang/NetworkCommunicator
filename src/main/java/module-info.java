module networkcommunicator.networkcommunicator {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.jetbrains.annotations;


    opens network_communicator to javafx.fxml;
    exports network_communicator;
    exports network_communicator.utils;
    opens network_communicator.utils to javafx.fxml;
}