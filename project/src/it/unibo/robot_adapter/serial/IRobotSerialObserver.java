package it.unibo.robot_adapter.serial;

/**
 * Interface for an Observer of a SerialRobot.
 */
@FunctionalInterface
public interface IRobotSerialObserver {

    /**
     * Notify the observer that the observable it is registered to was updated.
     *
     * @param data the new data from the observable object
     */
    void notify(String data);
}
