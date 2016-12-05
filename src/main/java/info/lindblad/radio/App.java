package info.lindblad.radio;

import info.lindblad.radio.model.Coverage;
import info.lindblad.radio.model.Island;
import info.lindblad.radio.model.TransmitterTower;
import info.lindblad.radio.solver.MatrixSolver;
import info.lindblad.radio.solver.Solver;
import info.lindblad.radio.util.InputParser;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.*;


public class App
{

    public static void main(String[] args)
    {
        /*
          Convert provided command line flags to a set.
         */
        HashSet<String> optionFlags = new HashSet<>(Arrays.asList(args));

        /*
          Attempt to parse standard input and construct an island if the --stdin flag is set, otherwise
          we read from a file called input.txt
         */
        Island island;
        if (optionFlags.contains("--stdin")) {
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            island = InputParser.parse(br).orElseThrow(() -> new RuntimeException("Could not parse input and construct an island. Exiting."));
        } else {
            island = InputParser.islandFromFile(InputParser.DEFAULT_INPUT_FILENAME);
        }

        Optional<Island> islandOptional= Optional.ofNullable(island);

        if (islandOptional.isPresent()) {
            island = islandOptional.get();

            if (optionFlags.contains("--visualise")) {
                System.out.println(island.toString(new Coverage(island)));
            }

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
            for (Map.Entry<TransmitterTower, Integer> change : solver.getRequiredTransmitterTowerChanges(island).entrySet()) {
                System.out.println(String.format("%d %d", change.getKey().getId(), change.getValue()));
            }
        } else {
            System.err.println("No island provided. Exiting.");
        }
    }
}
