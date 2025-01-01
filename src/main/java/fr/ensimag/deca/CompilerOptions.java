package fr.ensimag.deca;

import java.io.File;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collections;
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
    public static final int INFO  = 1;
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
    private List<File> sourceFiles = new ArrayList<File>();

    public int getDebug() {
        return debug;
    }

    public int getRegisters(){
        return registers;
    }

    public boolean getParallel() {
        return parallel;
    }

    public boolean getParse(){
        return parse;
    }

    public boolean getPrintBanner() {
        return printBanner;
    }
    
    public boolean getVerify(){
        return verify;
    }

    public boolean getNoCheck(){
        return noCheck;
    }

    
    public List<File> getSourceFiles() {
        return Collections.unmodifiableList(sourceFiles);
    }

    
    public void parseArgs(String[] args) throws CLIException {
        if (args.length == 0){
            displayUsage();
            return;
        }

        for(int i = 0; i < args.length; i++){
            if (args[i].equals("-b")){
                printBanner = true;
            }
            else if (args[i].equals("-p")){
                parse = true;
            }
            else if (args[i].equals("-v")){
                verify = true;
            }
            else if (args[i].equals("-n")){
                noCheck = true;
            }
            else if (args[i].equals("-r")){
                registers = Integer.parseInt(args[++i]);
            }
            else if (args[i].startsWith("-d")){
                debug = args[i].length() - 1; // We remove 1 for '-'
            }
            else if (args[i].equals("-P")){
                parallel = true;
            }
            else{
                throw new CLIException(args[i] + " is not recognized.");
            }
        }

        Logger logger = Logger.getRootLogger();
        // map command-line debug option to log4j's level.
        switch (getDebug()) {
        case QUIET: break; // keep default
        case INFO:
            logger.setLevel(Level.INFO); break;
        case DEBUG:
            logger.setLevel(Level.DEBUG); break;
        case TRACE:
            logger.setLevel(Level.TRACE); break;
        default:
            logger.setLevel(Level.ALL); break;
        }
        logger.info("Application-wide trace level set to " + logger.getLevel());

        boolean assertsEnabled = false;
        assert assertsEnabled = true; // Intentional side effect!!!
        if (assertsEnabled) {
            logger.info("Java assertions enabled");
        } else {
            logger.info("Java assertions disabled");
        }
    }

    protected void displayUsage() {
        String usage = "usage : decac [-h] [-b] [-p] [-v] [-n] [-r X] [-d]* [-P] <fichier deca>...\n\n" 
             + "options:\n" 
             + "    -b (banner) : affiche une bannière indiquant le nom de l'équipe\n"
             + "    -p (parse) : arrête decac après l'étape de construction de l'arbre, et affiche la décompilation de ce dernier\n"
             + "           (i.e. s'il n'y a qu'un fichier source à compiler, la sortie doit être un programme deca syntaxiquement correct)\n"
             + "    -v (verification) : arrête decac après l'étape de vérifications (ne produit aucune sortie en l'absence d'erreur)\n"
             + "    -n (no check) : supprime les tests à l'exécution spécifiés dans les points 11.1 et 11.3 de la sémantique de Deca.\n"
             + "    -r X (registers) : limite les registres banalisés disponibles à R0 ... R{X-1}, avec 4 <= X <= 16\n"
             + "    -d (debug) : active les traces de debug. Répéter l'option plusieurs fois pour avoir plus de traces.\n"
             + "    -P (parallel) : s'il y a plusieurs fichiers sources, lance la compilation des fichiers en parallèle (pour accélérer la compilation)\n";

        System.out.println(usage);
    }
}
