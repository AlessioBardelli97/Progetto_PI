import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.geom.RoundRectangle2D;

class Card {

    private Point pos;
    private int altezza;
    private int larghezza;
    private Box inBox;
    private Integer valore;
    private Seme seme;
    private Rectangle rect;
    private int arcSize;
    private Point lastPos;
    private Box lastBox;

    Card(Point pos, Box box, int valore, Seme seme) {

        this.pos = pos;
        this.altezza = 120;
        this.larghezza = 80;
        this.arcSize = 25;
        this.inBox = box;

        this.valore = valore;
        this.seme = seme;

        this.lastBox = null;
        this.lastPos = null;

        this.rect = new Rectangle(pos.x+20,pos.y+20,larghezza-40,altezza-40);
    }

    void paint(Graphics2D g) {

        g.setColor(Color.WHITE);
        g.fillRoundRect(pos.x,pos.y,larghezza,altezza,arcSize,arcSize);

        g.setColor(Color.BLACK);
        g.drawRoundRect(pos.x,pos.y,larghezza,altezza,arcSize,arcSize);

        Font old = g.getFont();
        Font _new = new Font(old.getName(),old.getStyle(),old.getSize()+5);
        g.setFont(_new);

        if (seme == Seme.CUORI || seme == Seme.QUADRI)
            g.setColor(Color.RED);
        else
            g.setColor(Color.BLACK);

        if (this.valore <= 10)
            drawValore(valore.toString(),g);

        else {

            switch(valore) {

                case 11: drawValore("J",g);
                break;

                case 12: drawValore("Q",g);
                break;

                case 13: drawValore("K",g);
            }
        }

        g.setFont(old);
        seme.drawSeme(g,new Point(rect.x,rect.y+15));
    }

    boolean isIn(MouseEvent e) {

        return new RoundRectangle2D.Float(pos.x, pos.y, larghezza, altezza, arcSize, arcSize).contains(e.getPoint());
    }

    Point getPos() { return new Point(pos.x, pos.y); }

    void setPos(Point p) {
        this.pos = p;
        this.rect.setLocation(p.x+20,p.y+20);
    }

    void setInBox(Box b) { this.inBox = b; }

    Box getInBox() { return this.inBox; }

    int getValore() { return this.valore; }

    Seme getSeme() { return this.seme; }

    Rectangle getRect() { return this.rect; }

    void setLastPos(Point p) { this.lastPos = p; }

    Point getLastPos() { return new Point(lastPos.x,lastPos.y); }

    void setLastBox(Box b) { this.lastBox = b; }

    Box getLastBox() { return this.lastBox; }

    boolean canPutUp(Card inBox) {

        Seme semeCur = this.seme;
        Seme semeInBox = inBox.getSeme();

        int valCur = this.valore;
        int valInBox = inBox.getValore();

        if (semeCur == Seme.CUORI || semeCur == Seme.QUADRI) {

            if (semeInBox == Seme.FIORI || semeInBox == Seme.PICCHE)
                return (valCur + 1) == valInBox;

            else return false;
        }

        else if (semeCur == Seme.FIORI || semeCur == Seme.PICCHE) {

            if (semeInBox == Seme.CUORI || semeInBox == Seme.QUADRI)
                return (valCur + 1) == valInBox;

            else return false;
        }

        else return false;
    }

    private void drawValore(String valore, Graphics2D g) {

        g.drawString(valore, pos.x + 10, pos.y + 20);
        g.drawString(valore, pos.x + 55, pos.y + 20);
        g.drawString(valore, pos.x + 10, pos.y + 105);
        g.drawString(valore, pos.x + 55, pos.y + 105);
    }

    @Override
    public boolean equals(Object obj) {

        if (obj == null)
            return false;

        if (this == obj)
            return true;

        if (obj.getClass() != getClass())
            return false;

        Card other = (Card)obj;

        return this.valore == other.getValore() && this.seme == other.getSeme();
    }
}
