import java.awt.*;

abstract class Box {

    private Point pos;
    private int altezza;
    private int larghezza;
    private int arcSize;

    private Rectangle rect;

    Box(Point p) {

        this.pos = p;
        this.altezza = 120;
        this.larghezza = 80;
        this.arcSize = 25;
        this.rect = new Rectangle(pos.x, pos.y, larghezza, altezza);
    }

    void paint(Graphics2D g) {

        g.setColor(Color.GREEN.darker());
        g.fillRoundRect(pos.x,pos.y,larghezza,altezza,arcSize,arcSize);

        g.setColor(Color.WHITE);
        g.drawRoundRect(pos.x,pos.y,larghezza,altezza,arcSize,arcSize);
    }

    Point getPos() { return new Point(pos.x, pos.y); }

    void setPosRect(Point p) { this.rect.setLocation(p); }

    Point getPosRect() { return rect.getLocation(); }

    boolean isBoxUp(Card c) { return rect.intersects(c.getRect()); }

    abstract void removeCardUp();

    abstract void setCardUp(Card c);
}
