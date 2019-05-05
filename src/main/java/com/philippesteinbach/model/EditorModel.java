package com.philippesteinbach.model;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.List;

/**
 * This class represents our model (text) which can be saved and opened.
 */
public class EditorModel {

    /**
     * This function saves a textfile (usually the current file) to the hard drive.
     * @param textFile The textfile which should be saved
     */
    public IOResult<TextFile> save(TextFile textFile) {
        try {
            Files.write(textFile.getFile(), textFile.getContent(), StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
            System.out.println("saved file " + textFile.getFile());
            return new IOResult<>(true, textFile);
        } catch (IOException e) {
            e.printStackTrace();
            return new IOResult<>(false, null);
        }
    }

    /**
     * This function loads all lines of the desired file and builds an IOResult object.
     * @param file The path to the file we want to open.
     * @return IOResult object with success state and the TextFile object as payload
     */
    public IOResult<TextFile> open(Path file) {
        try {
            List<String> lines = Files.readAllLines(file);
            return new IOResult<>(true, new TextFile(file, lines));
        } catch (IOException e) {
            e.printStackTrace();
            return new IOResult<>(false, null);
        }
    }

    /**
     * Quit the application
     */
    public void close() {
        System.exit(0);
    }
}