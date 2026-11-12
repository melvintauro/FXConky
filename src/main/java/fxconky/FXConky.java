package fxconky;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Side;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import javafx.util.Duration;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class FXConky extends Application {

    private double xOffset = 0;
    private double yOffset = 0;
    ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
    
    @Override
    public void start(Stage  primaryStubStage) {
    	  Stage borderlessStage = new Stage();
    	// 1. Create the UI layout
    	
    	TabPane tabPane = new TabPane();
        tabPane.setSide(Side.BOTTOM); 
        Tab tab1 = new Tab("STOCK");
        Tab tab2 = new Tab("CURRENCY");
        Tab tab3 = new Tab("X");
        // Optional Configuration: Prevent the user from closing this specific tab
        tab1.setClosable(false);
        tab2.setClosable(false);
        tab3.setClosable(false);
        // Give the TabPane a custom class name to scope your styles
        tabPane.getStyleClass().add("custom-tab-pane");
       
        
 /*       // Add a simple label inside the widget
       scheduler.scheduleAtFixedRate(() -> {
            // Put your code cycle logic here
        	 ExcelCellReader.fillMap();
        }, 0, 5, TimeUnit.SECONDS); // Initial delay: 0s, Period: 1s */
        
       ExcelCellReader.fillMap();
        
        
        Label label = new Label("Nasdaq $" + ExcelCellReader.stockMap.get("NASDAQ"));
        Label label1 = new Label("DowJones $" + ExcelCellReader.stockMap.get("DOWJONES"));
        Label label2 = new Label("SENSEX $" + ExcelCellReader.stockMap.get("SENSEX"));
        Label label3 = new Label("NIFTY $" + ExcelCellReader.stockMap.get("NIFTY"));
        Label label4 = new Label("QAR/INR $" + ExcelCellReader.stockMap.get("QAR/INR"));
        Label label5 = new Label("USD/INR $" + ExcelCellReader.stockMap.get("USD/INR"));
        Label label6 = new Label("EUR/INR $" + ExcelCellReader.stockMap.get("EUR/INR"));
        
        
       label.getStyleClass().add("label");
       label1.getStyleClass().add("label"); 
       label2.getStyleClass().add("label");
       label3.getStyleClass().add("label");
       label4.getStyleClass().add("label");
       label5.getStyleClass().add("label");
       label6.getStyleClass().add("label");
       
       
       VBox content1 = new VBox(10);
       content1.setPadding(new Insets(5));
       content1.getChildren().addAll(label,label1,label2,label3);
       tab1.setContent(content1); // Assign layout node to the tab
       
       VBox content2 = new VBox(10);
       content2.setPadding(new Insets(5));
       content2.getChildren().addAll(label4,label5,label6);
       tab2.setContent(content2); // Assign layout node to the tab
      
       

        // 2. Enable window dragging (since there is no title bar)
        tabPane.setOnMousePressed(event -> {
            xOffset = event.getSceneX();
            yOffset = event.getSceneY();
        });

        tabPane.setOnMouseDragged(event -> {
            borderlessStage.setX(event.getScreenX() - xOffset);
            borderlessStage.setY(event.getScreenY() - yOffset);
        });
        Button closeBtn = new Button("X");
        closeBtn.getStyleClass().add("button");
        closeBtn.setOnAction(e -> {
        	scheduler.shutdown();
        	borderlessStage.close();
            primaryStubStage.close();
         });
          // 2a. Add the tabs to the TabPane
        tab3.setContent(closeBtn);
        tabPane.getTabs().addAll(tab1, tab2,tab3);
        
        // 3. Configure the Scene with a fully transparent fill
     // 5. Setup the Scene and Stage

      
         Scene scene = new Scene(tabPane, 250, 200);
         scene.setFill(Color.TRANSPARENT); 
        

     //4 Thread-safe update (if calling from a background thread)
         Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(5), event -> {
    	   ExcelCellReader.fillMap();
    	 label.setText("Nasdaq  " + ExcelCellReader.stockMap.get("NASDAQ"));
    	 label1.setText("DowJones  " + ExcelCellReader.stockMap.get("DOWJONES"));
    	 label2.setText("SENSEX  " + ExcelCellReader.stockMap.get("SENSEX"));
    	 label3.setText("NIFTY " + ExcelCellReader.stockMap.get("NIFTY"));
    	 label4.setText("QAR/INR " + ExcelCellReader.stockMap.get("QAR/INR"));
    	 label5.setText("USD/INR " + ExcelCellReader.stockMap.get("USD/INR"));
    	 label6.setText("EUR/INR " + ExcelCellReader.stockMap.get("EUR/INR"));
         })); 
         timeline.setCycleCount(Timeline.INDEFINITE);
         timeline.play();
         
     //4a Load the external CSS file
     scene.getStylesheets().add(getClass().getResource("/style.css").toExternalForm());
     
     // 1. Configure the hidden primary utility stage (keeps taskbar icon hidden)
     primaryStubStage.initStyle(StageStyle.UTILITY);
     primaryStubStage.setOpacity(0);
     primaryStubStage.setX(Double.MAX_VALUE); // Push off-screen
     primaryStubStage.show();

        // 5. Configure the Stage
      
        borderlessStage.initOwner(primaryStubStage);
        borderlessStage.initStyle(StageStyle.TRANSPARENT); // Removes window borders/title bar
        borderlessStage.setScene(scene);
        borderlessStage.setX(1000);
        borderlessStage.setY(25);
        borderlessStage.setTitle("FX Conky");
        borderlessStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
