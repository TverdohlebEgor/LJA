package test;

import lja.file.model.File;
import lja.file.util.FileFactory;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

public class FileFactoryTest extends Test{
    private Path path = Path.of(System.getProperty("scriptDir"),"..","src","java","test","FileFactoryTest.java");
    public void test(){
        checkNormalization();
        checkFileMaking();
    }

    private void checkNormalization(){
        try {
            List<String> lines = FileFactory.getLines(path);
            for(String line : lines){
                if(line.contains("{") || line.contains("}")){
                    assertThat(line.length() == 1);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void checkFileMaking() {
        try {
            File file = FileFactory.fromPath(path);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
