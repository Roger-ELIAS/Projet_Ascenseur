package sample;

import java.util.ArrayList;

import static java.lang.Thread.sleep;

public class Controller {

    Cabin cabin;
    Strategy movingStrategy;
    volatile int currentFloor = 0;
    Movement cabinDirection;
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
        if(cabinDirection.equals(Movement.UP))
            currentFloor++;
        else if(cabinDirection.equals(Movement.DOWN))
            currentFloor--;
        else
            System.out.println(currentFloor);
        startTime = System.nanoTime();
    }

    public Controller(Cabin cabin) {
        this.cabinDirection = Movement.STOP;
        this.cabin = cabin;
        this.movingStrategy = new BaseStrategy();
    }

    public void addInPath(Controller controller, int floorDest, Movement requestedMovement) {
        if(cabinDirection.equals(Movement.STOP)){
            if(controller.currentFloor < floorDest)
                controller.cabinDirection = Movement.UP;
            else if(controller.currentFloor > floorDest)
                controller.cabinDirection = Movement.DOWN;
            else
                controller.cabinDirection = Movement.STOP;
        }

        movingStrategy.addInPath(this, floorDest, requestedMovement);
    }

    public void addInPath(Controller controller, int floorDest){

        if(cabinDirection.equals(Movement.STOP)){
            if(controller.currentFloor < floorDest - 1 ) {
                controller.cabinDirection = Movement.UP;
            }
            else if(controller.currentFloor > floorDest - 1) {
                controller.cabinDirection = Movement.DOWN;
            }
            else
                controller.cabinDirection = Movement.STOP;
        }
        movingStrategy.addInPath(this, floorDest);
    }

    public void emergencyStop(){
        emergencyStopped = true;
        cabinDirection = Movement.STOP;
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
        if(cabinDirection.equals(Movement.DOWN))
            return currentFloor;
        else
            return 6 - currentFloor;
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
            try {
                sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            while (cabinDirection.equals(Movement.STOP)) {
                try {
                    sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            if(!upList.isEmpty()){
                destination = upList.get(0);
                cabin.goUp();
                if (currentFloor + 1 == 6 ||currentFloor + 1 == destination) {
                    cabin.isMoving = false;
                    cabin.stopNext();
                    upList.remove(upList.indexOf(destination));
                    Test2.changeButtonColor(destination, false, true);
                    Test2.changeLiftButtonColor(destination + 1  , false);

                    try {
                        cabin.sensor.join();
                        cabinDirection = Movement.STOP;
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    if(!upList.isEmpty()){
                        destination = upList.get(0);
                        cabin.goUp();
                    }
                    else{
                        if(!downList.isEmpty()){
                            destination = downList.get(0);
                            cabin.goDown();
                        }
                        else {
                            cabin.isMoving = false;
                            cabinDirection = Movement.STOP;
                        }
                    }
                }
            }
            else{
                if(!downList.isEmpty()){
                    destination = downList.get(0);
                    cabin.goDown();
                    if (currentFloor - 1 == 0 ||currentFloor - 1 == destination) {
                        Test2.changeButtonColor(destination, false, false);
                        Test2.changeLiftButtonColor(destination + 1  , false);
                        cabin.isMoving = false;
                        cabin.stopNext();
                        downList.remove(downList.indexOf(destination));
                        try {
                            cabin.sensor.join();
                            cabinDirection = Movement.STOP;
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        if(!downList.isEmpty()){
                            destination = downList.get(0);
                            cabin.goDown();
                        }
                        else{
                            if(!upList.isEmpty()){
                                destination = upList.get(0);
                                cabin.goUp();
                            }
                            else {
                                cabin.isMoving = false;
                                cabinDirection = Movement.STOP;
                            }
                        }
                    }
                }
            }
        }
    }
}



