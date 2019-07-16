import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Point2D;
import java.util.ArrayList;

public class AnimazioneFinePartita extends JComponent implements ActionListener {

    Timer timer;
    private int cur = 0;

    private Campo campo;
    private Polygon poly;
    private ArrayList<Card> cards;

    private double n;

    AnimazioneFinePartita(Campo c) {

        Point2D.Double centro = new Point2D.Double((double)450,(double)275);
        poly = new Polygon();
        cards = new ArrayList<>();
        this.campo = c;
        double raggio = 270;
        n = 80;

        double x,y;

        for (int i = 0; i < n; i++) {

            x = centro.x + raggio * Math.cos(2 * Math.PI * i / n);
            y = centro.y + raggio * Math.sin(2 * Math.PI * i / n);

            poly.addPoint(Math.round((float)x),Math.round((float)y));
        }

        int i,j = 0;

        for (i = 0; i < 13; i++,j++)
            cards.add(new Card(new Point(poly.xpoints[j],poly.ypoints[j]),null,i+1,Seme.CUORI));

        for (i = 0; i < 13; i++,j++)
            cards.add(new Card(new Point(poly.xpoints[j],poly.ypoints[j]),null,i+1,Seme.QUADRI));

        for (i = 0; i < 13; i++,j++)
            cards.add(new Card(new Point(poly.xpoints[j],poly.ypoints[j]),null,i+1,Seme.FIORI));

        for (i = 0; i < 13; i++,j++)
            cards.add(new Card(new Point(poly.xpoints[j],poly.ypoints[j]),null,i+1,Seme.PICCHE));
    }

    ArrayList<Card> getCards() { return cards; }

    @Override
    public void actionPerformed(ActionEvent e) {

        int index;
        Point tmp;

        for (int i = 0; i < 52; i++) {

            index = (cur + i) % (int)n;
            tmp = new Point(poly.xpoints[index], poly.ypoints[index]);
            cards.get(i).setPos(tmp);
        }

        campo.repaint();
        //this.repaint();
        cur++;

        if (cur == n) {

            timer.stop();
            campo.afp = null;
            campo.repaint();
        }
    }

    @Override
    public void paint(Graphics g) {

        Graphics2D g2d = (Graphics2D)g;

        for (Card i : cards)
            i.paint(g2d);
    }

    public static void main(String[] args) {

        Campo c = new Campo();

        JFrame f = new JFrame("FreeCell");
        f.setBounds(100, 100, 1000, 705);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        Container container = f.getContentPane();
        container.add(c);

        AnimazioneFinePartita afp = new AnimazioneFinePartita(c);
        container.add(afp);

        container.setBackground(Color.GREEN.darker());
        f.setVisible(true);

        afp.timer = new Timer(100,afp);
        afp.timer.start();
    }
}
