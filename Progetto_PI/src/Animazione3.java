import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class Animazione3 implements ActionListener {

    Timer timer;
    private ArrayList<Point> points;

    private int cur;
    private ArrayList<Card> cards;
    private Campo campo;

    Animazione3(ArrayList<Card> ca, ArrayList<Point> p, Campo c) {

        this.campo = c;
        this.cards = ca;
        this.points = p;

        int x = p.get(0).x - ca.get(0).getPos().x;
        int y = ca.get(0).getPos().y - p.get(0).y;

        cur = (int) Math.sqrt((x*x) + (y*y)) / 10;
    }

    Card getCard(int i) { return this.cards.get(i); }

    int getCardsSize() { return this.cards.size(); }

    @Override
    public void actionPerformed(ActionEvent e) {

        if (cur == 0) {

            timer.stop();
            campo.animazione3 = null;
            return;
        }

        for (int i = 0; i < cards.size(); i++) {

            Point posCard = cards.get(i).getPos();
            Point pos = points.get(i);

            int dx = (pos.x - posCard.x) / cur;
            int dy = (pos.y - posCard.y) / cur;

            posCard.translate(dx,dy);
            cards.get(i).setPos(posCard);
        }

        campo.repaint();
        cur--;

        if (cur == 0) {

            timer.stop();
            campo.animazione3 = null;
        }
    }
}
