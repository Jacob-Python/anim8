import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.net.URL;

public class Main implements ActionListener {
    private static JPanel panel;
    private static JFrame frame;
    private static JLabel head;
    private static JCheckBox docs;
    private static JButton submit;
    private static int sub = 0;
    private static String[] files = new String[]{"anim8.exe","a8r.exe"};

    public static void main(String[] args) {
        panel = new JPanel();
        panel.setLayout(null);
        frame = new JFrame("Uninstall Anim8");
        frame.setSize(500,300);
        frame.setVisible(true);
        URL imageURL = Main.class.getClassLoader().getResource("icon.png");
        ImageIcon img = new ImageIcon(imageURL);
        frame.setIconImage(img.getImage());
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.add(panel);
        components();
    }
    public static void components(){
        head = new JLabel("<html><h1>Uninstall Anim8");
        head.setBounds(50, 20, 200, 90);
        panel.add(head);
        docs = new JCheckBox("Remove the Documents folder too");
        docs.setBounds(50, 100, 300, 100);
        panel.add(docs);
        submit = new JButton("Delete files>>");
        submit.setBounds(50, 220, 300, 40);
        submit.addActionListener(new Main());
        panel.add(submit);
    }

    @Override
    public void actionPerformed(ActionEvent event) {
        if (sub==0){
            head.setText("<html><h1>We're sorry to see you go.");
            docs.setVisible(false);
            submit.setText("Close");
            for (String f : files){
                String fpath = String.format("C:/Users/%s/AppData/Local/anim8/%s",System.getProperty("user.name"),f);
                try {
                    new File(fpath).delete();
                } catch(Exception e){
                    System.out.println(e.getCause());
                }
            }
            if (docs.isSelected()){
                try {
                    new File(String.format("C:/Users/%s/Documents/anim8",System.getProperty("user.name"))).delete();
                } catch(Exception e){
                }
            }
            sub++;
        } else {
            System.exit(0);
        }
    }
}
