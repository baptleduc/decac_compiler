package fr.ensimag.deca;

import java.io.File;
import org.apache.log4j.Logger;

/**
 * Main class for the command-line Deca compiler.
 *
 * @author gl12
 * @date 01/01/2025
 */
public class DecacMain {
    private static Logger LOG = Logger.getLogger(DecacMain.class);
    private static final String BANNER = 
                "    *********************************************\r\n" + //
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
        if (options.getPrintHelp()) {
            options.displayUsage();
            System.exit(0);
        }
        if (options.getPrintBanner()) {
            System.out.println(BANNER);
        }
        if (options.getSourceFiles().isEmpty()) {
            throw new UnsupportedOperationException("decac without argument not yet implemented");
        }
        if (options.getParallel()) {
            // A FAIRE : instancier DecacCompiler pour chaque fichier à
            // compiler, et lancer l'exécution des méthodes compile() de chaque
            // instance en parallèle. Il est conseillé d'utiliser
            // java.util.concurrent de la bibliothèque standard Java.
            throw new UnsupportedOperationException("Parallel build not yet implemented");
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
}
