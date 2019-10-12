package sample;

public class Cabin {

    boolean isMoving=false;
    boolean stopNext = false;
    boolean stop = false;
    Controller controller;
    FloorSensor sensor = new FloorSensor();

    public class FloorSensor extends Thread {
        public void run() {
            try {
                if(controller.desynchTime == 0) {
                    sleep(1000);
                }
                else{
                    long time = (long)controller.desynchTime*1000000;
                    if(controller.cabinMovement.equals(Movement.UP))
                        sleep(1000 - time);
                    else
                        sleep(time);
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
        if(isMoving) return;
        isMoving=true;
        System.out.println("The cabin goes up");
        sensor.start();
        try {
            sensor.join();
            while(!stop) {
                sensor.start();
                sensor.join();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void goDown(){
        if(isMoving) return;
        isMoving=true;
        System.out.println("The cabin goes down");
    }

    public void emergencyStop(){
        System.out.println("Instantly stop the cabin");

    }

    public void stopNext(){
        System.out.println("The cabin will stop to the next floor");
        stopNext = true;
    }
}