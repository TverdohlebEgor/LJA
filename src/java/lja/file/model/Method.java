package lja.file.model;

import java.util.List;

public class Method{
    private int initialLine;
    private int finalLine;
    private String name;
    private AccessLevel accessLevel;
    private List<Field> arguments;

    public Method(int initialLine,
                  int finalLine,
                  String name,
                  AccessLevel accessLevel,
                  List<Field> arguments){
        this.initialLine = initialLine;
        this.finalLine = finalLine;
        this.name = name;
        this.accessLevel = accessLevel;
        this.arguments = arguments;
    }

    public int initialLine(){
        return this.initialLine;
    }
    public int finalLine(){
        return this.finalLine;
    }

    public String name(){
        return this.name;
    }

    public void incrementAllHigher(int y,int delta){
        if(this.initialLine >= y) this.initialLine+=delta;
        if(this.finalLine >= y) this.finalLine+=delta;
    }
}
