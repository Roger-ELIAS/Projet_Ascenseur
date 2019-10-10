package sample;

import java.util.ArrayList;

import static java.lang.Thread.sleep;

public class Controller {

    Cabin cabin = new Cabin();

    int currentFloor = 0;
    Movement cabinMovement = Movement.STOP;
    int destination;

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

    public void moveCabin() {
        while (true) {
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
                    cabin.goUp();
                    while (!upList.isEmpty()) {
                        if (destination == currentFloor + 1 || currentFloor + 1 == 7) {
                            cabin.stopNext();
                            upList.remove(upList.indexOf(destination));
                            destination = upList.get(0);
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
                    cabinMovement = Movement.DOWN;
                    cabin.goDown();
                    cabinMovement = Movement.DOWN;
                        if (!downList.isEmpty()) {
                            cabin.goDown();
                            while (!downList.isEmpty()) {
                                if (destination == currentFloor - 1 || currentFloor - 1 == 0) {
                                    cabin.stopNext();
                                    downList.remove(downList.indexOf(destination));
                                    destination = downList.get(0);
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
                }
            }
        }
    }


