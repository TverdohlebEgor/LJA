package lja.file.util;

import lja.file.model.*;
import lja.file.model.Class;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static lja.file.model.FilePieceType.*;
import static lja.file.util.Constant.accessLevelNames;


public class FileFactory {
   public static File fromPath(Path path) throws IOException {
      List<String> lines = getLines(path);
      File file = new File(path.getFileName().toString(),lines);

      file.addClasses(findClasses(lines));
      file.setImports(findImports(lines));
      file.setPackage(findPackage(lines));
      for(Class clazz : file.classes()){
         clazz.addMethods(findMethods(clazz.initialLine(),clazz.finalLine(),lines));
         clazz.addClassFields(findClassFields(clazz.initialLine(),clazz.finalLine(),lines));
      }

      return file;
   }

   public static List<String> getLines(Path path) throws IOException{
      List<String> lines = Files.readAllLines(path);
      normalizeCurlyBrackets(lines);
      return lines;
   }

   private static String findPackage(List<String> lines){
      for(String line : lines){
         List<String> tokens = List.of(line.split(" "));
         if(tokens.getFirst().equals("package")){
            String result = tokens.get(1);
            result = result.substring(0,result.length()-1);
            return result;
         }
      }
      return null;
   }

   private static Imports findImports(List<String> lines){
     int initPos = -1;
     int finalPos = 0;
     for(int y = 0; y < lines.size(); ++y){
        String line = lines.get(y);
        if(line.contains("import")){
           if(initPos == -1){
              initPos = y;
           }
           finalPos = y;
        }
     }
     return new Imports(initPos,finalPos);
   }

   private static List<ClassField> findClassFields(int initLines, int finalLines,List<String> lines){
      List<ClassField> result = new ArrayList<>();
      for(int liney = initLines; liney <= finalLines; liney++){
         String line = lines.get(liney);
         if(!line.contains("(") && nextLineIs(liney,lines) != OPENING_BRACKETS && nextLineIs(liney-1,lines) == EXPRESSION){
            List<String> tokens = Arrays.stream(lines.get(liney).split(" ")).collect(Collectors.toCollection(ArrayList::new));
            AccessLevel accessLevel = extractAccessLevel(tokens);
            if(accessLevelNames.contains(tokens.getFirst())){
               tokens.removeFirst();
            }
            boolean isStatic = false;
            if(tokens.getFirst().equals("static")){
               isStatic= true;
               tokens.removeFirst();
            }
            String type = tokens.removeFirst();
            String name = tokens.removeFirst();
            result.add(
                  new ClassField(
                        accessLevel,
                        isStatic,
                        type,
                        name,
                        liney
                  )
            );
         }
      }
      return result;
   }

   private static List<Method> findMethods(int initLines, int finalLines, List<String> lines){
      List<Method> result = new ArrayList<>();
      int liney = initLines;
      while(liney < lines.size() && liney <= finalLines){
         String line = lines.get(liney);
         if(lineIsMethod(line,lines)){
            int initialLine = liney;
            int finalLine = findClosingBracketsOf(liney+1,lines);

            String name;
            List<String> tokens = List.of(line.split(" "));
            AccessLevel accessLevel = extractAccessLevel(tokens);
            tokens = Arrays.stream(line.split(" ")).filter(t -> t.contains("(")).toList();
            name = tokens.getFirst().substring(0,tokens.getFirst().indexOf("("));

            List<Field> fields = new ArrayList<>();
            String allMethodFieldsString = line.substring(line.indexOf("(")+1,line.lastIndexOf(")"));
            List<String> fieldList= List.of(allMethodFieldsString.split(","));
            if(!fieldList.isEmpty() && !fieldList.getFirst().isEmpty()) {
               for (String couple : fieldList) {
                  couple = couple.strip();
                  List<String> singleFieldTokens = List.of(couple.split(" "));
                  if (singleFieldTokens.size() != 2) {
                     throw new IllegalArgumentException("In this line an odd number of fields have been found, this should be impossible (Every field has type and name) y:" + liney + "\nline: " + line);
                  }
                  fields.add(
                        new Field(
                              singleFieldTokens.get(0),
                              singleFieldTokens.get(1)
                        )
                  );
               }
            }
            result.add(new Method(
                  initialLine,
                  finalLine,
                  name,
                  accessLevel,
                  fields
            ));
         }
         liney+=1;
      }
      return result;
   }

   private static Optional<StaticBlock> findStatickBlock(int initLines, int finalLines, List<String> lines){
      Optional<StaticBlock> staticBlock = Optional.empty();

      for(int liney = initLines; liney <= finalLines; liney++){
         List<String> tokens = List.of(lines.get(liney).split(" "));
         if(tokens.size() == 1 && tokens.getFirst().equals("static") && nextLineIs(liney,lines) == OPENING_BRACKETS){
            staticBlock = Optional.of(new StaticBlock(liney+1, findClosingBracketsOf(liney+1,lines)));
            break;
         }
      }

      return staticBlock;
   }

   private static List<Class> findClasses(List<String> lines){
      List<Class> classes = new ArrayList<>();
      for(String line : lines){
         if(line.contains("class")
               && !line.contains(";")
               && nextLineIs(lines.indexOf(line),lines) == OPENING_BRACKETS
               && nextLineIs(lines.indexOf(line)-1,lines) == CLASS
         ){
            int iniLine = lines.indexOf(line);
            int finalLine = findClosingBracketsOf(iniLine+1,lines);
            List<String> tokens = Arrays.stream(line.split(" ")).collect(Collectors.toCollection(ArrayList::new));
            AccessLevel accessLevel = extractAccessLevel(tokens);
            while(!tokens.getFirst().equals("class")){
               tokens.removeFirst();
            }
            tokens.removeFirst();
            String name = tokens.getFirst();
            classes.add(
                  new Class(
                        iniLine,
                        finalLine,
                        name,
                        accessLevel,
                        findStatickBlock(iniLine,finalLine,lines)
                  )
            );
         }
      }
      return classes;
   }

   private static AccessLevel extractAccessLevel(List<String> tokens){
      AccessLevel accessLevel;
      if(accessLevelNames.contains(tokens.getFirst())){
         accessLevel = AccessLevel.valueOf(tokens.getFirst().toUpperCase());
      } else {
         accessLevel = AccessLevel.PROTECTED;
      }
      return accessLevel;
   }

   //put's every curly brakest on its own line
   private static void normalizeCurlyBrackets(List<String> lines) {
      List<String> normalizedLines = new ArrayList<>();
      for (String line : lines) {
         String[] parts = line.split("(?=[{}])|(?<=[{}])");
         for (String part : parts) {
            String strippedPart = part.strip();
            if (!strippedPart.isEmpty()) {
               normalizedLines.add(strippedPart);
            }
         }
      }
      lines.clear();
      lines.addAll(normalizedLines);
   }

   private static FilePieceType nextLineIs(int liney, List<String> lines){
      if(liney+1 >= lines.size()){
         return EOF;
      }
      String line = lines.get(liney+1);
      switch (line) {
         case "{" -> {
            return OPENING_BRACKETS;
         }
         case "}" -> {
            return CLOSING_BRACKETS;
         }
         case "static" -> {
            return STATIC_BLOCK;
         }
      }
      if(lineIsMethod(line,lines)){
         return METHOD;
      } else if (lineIsClassField(line,lines)) {
         return CLASS_FIELD;
      }  else if(line.contains("for (") || line.contains("for(")){
         return FOR;
      } else if(line.contains("while (") || line.contains("while(")){
         return WHILE;
      } else if(line.contains(";")){
         return EXPRESSION;
      } else if(line.contains("class") && !line.contains("(") && !line.contains(")")){
         return CLASS;
      }
      return NOP;
   }

   private static boolean lineIsClassField(String line, List<String> lines){
      int liney = lines.indexOf(line);
      return !line.contains("(") && !line.contains("@") && nextLineIs(liney,lines) != OPENING_BRACKETS;
   }

   private static boolean lineIsMethod(String line, List<String> lines){
      int liney = lines.indexOf(line);
      if(!line.contains("(") || line.contains("new") || line.contains("?")) return false;
      List<String> tokens = Arrays.stream(line.split(" ")).toList();
      return !tokens.isEmpty() &&
            !(tokens.getFirst().contains("for")
                  || tokens.getFirst().contains("while")
                  || tokens.getFirst().contains("catch")
                  || tokens.getFirst().contains("try")
                  || tokens.getFirst().contains("filter")
                  || tokens.getFirst().contains("forEach")
                  || tokens.getFirst().contains("switch")
                  || tokens.getFirst().contains("else")
                  || tokens.getFirst().contains(".")
                  || tokens.getFirst().contains("if"))
            && nextLineIs(liney,lines) == OPENING_BRACKETS;
   }

   private static int findClosingBracketsOf(int liney, List<String> lines) {
      int counter = 0;
      do {
         if(lines.get(liney).equals("{")){
            counter+=1;
         } else if (lines.get(liney).equals("}")){
            counter-=1;
         }
         if(counter == 0){
            return liney;
         }
      } while (liney++ < lines.size());
      return -1;
   }
}
