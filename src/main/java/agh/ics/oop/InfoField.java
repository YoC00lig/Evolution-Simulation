package agh.ics.oop;

public class InfoField { // zawiera informacje o danej pozycji na mapie - liczbę elementów na tym polu, liczbę zwierzątek które na nim umarły
    public int death = 0;
    public int elements = 0;
    public Vector2d position;

    public InfoField(Vector2d pos) {
        this.position = pos;
    }

    public void incrementElementsStatus() {
        this.elements += 1;
    }
    public void decrementElementsStatus() {
        this.elements -= 1;
    }
    public void incrementDeathStatus() {
        this.death += 1;
    }
    public void setDeathStatus(int value) {
        this.death = value;
    }
}
