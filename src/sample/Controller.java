package sample;

import java.util.ArrayList;

import static java.lang.Thread.sleep;

public class Controller {

    Cabin cabin;
    Strategy movingStrategy;
    int currentFloor = 0;
    Movement cabinMovement;
    int destination;
    boolean emergencyStopped = false;
    float startTime = 0;
    float stoppedTime = 0;
    float desynchTime = 0;
    CabinMover cabinMover;

    ArrayList<Integer> upList = new ArrayList<>();
    ArrayList<Integer> downList = new ArrayList<>();
    ArrayList<Integer> upListNext = new ArrayList<>();
    ArrayList<Integer> downListNext = new ArrayList<>();

    public void sendNotif(){
        if(cabinMovement.equals(Movement.UP))
            currentFloor++;
        else
            currentFloor--;
        startTime = System.nanoTime();
    }

    public Controller(Cabin cabin) {
        this.cabinMovement = Movement.STOP;
        this.cabin = cabin;
        this.movingStrategy = new BaseStrategy();
    }

    public void addInPath(Controller controller, int floorDest, Movement requestedMovement) {
        if(cabinMovement.equals(Movement.STOP)){
            if(controller.currentFloor < floorDest)
                controller.cabinMovement = Movement.UP;
            else if(controller.currentFloor > floorDest)
                controller.cabinMovement = Movement.DOWN;
            else
                controller.cabinMovement = Movement.STOP;
        }

        movingStrategy.addInPath(this, floorDest, requestedMovement);
    }

    public void addInPath(Controller controller, int floorDest){

        if(cabinMovement.equals(Movement.STOP)){
            if(controller.currentFloor < floorDest - 1 ) {
                controller.cabinMovement = Movement.UP;
            }
            else if(controller.currentFloor > floorDest - 1) {
                controller.cabinMovement = Movement.DOWN;
            }
            else
                controller.cabinMovement = Movement.STOP;
        }

        movingStrategy.addInPath(this, floorDest);
    }

    public void emergencyStop(){
        emergencyStopped = true;
        cabinMovement = Movement.STOP;
        upList = new ArrayList<>();
        downList = new ArrayList<>();
        upListNext = new ArrayList<>();
        downListNext = new ArrayList<>();
        stoppedTime = System.nanoTime();
        cabinMover.interrupt();
        desynchTime = stoppedTime - startTime;
        moveCabin();
    }

    public int getMaxTravelValue(){
        if(cabinMovement.equals(Movement.DOWN))
            return currentFloor;
        else
            return 7 - currentFloor;
    }

    public class CabinMover extends Thread {
        public void run(){
            howToMoveCabin();
        }
    }

    public void moveCabin(){
        cabinMover = new CabinMover();
        cabinMover.start();
    }

    public void howToMoveCabin() {
        while (!emergencyStopped) {
            while(cabinMovement.equals(Movement.STOP)) {
                try {
                    sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            if (cabinMovement.equals(Movement.UP)) {
                System.out.println(upList);
                while (!upList.isEmpty()) {
                    if(currentFloor < upList.get(0)) {
                        cabin.goUp() ;
                        if (destination == currentFloor + 1 || currentFloor + 1 == 6) {
                            cabin.stopNext();
                            upList.remove(upList.indexOf(destination));
                            destination = upList.get(0);
                        }
                    }
                    else{
                        cabin.goDown();
                        if(destination == currentFloor - 1 || currentFloor - 1 == 0 ){
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

        }

    }
}


