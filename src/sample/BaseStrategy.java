package sample;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

public class BaseStrategy implements Strategy {

    @Override
    public void addInPath(Controller controller, int floorDest, Movement requestedMovement) {
        if(!controller.upList.contains(floorDest)  && !controller.downList.contains(floorDest)  && !controller.upListNext.contains(floorDest)  && !controller.downListNext.contains(floorDest)) return;

        if(controller.cabinMovement.equals(Movement.STOP)){
            if(controller.currentFloor < floorDest)
                controller.upList.add(floorDest);
            else
                controller.downList.add(floorDest);
            controller.destination = floorDest;
        }
        else if(controller.cabinMovement.equals(Movement.UP)){
            if(requestedMovement.equals(Movement.UP)){
                if(controller.currentFloor + 1 < floorDest) {
                    controller.upList.add(floorDest);
                    Collections.sort(controller.upList);
                    controller.destination = controller.upList.get(0);
                }
                else{
                    controller.upListNext.add(floorDest);
                    Collections.sort(controller.upList);
                }
            }
            else{
                controller.downList.add(floorDest);
                Collections.sort(controller.downList, Collections.reverseOrder());
            }
        }
        else{
            if(requestedMovement.equals(Movement.DOWN)){
                if(controller.currentFloor - 1 > floorDest){
                    controller.downList.add(floorDest);
                    Collections.sort(controller.downList, Collections.reverseOrder());
                    controller.destination = controller.downList.get(0);
                }
                else{
                    controller.downListNext.add(floorDest);
                    Collections.sort(controller.downList);
                }
            }
            else{
                controller.upList.add(floorDest);
                Collections.sort(controller.upList);
            }
        }
    }

    @Override
    public void addInPath(Controller controller, int floorDest) {
        if(!controller.upList.contains(floorDest)  && !controller.downList.contains(floorDest)  && !controller.upListNext.contains(floorDest)  && !controller.downListNext.contains(floorDest)) return;
        if(controller.currentFloor < floorDest){
            controller.upList.add(floorDest);
            Collections.sort(controller.upList);
            if(controller.cabinMovement.equals(Movement.UP))
                controller.destination = controller.upList.get(0);
        }
        else{
            controller.downList.add(floorDest);
            Collections.sort(controller.upList, Collections.reverseOrder());
            if(controller.cabinMovement.equals(Movement.DOWN))
                controller.destination = controller.downList.get(0);
        }
    }
}
