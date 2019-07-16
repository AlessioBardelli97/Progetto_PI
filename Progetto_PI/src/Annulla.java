import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Annulla implements ActionListener {

    Timer timer;
    Point point;
    Card card;
    Box box;

    private boolean repaint;

    private int cur;
    private Campo campo;

    Annulla(Campo c) {

        this.campo = c;

        timer = null;
        point = null;
        card = null;

        this.repaint = false;
    }

    boolean getRepaint() { return this.repaint; }

    void initCur() {

        int x = point.x - card.getPos().x;
        int y = card.getPos().y - point.y;

        cur = (int) Math.sqrt((x*x) + (y*y)) / 10;
    }

    void resetAnnulla() {

        if (timer != null && timer.isRunning())
            timer.stop();

        timer = null;

        card = null;
        point = null;
        box = null;
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        if (cur == 0) {

            resetAnnulla();
            return;
        }

        repaint = true;

        Point posCard = card.getPos();

        int dx = (point.x - posCard.x) / cur;
        int dy = (point.y - posCard.y) / cur;

        posCard.translate(dx,dy);
        card.setPos(posCard);

        campo.repaint();
        cur--;

        if (cur == 0) {

            card.getInBox().removeCardUp();
            card.setInBox(box);
            box.setCardUp(card);

            repaint = false;
            resetAnnulla();
        }
    }
}
