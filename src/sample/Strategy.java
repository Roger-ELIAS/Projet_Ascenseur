package sample;

import java.util.ArrayList;

public interface Strategy {
    public void addInPath(Controller controller, int floorDest, Movement requestedMovement);

    public void addInPath(Controller controller, int floorDest);

    public void howToMove(Controller controller);
}
