package test;

//@TestAnnotation
public class TestAnnotationTest extends Test {
    public void test(){
        assertThat(testAnnotation().equals("test"));
    }
}
