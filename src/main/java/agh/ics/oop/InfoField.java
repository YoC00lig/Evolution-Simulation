package agh.ics.oop;

public class InfoField {
    public int death = 0;
    public int elements = 0;
    // public Vector2d position

    public void incrementElementsStatus() {
        this.elements += 1;
    }
    public void decrementElementsStatus() {
        this.elements -= 1;
    }
    public void incrementDeathStatus() {
        this.death += 1;
    }
}
