package lja.file.model;

public class StaticBlock {
    private int initialLine;
    private int finalLine;
    public StaticBlock(int initialLine, int finalLine){
        this.initialLine = initialLine;
        this.finalLine = finalLine;
    }

    public int initialLine(){return this.initialLine;}

    public int finalLine(){return this.finalLine;}

    public void incrementAllHigher(int y, int delta){
        if(this.initialLine >= y) this.initialLine+=delta;
        if(this.finalLine >= y) this.finalLine+=delta;
    }
}
