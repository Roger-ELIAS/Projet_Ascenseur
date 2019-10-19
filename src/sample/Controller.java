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
        Test2.changeLiftText(currentFloor);
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

    public int getMaxTravelValue() {
        if (cabinDirection.equals(Movement.DOWN))
            return currentFloor;
        else
            return Test2.numberOfFloors - currentFloor;
    }

    public class CabinMover extends Thread {
        public void run() {
            howToMoveCabin();
        }
    }

    public void moveCabin() {
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
            while (!upList.isEmpty()) {
                destination = upList.get(0);
                if (destination < currentFloor) {
                    int firstDestination = destination;
                    while (currentFloor - 1 > firstDestination || currentFloor - 1 > 0) {
                        if (!downList.isEmpty())
                            destination = downList.get(0);
                        cabin.goDown();
                        Test2.changeIndicatorColor(true, false);
                        Test2.changeIndicatorColor(false, true);
                        try {
                            sleep(1);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        if (currentFloor - 1 == firstDestination) {
                            cabin.stopNext();
                            cabin.stop();
                            Test2.changeButtonColor(firstDestination, false, true);
                            Test2.changeLiftButtonColor(firstDestination + 1, false);
                            Test2.changeIndicatorColor(false, true);
                            Test2.changeIndicatorColor(false, false);

                            upList.remove(upList.indexOf(firstDestination));
                            try {
                                cabin.sensor.join();
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        } else if (currentFloor - 1 == destination || currentFloor - 1 == 0) {
                            cabin.stopNext();
                            cabin.stop();
                            int tmpDestination = destination;
                            Test2.changeButtonColor(tmpDestination, false, false);
                            Test2.changeLiftButtonColor(tmpDestination + 1, false);
                            Test2.changeIndicatorColor(false, true);
                            Test2.changeIndicatorColor(false, false);
                            try {
                                cabin.sensor.join();
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            if (!downList.isEmpty()) {
                                downList.remove(downList.indexOf(tmpDestination));
                            }
                        }

                    }
                } else {
                    cabin.goUp();
                    Test2.changeIndicatorColor(true, true);
                    Test2.changeIndicatorColor(false, false);

                    try {
                        sleep(1);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    if (currentFloor + 1 == Test2.numberOfFloors || currentFloor + 1 == destination) {
                        cabin.stopNext();
                        cabin.stop();
                        int tmpDestination = destination;
                        Test2.changeButtonColor(tmpDestination, false, true);
                        Test2.changeLiftButtonColor(tmpDestination + 1, false);
                        Test2.changeIndicatorColor(false, true);
                        Test2.changeIndicatorColor(false, false);
                        try {
                            cabin.sensor.join();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        upList.remove(upList.indexOf(tmpDestination));
                    }
                }
                if (upList.isEmpty() && !upListNext.isEmpty()) {
                    upList = upListNext;
                    upListNext = new ArrayList<>();
                }
                if (downList.isEmpty() && !downListNext.isEmpty()) {
                    downList = downListNext;
                    downListNext = new ArrayList<>();
                }
            }
            while (!downList.isEmpty()) {
                destination = downList.get(0);
                if (destination > currentFloor) {
                    int firstDestination = destination;
                    while (currentFloor + 1 < firstDestination || currentFloor + 1 > Test2.numberOfFloors) {
                        if (!upList.isEmpty())
                            destination = upList.get(0);
                        cabin.goUp();
                        Test2.changeIndicatorColor(true, true);
                        Test2.changeIndicatorColor(false, false);

                        try {
                            sleep(1);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        if (currentFloor + 1 == firstDestination) {
                            cabin.stopNext();
                            cabin.stop();
                            Test2.changeButtonColor(firstDestination, false, false);
                            Test2.changeLiftButtonColor(firstDestination + 1, false);
                            Test2.changeIndicatorColor(false, true);
                            Test2.changeIndicatorColor(false, false);
                            downList.remove(downList.indexOf(firstDestination));
                            try {
                                cabin.sensor.join();
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        } else if (currentFloor + 1 == destination || currentFloor + 1 == Test2.numberOfFloors) {
                            cabin.stopNext();
                            cabin.stop();
                            int tmpDestination = destination;
                            Test2.changeButtonColor(tmpDestination, false, true);
                            Test2.changeLiftButtonColor(tmpDestination + 1, false);
                            Test2.changeIndicatorColor(false, true);
                            Test2.changeIndicatorColor(false, false);
                            try {
                                cabin.sensor.join();
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            if (!upList.isEmpty()) {
                                upList.remove(upList.indexOf(tmpDestination));
                            }
                        }

                    }
                } else {
                    cabin.goDown();
                    Test2.changeIndicatorColor(true, false);
                    Test2.changeIndicatorColor(false, true);

                    try {
                        sleep(1);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    if (currentFloor - 1 == 0 || currentFloor - 1 == destination) {
                        cabin.stopNext();
                        cabin.stop();
                        int tmpDestination = destination;
                        Test2.changeButtonColor(tmpDestination, false, false);
                        Test2.changeLiftButtonColor(tmpDestination + 1, false);
                        Test2.changeIndicatorColor(false, true);
                        Test2.changeIndicatorColor(false, false);
                        try {
                            cabin.sensor.join();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        downList.remove(downList.indexOf(tmpDestination));
                    }
                    if (upList.isEmpty() && !upListNext.isEmpty()) {
                        upList = upListNext;
                        upListNext = new ArrayList<>();
                    }
                    if (downList.isEmpty() && !downListNext.isEmpty()) {
                        downList = downListNext;
                        downListNext = new ArrayList<>();
                    }
                }
                try {
                    sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}



