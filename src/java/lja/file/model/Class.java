package lja.file.model;

import javax.print.attribute.standard.MediaSize;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Class {
    private int initialLine;
    private int finalLine;
    private String name;
    private AccessLevel accessLevel;
    private Optional<StaticBlock> staticBlock;
    private List<Method> methods = new ArrayList<>();
    private List<ClassField> classFields = new ArrayList<>();

    public Class(int initialLine, int finalLine, String name, AccessLevel accessLevel, Optional<StaticBlock> staticBlock){
        this.initialLine = initialLine;
        this.finalLine = finalLine;
        this.name = name;
        this.accessLevel = accessLevel;
        this.staticBlock = staticBlock;
    }

    public int initialLine(){
        return this.initialLine;
    }
    public int finalLine(){
        return this.finalLine;
    }

    public Optional<StaticBlock> staticBlock(){
        return this.staticBlock;
    }

    public void setStaticBlock(Optional<StaticBlock> newValue){
        this.staticBlock = newValue;
    }

    public String name(){
        return this.name;
    }

    public void addMethod(Method newVal){
        this.methods.add(newVal);
    }

    public void addMethods(List<Method> newVal){
        this.methods.addAll(newVal);
    }

    public void addClassField(ClassField classField){classFields.add(classField);}
    public void addClassFields(List<ClassField> classFields){this.classFields.addAll(classFields);}

    public void incrementAllHigher(int y, int delta){
        if(this.initialLine >= y) this.initialLine+=delta;
        if(this.finalLine >= y) this.finalLine+=delta;
        staticBlock.ifPresent(block -> block.incrementAllHigher(y,delta));
        methods.stream().forEach(m -> m.incrementAllHigher(y,delta));
    }

    public Method findMethodByName(String methodName){
        for(Method method : methods){
           if(method.name().equals(methodName)){
               return method;
           }
        }
        return null;
    }
}
