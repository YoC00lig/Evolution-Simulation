package agh.ics.oop;

public interface IPositionChangeObserver {
    void positionChanged(Vector2d animal, Vector2d oldPosition, Animal newPosition);

}
