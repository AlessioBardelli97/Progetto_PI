import java.awt.*;

class Box_Normal extends Box {

    private Card cardUp;

    Box_Normal(Point p) {

        super(p);
        cardUp = null;
    }

    Card getCardUp() { return this.cardUp; }

    void removeCardUp() { cardUp = null; }

    void setCardUp(Card c) { this.cardUp = c; }

    @Override
    public void paint(Graphics2D g) {

        super.paint(g);

        if (cardUp != null)
            cardUp.paint(g);
    }
}
