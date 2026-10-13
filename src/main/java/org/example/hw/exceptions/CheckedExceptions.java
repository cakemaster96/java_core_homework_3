package org.example.hw.exceptions;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Path;

public class CheckedExceptions {


    public void throwsFileNotFoundTest() throws FileNotFoundException {
        Path path = Path.of("testFile.txt");

        try (FileInputStream fileInputStream = new FileInputStream(path.toFile())) {
        }
       catch (FileNotFoundException e) {
           throw e;
       }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}

