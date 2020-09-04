import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.URL;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Path;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Main implements ActionListener {
    static JLabel header;
    static JButton submit1;
    static JButton submit2;
    static JButton submit3;
    static JLabel med;
    static JPanel panel;
    static JFrame frame;
    static String[][] files = new String[][]{{"a8r.exe","1"},{"anim8.exe","1"},{"uninstall.exe","1"},{"Anim8.lnk","0"}};

    public static void main(String[] args) {
        panel = new JPanel();
        panel.setLayout(null);
        frame = new JFrame("Install Anim8");
        frame.setSize(700,600);
        frame.setVisible(true);
        URL imageURL = Main.class.getClassLoader().getResource("icon.png");
        ImageIcon img = new ImageIcon(imageURL);
        frame.setIconImage(img.getImage());
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.add(panel);
        draw();
    }
    public static void draw(){
        header = new JLabel("<html><h1>Welcome to the Anim8 installer.");
        header.setBounds(100, 100, 700, 70);
        panel.add(header);
        med = new JLabel("Hit Next to begin.");
        med.setBounds(100, 200, 700, 70);
        panel.add(med);
        submit1 = new JButton("Next>>");
        submit1.setBounds(100, 500, 100, 20);
        submit1.addActionListener(new Main());
        panel.add(submit1);
        submit2 = new JButton("Agree");
        submit2.setBounds(100, 400, 100, 20);
        submit2.addActionListener(new Main());
        submit2.setVisible(false);
        panel.add(submit2);
        submit3 = new JButton("Close");
        submit3.setBounds(100, 400, 100, 20);
        submit3.addActionListener(new Main());
        submit3.setVisible(false);
        panel.add(submit3);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == submit1){
            header.setText("<html><h1>Agree to the License Agreement");
            med.setText("<html>MIT License<br>Copyright (c) 2020 Jacob Myers<br>Permission is hereby granted, free of charge, to any person obtaining a copy<br>of this software and associated documentation files (the \"Software\"), to deal<br>in the Software without restriction, including without limitation the rights<br>to use, copy, modify, merge, publish, distribute, sublicense, and/or sell<br>copies of the Software, and to permit persons to whom the Software is<br>furnished to do so, subject to the following conditions:<br>The above copyright notice and this permission notice shall be included in all<br>copies or substantial portions of the Software.<br>THE SOFTWARE IS PROVIDED \"AS IS\", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR<br>IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,<br>FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE<br>AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER<br>LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,<br>OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.\n");
            med.setBounds(100, 100, 700, 300);
            header.setBounds(100, 70, 700, 70);
            submit1.setVisible(false);
            submit2.setVisible(true);
        }
        if (e.getSource() == submit2){
            makeDirs();
            copyFiles();
            header.setText("<html><h1>Installed.");
            med.setText("Hit Close to close.");
            header.setBounds(100, 100, 700, 70);
            med.setBounds(100, 200, 700, 70);
            submit2.setVisible(false);
            submit3.setVisible(true);
        }
        if (e.getSource() == submit3){
            System.exit(0);
        }
    }

    private void copyFiles() {
        for (String[] f : files){
            if (f[1].equals("1")){
                copyFile(f[0],String.format("C:/Users/%s/AppData/Local/anim8/%s",System.getProperty("user.name"),f[0]));
            }
            if (f[1].equals("0")){
                copyFile(f[0],String.format("C:/Users/%s/Desktop/%s",System.getProperty("user.name"),f[0]));
            }
        }
    }

    private void copyFile(String s, String s1) {
        try {
            Path p2 = Paths.get(s1);
            Files.copy(Main.class.getResourceAsStream(s), p2);
        } catch (FileAlreadyExistsException e){
            
        } catch (IOException e) {
            
        }
    }

    private void makeDirs() {
        try {
            makeDir(String.format("C:/Users/%s/AppData/Local/anim8",System.getProperty("user.name")));
            makeDir(String.format("C:/Users/%s/Documents/anim8",System.getProperty("user.name")));
        } catch (IOException e) {
            
        }
    }

    private void makeDir(String dirname) throws IOException {
        Path p = Paths.get(dirname);
        if (!Files.exists(p)){
            Files.createDirectories(p);
        }
    }
}
