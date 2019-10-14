package sample;

public class Cabin {

    boolean isMoving=false;
    boolean stopNext = false;
    boolean stop = false;
    Controller controller;
    FloorSensor sensor = new FloorSensor();
    Motor simpleMotor;

    public void setController(Controller controller) {
        this.controller = controller;
        this.simpleMotor = new SimpleMotor(controller);
    }

    public class FloorSensor extends Thread {
        public void run() {
            try {
                while(!stopNext) {
                    System.out.println(controller.currentFloor);
                    if (controller.desynchTime == 0) {
                        sleep(1000);
                    } else {
                        long time = (long) controller.desynchTime / 1000000;
                        if (controller.cabinMovement.equals(Movement.UP))
                            sleep(1000 - time);
                        else
                            sleep(time);
                        controller.desynchTime = 0;
                    }
                    System.out.println("I passed a floor");
                    controller.sendNotif();
                    System.out.println(controller.currentFloor);

                    if (stopNext) {
                        stop = true;
                    }
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }


    public void goUp(){
        if(isMoving) return;
        isMoving=true;
        simpleMotor.goUp();
        sensor.start();

    }

    public void goDown(){
        if(isMoving) return;
        isMoving=true;
        simpleMotor.goDown();
        sensor.start();
    }

    public void emergencyStop(){
        simpleMotor.emergencyStop();
    }

    public void stopNext(){
        System.out.println("The cabin will stop at the next floor");
        simpleMotor.stopNext();
        sensor.interrupt();
        stopNext = true;
    }
}