package ChatApp_Server;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.URL;
import java.util.ResourceBundle;

public class Controller implements Initializable {
    @FXML
    private Button sendButton;
    @FXML
    private TextField MessageField;
    @FXML
    private VBox vbMain;
    @FXML
    private ScrollPane spMain;

    private Server server;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        try{
            server = new Server(new ServerSocket(1234));
        } catch (IOException e) {
            System.out.println("Error Creating server");
        }

        vbMain.heightProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number oldValue, Number newValue) {
                spMain.setVvalue((Double) newValue);
            }
        });

        server.receiveMessageFromClient(vbMain);

        sendButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                String messageToSend = MessageField.getText();
                if(!messageToSend.isEmpty()){
                    HBox hb = new HBox();
                    hb.setAlignment(Pos.CENTER_RIGHT);
                    hb.setPadding(new Insets(5,5,5,10));

                    Text text = new Text(messageToSend);
                    TextFlow textFlow = new TextFlow(text);

                    textFlow.setStyle("-fx-color: rgb(239,242,255);" +
                            "-fx-background-color: rgb(15,125,242);" +
                            "-fx-background-radius: 20px");

                    textFlow.setPadding(new Insets(5,10,5,10));
                    text.setFill(Color.color(0.934,0.945,0.965));

                    hb.getChildren().add(textFlow);
                    vbMain.getChildren().add(hb);

                    server.sendMessageToClient(messageToSend);
                    MessageField.clear();
                }
            }
        });

    }
    public static void addLabel(String messageFromClient, VBox vbox){
        HBox hb = new HBox();
        hb.setAlignment(Pos.CENTER_LEFT);
        hb.setPadding(new Insets(5,5,5,10));

        Text text = new Text(messageFromClient);
        TextFlow textFlow = new TextFlow(text);
        textFlow.setStyle(
                "-fx-background-color: rgb(233,233,235);" +
                "-fx-background-radius: 20px");

        textFlow.setPadding(new Insets(5,10,5,10));
        hb.getChildren().add(textFlow);


        Platform.runLater(new Runnable() {

            @Override
            public void run() {
               vbox.getChildren().add(hb);
            }
        });

    }

}
