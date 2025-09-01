package lja.file.model;

public class ClassField {
   private AccessLevel accessLevel;
   private boolean isStatic;
   private String type;
   private String name;
   private int y;
   public ClassField(
         AccessLevel accessLevel,
         boolean isStatic,
         String type,
         String name,
         int y
   ){
      this.accessLevel =accessLevel;
      this.isStatic =isStatic;
      this.type =type;
      this.name =name;
      this.y = y;
   }
}