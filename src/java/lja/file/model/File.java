package lja.file.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class File implements FileI {

   private List<Class> classes = new ArrayList<>();
   private List<String> lines;
   private Imports imports;
   private String fileName;
   private String allPackage;
   private Class mainClass;

   public File(String fileName , List<String> lines){
      this.fileName = fileName;
      this.fileName = this.fileName.substring(0,fileName.indexOf(".java"));
      this.lines = lines;
   }

   public void addClass(Class newVal){
      this.classes.add(newVal);
      updateMainClass();
   }

   public void addClasses(List<Class> newVal){
      this.classes.addAll(newVal);
      updateMainClass();
   }

   public void setImports(Imports newVal){
      this.imports = newVal;
   }

   public void setPackage(String newVal){this.allPackage = newVal;}

   public List<Class> classes(){
      return classes;
   }

   private void updateMainClass(){
      for(Class clazz : this.classes){
         if(clazz.name().equals(fileName)){
            mainClass = clazz;
         }
      }
   }

   private Class findClassByname(String className){
      for(Class clazz : classes){
         if(clazz.name().equals(className)){
            return clazz;
         }
      }
      return null;
   }

   private void addLine(Class clazz, String newLine, int y){
      if(clazz == null){
         throw new IllegalStateException("Class not founded");
      }
      lines.add(y,newLine);
      clazz.incrementAllHigher(y,1);
   }
   private void addLine(String newLine, int y){addLine(mainClass,newLine,y);}
   private void addLine(String className, String newLine, int y){addLine(findClassByname(className),newLine,y);}

   private void addLines(Class clazz, List<String> newLine, int y){
      if(clazz == null){
         throw new IllegalStateException("Class not founded");
      }
      lines.addAll(y,newLine);
      clazz.incrementAllHigher(y,newLine.size());
   }
   private void addLines(List<String> newLine, int y){addLines(mainClass,newLine,y);}
   private void addLines(String className, List<String> newLine, int y){addLines(findClassByname(className),newLine,y);}


   private void addToMethod(Class clazz, Method method , String newLine, int y){
      if(clazz == null){
         throw new IllegalStateException("Class not founded");
      }
      addLine(clazz,newLine,y);
   }
   private void addToStartOfMethod(Class clazz, String methodName, String newLine){
      Method method = clazz.findMethodByName(methodName);
      if(method == null){
         throw new IllegalStateException("method not founded in class");
      }
      addToMethod(clazz,method,newLine,method.initialLine()+1);
   }
   public void addToStartOfMethod(String methodName, String newLine){ addToStartOfMethod(mainClass,methodName,newLine);}
   public void addToStartOfMethod(String className, String methodName, String newLine){addToStartOfMethod(findClassByname(className),methodName,newLine);}


   private void addToEndOfMethod(Class clazz, String methodName, String newLine){
      Method method = clazz.findMethodByName(methodName);
      if(method == null){
         throw new IllegalStateException("method not founded in class");
      }
      addToMethod(clazz,method,newLine,method.finalLine());
   }
   public void addToEndOfMethod(String methodName, String newLine){ addToEndOfMethod(mainClass,methodName,newLine);}
   public void addToEndOfMethod(String className, String methodName, String newLine){addToEndOfMethod(findClassByname(className),methodName,newLine);}


   private void createStaticBlock(Class clazz){
      if(clazz == null){
         throw new IllegalStateException("Class not founded");
      }
      addLines(
            clazz,
            List.of(
                  "Static",
                  "{",
                  "}"),
            clazz.initialLine()+1
      );

      clazz.setStaticBlock(Optional.of(
            new StaticBlock(
                  clazz.initialLine()+1,
                  clazz.initialLine()+3
            )
      ));
   }
   public void createStaticBlock(){createStaticBlock(mainClass);}
   public void createStaticBlock(String className){createStaticBlock(findClassByname(className));}

   private void addToStaticBlock(Class clazz, String newLine){
      if(clazz == null){
         throw new IllegalStateException("Class not founded");
      }
      if(clazz.staticBlock().isEmpty()){
         createStaticBlock(clazz);
      }
      addLine(
            clazz,
            newLine,
            clazz.staticBlock().get().initialLine()+1
      );
   }
   public void addToStaticBlock(String newLine){ addToStaticBlock(mainClass, newLine);}
   public void addToStaticBlock(String className, String newLine) {addToStaticBlock(findClassByname(className),newLine);}

   private void addClassField(Class clazz, ClassField classField,String newLine){
      if(clazz == null){
         throw new IllegalStateException("Class not founded");
      }
      addLine(clazz,newLine,clazz.initialLine()+1);
      clazz.addClassField(classField);
   }
   public void addClassField(String newLine, ClassField classField){addClassField(mainClass,classField,newLine);}
   public void addClassField(String className, ClassField classField ,String newLine){addClassField(findClassByname(className),classField,newLine);}

   private void addMethod(Class clazz, Method method, List<String> lines){
      if(clazz == null){
         throw new IllegalStateException("Class not founded");
      }
      addLines(lines,clazz.finalLine()-1);
      clazz.addMethod(method);
   }
   public void addMethod(List<String> lines, Method method){addMethod(mainClass,method,lines);}
   public void addMethod(String className,List<String> lines, Method method){addMethod(findClassByname(className),method,lines);}

   public String getFullClassName(){return allPackage+"."+mainClass.name();}
   public boolean hasMainClass(){return mainClass != null;}
}
