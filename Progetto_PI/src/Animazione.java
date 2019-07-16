import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

class Animazione implements ActionListener {

    Timer timer;

    private long startTime;
    private long duration;

    private int j;

    private Card card;
    private Box_Semi boxSemi;
    private Campo campo;

    Animazione(Card ca, Box_Semi b, Campo c, int j, long startTime) {

        this.startTime = startTime;
        this.duration = 2000;

        this.card = ca;
        this.boxSemi = b;
        this.campo = c;
        this.j = j;
    }

    Card getCard() { return this.card; }

    @Override
    public void actionPerformed(ActionEvent e) {

        long dt = System.currentTimeMillis() - startTime;

        Point posCard = card.getPos();
        Point posBox = boxSemi.getPos();

        int dx = (int)((float)(posBox.x - posCard.x) * ((float)dt / (float)duration));
        int dy = (int)((float)(posBox.y - posCard.y) * ((float)dt / (float)duration));

        posCard.translate(dx,dy);
        card.setPos(posCard);

        campo.repaint();

        if (Math.abs(posCard.x - posBox.x) < 5 && Math.abs(posCard.y - posBox.y) < 5) {

            timer.stop();
            boxSemi.setCardUp(card);
            card.setPos(posBox);
            campo.animazioni[j] = null;

            campo.isGameWon();
        }
    }
}
