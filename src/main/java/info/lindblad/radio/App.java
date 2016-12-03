package info.lindblad.radio;

import info.lindblad.radio.model.*;
import info.lindblad.radio.solver.*;
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

            System.out.println(island.toString(new Coverage(island)));

            MatrixSolver solver = new MatrixSolver();

            /*
                Use the solver to calculate the number of receiver towers that have signal coverage.
             */
            int nbrOfReceiversWithCoverage = island.getNbrOfReceiverTowers() - Solver.nbrOfReceiverTowersWithoutCoverage(island);
            System.out.println(String.format("%d/%d", nbrOfReceiversWithCoverage, island.getNbrOfReceiverTowers()));

            /*
                Use the solver to calculate the required transmitter tower power changes that will give
                all receiver towers signal coverage.
             */
//            Solver solver = new IterativeSolver();
            for (Map.Entry<TransmitterTower, Integer> change : solver.getRequiredTransmitterTowerChanges(island).entrySet()) {
                System.out.println(String.format("%d %d", change.getKey().getId(), change.getValue()));
            }
        } else {
            System.err.println("Could not parse input and construct an island. Exiting.");
            System.exit(1);
        }
    }
}
