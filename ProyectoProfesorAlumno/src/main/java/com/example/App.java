package com.example;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import lombok.Getter;
import lombok.Setter;

import java.io.IOException;

public class App extends Application {
    @Getter
    @Setter
    private static Stage myStage;
    @Override
    public void start(Stage stage) throws IOException {
        myStage = stage;
        FXMLLoader fxmlLoader = new FXMLLoader( App.class.getResource( "controllers/login-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 360, 380);
        stage.setTitle("Login");
        stage.setScene(scene);
        stage.show();
        stage.setResizable(false);
    }

    public static void main(String[] args) {
        launch();
    }

    public static void loadFXML(String fxml, String titulo){
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("controllers/" +fxml));
            Scene scene = new Scene(fxmlLoader.load());
            myStage.setTitle(titulo);
            myStage.setScene(scene);
            myStage.show();
        } catch (IOException e) {
            System.out.println("Error al cargar el archivo "+fxml);
            throw new RuntimeException(e);
        }
    }
}
