import java.awt.*;

class Box_Semi extends Box {

    private Seme seme;
    private Card cardUp;

    Box_Semi(Point p, Seme seme) {

        super(p);
        this.seme = seme;
        this.cardUp = null;
    }

    boolean canPutUp(Card c) {

        if (c.getSeme() == this.seme) {

            if (cardUp == null)
                return c.getValore() == 1;

            else return cardUp.getValore()+1 == c.getValore();

        } else return false;
    }

    void setCardUp(Card c) { cardUp = c; }

    Card getCardUp() { return cardUp; }

    @Override
    void removeCardUp() { cardUp = null; }

    @Override
    void paint(Graphics2D g) {

        super.paint(g);

        if (cardUp == null) {

            Point p = getPos();
            p.translate(20,35);
            seme.drawSeme(g,p);
        }

        else
            cardUp.paint(g);
    }
}
