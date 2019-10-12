package sample;

import java.util.ArrayList;

public class Controller {

    Cabin cabin = new Cabin();

    int currentFloor = 0;
    Movement cabinMovement = Movement.STOP;
    int destination;
    boolean emergencyStopped = false;
    float startTime;

    ArrayList<Integer> upList = new ArrayList<>();
    ArrayList<Integer> downList = new ArrayList<>();
    ArrayList<Integer> upListNext = new ArrayList<>();
    ArrayList<Integer> downListNext = new ArrayList<>();

    public void sendNotif(){
        if(cabinMovement.equals(Movement.UP))
            currentFloor++;
        else
            currentFloor--;
    }

    public void emergencyStop(){
        emergencyStopped = true;
        cabinMovement = Movement.STOP;
        upList = new ArrayList<>();
        downList = new ArrayList<>();
        upListNext = new ArrayList<>();
        downListNext = new ArrayList<>();
    }

    public int getMaxTravelValue(){
        if(cabinMovement.equals(Movement.DOWN))
            return currentFloor;
        else
            return 7 - currentFloor;
    }

    public void moveCabin() {
        while (!emergencyStopped) {
            if (!cabinMovement.equals(Movement.STOP)) {
                if (!upList.isEmpty() || !downList.isEmpty()) {
                    if (upList.isEmpty())
                        cabinMovement = Movement.DOWN;
                    else if (downList.isEmpty())
                        cabinMovement = Movement.UP;
                    else if (Math.abs(upList.get(0) - currentFloor) < Math.abs(downList.get(0) - currentFloor))
                        cabinMovement = Movement.UP;
                    else
                        cabinMovement = Movement.DOWN;
                }
                if (cabinMovement.equals(Movement.UP)) {
                    while (!upList.isEmpty()) {
                        if(currentFloor < upList.get(0)) {
                            cabin.goUp();
                            if (destination == currentFloor + 1 || currentFloor + 1 == 6) {
                                cabin.stopNext();
                                upList.remove(upList.indexOf(destination));
                                destination = upList.get(0);
                            }
                        }
                        else{
                            cabin.goDown();
                            if(destination == currentFloor -1 || currentFloor - 1== 0 ){
                                cabin.stopNext();
                                upList.remove(upList.indexOf(destination));
                                destination = upList.get(0);
                            }
                        }
                    }
                    if(!downList.isEmpty() || !downListNext.isEmpty())
                        cabinMovement = Movement.DOWN;

                    if(!upListNext.isEmpty()) {
                        upList = upListNext;
                        upListNext = new ArrayList<>();
                    }
                }
                else {
                    if (!downList.isEmpty()) {
                        while (!downList.isEmpty()) {
                            if(currentFloor > downList.get(0)) {
                                cabin.goDown();
                                if (destination == currentFloor - 1 || currentFloor - 1 == 0) {
                                    cabin.stopNext();
                                    downList.remove(downList.indexOf(destination));
                                    destination = downList.get(0);
                                }
                            }
                            else{
                                cabin.goUp();
                                if(destination == currentFloor  + 1 || currentFloor + 1 == 0 ){
                                    cabin.stopNext();
                                    downList.remove(downList.indexOf(destination));
                                    destination = downList.get(0);
                                }
                            }
                        }
                        if(!upList.isEmpty() || !upListNext.isEmpty()){
                            cabinMovement = Movement.UP;
                            if(upList.isEmpty() && !upListNext.isEmpty()) {
                                upList = upListNext;
                                upListNext = new ArrayList<>();
                            }
                        }
                    }
                }
                if(upList.isEmpty() && upListNext.isEmpty() && downList.isEmpty() && downListNext.isEmpty())
                    cabinMovement = Movement.STOP;
            }
        }
    }
}


