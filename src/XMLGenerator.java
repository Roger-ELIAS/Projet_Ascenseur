import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

public class XMLGenerator {
    private String text;

    public XMLGenerator() {
    }

    public void GenerateElevator(int liftFloorNumber, int peopleInElevator, int numberOfFloors){
        text += "<AnchorPane maxHeight=\"-Infinity\" maxWidth=\"-Infinity\" minHeight=\"-Infinity\" minWidth=\"-Infinity\" prefHeight=\"540.0\" prefWidth=\"960.0\" style=\"-fx-background-color: #333333;\" xmlns=\"http://javafx.com/javafx/11.0.1\" xmlns:fx=\"http://javafx.com/fxml/1\">\n" +
                "<children>\n" +
                "<AnchorPane layoutX=\"343.0\" prefHeight=\"66.0\" prefWidth=\"274.0\" style=\"-fx-border-color: #ffffff; -fx-border-width: 5;\">\n" +
                "<children>\n" +
                "<HBox alignment=\"CENTER\" layoutX=\"12.0\" layoutY=\"17.0\" prefHeight=\"46.0\" prefWidth=\"246.0\" style=\"-fx-padding: 0;\">\n" +
                "<children>\n" +
                "<VBox alignment=\"CENTER\" prefHeight=\"40.0\" prefWidth=\"26.0\">\n" +
                "<children>\n" +
                "<Rectangle arcHeight=\"5.0\" arcWidth=\"5.0\" fill=\"WHITE\" height=\"8.0\" stroke=\"BLACK\" strokeType=\"INSIDE\" width=\"17.0\" />\n" +
                "<!--CHANGE LIFT FLOOR NUMBER-->\n" +
                "<Text boundsType=\"VISUAL\" fill=\"WHITE\" strokeType=\"OUTSIDE\" strokeWidth=\"0.0\" text=\"" +
                liftFloorNumber +
                "\" textAlignment=\"CENTER\" wrappingWidth=\"53.0\" y=\"76.0\">\n" +
                "<font>\n" +
                "<Font name=\"Arial Bold\" size=\"37.0\" />\n" +
                "</font>\n" +
                "</Text>\n" +
                "<Rectangle arcHeight=\"5.0\" arcWidth=\"5.0\" fill=\"WHITE\" height=\"8.0\" stroke=\"BLACK\" strokeType=\"INSIDE\" width=\"17.0\" />\n" +
                "</children>\n" +
                "</VBox>\n" +
                "<HBox alignment=\"CENTER_RIGHT\" prefHeight=\"46.0\" prefWidth=\"231.0\">\n" +
                "<children>";
        for(int i = 0; i < peopleInElevator; i++){
            text += "<Rectangle arcHeight=\"5.0\" arcWidth=\"5.0\" fill=\"WHITE\" height=\"46.0\" stroke=\"BLACK\" strokeType=\"INSIDE\" width=\"28.0\" />";
        }
        text += "</children>\n" +
                "</HBox>\n" +
                "</children>\n" +
                "</HBox>\n" +
                "</children>\n" +
                "</AnchorPane>\n" +
                "<AnchorPane layoutY=\"500.0\" prefHeight=\"40.0\" prefWidth=\"960.0\">\n" +
                "<children>\n" +
                "<HBox alignment=\"CENTER_LEFT\" prefHeight=\"40.0\" prefWidth=\"960.0\">\n" +
                "<children>\n" +
                "<Button mnemonicParsing=\"false\" prefHeight=\"26.0\" prefWidth=\"128.0\" text=\"STOP\" />\n" +
                "<HBox alignment=\"CENTER_LEFT\" prefHeight=\"40.0\" prefWidth=\"960.0\">\n" +
                "<children>";
        for(int i = 0; i < numberOfFloors; i++){
            text += "<Button mnemonicParsing=\"false\" text=\"" + i + " />";
        }
        text += "</children>\n" +
                "</HBox>\n" +
                "</children>\n" +
                "</HBox>\n" +
                "</children>\n" +
                "</AnchorPane>";
    }

    public void GenerateFloors(int floorNumber, int numberOfWaitingPeople){
        text += "<HBox prefHeight=\"66.0\" prefWidth=\"960.0\">\n" +
                "<children>\n" +
                "<HBox prefHeight=\"66.0\" prefWidth=\"341.0\" style=\"-fx-border-color: white; -fx-border-width: 0 0 5 0;\">\n" +
                "<children>\n" +
                "<!--CHANGE FLOOR NUMBER-->" +
                "<Text boundsType=\"VISUAL\" fill=\"#535353\" strokeType=\"OUTSIDE\" strokeWidth=\"0.0\" text=\"" +
                floorNumber +
                "\" textAlignment=\"CENTER\" wrappingWidth=\"53.0\" y=\"76.0\">\n" +
                "<font>\n" +
                "<Font name=\"Arial Bold\" size=\"78.0\" />\n" +
                "</font>\n" +
                "</Text>\n" +
                "<HBox prefHeight=\"62.0\" prefWidth=\"343.0\">\n" +
                "<children>\n" +
                "<HBox alignment=\"CENTER_RIGHT\" prefHeight=\"62.0\" prefWidth=\"234.0\">\n" +
                "<children>\n" +
                "<!--ADD WAITING PERSON-->";
        for(int i = 0; i < numberOfWaitingPeople; i++){
            text += "<Rectangle arcHeight=\"5.0\" arcWidth=\"5.0\" fill=\"WHITE\" height=\"46.0\" stroke=\"BLACK\" strokeType=\"INSIDE\" width=\"28.0\" />";
        }



    }

    public void GenerateElevatorButtons(){

    }

    public void GenerateFloorButtons(int floor){

    }

    public void FileWriter() throws FileNotFoundException, UnsupportedEncodingException {
        PrintWriter writer = new PrintWriter("window.fxml", "UTF-8");
        writer.println(text);
    }
}
