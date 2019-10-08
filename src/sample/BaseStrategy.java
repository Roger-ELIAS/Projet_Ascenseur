package sample;

import java.util.ArrayList;
import java.util.Collections;

public class BaseStrategy implements Strategy {

    @Override
    public void addInPath(Controller controller, String request) {
        int floor = Integer.parseInt(request.substring(2));
        String direction = request.substring(0, 2);

        if (controller.getCurrentPath().contains(floor) || controller.getMissingPath().contains(floor))
            return; //Request already existing

        if (controller.getCabinMovement().equals(Movement.STOP)) controller.getCurrentPath().add(floor);

        if(direction.equals("UP") && controller.getCabinMovement().equals(Movement.UP) && floor > controller.getCurrentFloor()){
            controller.getCurrentPath().add(floor);
            Collections.sort(controller.getCurrentPath());
        }
        else if(direction.equals("DO") && controller.getCabinMovement().equals(Movement.DOWN) && floor < controller.getCurrentFloor()){
            controller.getCurrentPath().add(floor);
            Collections.sort(controller.getCurrentPath(), Collections.reverseOrder());
        }
        else if(controller.getCabinMovement().equals(Movement.UP)){
            controller.getMissingPath().add(floor);
            Collections.sort(controller.getMissingPath(), Collections.reverseOrder());
        }
        else{
            controller.getMissingPath().add(floor);
            Collections.sort(controller.getMissingPath());
        }

    }
}
