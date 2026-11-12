package fxconky;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class MessageBoardApp extends Application {

    private ObservableList<String> messageList = FXCollections.observableArrayList();

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("JavaFX Message Board");

        // Message display view
        ListView<String> listView = new ListView<>(messageList);
        listView.setPrefHeight(300);

        // Input field and send button
        TextField inputField = new TextField();
        inputField.setPromptText("Type your message here...");
        HBox.setHgrow(inputField, javafx.scene.layout.Priority.ALWAYS);

        Button sendButton = new Button("Send");

        // Action to send message
        Runnable sendMessageAction = () -> {
            String text = inputField.getText().trim();
            if (!text.isEmpty()) {
                String timestamp = LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"));
                messageList.add("[" + timestamp + "] " + text);
                inputField.clear();
                listView.scrollTo(messageList.size() - 1);
            }
        };

        sendButton.setOnAction(e -> sendMessageAction.run());
        inputField.setOnAction(e -> sendMessageAction.run()); // Triggered by pressing Enter

        // Layout for bottom input bar
        HBox inputBar = new HBox(10, inputField, sendButton);
        inputBar.setPadding(new Insets(10));

        // Main layout
        VBox root = new VBox(10, listView, inputBar);
        root.setPadding(new Insets(10));

        Scene scene = new Scene(root, 450, 400);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
