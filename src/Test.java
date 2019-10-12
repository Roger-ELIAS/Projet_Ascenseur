import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontSmoothingType;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

public class Test extends Application {

    public static void main(String[] args) {
        Application.launch(Test.class, args);
    }

    public void GenerateButtons(AnchorPane aPane, int numberOfFloor){
        AnchorPane buttonsPane = new AnchorPane();
        buttonsPane.setPrefHeight(40);
        buttonsPane.setPrefWidth(960);
        buttonsPane.setLayoutY(80 * numberOfFloor);

        HBox container = new HBox();
        container.setAlignment(Pos.CENTER_LEFT);
        container.setPrefHeight(40);
        container.setPrefWidth(960);

        Button stop = new Button();
        stop.setText("STOP");
        stop.setFont(new Font("System", 12));
        stop.setPrefHeight(26);
        stop.setPrefWidth(128);

        HBox buttons = new HBox();
        buttons.setAlignment(Pos.CENTER_LEFT);
        buttons.setPrefHeight(40);
        buttons.setPrefWidth(960);

        for(int i = 0; i < numberOfFloor; i++) {
            Button btn = new Button();
            btn.setText(Integer.toString(i));
            buttons.getChildren().add(btn);
        }
        container.getChildren().add(stop);
        container.getChildren().add(buttons);
        buttonsPane.getChildren().add(container);
        aPane.getChildren().add(buttonsPane);
        aPane.setPrefHeight(40);
        aPane.setPrefWidth(960);
    }

    public void GenerateFloor(AnchorPane aPane, int numberOfFloor){
        for(int i = 0; i < numberOfFloor; i++){
            HBox floor = new HBox();
            floor.setPrefWidth(960);
            floor.setPrefHeight(66);
            floor.setLayoutY(80 * i);
            HBox floorEnter = new HBox();
            floorEnter.setAlignment(Pos.TOP_LEFT);
            floorEnter.setStyle("-fx-border-color: white; -fx-border-width: 0 0 5 0;");
            floorEnter.setPrefWidth(341);
            floorEnter.setPrefHeight(66);
            HBox liftCage = new HBox();
            liftCage.setAlignment(Pos.TOP_LEFT);
            liftCage.setPrefWidth(278);
            liftCage.setPrefHeight(66);
            HBox floorExit = new HBox();
            floorExit.setAlignment(Pos.TOP_LEFT);
            floorExit.setStyle("-fx-border-color: white; -fx-border-width: 0 0 5 0;");
            floorExit.setPrefWidth(341);
            floorExit.setPrefHeight(66);

            Text floorNumber = new Text(Integer.toString(numberOfFloor - i - 1));
            floorNumber.setFont(new Font("Arial", 78));
            floorNumber.setWrappingWidth(53);
            floorNumber.setFill(Color.web("#535353"));
            floorNumber.setFontSmoothingType(FontSmoothingType.GRAY);
            floorNumber.setTextAlignment(TextAlignment.CENTER);
            floorNumber.setY(76);
            HBox container = new HBox();
            container.setAlignment(Pos.TOP_LEFT);
            container.setPrefWidth(343);
            container.setPrefHeight(62);
            HBox waitingArea = new HBox();
            waitingArea.setAlignment(Pos.CENTER_RIGHT);
            waitingArea.setPrefWidth(234);
            waitingArea.setPrefHeight(62);
            VBox liftInformation = new VBox();
            liftInformation.setAlignment(Pos.CENTER);
            liftInformation.setPrefWidth(27);
            liftInformation.setPrefHeight(62);
            Rectangle goingUp = new Rectangle();
            goingUp.setArcWidth(5);
            goingUp.setArcHeight(5);
            goingUp.setFill(Color.WHITE);
            goingUp.setHeight(8);
            goingUp.setWidth(17);
            Text liftFloor = new Text("0");
            liftFloor.setFont(new Font("Arial", 37));
            liftFloor.setWrappingWidth(53);
            liftFloor.setFill(Color.web("#ffffff"));
            liftFloor.setFontSmoothingType(FontSmoothingType.GRAY);
            liftFloor.setTextAlignment(TextAlignment.CENTER);
            liftFloor.setY(76);
            Rectangle goingDown = new Rectangle();
            goingDown.setArcWidth(5);
            goingDown.setArcHeight(5);
            goingDown.setFill(Color.WHITE);
            goingDown.setHeight(8);
            goingDown.setWidth(17);
            VBox callButtons = new VBox();
            callButtons.setPrefWidth(42);
            callButtons.setPrefHeight(62);
            callButtons.setAlignment(Pos.CENTER);
            Button liftCallUp = new Button();
            liftCallUp.setText("/\\");
            liftCallUp.setFont(new Font("System", 12));
            Button liftCallDown = new Button();
            liftCallDown.setText("\\/");
            liftCallDown.setFont(new Font("System", 12));

            callButtons.getChildren().addAll(liftCallUp, liftCallDown);
            liftInformation.getChildren().addAll(goingUp, liftFloor, goingDown);
            container.getChildren().addAll(waitingArea, liftInformation, callButtons);
            floorEnter.getChildren().addAll(floorNumber, container);
            floor.getChildren().addAll(floorEnter, liftCage, floorExit);
            aPane.getChildren().add(floor);
        }
    }

    @Override
    public void start(Stage primaryStage) {
        int numberOfFloor = 8;
        Group root = new Group();
        Scene scene = new Scene(root, 960, 66 * numberOfFloor, Color.web("#333333"));
        primaryStage.setScene(scene);
        AnchorPane aPane = new AnchorPane();
        GenerateFloor(aPane, numberOfFloor);
        GenerateButtons(aPane, numberOfFloor);
        root.getChildren().add(aPane);
        primaryStage.show();
    }

}
