package test;

import lja.annotation.TestAnnotationI;

@TestAnnotationI
public class TestAnnotationTest extends Test {
    public void test(){
        assertThat(testAnnotation().equals("test"));
    }
}
