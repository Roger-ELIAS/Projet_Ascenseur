package sample;

public class Cabin {

    boolean isMoving=false;
    boolean stopNext = false;
    boolean stop = false;
    Controller controller;
    FloorSensor sensor;
    Motor simpleMotor;

    public void setController(Controller controller) {
        this.controller = controller;
        this.simpleMotor = new SimpleMotor(controller);
    }

    public class FloorSensor extends Thread {
        public void run() {
            try {
                while(true) {
                    if (controller.desynchTime == 0) {
                        sleep(1000);
                    }
                    else {
                        long time = (long) controller.desynchTime / 1000000;
                        System.out.println(time);
                        if (controller.cabinDirection.equals(Movement.UP))
                            sleep(1000 - time%1000);
                        else
                            sleep(time%1000);
                        System.out.println("Here");
                        controller.desynchTime = 0;
                    }
                    if(!stopNext) {
                        System.out.println("I passed a floor");
                        controller.sendNotif();
                    }
                    else{
                        isMoving = false;
                        sleep(5000);
                        System.out.println("Exiting");
                        System.out.println(controller.upList);
                        stopNext = false;
                        this.interrupt();
                    }
                }
            } catch (InterruptedException e) {
                if(isInterrupted())
                    this.interrupt();
            }
        }
    }


    public void goUp() {
        if(isMoving) return;
        isMoving=true;
        controller.cabinDirection = Movement.UP;
        simpleMotor.goUp();
        sensor = new FloorSensor();
        sensor.start();

    }

    public void goDown(){
        if(isMoving) return;
        isMoving=true;
        controller.cabinDirection = Movement.DOWN;
        simpleMotor.goDown();
        sensor = new FloorSensor();
        sensor.start();
    }

    public void emergencyStop(){
        simpleMotor.emergencyStop();
    }

    public void stopNext(){
        System.out.println("The cabin will stop at the next floor");
        simpleMotor.stopNext();
        stopNext = true;
    }

    public void stop(){ isMoving = false;}
}