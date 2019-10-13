package sample;
import java.util.Collections;

public class BaseStrategy implements Strategy {

    @Override
    public void addInPath(Controller controller, int floorDest, Movement requestedMovement) {
        if (controller.upList.contains(floorDest) || controller.downList.contains(floorDest) || controller.upListNext.contains(floorDest) || controller.downListNext.contains(floorDest))
            return;

        if (requestedMovement.equals(Movement.UP)) {
            Test2.changeButtonColor(floorDest, true, true);
            if(controller.currentFloor > floorDest)
                controller.upListNext.add(floorDest);
            else {
                controller.upList.add(floorDest);
                if(controller.destination<controller.upList.get(0))
                    controller.destination = floorDest;
            }
            Collections.sort(controller.upList);
            Collections.sort(controller.upListNext);
        }
        else {
            Test2.changeButtonColor(floorDest, true, false);
            if(controller.currentFloor < floorDest)
                controller.downListNext.add(floorDest);
            else {
                controller.downList.add(floorDest);
                if(controller.destination<controller.downList.get(0))
                    controller.destination = floorDest;
            }
            Collections.sort(controller.downList, Collections.reverseOrder());
            Collections.sort(controller.downListNext, Collections.reverseOrder());
        }
    }

    @Override
    public void addInPath(Controller controller, int floorDest) {

        Test2.changeLiftButtonColor(floorDest, true);

        if(controller.cabinMovement.equals(Movement.STOP)){
            if(controller.currentFloor < floorDest - 1)
                controller.upList.add(floorDest - 1);
            else
                controller.downList.add(floorDest - 1);
        }

        else if(controller.currentFloor < floorDest -1) {
            if(controller.cabinMovement.equals(Movement.UP)) {
                controller.upList.add(floorDest - 1);
                if (floorDest - 1 < controller.destination)
                    controller.destination = floorDest - 1;
            }
            else
                controller.downList.add(floorDest - 1);
        }
        else {
            if(controller.cabinMovement.equals(Movement.DOWN)) {
                controller.downList.add(floorDest - 1);
                if (floorDest - 1 > controller.destination)
                    controller.destination = floorDest - 1;
            }
            else
                controller.upList.add(floorDest - 1);
        }
        Collections.sort(controller.downList, Collections.reverseOrder());
        Collections.sort(controller.upList);
    }
}
