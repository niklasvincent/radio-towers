package info.lindblad.radio;

import info.lindblad.radio.model.Point;
import info.lindblad.radio.model.Island;
import info.lindblad.radio.model.ReceiverTower;
import info.lindblad.radio.model.TransmitterTower;

import java.util.Map;
import java.util.Scanner;

public class App
{

    public static void main( String[] args )
    {
        Scanner scan = new Scanner(System.in);
        int sizeX = scan.nextInt();
        int sizeY = scan.nextInt();

        Island island = new Island(sizeX, sizeY);

        // TODO(nlindblad): Improve this
        boolean readingTransmitters = false;
        while (scan.hasNextInt()) {
            int id = scan.nextInt();
            int x = scan.nextInt();
            int y = scan.nextInt();
            Point point = new Point(x, y);
            readingTransmitters = (id == 1) != readingTransmitters;
            if (readingTransmitters) {
                int power = scan.nextInt();
                TransmitterTower transmitterTower = new TransmitterTower(id, point, power);
                island.addTransmitterTower(transmitterTower);
            } else {
                ReceiverTower receiverTower = new ReceiverTower(id, point);
                island.addReceiverTower(receiverTower);
            }
        }

        System.out.println(island);

        System.out.println(String.format("%d/%d", island.nbrOfReceiverTowersWithCoverage(), island.nbrOfReceiverTowers()));

        for ( Map.Entry<TransmitterTower, Integer> change : island.getNecessaryChanges().entrySet() ) {
            System.out.println(String.format("%d %d", change.getKey().getId(), change.getValue()));
        }

    }
}
