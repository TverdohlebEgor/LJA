package lja;

import lja.annotation.Annotation;
import lja.annotation.TestAnnotation;
import lja.file.model.File;
import lja.file.util.FileFactory;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Stream;

class ProcessorEntryPoint {
    private static Path projectRoot;
    private static Path targetRoot;
    private static final Logger log = Logger.getLogger(ProcessorEntryPoint.class.getName());
    private static final List<Annotation> annotationList =  new ArrayList<>();

    private static void initAnnotations(){
        annotationList.add(new TestAnnotation());

        annotationList.sort(Comparator.comparingInt(Annotation::priority));
    }

    public static void main(String[] args) throws IOException {
        initAnnotations();

        if(args.length <= 1){
            throw new IllegalArgumentException("Specify the project folder and target folder");
        }
        projectRoot = Paths.get(args[0]).toAbsolutePath();
        targetRoot = Paths.get(args[1]).toAbsolutePath();
        if(!Files.isDirectory(projectRoot)){
            throw new IllegalArgumentException("the project root "+projectRoot+" doesn't exist / is not a directory");
        }
        log.info("Processing "+projectRoot);

        targetRoot = targetRoot.resolve("processed").resolve(projectRoot.getFileName());
        Files.createDirectories(targetRoot);
        copyFolder(projectRoot,targetRoot);

        try (Stream<Path> pathStream = Files.walk(targetRoot)) {
            pathStream.filter(Files::isRegularFile).filter(path -> path.toString().endsWith(".java")).forEach(path -> {
                try {
                    File file = FileFactory.fromPath(path);
                    if(file.hasMainClass()) {
                        Processor.printAnnotationsUsingReflection(file.getFullClassName());
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
        } catch (IOException e) {
            log.severe(e.getMessage());
        }
    }

    public static void processAnnotations(Path filePath) throws IOException {
        for(Annotation annotation : annotationList){
            for(String line : Files.readAllLines(filePath, StandardCharsets.UTF_8)){
                List<String> tokens = List.of(line.split(" "));
                if(
                   tokens.contains("@"+annotation.name())
                ) {
                    annotation.process(filePath);
                }
            }
        }
    }

    private static void copyFolder(Path source, Path target) throws IOException {
        Files.walkFileTree(source, new SimpleFileVisitor<Path>() {
            @Override
            public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
                Path newDir = target.resolve(source.relativize(dir));
                try {
                    Files.copy(dir, newDir, StandardCopyOption.REPLACE_EXISTING);
                } catch (FileAlreadyExistsException e) {
                    return FileVisitResult.CONTINUE;
                }
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                Path newFile = target.resolve(source.relativize(file));
                Files.copy(file, newFile, StandardCopyOption.REPLACE_EXISTING);
                return FileVisitResult.CONTINUE;
            }
        });
    }
}

