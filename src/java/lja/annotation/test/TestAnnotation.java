package lja.annotation.test;

import lja.annotation.Annotation;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class TestAnnotation implements Annotation {
    @Override
    public String name(){
        return "TestAnnotationI";
    }
    @Override
    public void process(Path filePath) {
        List<String> lines = new ArrayList<>();
        try {
            lines = Files.readAllLines(filePath);
        } catch (IOException e) {
            throw new IllegalArgumentException("Annotation "+name()+" Cant read the file at "+filePath.toAbsolutePath());
        }

        lines.add(lines.size()-1,"public String testAnnotation() { return \"test\"; }");

        try {
            Files.write(filePath, lines);
        } catch (IOException e) {
            throw new IllegalArgumentException("Annotation "+name()+" Cant write the file at "+filePath.toAbsolutePath());
        }
    }

    @Override
    public int priority(){
        return 0;
    }
}
