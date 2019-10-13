import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import sample.Cabin;

public class Window {
    Cabin myCabin;

    @FXML
    private Label messagesText ;



    @FXML
    private void fullStop(Event event) {
        Button btn = (Button) event.getSource();
//        String id = btn.getId();
        messagesText.setText("EMERGENCY STOP");
    }

    @FXML
    private void goUp(Event event) {
        Button btn = (Button) event.getSource();
        String id = btn.getId();
        String result = id.substring(7);
        messagesText.setText("PERSON AT FLOOR " + result + " WANT TO GO UP");
    }

    @FXML
    private void goDown(Event event) {
        Button btn = (Button) event.getSource();
        String id = btn.getId();
        String result = id.substring(9);
        messagesText.setText("PERSON AT FLOOR " + result + " WANT TO GO DOWN");
    }

    @FXML
    private void goTo(Event event) {
        Button btn = (Button) event.getSource();
        String id = btn.getId();
        String result = id.substring(3);
        messagesText.setText("MAKE LIFT GO TO FLOOR " + result);
    }
}
