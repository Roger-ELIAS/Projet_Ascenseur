package sample;

import java.util.ArrayList;

public class Controller {

    Object object = new Object();

    private volatile int currentFloor = 0;
    private Cabin cabin;
    private Strategy strategy;

    private Movement cabinMovement = Movement.STOP;

    private ArrayList<Integer> currentPath = new ArrayList<>();
    private ArrayList<Integer> missingPath = new ArrayList<>();


    public void callLift(int floor, String direction){
        String formatedDirection;
        if(direction.equals("upFloor"))
            formatedDirection = "UP";
        else
            formatedDirection = "DO";
        strategy.addInPath(this,formatedDirection + Integer.toString(floor));
    }

    public void goTo(int floor){
        strategy.addInPath(this,"GO" + Integer.toString(floor));
    }

    public void emergencyStop(){
        cabin.emergencyStop();
        cabinMovement = Movement.STOP;
        currentPath = new ArrayList<>();
        missingPath = new ArrayList<>();
    }

    public void move(){
        while(true){
            if(!currentPath.isEmpty()){
                if(currentPath.get(0) > currentFloor) {
                    if(currentFloor == 6) return;
                    GoUpThread t = new GoUpThread();
                    t.start();
                    if(currentFloor + 1 == currentPath.get(0)) {
                        cabin.stopNext();
                        currentPath.remove(0);
                    }
                }
                else {
                    if(currentFloor == 0) return;
                    GoDownThread t = new GoDownThread();
                    t.start();
                }
            }
            else{
                if(!missingPath.isEmpty()) {
                    currentPath = missingPath;
                    missingPath = new ArrayList<>();
                }
            }
        }
    }

    public int getCurrentFloor() {
        return currentFloor;
    }

    public Movement getCabinMovement() {
        return cabinMovement;
    }

    public ArrayList<Integer> getCurrentPath() {
        return currentPath;
    }

    public ArrayList<Integer> getMissingPath() {
        return missingPath;
    }

    public void sendNotif(){
        synchronized (object) {
            if (cabinMovement.equals(Movement.UP)) {
                ++currentFloor;
            } else {
                --currentFloor;
            }
        }
    }

    public class GoUpThread extends Thread {
        public void run(){
            cabin.goUp();
        }
    }

    public class GoDownThread extends Thread {
        public void run(){
            cabin.goDown();
        }
    }
}
