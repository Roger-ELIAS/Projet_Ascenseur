import javafx.animation.TranslateTransition;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeType;
import javafx.scene.text.*;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.Collections;

public class Test2 extends Application {

    private ArrayList<Button> upButtons = new ArrayList<>();
    private ArrayList<Button> downButtons = new ArrayList<>();
    private ArrayList<Button> controlButtons = new ArrayList<>();
    private ArrayList<Rectangle> upIndicators = new ArrayList<>();
    private ArrayList<Text> floorNumberIndicators = new ArrayList<>();
    private ArrayList<Rectangle> downIndicators = new ArrayList<>();
    private Rectangle liftUpRectangle;
    private Rectangle liftDownRectangle;
    private Text liftFloorText;
    private AnchorPane liftObject;
    int numberOfFloors = 7;

    public static void main(String[] args) {
        Application.launch(Test2.class, args);
    }

    public void generateWindow(AnchorPane window){
        window.setPrefHeight(540);
        window.setPrefWidth(960);
        window.setStyle("-fx-background-color: #333333;");
    }

    public void generateLift(AnchorPane window){
        AnchorPane lift = new AnchorPane();
        liftObject = lift;
        lift.setLayoutX(343);
        lift.setPrefHeight(66);
        lift.setPrefWidth(274);
        lift.setStyle("-fx-border-color: #ffffff; -fx-border-width: 5;");
        HBox liftContainer = new HBox();
        liftContainer.setAlignment(Pos.CENTER);
        liftContainer.setLayoutX(12);
        liftContainer.setLayoutY(17);
        liftContainer.setPrefHeight(46);
        liftContainer.setPrefWidth(246);
        liftContainer.setStyle("-fx-padding: 0;");
        VBox liftFloorIndicator = new VBox();
        liftFloorIndicator.setAlignment(Pos.CENTER);
        liftFloorIndicator.setPrefHeight(40);
        liftFloorIndicator.setPrefWidth(26);
        Rectangle liftUpIndicator = new Rectangle();
        liftUpIndicator.setArcHeight(5);
        liftUpIndicator.setArcWidth(5);
        liftUpIndicator.setFill(Color.WHITE);
        liftUpIndicator.setHeight(8);
        liftUpIndicator.setStroke(Color.BLACK);
        liftUpIndicator.setStrokeType(StrokeType.INSIDE);
        liftUpIndicator.setWidth(17);
        liftUpRectangle = liftUpIndicator;
        Text liftFloorNumber = new Text();
        liftFloorNumber.setBoundsType(TextBoundsType.VISUAL);
        liftFloorNumber.setFill(Color.WHITE);
        liftFloorNumber.setStrokeType(StrokeType.OUTSIDE);
        liftFloorNumber.setStrokeWidth(0);
        liftFloorNumber.setText("0");
        liftFloorNumber.setTextAlignment(TextAlignment.CENTER);
        liftFloorNumber.setWrappingWidth(53);
        liftFloorNumber.setY(76);
        liftFloorNumber.setFont(Font.font("Arial Bold", 37));
        liftFloorText = liftFloorNumber;
        Rectangle liftDownIndicator = new Rectangle();
        liftDownIndicator.setArcHeight(5);
        liftDownIndicator.setArcWidth(5);
        liftDownIndicator.setFill(Color.WHITE);
        liftDownIndicator.setHeight(8);
        liftDownIndicator.setStroke(Color.BLACK);
        liftDownIndicator.setStrokeType(StrokeType.INSIDE);
        liftDownIndicator.setWidth(17);
        liftDownRectangle = liftDownIndicator;
        HBox liftPeopleRenderer = new HBox();
        liftPeopleRenderer.setAlignment(Pos.CENTER_RIGHT);
        liftPeopleRenderer.setPrefHeight(46);
        liftPeopleRenderer.setPrefWidth(231);

        liftFloorIndicator.getChildren().addAll(liftUpIndicator, liftFloorNumber, liftDownIndicator);
        liftContainer.getChildren().addAll(liftFloorIndicator, liftPeopleRenderer);
        lift.getChildren().add(liftContainer);
        window.getChildren().add(lift);
    }

    public void generateControlPanel(AnchorPane window, int numberOfFloors){
        AnchorPane controlPanel = new AnchorPane();
        controlPanel.setLayoutY(66 * numberOfFloors);
        controlPanel.setPrefHeight(40);
        controlPanel.setPrefWidth(960);
        HBox controlContainer = new HBox();
        controlContainer.setAlignment(Pos.CENTER_LEFT);
        controlContainer.setPrefHeight(40);
        controlContainer.setPrefWidth(960);
        Button emergencyButton = new Button();
        controlButtons.add(emergencyButton);
        emergencyButton.setId("stop");
        emergencyButton.setMnemonicParsing(false);
        emergencyButton.setOnAction(new EventHandler<ActionEvent>(){
            @Override
            public void handle(ActionEvent actionEvent) {
                fullStop(actionEvent);
            }
        });
        emergencyButton.setPrefHeight(26);
        emergencyButton.setPrefWidth(128);
        emergencyButton.setText("STOP");
        HBox controlButtonContainer = new HBox();
        controlButtonContainer.setAlignment(Pos.CENTER_LEFT);
        controlButtonContainer.setPrefHeight(40);
        controlButtonContainer.setPrefWidth(960);

        for(int i = 0; i < numberOfFloors; i++) {
            Button floorControlButton = new Button();
            floorControlButton.setId("btn" + i);
            floorControlButton.setMnemonicParsing(false);
            floorControlButton.setOnAction(new EventHandler<ActionEvent>(){
                @Override
                public void handle(ActionEvent actionEvent) {
                    goTo(actionEvent);
                }
            });
            floorControlButton.setText(Integer.toString(i));
            controlButtonContainer.getChildren().add(floorControlButton);
            controlButtons.add(floorControlButton);
        }

        Label messageDisplayer = new Label();
        messagesText = messageDisplayer;
        messageDisplayer.setId("messagesText");
        messageDisplayer.setPrefHeight(39);
        messageDisplayer.setPrefWidth(587);
        messageDisplayer.setText("Lift waiting for instruction");
        messageDisplayer.setTextFill(Color.WHITE);
        messageDisplayer.setFont(Font.font(27));

        controlContainer.getChildren().addAll(emergencyButton, controlButtonContainer);
        controlButtonContainer.getChildren().add(messageDisplayer);
        controlPanel.getChildren().add(controlContainer);
        window.getChildren().add(controlPanel);
    }

    public void generateFloor(AnchorPane window, int numberOfFloors){
        for(int i = 0; i < numberOfFloors; i++) {
            HBox floor = new HBox();
            floor.setPrefHeight(66);
            floor.setPrefWidth(960);
            floor.setLayoutY(66 * i);
            HBox leftContainer = new HBox();
            leftContainer.setPrefHeight(66);
            leftContainer.prefWidth(330);
            leftContainer.setStyle("-fx-border-color: white; -fx-border-width: 0 0 5 0;");
            HBox letterContainer = new HBox();
            letterContainer.setMinWidth(50);
            Text floorNumber = new Text();
            floorNumber.setBoundsType(TextBoundsType.VISUAL);
            floorNumber.setFill(Color.web("#535353"));
            floorNumber.setStrokeType(StrokeType.OUTSIDE);
            floorNumber.setStrokeWidth(0);
            floorNumber.setText(Integer.toString(numberOfFloors - i - 1));
            floorNumber.setTextAlignment(TextAlignment.CENTER);
            floorNumber.setWrappingWidth(53);
            floorNumber.setY(76);
            floorNumber.setFont(Font.font("Arial Bold", 78));
            HBox mainLeftContainer = new HBox();
            mainLeftContainer.setPrefHeight(62);
            mainLeftContainer.setPrefWidth(290);
            mainLeftContainer.setMinWidth(290);
            HBox waitingArea = new HBox();
            waitingArea.setAlignment(Pos.CENTER_RIGHT);
            waitingArea.setPrefHeight(62);
            waitingArea.setPrefWidth(223);
            VBox floorLiftIndicator = new VBox();
            floorLiftIndicator.setAlignment(Pos.CENTER);
            floorLiftIndicator.setPrefHeight(62);
            floorLiftIndicator.setPrefWidth(27);
            Rectangle floorUpIndicator = new Rectangle();
            upIndicators.add(floorUpIndicator);
            floorUpIndicator.setArcHeight(5);
            floorUpIndicator.setArcWidth(5);
            floorUpIndicator.setFill(Color.WHITE);
            floorUpIndicator.setHeight(8);
            floorUpIndicator.setStroke(Color.BLACK);
            floorUpIndicator.setStrokeType(StrokeType.INSIDE);
            floorUpIndicator.setWidth(17);
            Text floorIndicator = new Text();
            floorNumberIndicators.add(floorIndicator);
            floorIndicator.setBoundsType(TextBoundsType.VISUAL);
            floorIndicator.setFill(Color.WHITE);
            floorIndicator.setStrokeType(StrokeType.OUTSIDE);
            floorIndicator.setStrokeWidth(0);
            floorIndicator.setText("0");
            floorIndicator.setTextAlignment(TextAlignment.CENTER);
            floorIndicator.setWrappingWidth(53);
            floorIndicator.setY(76);
            floorIndicator.setFont(Font.font("Arial Bold", 37));
            Rectangle floorDownIndicator = new Rectangle();
            upIndicators.add(floorDownIndicator);
            floorDownIndicator.setArcHeight(5);
            floorDownIndicator.setArcWidth(5);
            floorDownIndicator.setFill(Color.WHITE);
            floorDownIndicator.setHeight(8);
            floorDownIndicator.setStroke(Color.BLACK);
            floorDownIndicator.setStrokeType(StrokeType.INSIDE);
            floorDownIndicator.setWidth(17);
            VBox floorButtons = new VBox();
            floorButtons.setAlignment(Pos.CENTER);
            floorButtons.setPrefHeight(62);
            floorButtons.setPrefWidth(42);
            Button floorUpButton = new Button();
            upButtons.add(floorUpButton);
            floorUpButton.setId("upFloor" + (numberOfFloors - i - 1));
            floorUpButton.setMnemonicParsing(false);
            floorUpButton.setOnAction(new EventHandler<ActionEvent>(){
                @Override
                public void handle(ActionEvent actionEvent) {
                    goUp(actionEvent);
                }
            });
            floorUpButton.setText("/\\");
            Button floorDownButton = new Button();
            downButtons.add(floorDownButton);
            floorDownButton.setId("downFloor" + (numberOfFloors - i - 1));
            floorDownButton.setMnemonicParsing(false);
            floorDownButton.setOnAction(new EventHandler<ActionEvent>(){
                @Override
                public void handle(ActionEvent actionEvent) {
                    goDown(actionEvent);
                }
            });
            floorDownButton.setText("\\/");
            HBox middleContainer = new HBox();
            middleContainer.setPrefHeight(66);
            middleContainer.setPrefWidth(278);
            HBox rightContainer = new HBox();
            rightContainer.setPrefHeight(66);
            rightContainer.setPrefWidth(342);
            rightContainer.setStyle("-fx-border-color: white; -fx-border-width: 0 0 5 0;");

            floorButtons.getChildren().addAll(floorUpButton, floorDownButton);
            floorLiftIndicator.getChildren().addAll(floorUpIndicator, floorIndicator, floorDownIndicator);
            mainLeftContainer.getChildren().addAll(waitingArea, floorLiftIndicator, floorButtons);
            letterContainer.getChildren().add(floorNumber);
            leftContainer.getChildren().addAll(letterContainer, mainLeftContainer);
            floor.getChildren().addAll(leftContainer, middleContainer, rightContainer);
            window.getChildren().add(floor);
        }
    }

    public int fl = 0;
    @Override
    public void start(Stage primaryStage) {

        Group root = new Group();
        Scene scene = new Scene(root, 960, 540, Color.web("#333333"));

        primaryStage.setScene(scene);

        AnchorPane window = new AnchorPane();
        generateWindow(window);
        generateLift(window);
        generateControlPanel(window, numberOfFloors);
        generateFloor(window, numberOfFloors);

        Collections.reverse(upButtons);
        Collections.reverse(downButtons);
        Collections.reverse(upIndicators);
        Collections.reverse(floorNumberIndicators);
        Collections.reverse(downIndicators);

        root.getChildren().add(window);
        primaryStage.show();
        moveLift(numberOfFloors-1, false);
        scene.addEventHandler(KeyEvent.KEY_PRESSED, (key) -> {
            if(key.getCode() == KeyCode.Z) {
                moveLift(1,true);
            }
        });
        scene.addEventHandler(KeyEvent.KEY_PRESSED, (key) -> {
            if(key.getCode() == KeyCode.S) {
                moveLift(1,false);
            }
        });
    }

    @FXML
    private Label messagesText ;

    @FXML
    public void fullStop(Event event) {
        Button btn = (Button) event.getSource();
//        String id = btn.getId();
        messagesText.setText("EMERGENCY STOP");
    }

    @FXML
    public void goUp(Event event) {
        Button btn = (Button) event.getSource();
        String id = btn.getId();
        String result = id.substring(7);
        messagesText.setText("PERSON AT FLOOR " + result + " WANT TO GO UP");
    }

    @FXML
    public void goDown(Event event) {
        Button btn = (Button) event.getSource();
        String id = btn.getId();
        String result = id.substring(9);
        messagesText.setText("PERSON AT FLOOR " + result + " WANT TO GO DOWN");
    }

    @FXML
    public void goTo(Event event) {
        Button btn = (Button) event.getSource();
        String id = btn.getId();
        String result = id.substring(3);
        messagesText.setText("MAKE LIFT GO TO FLOOR " + result);
    }

    @FXML
    public void changeButtonColor(int index, boolean stat, boolean direction){
        if(direction == true){
            if(stat == true){
                upButtons.get(index).setBackground(new Background(new BackgroundFill(Color.RED, CornerRadii.EMPTY, Insets.EMPTY)));
            }else{
                upButtons.get(index).setBackground(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));
            }
        }else {
            if(stat == true){
                downButtons.get(index).setBackground(new Background(new BackgroundFill(Color.RED, CornerRadii.EMPTY, Insets.EMPTY)));
            }else{
                downButtons.get(index).setBackground(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));
            }
        }
    }

    @FXML
    public void changeIndicatorolor(boolean stat, boolean direction){
        if(direction == true){
            if(stat == true){
                for(Rectangle r : upIndicators)
                    r.setFill(Color.RED);
                liftUpRectangle.setFill(Color.RED);
            }else{
                for(Rectangle r : upIndicators)
                    r.setFill(Color.WHITE);
                liftUpRectangle.setFill(Color.WHITE);
            }
        }else {
            if(stat == true){
                for(Rectangle r : downIndicators)
                    r.setFill(Color.RED);
                liftDownRectangle.setFill(Color.RED);
            }else{
                for(Rectangle r : downIndicators)
                    r.setFill(Color.WHITE);
                liftDownRectangle.setFill(Color.WHITE);
            }
        }
    }

    @FXML
    public void changeLiftButtonColor(int index, boolean stat){
        if(stat == true){
            controlButtons.get(index).setBackground(new Background(new BackgroundFill(Color.RED, CornerRadii.EMPTY, Insets.EMPTY)));
        }else{
            controlButtons.get(index).setBackground(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));
        }
    }

    @FXML
    public void changeLiftText(int floor){
        liftFloorText.setText(Integer.toString(floor));
    }

    public void moveLift(int floor,boolean direction){
        TranslateTransition tt = new TranslateTransition(Duration.millis(1000), liftObject);
        if(direction){
            tt.setByY(-66 * floor);
        } else {
            tt.setByY(66 * floor);
        }
        tt.play();
//        liftObject.setLayoutY((66 * numberOfFloors) - 66 * floor - 66);
    }
}
