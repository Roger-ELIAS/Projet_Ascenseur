package sample;

public class Cabin {

    boolean isMoving=false;
    boolean stopNext = false;
    boolean stop = false;
    FloorSensor sensor = new FloorSensor();


    public class FloorSensor extends Thread {
        public void run() {
            try {
                sleep(1000);
                System.out.println("I passed a floor");
                if(stopNext)
                    stop = true;
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
        //Ajouter un signal quand on passe un étage
    }

    public void emergencyStop(){
        System.out.println("Instantly stop the cabin");
        sensor.interrupt();
    }

    public void stopNext(){
        System.out.println("The cabin will stop to the next floor");
        stopNext = true;
    }
}
