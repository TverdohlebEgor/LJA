package test;

public abstract class Test {
    abstract void test();
    void assertThat(boolean condition){
        if(!condition){
            throw new AssertionError();
        }
    }
}
