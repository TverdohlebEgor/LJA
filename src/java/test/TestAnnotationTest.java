package test;

import lja.annotation.test.TestAnnotationI;

@TestAnnotationI
public class TestAnnotationTest extends Test {
    public void test(){
        assertThat(testAnnotation().equals("test"));
    }
}
