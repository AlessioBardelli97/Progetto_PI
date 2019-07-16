import java.awt.*;
import java.util.ArrayList;

class Box_Colonna extends Box {

    private ArrayList<Card> cardList;

    Box_Colonna(Point p) {

        super(p);
        cardList = new ArrayList<>();
        Point pos = getPosRect();
        pos.translate(0,-30);
        setPosRect(pos);
    }

    int getNumCardUp() { return cardList.size(); }

    Card getCard(int i) { return cardList.get(i); }

    public void paint(Graphics2D g) {

        super.paint(g);

        for (Card i : cardList)
            i.paint(g);
    }

    void removeCardUp() {

        cardList.remove(cardList.size()-1);
        Point pos = getPosRect();
        pos.translate(0,-30);
        setPosRect(pos);
    }

    void setCardUp(Card c) {

        this.cardList.add(c);
        Point pos = getPosRect();
        pos.translate(0,30);
        setPosRect(pos);
    }
}
