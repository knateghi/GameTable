package sample;


import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        primaryStage.setTitle("Games");
        primaryStage.setScene(new Scene(root, 545, 438));
        primaryStage.show();
    }


//    --module-path "C:\Users\jorgw\Downloads\openjfx-11.0.2_windows-x64_bin-sdk\javafx-sdk-11.0.2\lib" --add-modules=javafx.controls,javafx.fxml


    public static void main(String[] args) {
        launch(args);
    }
}
