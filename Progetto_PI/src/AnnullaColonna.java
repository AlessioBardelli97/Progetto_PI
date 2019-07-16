import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

class AnnullaColonna implements ActionListener {

    Timer timer;
    ArrayList<Point> points;
    ArrayList<Card> cards;
    Box box;

    private boolean repaint;

    private int cur;
    private Campo campo;

    AnnullaColonna(Campo c) {

        this.campo = c;

        timer = null;
        points = new ArrayList<>();
        cards = new ArrayList<>();

        this.repaint = false;
    }

    boolean getRepaint() { return this.repaint; }

    void initCur() {

        int x = points.get(0).x - cards.get(0).getPos().x;
        int y = cards.get(0).getPos().y - points.get(0).y;

        cur = (int) Math.sqrt((x*x) + (y*y)) / 10;
    }

    void resetAnnullaColonna() {

        if (timer != null && timer.isRunning())
            timer.stop();

        timer = null;

        cards = new ArrayList<>();
        points = new ArrayList<>();
        box = null;
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        if (cur == 0) {

            resetAnnullaColonna();
            return;
        }

        repaint = true;

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

            for (Card ic : cards) {

                ic.getInBox().removeCardUp();
                ic.setInBox(box);
                box.setCardUp(ic);
            }

            repaint = false;
            resetAnnullaColonna();
        }
    }
}
