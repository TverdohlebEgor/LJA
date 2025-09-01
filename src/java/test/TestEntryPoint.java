package test;

import java.util.logging.Logger;
public class TestEntryPoint {

    private static final Logger log = Logger.getLogger(TestEntryPoint.class.getName());
    public static void main(String[] args) {
        testAnnotation(new TestAnnotationTest(),"Test Annotation");
        testAnnotation(new FileFactoryTest(), "Test FileFactory");
    }

	public static void testAnnotation(Test test, String name){
		log.info("Testing "+name);
		try{
			test.test();
			log.info("SUCCESS");
		} catch(Exception e){
			log.severe("FAILED");
			log.severe(e.getMessage());
		}
	}
}
