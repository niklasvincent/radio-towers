package info.lindblad.radio.model;

public class ReceiverTower extends Tower {

    public ReceiverTower(int id, Coordinates coordinates) {
        super(id, coordinates);
    }

    public String toString() {
        return String.format("Reciever at %s", this.coordinates.toString());
    }

}