package com.philippesteinbach;

import com.philippesteinbach.control.EditorController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import com.philippesteinbach.model.EditorModel;
import java.security.Security;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

/**
 * This class initializes our application and loads the user interface notated in ui.fxml
 * Also the controller of the text editor application is defined here.
 */
public class EditorApp extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/ui.fxml"));

        loader.setControllerFactory(t -> new EditorController(new EditorModel())); // Create & set factory for creating the controller

        Security.addProvider(new BouncyCastleProvider());

        primaryStage.setTitle("Simple Text Editor");
        primaryStage.setScene(new Scene(loader.load()));
        primaryStage.show();
    }

    /**
     * Launch the application
     * @param args
     */
    public static void main(String[] args) {
        launch(args);
    }

}
