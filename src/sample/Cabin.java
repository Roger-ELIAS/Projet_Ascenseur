package sample;

public class Cabin {

    boolean isMoving=false;
    boolean stopNext = false;
    boolean stop = false;
    Controller controller;
    FloorSensor sensor;
    Motor simpleMotor;

    public Cabin() {
        simpleMotor = new SimpleMotor(controller);
    }

    public class FloorSensor extends Thread {
        public void run() {
            try {
                if(controller.desynchTime == 0) {
                    sleep(1000);
                }
                else{
                    long time = (long)controller.desynchTime/1000000;
                    if(controller.cabinMovement.equals(Movement.UP))
                        sleep(1000 - time);
                    else
                        sleep(time);
                    controller.desynchTime = 0;
                }
                System.out.println("I passed a floor");
                controller.sendNotif();
                if(stopNext) {
                    stop = true;
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }


    public void goUp(){
        System.out.println("The cabin goes up");
        if(isMoving) return;
        isMoving=true;
        simpleMotor.goUp();
        sensor = new FloorSensor();
        if(sensor.getState().equals("NEW"))
            sensor.start();
        try {
            sensor.join();
            while(!stop) {
                if(sensor.getState().equals("TERMINATED"))
                    sensor = new FloorSensor();
                sensor.start();
                sensor.join();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void goDown(){
        System.out.println("The cabin goes down");
        if(isMoving) return;
        isMoving=true;
        simpleMotor.goDown();
        sensor = new FloorSensor();
        sensor.start();
        try {
            sensor.join();
            while(!stop) {
                sensor = new FloorSensor();
                sensor.start();
                sensor.join();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void emergencyStop(){
        simpleMotor.emergencyStop();
    }

    public void stopNext(){
        System.out.println("The cabin will stop at the next floor");
        simpleMotor.stopNext();
        stopNext = true;
    }
}