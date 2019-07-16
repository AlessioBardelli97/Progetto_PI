import java.awt.*;

public enum Seme {

    FIORI,
    CUORI,
    QUADRI,
    PICCHE;

    public void drawSeme(Graphics2D g, Point pos) {

        if (this == QUADRI) {

            g.setColor(Color.RED);

            int[] xPoint = new int[]{pos.x+20,pos.x+35,pos.x+20,pos.x+5};
            int[] yPoint = new int[]{pos.y,pos.y+20,pos.y+40,pos.y+20};

            Polygon polygon = new Polygon(xPoint, yPoint,4);

            g.fillPolygon(polygon);

        } else if (this == CUORI) {

            int[] xPoint = new int[] {pos.x,pos.x+20,pos.x+40,pos.x+20};
            int[] yPoint = new int[] {pos.y+11,pos.y+40,pos.y+11,pos.y+6};

            Polygon polygon = new Polygon(xPoint, yPoint, 4);

            g.setColor(Color.RED);

            g.fillOval(pos.x+1, pos.y,20,20);
            g.fillOval(pos.x+19, pos.y,20,20);

            g.fillPolygon(polygon);

        } if (this == FIORI) {

            g.setColor(Color.BLACK);

            int[] xPoint = new int[] {pos.x+20,pos.x+27,pos.x+13};
            int[] yPoint = new int[] {pos.y+18,pos.y+45,pos.y+45};

            Polygon polygon = new Polygon(xPoint,yPoint,3);

            g.fillOval(pos.x+10,pos.y,20,20);
            g.fillOval(pos.x,pos.y+13,20,20);
            g.fillOval(pos.x+20,pos.y+13,20,20);

            g.fillPolygon(polygon);

        } else if (this == PICCHE) {

            g.setColor(Color.BLACK);

            int[] xPoint = new int[] {pos.x,pos.x+20,pos.x+40,pos.x+20};
            int[] yPoint = new int[] {pos.y+17,pos.y,pos.y+17,pos.y+25};

            int[] xPoint1 = new int[] {pos.x+20,pos.x+27,pos.x+13};
            int[] yPoint1 = new int[] {pos.y+18,pos.y+42,pos.y+42};

            Polygon polygon = new Polygon(xPoint, yPoint, 4);
            Polygon polygon1 = new Polygon(xPoint1, yPoint1, 3);


            g.fillOval(pos.x,pos.y+10,20,20);
            g.fillOval(pos.x+20, pos.y+10,20,20);

            g.fillPolygon(polygon);
            g.fillPolygon(polygon1);
        }
    }
}
