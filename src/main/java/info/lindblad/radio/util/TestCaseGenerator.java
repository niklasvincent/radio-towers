package info.lindblad.radio.util;


import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.IntStream;

import info.lindblad.radio.model.*;

public class TestCaseGenerator {

    private static final int MIN_ISLAND_SIZE = 10;
    private static final int MAX_ISLAND_SIZE = 40;
    private static final int MAX_NBR_OF_TRANSMITTER_TOWERS_= 10;
    private static final int MAX_TRANSMITTER_TOWER_POWER = 5;
    private static final int MAX_NBR_OF_RECEIVER_TOWERS_= 10;

    public static Island generateIsland() {
        int sizeX = getRandomNumberBetween(MIN_ISLAND_SIZE, MAX_ISLAND_SIZE);
        int sizeY = getRandomNumberBetween(MIN_ISLAND_SIZE, MAX_ISLAND_SIZE);
        int nbrOfTransmitterTowers = getRandomNumberBetween(1, MAX_NBR_OF_TRANSMITTER_TOWERS_);
        int nbrOfReceiverTowers = getRandomNumberBetween(1, MAX_NBR_OF_RECEIVER_TOWERS_);

        Island island = new Island(sizeX, sizeY);
        IntStream.rangeClosed(1, nbrOfTransmitterTowers)
                .forEach(id -> {
                        Point point = getRandomPoint(island.getBounds());
                        int power = getRandomNumberBetween(1, MAX_TRANSMITTER_TOWER_POWER);
                        TransmitterTower transmitterTower = new TransmitterTower(id, point, power);
                        island.addTransmitterTower(transmitterTower);
                });
        IntStream.rangeClosed(1, nbrOfReceiverTowers)
                .forEach(id -> {
                    Point point = getRandomPoint(island.getBounds());
                    ReceiverTower receiverTower = new ReceiverTower(id, point);
                    island.addReceiverTower(receiverTower);
                });

        return island;
    }

    private static Point getRandomPoint(Bounds bounds) {
        int x = getRandomNumberBetween(0, bounds.getSizeX() - 1);
        int y = getRandomNumberBetween(0, bounds.getSizeY() - 1);
        return new Point(x, y);
    }

    private static int getRandomNumberBetween(int min, int max) {
        return ThreadLocalRandom.current().nextInt(min, max + 1);
    }

}
