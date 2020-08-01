package jerbx.MainApplication.logicOniChan.tabObject;
import java.io.*;

public class runOnichan{

    private String readed = "";

    public String runCode(String path){ return runCode_OniChan(path,System.getProperty("os.name").
            toLowerCase().contains("lin")?".sh":".bat");
    }

    public String runCode_OniChan(String path,String OS){

        int pLen = path.length();
        int lIsl = path.lastIndexOf("/");
        int lIdot = path.lastIndexOf(".");

        String filename = path.substring(lIsl+1,pLen);
        String pathOnly = path.substring(0,lIsl);
        String langType = path.substring(lIdot,pLen);

        String command = "";

        String hangScrpt = ";#!/bin/bash\n" +
                "echo \"\"\n" +
                "echo \"\"\n" +
                "echo \"\"\n" +
                "echo \"\"\n" +
                "echo \"----------------------------------------------------------\"\n" +
                "echo -n \"Done Executing Press [ENTER] To Continue...... \"\n" +
                "read var_year";
        switch(langType){
            case ".java" : command = String.format("cd '%s'; javac %s; java %s; rm %s",pathOnly,filename,filename.
                    replace(".java",""),filename.replace(".java",".class"));break;
            case ".c" : command = String.format("cd '%s'; gcc %s; ./a.out",pathOnly,filename);break;
            case ".cpp" : command = String.format("cd '%s'; g++ %s; ./a.out",pathOnly,filename);break;
            case ".py" : command = String.format("cd '%s'; python3 %s",pathOnly,filename);break;
        }
        try{
            writeScrpt(command,hangScrpt);
            callEx("gnome-terminal -e ./OniChan"+OS);
            remScrpt();
            System.out.println("⌬ [LOG] Output ⤵\n"+readed);
            return  "▩ Done Compiling ▩ ⤵\n" +
                    "⟖⟕⟖⟕⟖⟕⟖⟕⟖⟕⟖⟕⟖⟕⟖⟕⟖⟕⟖⟕⟖⟕⟖⟕⟖⟕⟖⟕⟖⟕⟖⟕⟖⟕⟖⟕⟖⟕⟖⟕\n\n";
        }catch (Exception e){
            System.out.println(readed);
            e.printStackTrace();
            return "⎇ Error Occured ⤵\n"+
                    "⟖⟕⟖⟕⟖⟕⟖⟕⟖⟕⟖⟕⟖⟕⟖⟕⟖⟕⟖⟕⟖⟕⟖⟕⟖⟕⟖⟕⟖⟕⟖⟕⟖⟕⟖⟕⟖⟕⟖⟕\n\n"+readed;
        }
    }

    private void callEx(String command) throws Exception {
        Process pro = Runtime.getRuntime().exec(command);
        writeRes(pro.getInputStream());
        writeRes(pro.getErrorStream());
        pro.waitFor();
    }

    private void writeRes( InputStream is) throws Exception {
        String line = null;
        BufferedReader a = new BufferedReader(
                new InputStreamReader(is));
        while ((line = a.readLine()) != null) readed += line +"\n";
    }

    public void writeScrpt(String scpt, String hangScrpt){
        try{
            PrintWriter scrptWriter = new PrintWriter(new File("OniChan.sh"));
            scrptWriter.println(scpt+hangScrpt);
            scrptWriter.flush();
            scrptWriter.close();
            callEx("chmod +x OniChan.sh");
        }catch(Exception e){}
    }

    public void remScrpt(){
        File scr = new File("OniChan.sh");
        //scr.delete();
    }
}
