import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class Animazione2 implements ActionListener {

    Timer timer;
    private Point pos;

    private int cur;
    private Card card;
    private Campo campo;

    Animazione2(Card ca, Point p, Campo c) {

        this.campo = c;
        this.card = ca;
        this.pos = p;

        int x = p.x - ca.getPos().x;
        int y = ca.getPos().y - p.y;

        cur = (int) Math.sqrt((x*x) + (y*y)) / 10;
    }

    Card getCard() { return this.card; }

    @Override
    public void actionPerformed(ActionEvent e) {

        if (cur == 0) {

            timer.stop();
            campo.animazione2 = null;
            return;
        }

        Point posCard = card.getPos();

        int dx = (pos.x - posCard.x) / cur;
        int dy = (pos.y - posCard.y) / cur;

        posCard.translate(dx,dy);
        card.setPos(posCard);

        campo.repaint();
        cur--;

        if (cur == 0) {

            timer.stop();
            campo.animazione2 = null;
        }
    }
}
