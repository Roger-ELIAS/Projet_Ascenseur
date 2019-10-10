import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;

public class Window {
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
        btn.setBackground(new Background(new BackgroundFill(Color.RED, CornerRadii.EMPTY, Insets.EMPTY)));
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
    public void changeButtonColor(int index, boolean stat){

    }
}
