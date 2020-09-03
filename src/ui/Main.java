import com.xuggle.mediatool.IMediaWriter;
import com.xuggle.mediatool.ToolFactory;
import com.xuggle.xuggler.ICodec;
import processing.core.PApplet;
import processing.core.PImage;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.util.*;
import java.util.concurrent.TimeUnit;

import static com.xuggle.xuggler.video.ConverterFactory.convertToType;

public class Main extends PApplet implements ActionListener {
    static int mode = 1;
    int shape = 0;
    int r = 50;
    int sel = 0;
    static int modelNum = 0;
    String name;
    static String path = "";
    public static ArrayList<Shape> shapes = new ArrayList<>();
    public static ArrayList<String> globalData = new ArrayList<>();
    public static ArrayList<String> blocks = new ArrayList<>();
    public static ArrayList<Transform> trans = new ArrayList<>();
    public static ArrayList<ArrayList<Transform>> frames = new ArrayList<>();
    public static SwingUI ui = new SwingUI();
    public static String animb = "";
    public static int framei = 0;

    public static void main(String[] args) {
        PApplet.main("Main");
    }

    public void settings(){
        size(Constants.windowWidth, Constants.windowHeight);
    }

    public void setup(){
        background(255);
        getSurface();
        PImage icon = loadImage("icon.png");
        surface.setIcon(icon);
        surface.setTitle("Anim8 Paint Window");
    }

    public void draw(){
        clear();
        background(255);
        if (mode == 0) {
            File myObj = new File(path);
            ArrayList<String> lines = new ArrayList<>();
            Scanner myReader = null;
            try {
                myReader = new Scanner(myObj);
                while (myReader.hasNextLine()){
                    lines.add(myReader.nextLine());
                }
                int has = 0;
                for (String l : lines) {
                    if (has == 0 && l.contains(new StringBuffer("@anim"))) {
                        has = 1;
                        animb = "@anim\n";
                    }
                    if (has == 1 && l.contains(new StringBuffer("@ani "))) {
                        animb = animb+l+"\n";
                    }
                    if (has == 1 && l.contains(new StringBuffer("@end"))) {
                        has = 0;
                        animb = animb+"@end";
                    }
                }
            } catch (FileNotFoundException e) {
                popup("File not found.");
            }
            ui.msel.setText(String.format("<html>Enter model number. <br>There are %s models in this file.", blocks.size()));
            ui.fct.setText(String.format("You are on Frame %s",framei+1));
            String txt;
            String[] shp = {"Square","Circle","Vertical Line","Horizontal Line","Up and Left Line","Up and Right Line","Down and Left Line","Down and Right Line"};
            if (shape < 2){
                txt="Click to place shape. Selected: "+shp[shape];
            } else {
                txt="Click where you want start and end. Selected: "+shp[shape];
            }
            fill(ui.r,ui.g,ui.b);
            circle(350,Constants.windowHeight-205,20);
            fill(0);
            textSize(14);
            text("S to save model/animations", 30, Constants.windowHeight-20);
            text("H to change shape", 30, Constants.windowHeight-40);
            text("Left/Right Arrows to adjust diameter", 30, Constants.windowHeight-100);
            text("E to select. Blue cross appears on selected shapes. Space to delete selected.", 30, Constants.windowHeight-80);
            text("Current diameter: "+r, 30, Constants.windowHeight-60);
            text("C to clear ", 30, Constants.windowHeight-140);
            text("T to texture selected", 30, Constants.windowHeight-160);
            text("Use the Tool Menu to set the RGB color. Preview:", 30, Constants.windowHeight-200);
            text(txt, 30, Constants.windowHeight-120);
            fill(0);
            stroke(0);
            strokeWeight(1);
            line(0,Constants.windowHeight-220,Constants.windowWidth,Constants.windowHeight-220);
            stroke(0);
            noFill();
            for (Shape s: shapes){
                String[] rgb = s.fill.split("\\|");
                fill(Integer.parseInt(rgb[0]),Integer.parseInt(rgb[1]),Integer.parseInt(rgb[2]));
                stroke(Integer.parseInt(rgb[0]),Integer.parseInt(rgb[1]),Integer.parseInt(rgb[2]));
                strokeWeight(3);
                if (s instanceof Square) {
                    rect(s.c.x, s.c.y, s.r, s.r);
                } else if (s instanceof Circle) {
                    circle(s.c.x, s.c.y, s.r);
                } else if (s instanceof Line){
                    Line n = ((Line) s);
                    line(n.b.x, n.b.y, n.e.x, n.e.y);
                }
            }
            if (shapes.size() > 0){
                try {
                    Coordinates selc = shapes.get(sel).s;
                    fill(0,0,255);
                    stroke(0,0,255);
                    strokeWeight(3);
                    line(selc.x - 5, selc.y, selc.x + 5, selc.y);
                    line(selc.x, selc.y - 5, selc.x, selc.y + 5);
                } catch (IndexOutOfBoundsException e){}
            }
            stroke(0);
        } else if (mode == 1){
            textSize(40);
            fill(0);
            text("Make/open a project first!", 50, Constants.windowHeight/2);
        }
    }

    public void mousePressed(){
        if (mode == 0){
            if (mouseY < (Constants.windowHeight-220)-r/2) {
                if (shape == 0) {
                    shapes.add(new Square(new Coordinates(mouseX, mouseY), new Coordinates(mouseX + r / 2, mouseY + r / 2), r, "0|0|0"));
                }
                if (shape == 1) {
                    shapes.add(new Circle(new Coordinates(mouseX, mouseY), new Coordinates(mouseX, mouseY), r,"0|0|0"));
                }
                if (shape == 2){
                    shapes.add(new Line(new Coordinates(mouseX, mouseY), new Coordinates(mouseX, mouseY+r), new Coordinates(mouseX, mouseY+r/2), r,"0|0|0"));
                }
                if (shape == 3){
                    shapes.add(new Line(new Coordinates(mouseX, mouseY), new Coordinates(mouseX+r, mouseY), new Coordinates(mouseX+r/2, mouseY), r,"0|0|0"));
                }
                if (shape == 4) {
                    shapes.add(new Line(new Coordinates(mouseX, mouseY), new Coordinates(mouseX-r, mouseY-r), new Coordinates(mouseX - r / 2, mouseY - r / 2), r, "0|0|0"));
                }
                if (shape == 5) {
                    shapes.add(new Line(new Coordinates(mouseX, mouseY), new Coordinates(mouseX+r, mouseY-r), new Coordinates(mouseX + r / 2, mouseY - r / 2), r,"0|0|0"));
                }
                if (shape == 6){
                    shapes.add(new Line(new Coordinates(mouseX, mouseY), new Coordinates(mouseX-r, mouseY+r), new Coordinates(mouseX - r / 2, mouseY + r / 2), r,"0|0|0"));
                }
                if (shape == 7){
                    shapes.add(new Line(new Coordinates(mouseX, mouseY), new Coordinates(mouseX+r, mouseY+r), new Coordinates(mouseX + r / 2, mouseY + r / 2), r,"0|0|0"));
                }
            }
        }
    }

    public void keyPressed(){
        if (mode == 0) {
            if (key == 's') {
                genCode();
            }
            if (key == 'h') {
                if (shape < 7) {
                    shape++;
                } else if (shape >= 7) {
                    shape = 0;
                }
            }
            if (key == 'e') {
                if (sel < shapes.size() - 1) {
                    sel++;
                } else if (sel >= shapes.size() - 1) {
                    sel = 0;
                }
            }
            if (key == 't'){
                try {
                    shapes.get(sel).fill = ui.r + "|" + ui.g + "|" + ui.b;
                } catch (IndexOutOfBoundsException e){}
            }
            if (keyCode == LEFT && r > 0) {
                r--;
            }
            if (keyCode == RIGHT) {
                r++;
            }
            if (key == ' ') {
                try {
                    shapes.remove(sel);
                } catch (IndexOutOfBoundsException e) {
                }
            }
            if (key == 'c') {
                shapes = new ArrayList<Shape>();
            }
        }
    }

    private void genCode() {
        ArrayList<String> out = new ArrayList<String>();
        out.add("@model "+modelNum);
        for (Shape s : shapes) {
            if (s instanceof Circle) {
                out.add(String.format("\t@shape (%s,%s) circle (%s,%s) %s %s", s.c.x, s.c.y, s.s.x, s.s.y, s.r, s.fill));
            }
            if (s instanceof Square){
                out.add(String.format("\t@shape (%s,%s) square (%s,%s) %s %s", s.c.x, s.c.y, s.s.x, s.s.y, s.r, s.fill));
            }
            if (s instanceof Line){
                Line n = ((Line) s);
                out.add(String.format("\t@shape (%s,%s) (%s,%s) (%s,%s) %s %s", n.b.x, n.b.y, n.e.x, n.e.y, s.s.x, s.s.y, s.r, s.fill));
            }
        }
        out.add("@end");
        String outStr = "";
        for (String l : out){
            outStr=outStr+l+"\n";
        }
        globalData.add(outStr);
        blocks.add(outStr);
        FileWriter myWriter = null;
        String jGlobalData = "";
        for (String d : globalData){
            jGlobalData=jGlobalData+d+"\n";
        }
        try {
            myWriter = new FileWriter(Main.path);
            myWriter.write(jGlobalData);
            myWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        modelNum++;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == ui.adda){
            if (trans.size() < 5){
                int rc = 0;
                Coordinates coordc = new Coordinates(0,0);
                boolean error = false;
                if (ui.sel.getSelectedIndex() == 0) {
                    try{
                        rc = Integer.parseInt(ui.coordorrad.getText());
                    } catch (NumberFormatException ee){
                        popup("Invalid number in coordinates.");
                        error = true;
                    }
                } else if (ui.sel.getSelectedIndex() == 1) {
                    String[] pc = ui.coordorrad.getText().split(",");
                    try {
                        coordc = new Coordinates(Integer.parseInt(pc[0]), Integer.parseInt(pc[1]));
                    } catch (NumberFormatException ee){
                        popup("Invalid number in coordinates.");
                        error = true;
                    } catch (IndexOutOfBoundsException ee){
                        popup("Invalid coordinate pair.");
                        error = true;
                    }
                }
                if (!error) {
                    trans.add(new Transform(ui.sel.getSelectedIndex(), Main.modelNum, rc, coordc));
                    updateList();
                }
            } else {
                popup("Too many items, delete some or move on to the next frame.");
            }
        }
        if (e.getSource() == ui.buildFrame){
            boolean want = yesNo("This cannot be undone. Are you sure you want to do this?");
            if (want){
                frames.add(trans);
                trans = new ArrayList<>();
                updateList();
                Main.framei++;
            }
        }
        if (e.getSource() == ui.popFrame){
            try {
                trans.remove(Integer.parseInt(ui.num.getText())-1);
                updateList();
            } catch (IndexOutOfBoundsException ed){
                popup("Number too big or small.");
            }
        }
        if (e.getSource() == ui.lf){
            framei--;
            Main.trans=Main.frames.get(framei);
            updateList();
            if (framei-1<=-1){
                ui.lf.setEnabled(false);
            }
            if (framei-1<Main.frames.size()){
                ui.rf1.setEnabled(true);
            }
        }
        if (e.getSource() == ui.rf1){
            framei++;
            if (framei+1>Main.frames.size()){
                ui.rf1.setEnabled(false);
            }
            try {
                Main.trans=Main.frames.get(framei);
                updateList();
            } catch (IndexOutOfBoundsException ee) {

                Main.trans=new ArrayList<>();
                updateList();
            }
            if (framei+1>-1){
                ui.lf.setEnabled(true);
            }
        }
        if (e.getSource() == ui.ups){
            Main.frames.add(Main.trans);
        }
        if (e.getSource() == ui.pop){
            Main.frames.remove(Main.framei);
        }
        if (e.getSource() == ui.render){
            Runtime rt = Runtime.getRuntime();
            try {
                rt.exec(String.format(String.format("C:/Users/%s/AppData/Local/anim8/a8r.exe 1 %s",System.getProperty("user.name"),(String)ui.nt.getSelectedItem())));
            } catch (IOException ioException) {
                ioException.printStackTrace();
                popup("Sorry, couldn't render.");
            }
            String name = (String)ui.nt.getSelectedItem();
            final IMediaWriter writer = ToolFactory.makeWriter(Constants.documents+name+"/"+name+".mp4");
            writer.addVideoStream(0, 0, ICodec.ID.CODEC_ID_MPEG4, Constants.windowWidth, Constants.windowHeight);
            long startTime = System.nanoTime();
            int frame = 0;
            File folder = new File(Constants.documents+name+"/frames");
            File[] listOfFiles = folder.listFiles();
            int indexVal = 0;
            ArrayList<File> im = new ArrayList<>();
            for (File file : listOfFiles) {
                if (file.isFile()) {
                    indexVal++;
                    im.add(file);
                }
            }
            for (int index = 1; index < listOfFiles.length; index++) {
                BufferedImage screen = null;
                try {
                    screen = ImageIO.read(im.get(index));
                    BufferedImage bgrScreen = convertToType(screen, BufferedImage.TYPE_3BYTE_BGR);
                    writer.encodeVideo(0, bgrScreen, 300*index, TimeUnit.MILLISECONDS);
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
            }
            writer.close();
        }
        if (e.getSource() == ui.show){
            Runtime rt = Runtime.getRuntime();
            try {
                rt.exec(String.format("C:/Users/%s/AppData/Local/anim8/a8r.exe 0 %s",System.getProperty("user.name"),(String)ui.nt.getSelectedItem()));
            } catch (IOException ioException) {
                ioException.printStackTrace();
                popup("Sorry, couldn't preview.");
            }
        }
        if (e.getSource() == ui.open){
            String name = (String)ui.nt.getSelectedItem();
            Main.path = Constants.documents+name+"/"+name+".a8p";
            File myObj = new File(Main.path);
            ArrayList<String> lines = new ArrayList<>();
            boolean success=true;
            try {
                Scanner myReader = new Scanner(myObj);
                while (myReader.hasNextLine()){
                    lines.add(myReader.nextLine());
                }
                Main.globalData.add(lines.get(0));
            } catch (FileNotFoundException fileNotFoundException) {
                success=false;
                popup(String.format("File %s not found.",Main.path));
            }
            if (success) {
                String lbundle = "";
                int ahas = 0;
                int has = 0;
                ArrayList<String> liness = new ArrayList<>();
                for (String l : lines) {
                    if (has == 0 && l.contains(new StringBuffer("@model"))) {
                        has = 1;
                        lbundle = l + "\n";
                    }
                    if (has == 1 && l.contains(new StringBuffer("@shape "))) {
                        lbundle = lbundle + l + "\n";
                    }
                    if (has == 1 && l.contains(new StringBuffer("@end"))) {
                        lbundle = lbundle + "@end";
                        Main.blocks.add(lbundle);
                        Main.globalData.add(lbundle);
                        has = 0;
                    }
                    //Anim block
                    if (has == 0 && l.contains(new StringBuffer("@anim"))) {
                        ahas = 1;
                        Main.animb = "@anim\n";
                    }
                    if (ahas == 1 && l.contains(new StringBuffer("@ani "))) {
                        liness.add(l.replace("\t", ""));
                        Main.animb = Main.animb+l+"\n";
                    }
                    if (ahas == 1 && l.contains(new StringBuffer("@end"))) {
                        ahas = 0;
                        Main.animb = Main.animb+"@end";
                    }
                }
                ArrayList<String> frameblks = new ArrayList<>();
                int f = 0;
                String buf = "";
                for (String s : liness) {
                    String[] spll = s.split(" ");
                    if (ip(spll[1]) != f) {
                        frameblks.add(buf);
                        buf = "";
                        f++;
                    }
                    buf = buf + s + "\n";
                }
                if (!buf.equals("")) {
                    frameblks.add(buf);
                }
                f = 0;
                for (String fr : frameblks) {
                    ArrayList<Transform> trans_t = new ArrayList<>();
                    String[] spli = fr.split("\n");
                    for (String s : spli) {
                        String[] l_tokens = s.split(" ");
                        trans_t.add(new Transform(ip(l_tokens[2]), ip(l_tokens[3]), ip(l_tokens[4]), new Coordinates(ip(l_tokens[5]), ip(l_tokens[6]))));
                    }
                    Main.frames.add(trans_t);
                    f++;
                }
                Main.mode = 0;
                Main.framei=Main.frames.size();
                Main.modelNum=Main.blocks.size();
                Main.parseBlock(0);
                ui.name.setVisible(false);
                ui.nt.setVisible(false);
                ui.submit.setVisible(false);
                ui.open.setVisible(false);
                ui.rf.setVisible(true);
                ui.ups.setVisible(true);
                ui.gf.setVisible(true);
                ui.bf.setVisible(true);
                ui.submit1.setVisible(true);
                ui.rgbl.setVisible(true);
                ui.msel.setVisible(true);
                ui.fct.setVisible(true);
                ui.msel.setText(String.format("<html>Enter model number. <br>There are %s models in this file.", Main.blocks.size()));
                ui.m.setVisible(true);
                ui.rf1.setVisible(true);
                ui.render.setVisible(true);
                ui.lf.setVisible(true);
                ui.dm.setVisible(true);
                ui.um.setVisible(true);
                ui.setm.setVisible(true);
                ui.header.setVisible(false);
                ui.sel.setVisible(true);
                ui.show.setVisible(true);
                ui.adda.setVisible(true);
                ui.animop.setVisible(true);
                ui.coordorrad.setVisible(true);
                ui.num.setVisible(true);
                ui.popFrame.setVisible(true);
                ui.buildFrame.setVisible(true);
                ui.saveFrame.setVisible(true);
                ui.list.setVisible(true);
                ui.pop.setVisible(true);
            }
        }
        if  (e.getSource() == ui.setm){
            Main.shapes=new ArrayList<>();
            int uint = Integer.parseInt(ui.m.getText());
            try{
                parseBlock(uint-1);
            } catch (IndexOutOfBoundsException w){
                popup("Model number too large or small");
            }
        }
        if (e.getSource()==ui.dm){
            Main.shapes=new ArrayList<>();
            int uint = Integer.parseInt(ui.m.getText());
            try{
                File myObj = new File(Main.path);
                ArrayList<String> lines = new ArrayList<>();
                try {
                    Scanner myReader = new Scanner(myObj);
                    while (myReader.hasNextLine()){
                        lines.add(myReader.nextLine());
                    }
                    String concat = "";
                    for (String l : lines){
                        concat=concat+l+"\n";
                    }
                    concat=concat.replace(blocks.get(uint-1), "");
                    FileWriter myWriter = new FileWriter(Main.path);
                    myWriter.write(concat);
                    myWriter.close();
                } catch (FileNotFoundException fileNotFoundException) {
                    popup(String.format("File %s not found.",Main.path));
                } catch (IOException ioException) {
                }
                blocks.remove(uint-1);
            } catch (IndexOutOfBoundsException w){
                popup("Model number too large or small");
            }
        }
        if (e.getSource()==ui.um){
            ArrayList<String> out = new ArrayList<String>();
            out.add("@model");
            for (Shape s : shapes) {
                if (s instanceof Circle) {
                    out.add(String.format("\t@shape (%s,%s) circle (%s,%s) %s %s", s.c.x, s.c.y, s.s.x, s.s.y, s.r, s.fill));
                }
                if (s instanceof Square){
                    out.add(String.format("\t@shape (%s,%s) square (%s,%s) %s %s", s.c.x, s.c.y, s.s.x, s.s.y, s.r, s.fill));
                }
                if (s instanceof Line){
                    Line n = ((Line) s);
                    out.add(String.format("\t@shape (%s,%s) (%s,%s) (%s,%s) %s %s", n.b.x, n.b.y, n.e.x, n.e.y, s.s.x, s.s.y, s.r, s.fill));
                }
            }
            out.add("@end");
            String outStr = "";
            for (String l : out){
                outStr=outStr+l+"\n";
            }
            int uint = Integer.parseInt(ui.m.getText());
            try{
                File myObj = new File(Main.path);
                ArrayList<String> lines = new ArrayList<>();
                try {
                    Scanner myReader = new Scanner(myObj);
                    while (myReader.hasNextLine()){
                        lines.add(myReader.nextLine());
                    }
                    String concat = "";
                    for (String l : lines){
                        concat=concat+l+"\n";
                    }
                    concat=concat.replace(blocks.get(uint-1), outStr);
                    FileWriter myWriter = new FileWriter(Main.path);
                    myWriter.write(concat);
                    myWriter.close();
                } catch (FileNotFoundException fileNotFoundException) {
                    popup(String.format("File %s not found.",Main.path));
                } catch (IOException ioException) {
                }
                blocks.remove(uint-1);
            } catch (IndexOutOfBoundsException w){
                popup("Model number too large or small");
            }
        }
        if (e.getSource() == ui.saveFrame){
            String out = "@anim\n";
            int frame = 0;
            for (ArrayList<Transform> t : Main.frames) {
                for (Transform tt : t) {
                    out = out + String.format("\t@ani %s %s %s %s %s %s\n", frame, tt.type, tt.applyi, tt.r, tt.p.x, tt.p.y);
                }
                frame++;
            }
            out=out+"@end";
            File myObj = new File(Main.path);
            ArrayList<String> lines = new ArrayList<>();
            try {
                Scanner myReader = new Scanner(myObj);
                while (myReader.hasNextLine()){
                    lines.add(myReader.nextLine());
                }
                String concat = "";
                for (String l : lines){
                    concat=concat+l+"\n";
                }
                System.out.println(Main.animb);
                concat=concat.replace(Main.animb, out);
                Main.animb=out;
                FileWriter myWriter = new FileWriter(Main.path);
                myWriter.write(concat);
                myWriter.close();
            } catch (FileNotFoundException fileNotFoundException) {
                popup(String.format("File %s not found.",Main.path));
            } catch (IOException ioException) {
            }

        }
        if (e.getSource() == ui.submit){
            name = (String)ui.nt.getSelectedItem();
            ui.name.setVisible(false);
            ui.nt.setVisible(false);
            ui.submit.setVisible(false);
            ui.open.setVisible(false);
            Main.mode = 0;
            if (!globalData.contains("@proj "+name+ "\n@anim\n@end")) {
                globalData.add("@proj " + name+ "\n@anim\n@end");
            }
            boolean success = true;
            Main.path = Constants.documents+name+"/"+name+".a8p";
            try {
                File myObj = new File(Main.path);
                File dir = new File(Constants.documents+name);
                if (dir.mkdir()) {
                    myObj.createNewFile();
                    FileWriter myWriter = new FileWriter(Main.path);
                    myWriter.write("@proj "+name+ "\n@anim\n@end");
                    myWriter.close();
                } else {
                    success = false;
                    popup("Project exists, try another name.");
                }
            } catch (IOException ioException) {
                success = false;
                ioException.printStackTrace();
            }
            if (success){
                ui.header.setVisible(false);
                ui.rf.setVisible(true);
                ui.render.setVisible(true);
                ui.show.setVisible(true);
                ui.fct.setVisible(true);
                ui.gf.setVisible(true);
                ui.saveFrame.setVisible(true);
                ui.bf.setVisible(true);
                ui.submit1.setVisible(true);
                ui.rgbl.setVisible(true);
                ui.sel.setVisible(true);
                ui.adda.setVisible(true);
                ui.animop.setVisible(true);
                ui.coordorrad.setVisible(true);
                ui.num.setVisible(true);
                ui.popFrame.setVisible(true);
                ui.buildFrame.setVisible(true);
                ui.list.setVisible(true);
            }
        }
        if (e.getSource() == ui.submit1){
            try {
                int irv = Integer.parseInt(ui.rf.getText());
                if (irv>255){
                    popup("R is too big (more than 255)");
                } else {
                    Main.ui.r = irv;
                }
            } catch (NumberFormatException gh){
                popup("Invalid integer in G input");
            }
            try{
                int igv = Integer.parseInt(ui.gf.getText());
                if (igv>255){
                    popup("G is too big (more than 255)");
                } else {
                    Main.ui.g = igv;
                }
            } catch (NumberFormatException gh){
                popup("Invalid integer in G input");
            }
            try {
                int ibv = Integer.parseInt(ui.bf.getText());
                if (ibv>255){
                    popup("B is too big (more than 255)");
                } else {
                    Main.ui.b = ibv;
                }
            } catch (NumberFormatException gh){
                popup("Invalid integer in B input");
            }
        }
    }

    private int ip(String l_token) {
        return Integer.parseInt(l_token);
    }

    private void updateList() {
        String li = "";
        int ct = 0;
        for (Transform t : trans){
            li=li+String.format("<li>Transform: %s<br>Number: %s",ui.transf[t.type], ct+1);
            ct++;
        }
        ui.list.setBounds(270, 420+(ct*5), 600, 90+(ct*30));
        ui.list.setText(String.format("<html>Animations: <ul>%s</ul>",li));
    }

    private boolean yesNo(String s) {
        return JOptionPane.showConfirmDialog(null, s) == 0;
    }

    private static void parseBlock(int i) {
        try {
            String[] parse = blocks.get(i).split("\n");
            for (String p : parse) {
                if (p.contains("@shape")) {
                    String[] tokens = p.split("\\s");
                    String[] sl = tokens[2].replace("(", "").replace(")", "").split(",");
                    String typeorln = tokens[3];
                    String[] cl = tokens[4].replace("(", "").replace(")", "").split(",");
                    int sr = Integer.parseInt(tokens[5]);
                    String sf = tokens[6];
                    Coordinates sc = new Coordinates(Integer.parseInt(sl[0]), Integer.parseInt(sl[1]));
                    Coordinates cc = new Coordinates(Integer.parseInt(cl[0]), Integer.parseInt(cl[1]));
                    if (typeorln.equals("circle")) {
                        shapes.add(new Circle(sc, cc, sr, sf));
                    } else if (typeorln.equals("square")) {
                        shapes.add(new Square(sc, cc, sr, sf));
                    } else {
                        String[] le = typeorln.replace("(", "").replace(")", "").split(",");
                        Coordinates lc = new Coordinates(Integer.parseInt(le[0]), Integer.parseInt(le[1]));
                        shapes.add(new Line(sc, lc, cc, sr, sf));
                    }
                }
            }
        } catch (IndexOutOfBoundsException e){

        }
    }

    private static void popup(String s) {
        ui.error.setText("<html><h4>"+s+" Click to dismiss.");
        ui.error.setVisible(true);
    }
}
