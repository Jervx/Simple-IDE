package jerbx.MainApplication.logicOniChan;

import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.input.MouseButton;

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.stage.FileChooser;
import jerbx.MainApplication.logicOniChan.tabObject.fileOpened;
import jerbx.MainApplication.logicOniChan.tabObject.runOnichan;

import java.io.File;
import java.io.PrintWriter;

public class mainSystem{

    @FXML TextArea textEditor;
    @FXML AnchorPane panel;
    @FXML TextArea Output;
    @FXML Circle sirkol;

    final int scrollDefHeight = 215;
    int anchorHeight = scrollDefHeight;
    private FileChooser fileCho;
    private String CurrentFile = null;
    private Boolean isCreated = false;
    private File createdFile = null;

    int fontSize = 15;


    public void addFile(fileOpened fileObj){
        panel.getChildren().add(fileObj);
        fileObj.setLayoutY(anchorHeight);
        anchorHeight += 40.0;
        panel.setPrefHeight(anchorHeight);
    }

    public void increaseFont(ActionEvent e){
        Output.setStyle(String.format("-fx-font-size:%d",++fontSize));
        textEditor.setStyle(String.format("-fx-font-size:%d",fontSize));
    }

    public void decreaseFont(ActionEvent e){
        Output.setStyle(String.format("-fx-font-size:%d",--fontSize));
        textEditor.setStyle(String.format("-fx-font-size:%d",fontSize));
    }

    public fileOpened currentFile(String name){
        for(int x = 0; x < panel.getChildren().size();x++)
            try {
                if (((fileOpened) panel.getChildren().get(x)).g_Name().equals(name)) return (fileOpened) panel.getChildren().get(x);
            }catch (Exception e){}
        return null;
    }

    public void saveFile(ActionEvent ev) {
        fileOpened curFile = currentFile(CurrentFile.substring(CurrentFile.lastIndexOf("/") + 1, CurrentFile.length()));
        if (curFile != null) curFile.update_content(textEditor.getText());
    }

    public void createFile(ActionEvent e){
        try {
            FileChooser creator = new FileChooser();
            createdFile = creator.showSaveDialog(null);

            if(createdFile == null) return;
            PrintWriter out = new PrintWriter(createdFile);
            out.println(" ");
            out.flush();
            out.close();
            isCreated = true;
            openFile(e);
            isCreated = false;
            createdFile = null;
        }catch(Exception ex){ex.printStackTrace();}
    }

    public boolean validFormat(String f){
        String extnsions [] = {".c",".cpp",".java",".py"};
        int match = 0;
        for(String str : extnsions) match = f.contains(str)? match+1 : match;
        return match > 0;
    }

    public void openFile(ActionEvent evee){
        fileCho = new FileChooser();
        FileChooser.ExtensionFilter filter = new FileChooser.ExtensionFilter("C/C++/py/java","*.c","*.cpp","*.java","*.py");
        fileCho.getExtensionFilters().add(filter);
        File f = isCreated ? createdFile : fileCho.showOpenDialog(null);

        if(f == null ) return;
        if(panel.getChildren().size() > 1) if(checkDuplicateFile(f.getAbsolutePath())) return;

        String fpath = f.getAbsolutePath();
        if(!validFormat(fpath)){
            Alert al = new Alert(Alert.AlertType.CONFIRMATION);
            al.setTitle("Unsupported Language");
            al.setHeaderText("Unsupported File Extension");
            al.setContentText("Only support [cpp,c,java,python]");
            al.showAndWait();
            return;
        }
        if(!f.exists()) return;

        String URL = f.getAbsolutePath();
        String realName = URL.substring(URL.lastIndexOf("/")+1,URL.length());
        System.out.println("⌬ [LOG] Selected File : ⋈ "+realName);
        Output.setText(Output.getText()+"\n⌬ [LOG] Selected File : ⋈ "+realName);
        boolean hasPrev = panel.getChildren().size() >= 13;
        fileOpened object1 = new fileOpened(new Line(),hasPrev? (fileOpened) panel
                .getChildren().get(panel.getChildren().size()-1) : null,hasPrev,realName,1,f);
        Line a = object1.getLine();
        Circle t = new Circle();

        t.setFill(Color.BLACK);
        t.setRadius(sirkol.getRadius());
        t.setLayoutY(anchorHeight-10);
        t.setLayoutX(object1.getLayoutX()-4);

        panel.getChildren().add(t);

        a.setStartX(t.getLayoutX());
        a.setStartY(t.getLayoutY());

        a.setEndX(object1.getLayoutX());
        a.setEndY(anchorHeight);

        object1.setOnMouseClicked(eve ->{
            if(CurrentFile != null)
                if(!CurrentFile.isEmpty())
                    currentFile(CurrentFile.substring(CurrentFile.lastIndexOf("/")+1,CurrentFile.length()))
                            .updateBuffer(textEditor.getText());
            textEditor.setText(object1.getBuffer());
            CurrentFile = object1.g_File().getAbsolutePath();

            if(eve.getButton() == MouseButton.SECONDARY){
                CurrentFile = null;
                System.out.println("⌬ [LOG] 1 File Removed From List");
                Output.setText(Output.getText()+"\n⌬ [LOG] 1 File Removed From List");
                panel.getChildren().remove(object1);
                panel.getChildren().remove(a);
                panel.getChildren().remove(t);
                textEditor.setText("");
                anchorHeight -= 40.0;
                panel.setPrefHeight(anchorHeight);
            }
        });
        object1.setOnMouseDragged(ev ->{
            object1.toFront();
            double newXVal = (ev.getX()-object1.getWidth()/2) + object1.getLayoutX() + object1.getTranslateX();
            double newYVal = ev.getY() + object1.getLayoutY() - object1.getPrefHeight()/2-10;
            if(newXVal >= 2 && newXVal <= 160 ){
                object1.setLayoutX(newXVal);
                a.setEndX(newXVal);
            }
            if(newYVal >= 215) {
                object1.setLayoutY(newYVal);
                a.setEndY(newYVal+object1.getHeight()/2);
            }
        });
        textEditor.setText(object1.getFileContent());
        CurrentFile = object1.g_File().getAbsolutePath();
        System.out.println("⌬ [LOG] New File Added ⎇");
        Output.setText(Output.getText()+"\n⌬ [LOG] New File Added ⎇");
        panel.getChildren().add(a);
        addFile(object1);
    }

    public boolean checkDuplicateFile(String tmp){
        for(int x = 0; x < panel.getChildren().size();x++)
            try{
               if( ((fileOpened)panel.getChildren().get(x)).g_File()
                       .getAbsolutePath().equals(tmp))
                   return true;
            }catch(Exception ex){}
            return false;
    }

    public void compileRun(ActionEvent ev){
        try {
            if (CurrentFile == null) return;
            if (CurrentFile.isEmpty()) return;
            saveFile(ev);
            System.out.println("⌬ [LOG] Compiling 1 file");
            Output.setText(Output.getText()+"\n⌬ [LOG] Compiling 1 file");
            Output.setText(Output.getText()+"\n⟖⟕⟖⟕⟖⟕⟖⟕⟖⟕⟖⟕⟖⟕⟖⟕⟖⟕⟖⟕⟖⟕⟖⟕⟖⟕⟖⟕⟖⟕⟖⟕⟖⟕⟖⟕⟖⟕⟖⟕\n⌬⟜ File "+ CurrentFile +" ⌬⊸\n");
            Output.setText(Output.getText() + new runOnichan().runCode(CurrentFile));
        }catch(Exception e){ e.printStackTrace(); }
    }
}
