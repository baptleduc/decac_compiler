package fr.ensimag.deca;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import java.io.File;
import java.util.List;


public class TestCompilerOptions {

    @Test
    public void testParseBannerOption() throws CLIException {
        CompilerOptions options = new CompilerOptions();
        options.parseArgs(new String[]{"-b"});
        assertTrue(options.getPrintBanner());
    }

    @Test
    public void testParseBannerOptionWithOtherOptions() {
        CompilerOptions options = new CompilerOptions();
        assertThrows(CLIException.class, () -> options.parseArgs(new String[]{"-b", "-v"}));
    }

    @Test
    public void testParseVerificationOption() throws CLIException {
        CompilerOptions options = new CompilerOptions();
        options.parseArgs(new String[]{"-v"});
        assertTrue(options.getVerify());
    }

    @Test
    public void testParseNoCheckOption() throws CLIException {
        CompilerOptions options = new CompilerOptions();
        options.parseArgs(new String[]{"-n"});
        assertTrue(options.getNoCheck());
    }

    @Test
    public void testParseDebugOption() throws CLIException {
        CompilerOptions options = new CompilerOptions();
        options.parseArgs(new String[]{"-d"});
        assertEquals(1, options.getDebug());
        options.parseArgs(new String[]{"-ddd"});
        assertEquals(3, options.getDebug());
    }

    @Test
    public void testParseRegisterOption() throws CLIException {
        CompilerOptions options = new CompilerOptions();
        options.parseArgs(new String[]{"-r", "8"});
        assertEquals(8, options.getRegisters());
    }

    @Test
    public void testParseInvalidRegisterOption() {
        CompilerOptions options = new CompilerOptions();
        assertThrows(CLIException.class, () -> options.parseArgs(new String[]{"-r", "17"}));
    }

    @Test
    public void testParseMissingRegisterValue() {
        CompilerOptions options = new CompilerOptions();
        assertThrows(CLIException.class, () -> options.parseArgs(new String[]{"-r"}));
    }

    @Test
    public void testParseParallelOption() throws CLIException {
        CompilerOptions options = new CompilerOptions();
        options.parseArgs(new String[]{"-P"});
        assertTrue(options.getParallel());
    }

    @Test
    public void testParseSourceFiles() throws CLIException {
        File file = new File("testFile.deca");
        // Create a temporary file to simulate a valid file
        try {
            file.createNewFile();
        } catch (Exception e) {
            fail("Failed to create test file.");
        }

        CompilerOptions options = new CompilerOptions();
        options.parseArgs(new String[]{file.getAbsolutePath()});
        List<File> sourceFiles = options.getSourceFiles();
        assertEquals(1, sourceFiles.size());
        assertEquals(file.getAbsolutePath(), sourceFiles.get(0).getAbsolutePath());

        // Clean up the temporary file
        assertTrue(file.delete());
    }

    @Test
    public void testParseNonExistentSourceFile() throws CLIException {
        CompilerOptions options = new CompilerOptions();
        assertThrows(CLIException.class, () -> options.parseArgs(new String[]{"nonExistentFile.deca"}));
    }

    @Test
    public void testParseSameSourceFileTwice() throws CLIException {
        File file = new File("testFile.deca");
        // Create a temporary file to simulate a valid file
        try {
            file.createNewFile();
        } catch (Exception e) {
            fail("Failed to create test file.");
        }

        CompilerOptions options = new CompilerOptions();
        options.parseArgs(new String[]{file.getAbsolutePath(), file.getAbsolutePath()});
        List<File> sourceFiles = options.getSourceFiles();
        assertEquals(1, sourceFiles.size());
        assertEquals(file.getAbsolutePath(), sourceFiles.get(0).getAbsolutePath());

        // Clean up the temporary file
        assertTrue(file.delete());
    }

    @Test
    public void testParseConflictingOptions() {
        CompilerOptions options = new CompilerOptions();
        try {
            options.parseArgs(new String[]{"-p", "-v"});
            fail("Expected CLIException for conflicting options.");
        } catch (CLIException e) {
            assertEquals("Cannot use -p and -v at the same time.", e.getMessage());
        }
    }

    @Test
    public void testParseMultipleOptions() throws CLIException {
        File file = new File("testFile.deca");
        try {
            assertTrue(file.createNewFile());
        } catch (Exception e) {
            fail("Failed to create test file.");
        }

        CompilerOptions options = new CompilerOptions();
        options.parseArgs(new String[]{"-P", "-n", "-r", "8", file.getAbsolutePath()});
        assertTrue(options.getParallel());
        assertTrue(options.getNoCheck());
        assertEquals(8, options.getRegisters());
        assertEquals(1, options.getSourceFiles().size());
        assertEquals(file.getAbsolutePath(), options.getSourceFiles().get(0).getAbsolutePath());

        // Clean up the temporary file
        assertTrue(file.delete());
    }
}
