package lja.annotation;

import java.nio.file.Path;

public interface Annotation {
    String name();
    void process(Path filePath);
    int priority();
}
