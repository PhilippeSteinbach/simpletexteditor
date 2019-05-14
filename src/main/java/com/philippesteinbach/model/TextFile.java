package com.philippesteinbach.model;

import java.nio.file.Path;
import java.util.List;

/**
 * This class represents our data structure which we use in the application
 */
public class TextFile {

    private final Path file;
    private final List<String> content;

    public TextFile(Path file, List<String> content) {
        this.file = file;
        this.content = content;
    }

    /**
     * This method returns the path of a file
     * @return path to file
     */
    public Path getFile(){
        return file;
    }

    /**
     * This method returns the content of a file.
     * @return content of textFile (the plain text)
     */
    public List<String> getContent(){
        return content;
    }

}
