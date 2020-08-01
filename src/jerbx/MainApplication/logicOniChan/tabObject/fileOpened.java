package jerbx.MainApplication.logicOniChan.tabObject;

import javafx.scene.control.Button;
import javafx.scene.shape.Line;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Scanner;

public class fileOpened extends Button {
    private String name;
    private int id;
    private File file;
    private boolean hasPrev;
    private boolean hasNext = false;
    private fileOpened previousP;
    private fileOpened nextNode;
    private Line line;
    private String buffer = "";

    public fileOpened(Line line, fileOpened prev, boolean hasPrev, String name, int id, File f){
        this.line = line;
        previousP = prev;
        this.hasPrev = hasPrev;
        this.name = name;
        //▥ ⊸
        this.setText("⌬⟜ "+name);
        this.id = id;
        this.file = f;
        this.getStyleClass().add("fileObject");
        this.setLayoutX(11.0);
        this.setMaxWidth(203);
        if(hasPrev) prev.setNext(this);
    }

    public void setNext(fileOpened nextNode){
        this.nextNode = nextNode;
        hasNext = true;
    }

    public void setPrev(fileOpened node){
        this.previousP = node;
        hasPrev = true;
    }

    public Line getLine(){ return line; }
    public fileOpened getNext(){ return nextNode;}
    public boolean isHasNext(){ return this.hasNext; }
    public boolean hasPrevious(){ return hasPrev; }
    public fileOpened previosNode(){ return previousP;}

    public void update_content(String x){
        try {
            System.out.println("⌬[LOG] Saving File : ");
            PrintWriter pr = new PrintWriter(file);
            pr.print(x);
            pr.flush();
            pr.close();
            getFileContent();
        } catch (FileNotFoundException e) { e.printStackTrace(); }
    }

    public void updateBuffer(String x){buffer = x;}

    public String getBuffer(){ return buffer; }

    public String getFileContent(){
        String str = "";
        try{
            Scanner reader = new Scanner(file);
            while(reader.hasNext()) str += reader.nextLine()+"\n";
        }catch (Exception ex){ ex.printStackTrace(); }
        buffer = str;
        System.out.println("⌬ [LOG] File & Buffer Updated");
        return str;
    }

    public String g_Name() { return name; }
    public int g_Id() { return id; }
    public File g_File() { return file; }
}
