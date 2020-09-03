import processing.core.PApplet;
import processing.core.PImage;

import java.io.IOException;
import java.nio.file.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.Scanner;

public class Main extends PApplet {
    public static String name = "";
    public static int type = 0;
    public static String fpath = "";
    public static ArrayList<String> blocks = new ArrayList<>();
    public static ArrayList<Transform> trans = new ArrayList<>();
    public static ArrayList<ArrayList<Shape>> models = new ArrayList<>();
    public static ArrayList<ArrayList<Transform>> frames = new ArrayList<>();
    public static int framei = 0;
    public static String animb = "";

    public static void main(String[] args) {
        type = ip(args[0]);
        name = args[1];
        File ff = new File("C:/Users/"+System.getProperty("user.name")+"/Documents/anim8/"+name+"/frames");
        if (!ff.mkdir()){
            ff.delete();
            ff.mkdir();
        }
        File myObj = new File("C:/Users/"+System.getProperty("user.name")+"/Documents/anim8/"+name+"/"+name+".a8p");
        ArrayList<String> lines = new ArrayList<>();
        boolean success=true;
        try {
            Scanner myReader = new Scanner(myObj);
            while (myReader.hasNextLine()){
                lines.add(myReader.nextLine());
            }
        } catch (FileNotFoundException fileNotFoundException) {
            success=false;
            System.out.println("File Not Found");
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
                    blocks.add(lbundle);
                    has = 0;
                }
                //Anim block
                if (has == 0 && l.contains(new StringBuffer("@anim"))) {
                    ahas = 1;
                    animb = "@anim\n";
                }
                if (ahas == 1 && l.contains(new StringBuffer("@ani "))) {
                    liness.add(l.replace("\t", ""));
                    animb = animb + l + "\n";
                }
                if (ahas == 1 && l.contains(new StringBuffer("@end"))) {
                    ahas = 0;
                    animb = animb + "@end";
                }
            }
            for (int i = 0; i < blocks.size(); i ++){
                models.add(parseBlock(i));
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
                frames.add(trans_t);
                f++;
            }
        }
        PApplet.main("Main");
    }

    private static int ip(String s) {
        return Integer.parseInt(s);
    }

    public void settings(){
        size(Constants.windowWidth, Constants.windowHeight);
    }

    public void setup(){
        background(255);
        getSurface();
        fill(0);
        PImage icon = loadImage("icon.png");
        surface.setIcon(icon);
        surface.setTitle("Anim8 Render Utility");
    }
    public void draw(){
        clear();
        background(255);
        for (Transform t: frames.get(framei)){
            if (t.type==0){
                for (Shape s: models.get(t.applyi)){
                    s.scale(t.r);
                }
            }else{
                for (Shape s: models.get(t.applyi)){
                    s.transform(t.p.x, t.p.y);
                }
            }
        }
        for (ArrayList<Shape> pb : models) {
            for (Shape s : pb) {
                String[] rgb = s.fill.split("\\|");
                fill(Integer.parseInt(rgb[0]), Integer.parseInt(rgb[1]), Integer.parseInt(rgb[2]));
                stroke(Integer.parseInt(rgb[0]), Integer.parseInt(rgb[1]), Integer.parseInt(rgb[2]));
                strokeWeight(3);
                if (s instanceof Square) {
                    rect(s.c.x, s.c.y, s.r, s.r);
                } else if (s instanceof Circle) {
                    circle(s.c.x, s.c.y, s.r);
                } else if (s instanceof Line) {
                    Line n = ((Line) s);
                    line(n.b.x, n.b.y, n.e.x, n.e.y);
                }
            }
        }
        framei++;
        if (framei >= frames.size()){
            noLoop();
        }
        if (type == 1) {
            save("C:/Users/" + System.getProperty("user.name") + "/Documents/anim8/" + name + "/frames/" + framei + ".png");
        }
    }

    private static ArrayList<Shape> parseBlock(int i) {
        try {
            String[] parse = blocks.get(i).split("\n");
            ArrayList<Shape> shapes=new ArrayList<>();
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
            return shapes;
        } catch (IndexOutOfBoundsException e){
            return new ArrayList<Shape>();
        }
    }
}
