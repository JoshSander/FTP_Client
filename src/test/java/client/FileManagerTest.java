//Used the following pages as resources:
//https://stackoverflow.com/questions/1119385/junit-test-for-system-out-println
//https://stackoverflow.com/questions/5694385/getting-the-filenames-of-all-files-in-a-folder
//https://stackoverflow.com/questions/1158777/rename-a-file-using-java

package client;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

// For BeforeEach
import java.io.File;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class FileManagerTest {

    private FileManager fileManager;

    private static final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private static final ByteArrayOutputStream errContent = new ByteArrayOutputStream();
    private static final PrintStream originalOut = System.out;
    private static final PrintStream originalErr = System.err;

    {
        fileManager = new FileManager();
    }

    @BeforeAll
    public static void setUpStreams() {
        System.setOut(new PrintStream(outContent));
        System.setErr(new PrintStream(errContent));
    }

    @BeforeEach
    public void setup() {
        // Make sure to restore sample.txt after renameFileLS()
        File sample = new File("src/main/resources/" + "sample.txt");
        File pizza = new File("src/main/resources/" + "pizza.txt");
        pizza.renameTo(sample);

    }

    @AfterAll
    public static void restoreStreams() {
        System.setOut(originalOut);
        System.setErr(originalErr);
    }

    @Test
    public void setLocalPath(){
        String test = "test";
        fileManager.setLocalPath(test);

        assertEquals(test,fileManager.getLocalPath());
    }

    @Test
    public void displayLocal() {
        fileManager.displayLocal();

        assertTrue(outContent.toString().contains("folder"));
        assertTrue(outContent.toString().contains("sample.txt"));

    }

    @Test
    public void renameFileLS() {
        // This unit test looks for sample.txt in src/main/resources/ and renames it to pizza.txt.
        // There should be a pizza.txt file after this test is run.
        String original = "sample.txt";
        String test = "pizza.txt";

        fileManager.setLocalPath("src/main/resources/");
        fileManager.renameFileLS(original, test);

        assertEquals("File " + original + " renamed to " + test + "!", outContent.toString());


    }

    @Test
    public void renameFileLSInvalid() {
        // This unit test looks for a non-existent file called "foobar.txt" in src/main/resources.
        // There should be no foobar.txt or pizza.txt.
        String original = "foobar.txt";
        String test = "pizza.txt";

        fileManager.setLocalPath("src/main/resources/");
        fileManager.renameFileLS(original, test);

        assertEquals("ERROR: File Not renamed!", outContent.toString());


    }

    @AfterEach
    public void teardown() {
        outContent.reset();
    }
}
