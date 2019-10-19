package sample;

import java.util.ArrayList;

import static java.lang.Thread.sleep;

public class Controller {

    Cabin cabin;

    Strategy movingStrategy;
    int currentFloor = 0;
    Movement cabinDirection;
    volatile int destination;
    boolean emergencyStopped = false;
    float startTime = 0;
    float stoppedTime = 0;
    float desynchTime = 0;
    CabinMover cabinMover;

    ArrayList<Integer> upList = new ArrayList<>();
    ArrayList<Integer> downList = new ArrayList<>();
    ArrayList<Integer> upListNext = new ArrayList<>();
    ArrayList<Integer> downListNext = new ArrayList<>();


    public void sendNotif() {
        if (cabinDirection.equals(Movement.UP))
            currentFloor++;
        else if (cabinDirection.equals(Movement.DOWN))
            currentFloor--;
        startTime = System.nanoTime();
        MainWindow.changeLiftText(currentFloor);
        MainWindow.changeFloorText(currentFloor);

    }

    public Controller(Cabin cabin) {
        this.cabinDirection = Movement.STOP;
        this.cabin = cabin;
        this.movingStrategy = new BaseStrategy();
    }

    public void addInPath(Controller controller, int floorDest, Movement requestedMovement) {
        if (cabinDirection.equals(Movement.STOP)) {
            if (controller.currentFloor < floorDest)
                controller.cabinDirection = Movement.UP;
            else if (controller.currentFloor > floorDest)
                controller.cabinDirection = Movement.DOWN;
            else
                controller.cabinDirection = Movement.STOP;
        }

        movingStrategy.addInPath(this, floorDest, requestedMovement);
    }

    public void addInPath(Controller controller, int floorDest) {

        if (cabinDirection.equals(Movement.STOP)) {
            if (controller.currentFloor < floorDest - 1) {
                controller.cabinDirection = Movement.UP;
            } else if (controller.currentFloor > floorDest - 1) {
                controller.cabinDirection = Movement.DOWN;
            } else
                controller.cabinDirection = Movement.STOP;
        }
        movingStrategy.addInPath(this, floorDest);
    }

    public void emergencyStop() {
        MainWindow.changeLiftButtonColor( 0, true);
        MainWindow.turnOffIndicators();
        emergencyStopped = true;
        cabinDirection = Movement.STOP;
        cabin.isMoving = false;
        cabin.stopNext = false;
        upList = new ArrayList<>();
        downList = new ArrayList<>();
        upListNext = new ArrayList<>();
        downListNext = new ArrayList<>();
        stoppedTime = System.nanoTime();
        cabinMover.interrupt();
        if(cabin.sensor!= null)
            cabin.sensor.interrupt();
        cabin.emergencyStop();
        desynchTime = stoppedTime - startTime;
        try {
            moveCabin();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void stopEmergencyStop(){
        emergencyStopped = false;
        MainWindow.changeLiftButtonColor( 0, false);

    }

    public int getMaxTravelValue() {
        if (cabinDirection.equals(Movement.DOWN))
            return currentFloor;
        else
            return MainWindow.numberOfFloors - currentFloor;
    }

    public class CabinMover extends Thread {
        public void run() {
            try {
                howToMoveCabin();
            }
            catch(ArrayIndexOutOfBoundsException e){
                System.out.println("Failed to remove");
            }
        }
    }

    public void moveCabin()throws InterruptedException, ArrayIndexOutOfBoundsException {
        cabinMover = new CabinMover();
        cabinMover.start();
    }

    public void howToMoveCabin() {
        movingStrategy.howToMove(this);
    }
}



