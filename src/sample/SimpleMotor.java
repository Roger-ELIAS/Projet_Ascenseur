package sample;

import static java.lang.Thread.sleep;

public class SimpleMotor implements Motor {

    public Controller controller;

    public SimpleMotor(Controller controller) {
        this.controller = controller;
    }

    @Override
    public void goUp() {
        Test2.startMovingLift(controller.getMaxTravelValue(), true);
    }

    @Override
    public void goDown() {
        Test2.startMovingLift(controller.getMaxTravelValue(), false);
    }

    @Override
    public void stopNext() {
        try {
            sleep(1000);
            Test2.stopMovingLift();
        } catch (InterruptedException e) {

        }
    }

    @Override
    public void emergencyStop() {
        Test2.stopMovingLift();
    }
}
