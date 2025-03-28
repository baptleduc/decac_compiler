package fr.ensimag.deca;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

/**
 * User-specified options influencing the compilation.
 *
 * @author gl12
 * @date 01/01/2025
 */
public class CompilerOptions {
    public static final int QUIET = 0;
    public static final int INFO = 1;
    public static final int DEBUG = 2;
    public static final int TRACE = 3;

    // Options
    private int debug = 0;
    private int registers = -1; // By default, no limitations on the registers to be used

    private boolean verify = false;
    private boolean parse = false;
    private boolean noCheck = false;
    private boolean parallel = false;
    private boolean printBanner = false;
    private boolean warnings = false;
    private boolean arm = false;
    private boolean isM2 = false;

    private List<File> sourceFiles = new ArrayList<File>();
    private HashSet<String> sourceFilesPath = new HashSet<String>();

    public int getDebug() {
        return debug;
    }

    public int getRegisters() {
        return registers;
    }

    public boolean getParallel() {
        return parallel;
    }

    public boolean getParse() {
        return parse;
    }

    public boolean getPrintBanner() {
        return printBanner;
    }

    public boolean getVerify() {
        return verify;
    }

    public boolean getNoCheck() {
        return noCheck;
    }

    public boolean getWarnings() {
        return warnings;
    }

    public boolean getArm() {
        return arm;
    }

    public boolean getIsM2() {
        return isM2;
    }

    public List<File> getSourceFiles() {
        return Collections.unmodifiableList(sourceFiles);
    }

    public void parseArgs(String[] args) throws CLIException {
        for (int i = 0; i < args.length; i++) {
            if (args[i].equals("-b")) {
                handleBannerOption(args);
            } else if (args[i].equals("-p")) {
                handleParseOption();
            } else if (args[i].equals("-v")) {
                handleVerifyOption();
            } else if (args[i].equals("-n")) {
                noCheck = true;
            } else if (args[i].equals("-r")) {
                i = handleRegistersOption(args, i);
            } else if (args[i].startsWith("-d")) {
                debug = args[i].length() - 1; // We remove 1 for '-'
            } else if (args[i].equals("-P")) {
                parallel = true;
            } else if (args[i].equals("-w")) {
                warnings = true;
            } else if (args[i].equals("--arm")) {
                arm = true;
            } else if (args[i].equals("--M2")) {
                isM2 = true;
            } else if (args[i].endsWith(".deca")) {
                handleSourceFile(args[i]);
            } else {
                throw new CLIException(args[i] + " is not recognized.");
            }
        }

        this.configureLogger();
        this.logAssertionsStatus();
    }

    private void configureLogger() {
        Logger logger = Logger.getRootLogger();
        // Map command-line debug option to log4j's level
        switch (getDebug()) {
            case QUIET:
                break; // Default
            case INFO:
                logger.setLevel(Level.INFO);
                break;
            case DEBUG:
                logger.setLevel(Level.DEBUG);
                break;
            case TRACE:
                logger.setLevel(Level.TRACE);
                break;
            default:
                logger.setLevel(Level.ALL);
                break;
        }
        logger.info("Application-wide trace level set to " + logger.getLevel());
    }

    private void logAssertionsStatus() {
        Logger logger = Logger.getRootLogger();
        boolean assertsEnabled = false;
        assert assertsEnabled = true; // Intentional side effect!!!
        if (assertsEnabled) {
            logger.info("Java assertions enabled");
        } else {
            logger.info("Java assertions disabled");
        }
    }

    private int handleRegistersOption(String[] args, int currentIndex) throws CLIException {
        if (currentIndex + 1 >= args.length) {
            throw new CLIException("Missing value for -r option.");
        }
        try {
            registers = Integer.parseInt(args[++currentIndex]);
            if (registers < 4 || registers > 16) {
                throw new CLIException("The number of registers must be between 4 and 16.");
            }
        } catch (NumberFormatException e) {
            throw new CLIException("The number of registers must be an integer.");
        }
        return currentIndex;
    }

    private void handleBannerOption(String[] args) throws CLIException {
        if (args.length > 1) {
            throw new CLIException("Cannot use -b with other options.");
        }
        printBanner = true;
    }

    private void handleVerifyOption() throws CLIException {
        if (parse) {
            throw new CLIException("Cannot use -p and -v at the same time.");
        }
        verify = true;
    }

    private void handleParseOption() throws CLIException {
        if (verify) {
            throw new CLIException("Cannot use -p and -v at the same time.");
        }
        parse = true;
    }

    private void handleSourceFile(String arg) throws CLIException {
        if (sourceFilesPath.contains(arg)) {
            return;
        }
        File file = new File(arg);
        if (!file.exists()) {
            throw new CLIException("File " + arg + " does not exist.");
        }
        sourceFilesPath.add(file.getAbsolutePath());
        sourceFiles.add(file);
    }

    protected void displayUsage() {
        String usage = "usage: decac [-h] [-b] [-p] [-v] [-n] [-r X] [-d]* [-P] [--arm] <deca file>...\n\n"
                + "options:\n"
                + "    -b (banner): displays a banner with the team's name\n"
                + "           (cannot be used with any other options)\n"
                + "    -p (parse): stops decac after the tree construction step and displays the decompilation of the tree\n"
                + "           (i.e., if there is only one source file to compile, the output should be a syntactically correct deca program)\n"
                + "    -v (verification): stops decac after the verification step (produces no output in the absence of errors)\n"
                + "    -n (no check): disables runtime checks specified in points 11.1 and 11.3 of the Deca semantics.\n"
                + "    -r X (registers): limits the available registers to R0 ... R{X-1}, with 4 <= X <= 16\n"
                + "    -d (debug): enables debug traces. Repeat the option multiple times for more traces.\n"
                + "    -P (parallel): if there are multiple source files, compiles them in parallel (to speed up compilation)\n"
                + "    --arm: generates ARM code\n"
                + "    --M2: (only for ARM) the code is generated so that it works at least on MAC M2\n";

        System.out.println(usage);
    }
}
