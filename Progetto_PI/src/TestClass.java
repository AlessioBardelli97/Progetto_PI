import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class TestClass implements ActionListener, KeyListener {

    private static Campo c = new Campo();

    public static void main(String[] args) {

        JFrame f = new JFrame("FreeCell");
        f.setBounds(100, 80, 1000, 730);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        Container container = f.getContentPane();

        container.add(c);
        container.setBackground(Color.GREEN.darker());

        JMenuItem chiudi = new JMenuItem("Chiudi");
        JMenuItem nuovaPartita = new JMenuItem("Nuova Partita");
        JMenuItem annulla = new JMenuItem("Annulla");

        JMenu file = new JMenu("File");
        file.add(nuovaPartita);
        file.add(annulla);
        file.add(chiudi);

        JMenuBar menuBar = new JMenuBar();
        menuBar.add(file);
        container.add(menuBar, BorderLayout.NORTH);

        TestClass tc = new TestClass();
        chiudi.addActionListener(tc);
        annulla.addActionListener(tc);
        nuovaPartita.addActionListener(tc);

        f.addKeyListener(tc);
        f.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        switch (e.getActionCommand()) {

            case "Chiudi": System.exit(0);
            break;

            case "Nuova Partita": { c.destroyGame(); c.setNuovaPartita(); }
            break;

            case "Annulla": c.annulla();
            break;
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {

        if (e.getKeyChar() == 110) {

            c.destroyGame();
            c.setNuovaPartita();

        } else if (e.getKeyChar() == 122) {

            c.annulla();
        }
    }

    // ================================================== \\

    @Override
    public void keyPressed(KeyEvent e) {}

    @Override
    public void keyReleased(KeyEvent e) {}

    // ================================================== \\
}
