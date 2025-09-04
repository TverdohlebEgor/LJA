package lja;

import lja.annotation.TestAnnotation2I;
import lja.annotation.TestAnnotationI;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

@TestAnnotationI
public class Processor {
   @TestAnnotation2I
   public static void printAnnotationsUsingReflection(String fullClassName) {
      try {
         Class<?> clazz = Class.forName(fullClassName);
         System.out.println("--- Annotations on " + fullClassName + " using reflection ---");
         System.out.println("Class Annotations:");
         Annotation[] classAnnotations = clazz.getAnnotations();
         if (classAnnotations.length == 0) {
            System.out.println("  No class annotations found.");
         }
         for (Annotation annotation : classAnnotations) {
            System.out.println("  - " + annotation.annotationType().getName());
         }
         System.out.println("Method Annotations:");
         Method[] methods = clazz.getDeclaredMethods();
         boolean foundMethodAnnotations = false;
         for (Method method : methods) {
            Annotation[] methodAnnotations = method.getAnnotations();
            if (methodAnnotations.length > 0) {
               foundMethodAnnotations = true;
               System.out.println("  - Method '" + method.getName() + "':");
               for (Annotation annotation : methodAnnotations) {
                  System.out.println("    - " + annotation.annotationType().getName());
               }
            }
         }
         if (!foundMethodAnnotations) {
            System.out.println("  No method annotations found.");
         }
         System.out.println("Field Annotations:");
         Field[] fields = clazz.getDeclaredFields();
         boolean foundFieldAnnotations = false;
         for (Field field : fields) {
            Annotation[] fieldAnnotations = field.getAnnotations();
            if (fieldAnnotations.length > 0) {
               foundFieldAnnotations = true;
               System.out.println("  - Field '" + field.getName() + "':");
               for (Annotation annotation : fieldAnnotations) {
                  System.out.println("    - " + annotation.annotationType().getName());
               }
            }
         }
         if (!foundFieldAnnotations) {
            System.out.println("  No field annotations found.");
         }

      } catch (ClassNotFoundException e) {
         System.err.println("Could not find class: " + fullClassName);
         e.printStackTrace();
      }
   }
}
