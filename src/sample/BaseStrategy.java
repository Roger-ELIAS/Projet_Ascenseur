package sample;
import java.util.ArrayList;
import java.util.Collections;

import static java.lang.Thread.sleep;

public class BaseStrategy implements Strategy {

    @Override
    public void addInPath(Controller controller, int floorDest, Movement requestedMovement) {
        if (controller.upList.contains(floorDest) && requestedMovement.equals(Movement.UP) || controller.upListNext.contains(floorDest) || controller.downListNext.contains(floorDest))
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

        if(controller.cabinDirection.equals(Movement.STOP)){
            if(controller.currentFloor < floorDest - 1)
                controller.upList.add(floorDest - 1);
            else
                controller.upListNext.add(floorDest - 1);
        }

        else if(controller.currentFloor < floorDest -1) {
            if(controller.cabinDirection.equals(Movement.UP)) {
                controller.upList.add(floorDest - 1);
                if (floorDest - 1 < controller.destination)
                    controller.destination = floorDest - 1;
            }
            else
                controller.downList.add(floorDest - 1);
        }
        else {
            if(controller.cabinDirection.equals(Movement.DOWN)) {
                controller.downList.add(floorDest - 1);
                if (floorDest - 1 > controller.destination)
                    controller.destination = floorDest - 1;
            }
            else
                controller.downListNext.add(floorDest - 1);
        }
        Collections.sort(controller.downList, Collections.reverseOrder());
        Collections.sort(controller.upList);
    }

    @Override
    public void howToMove(Controller controller) {
        while(controller.emergencyStopped) {
            try {
                sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        while (true) {
            try {
                sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            while (!controller.upList.isEmpty()) {
                controller.destination = controller.upList.get(0);
                if (controller.destination < controller.currentFloor) {
                    int firstDestination = controller.destination;
                    while (controller.currentFloor - 1 > firstDestination || controller.currentFloor - 1 > 0) {
                        if (!controller.downList.isEmpty())
                            controller.destination = controller.downList.get(0);
                        controller.cabin.goDown();
                        Test2.changeIndicatorColor(true, false);
                        Test2.changeIndicatorColor(false, true);
                        try {
                            sleep(1);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        if (controller.currentFloor - 1 == firstDestination) {
                            controller.cabin.stopNext();
                            controller.cabin.stop();
                            Test2.changeButtonColor(firstDestination, false, true);
                            Test2.changeLiftButtonColor(firstDestination + 1, false);
                            Test2.changeIndicatorColor(false, true);
                            Test2.changeIndicatorColor(false, false);

                            controller.upList.remove(controller.upList.indexOf(firstDestination));
                            try {
                                controller.cabin.sensor.join();
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        } else if (controller.currentFloor - 1 == controller.destination || controller.currentFloor - 1 == 0) {
                            controller.cabin.stopNext();
                            controller.cabin.stop();
                            int tmpDestination = controller.destination;
                            Test2.changeButtonColor(tmpDestination, false, false);
                            Test2.changeLiftButtonColor(tmpDestination + 1, false);
                            Test2.changeIndicatorColor(false, true);
                            Test2.changeIndicatorColor(false, false);
                            try {
                                controller.cabin.sensor.join();
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            if (!controller.downList.isEmpty()) {
                                controller.downList.remove(controller.downList.indexOf(tmpDestination));
                            }
                        }

                    }
                } else {
                    controller.cabin.goUp();
                    Test2.changeIndicatorColor(true, true);
                    Test2.changeIndicatorColor(false, false);

                    try {
                        sleep(1);
                    } catch (InterruptedException e) {
                        System.out.println("Sleep interrupted");
                    }
                    if (controller.currentFloor + 1 == Test2.numberOfFloors || controller.currentFloor + 1 == controller.destination) {
                        controller.cabin.stopNext();
                        controller.cabin.stop();
                        int tmpDestination = controller.destination;
                        Test2.changeButtonColor(tmpDestination, false, true);
                        Test2.changeLiftButtonColor(tmpDestination + 1, false);
                        Test2.changeIndicatorColor(false, true);
                        Test2.changeIndicatorColor(false, false);
                        try {
                            controller.cabin.sensor.join();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        controller.upList.remove(controller.upList.indexOf(tmpDestination));
                    }
                }
                if (controller.upList.isEmpty() && !controller.upListNext.isEmpty()) {
                    controller.upList = controller.upListNext;
                    controller.upListNext = new ArrayList<>();
                }
                if (controller.downList.isEmpty() && !controller.downListNext.isEmpty()) {
                    controller.downList = controller.downListNext;
                    controller.downListNext = new ArrayList<>();
                }
            }
            while (!controller.downList.isEmpty()) {
                controller.destination = controller.downList.get(0);
                if (controller.destination > controller.currentFloor) {
                    int firstDestination = controller.destination;
                    while (controller.currentFloor + 1 < firstDestination || controller.currentFloor + 1 > Test2.numberOfFloors) {
                        if (!controller.upList.isEmpty())
                            controller.destination = controller.upList.get(0);
                        controller.cabin.goUp();
                        Test2.changeIndicatorColor(true, true);
                        Test2.changeIndicatorColor(false, false);

                        try {
                            sleep(1);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        if (controller.currentFloor + 1 == firstDestination) {
                            controller.cabin.stopNext();
                            controller.cabin.stop();
                            Test2.changeButtonColor(firstDestination, false, false);
                            Test2.changeLiftButtonColor(firstDestination + 1, false);
                            Test2.changeIndicatorColor(false, true);
                            Test2.changeIndicatorColor(false, false);
                            controller.downList.remove(controller.downList.indexOf(firstDestination));
                            try {
                                controller.cabin.sensor.join();
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        } else if (controller.currentFloor + 1 == controller.destination || controller.currentFloor + 1 == Test2.numberOfFloors) {
                            controller.cabin.stopNext();
                            controller.cabin.stop();
                            int tmpDestination = controller.destination;
                            Test2.changeButtonColor(tmpDestination, false, true);
                            Test2.changeLiftButtonColor(tmpDestination + 1, false);
                            Test2.changeIndicatorColor(false, true);
                            Test2.changeIndicatorColor(false, false);
                            try {
                                controller.cabin.sensor.join();
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            if (!controller.upList.isEmpty()) {
                                controller.upList.remove(controller.upList.indexOf(tmpDestination));
                            }
                        }

                    }
                } else {
                    controller.cabin.goDown();
                    Test2.changeIndicatorColor(true, false);
                    Test2.changeIndicatorColor(false, true);

                    try {
                        sleep(1);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    if (controller.currentFloor - 1 == 0 || controller.currentFloor - 1 == controller.destination) {
                        controller.cabin.stopNext();
                        controller.cabin.stop();
                        int tmpDestination = controller.destination;
                        Test2.changeButtonColor(tmpDestination, false, false);
                        Test2.changeLiftButtonColor(tmpDestination + 1, false);
                        Test2.changeIndicatorColor(false, true);
                        Test2.changeIndicatorColor(false, false);
                        try {
                            controller.cabin.sensor.join();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        controller.downList.remove(controller.downList.indexOf(tmpDestination));
                    }
                    if (controller.upList.isEmpty() && !controller.upListNext.isEmpty()) {
                        controller.upList = controller.upListNext;
                        controller.upListNext = new ArrayList<>();
                    }
                    if (controller.downList.isEmpty() && !controller.downListNext.isEmpty()) {
                        controller.downList = controller.downListNext;
                        controller.downListNext = new ArrayList<>();
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

