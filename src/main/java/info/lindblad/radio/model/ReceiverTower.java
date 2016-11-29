package info.lindblad.radio.model;

public class ReceiverTower extends Tower {

    public ReceiverTower(int id, Point point) {
        super(id, point);
    }

    public String toString() {
        return String.format("Receiver %d at %s", this.id, this.point.toString());
    }

    /**
     * Check equality between two receiver towers
     *
     * @param o Other receiver tower
     * @return Whether the two receiver towers are the same
     */
    @Override
    public boolean equals(Object o) {
        return (o instanceof ReceiverTower) && (((ReceiverTower) o).getId() == getId()
                && ((ReceiverTower) o).getPoint().equals(getPoint())
        );
    }

}