package info.lindblad.radio;

import info.lindblad.radio.model.*;
import info.lindblad.radio.solver.IterativeSolver;
import info.lindblad.radio.util.InputParser;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Map;
import java.util.Optional;


public class App
{

    public static void main( String[] args )
    {
        /*
          Attempt to parse input and construct an island.
         */
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        Optional<Island> islandOptional = InputParser.parse(br);

        if (islandOptional.isPresent()) {
            Island island = islandOptional.get();

            /*
                Use the solver to calculate the number of receiver towers that have signal coverage.
             */
            int nbrOfReceiversWithCoverage = island.nbrOfReceiverTowers() - IterativeSolver.nbrOfReceiverTowersWithoutCoverage(island);
            System.out.println(String.format("%d/%d", nbrOfReceiversWithCoverage, island.nbrOfReceiverTowers()));

            /*
                Use the solver to calculate the required transmitter tower power changes that will give
                all receiver towers signal coverage.
             */
            for (Map.Entry<TransmitterTower, Integer> change : IterativeSolver.getRequiredTransmitterTowerChanges(island).entrySet()) {
                System.out.println(String.format("%d %d", change.getKey().getId(), change.getValue()));
            }
        } else {
            System.err.println("Could not parse input and construct an island. Exiting.");
            System.exit(1);
        }
    }
}
