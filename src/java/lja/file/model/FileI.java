package lja.file.model;

import java.util.List;

public interface FileI {

    public void addClass(Class newVal);
    public void addClasses(List<Class> newVal);

    public List<Class> classes();

    public void setImports(Imports newVal);
    public void setPackage(String newVal);

    public void addToStartOfMethod(String methodName, String newLine);
    public void addToStartOfMethod(String className, String methodName, String newLine);

    public void addToEndOfMethod(String methodName, String newLine);
    public void addToEndOfMethod(String className, String methodName, String newLine);

    public void createStaticBlock();
    public void createStaticBlock(String className);

    public void addToStaticBlock(String newLine);
    public void addToStaticBlock(String className, String newLine);

    public void addClassField(String newLine, ClassField classField);
    public void addClassField(String className, ClassField classField ,String newLine);

    public void addMethod(List<String> lines, Method method);
    public void addMethod(String className,List<String> lines, Method method);

    public String getFullClassName();
    public boolean hasMainClass();
}
