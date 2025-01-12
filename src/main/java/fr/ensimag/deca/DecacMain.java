package fr.ensimag.deca;

import java.io.File;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import org.apache.log4j.Logger;

/**
 * Main class for the command-line Deca compiler.
 *
 * @author gl12
 * @date 01/01/2025
 */
public class DecacMain {
    private static Logger LOG = Logger.getLogger(DecacMain.class);
    private static final String BANNER = "    *********************************************\r\n" + //
            "    *                                           *\r\n" + //
            "    *                 Team GL12                 *\r\n" + //
            "    *                                           *\r\n" + //
            "    * Members:                                  *\r\n" + //
            "    *  - Baptiste Le Duc                        *\r\n" + //
            "    *  - Ryan El Aroud                          *\r\n" + //
            "    *  - Mathéo Dupiat                          *\r\n" + //
            "    *  - Malo Nicolas                           *\r\n" + //
            "    *  - Theo Giovanazi                         *\r\n" + //
            "    *                                           *\r\n" + //
            "    *********************************************\r\n";

    public static void main(String[] args) {
        // example log4j message.
        LOG.info("Decac compiler started");
        boolean error = false;
        final CompilerOptions options = new CompilerOptions();
        try {
            options.parseArgs(args);
        } catch (CLIException e) {
            System.err.println("Error during option parsing:\n"
                    + "    " + e.getMessage() + " See the usage below :\n");
            options.displayUsage();
            System.exit(1);
        }
        if (options.getPrintBanner()) {
            System.out.println(BANNER);

	}
        if (!options.getPrintBanner() && options.getSourceFiles().isEmpty()) {
            System.err.println("No file to compile");
            options.displayUsage();
            System.exit(1);
        }
        if (options.getParallel()) {
            error = executeInParallel(options);
        } else {
            for (File source : options.getSourceFiles()) {
                DecacCompiler compiler = new DecacCompiler(options, source);
                if (compiler.compile()) {
                    error = true;
                }
            }
        }
        System.exit(error ? 1 : 0);
    }

    private static boolean executeInParallel(CompilerOptions options) {
        boolean error = false;
        // Create a thread pool with a fixed number of threads (equal to available
        // processors)
        ExecutorService executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
        ArrayList<Future<Boolean>> futures = new ArrayList<Future<Boolean>>();
        for (File source : options.getSourceFiles()) {
            DecacCompiler compiler = new DecacCompiler(options, source);
            // Submit the task to the thread pool
            Future<Boolean> future = executorService.submit(() -> {
                return compiler.compile();
            });
            futures.add(future);
        }

        // Wait for all threads to finish
        for (Future<Boolean> future : futures) {
            try {
                if (future.get()) { // If compilation returns true (error)
                    error = true;
                }
            } catch (Exception e) {
                e.printStackTrace();
                error = true;
            }
        }
        executorService.shutdown();
        return error;
    }
}
