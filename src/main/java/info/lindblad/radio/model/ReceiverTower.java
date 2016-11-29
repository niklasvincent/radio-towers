package info.lindblad.radio.model;

public class ReceiverTower extends Tower {

    public ReceiverTower(int id, Point point) {
        super(id, point);
    }

    public String toString() {
        return String.format("Reciever %d at %s", this.id, this.point.toString());
    }

}