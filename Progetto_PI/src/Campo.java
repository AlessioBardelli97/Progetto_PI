import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Random;

class Campo extends JComponent implements MouseMotionListener, MouseListener {

    private ArrayList<Card> cardList;

    private ArrayList<Box_Normal> boxList_Normal;
    private ArrayList<Box_Semi> boxList_Semi;
    private ArrayList<Box_Colonna> boxList_Colonna;

    Animazione[] animazioni;
    Animazione2 animazione2;
    Animazione3 animazione3;
    AnimazioneFinePartita afp;

    private Drag drag;
    private DragColonna dragColonna;

    private Annulla ultimaMossa1;
    private AnnullaColonna ultimaMossa2;

    // ============================ COSTRUTTORE ================================ \\

    Campo() {

        setDoubleBuffered(true);

        this.addMouseMotionListener(this);
        this.addMouseListener(this);

        cardList = null;
        boxList_Normal = new ArrayList<>();
        boxList_Semi = new ArrayList<>();
        boxList_Colonna = new ArrayList<>();

        int i,j;

        for(i = 0; i < 4; i++)
            boxList_Normal.add(new Box_Normal(new Point(100*i+20,20)));

        for(j = 0; j < 4; j++) {

            if (j == 0)
                boxList_Semi.add(new Box_Semi(new Point(100*(i+j)+60,20), Seme.CUORI));

            else if (j == 1)
                boxList_Semi.add(new Box_Semi(new Point(100*(i+j)+60,20), Seme.QUADRI));

            else if (j == 2)
                boxList_Semi.add(new Box_Semi(new Point(100*(i+j)+60,20), Seme.PICCHE));

            else if (j == 3)
                boxList_Semi.add(new Box_Semi(new Point(100*(i+j)+60,20), Seme.FIORI));
        }

        for(i = 0; i < 8; i++)
            boxList_Colonna.add(new Box_Colonna(new Point(100 * i + 20, 240)));

        afp = null;
        drag = null;
        ultimaMossa1 = new Annulla(this);
        ultimaMossa2 = new AnnullaColonna(this);
        animazione3 = null;
        animazione2 = null;
        dragColonna = null;

        animazioni = new Animazione[4];
        for (i = 0; i < animazioni.length; i++)
            animazioni[i] = null;


    }

    // =========================== GESTIONE DEGLI EVENTI =============================== \\

    @Override
    public void mousePressed(MouseEvent e) {

        if (e.getButton() == MouseEvent.BUTTON3) {

            Card curCard = null;

            int k = 0;
            while (k < boxList_Colonna.size() && curCard == null) {

                Box_Colonna curBox = boxList_Colonna.get(k);

                if (curBox.getNumCardUp() == 0)
                    k++;

                else {

                    Card c = curBox.getCard(curBox.getNumCardUp()-1);

                    if (c.isIn(e))
                        curCard = c;

                    else k++;
                }
            }

            k = 0;
            while (k < boxList_Normal.size() && curCard == null) {

                Box_Normal curBox = boxList_Normal.get(k);
                Card c = curBox.getCardUp();

                if (c != null && c.isIn(e))
                    curCard = c;

                else k++;
            }

            if (curCard != null) {

                int i = 0;
                while (i < boxList_Semi.size()) {

                    Box_Semi curBox = boxList_Semi.get(i);

                    if (curBox.canPutUp(curCard)) {

                        int j = 0;
                        while (j < animazioni.length && animazioni[j] != null)
                            j++;

                        animazioni[j] = new Animazione(curCard,curBox,this,j,System.currentTimeMillis());
                        animazioni[j].timer = new Timer(4, animazioni[j]);
                        animazioni[j].timer.start();

                        curCard.getInBox().removeCardUp();
                        curCard.setInBox(curBox);

                        break;

                    } else i++;
                }
            }

        } else if (e.getButton() == MouseEvent.BUTTON1) {

            Card c = findCardPressed(e);

            if (c != null && !canMoveCard(c))
                return;

            if (c != null) {

                Box b = c.getInBox();

                try {

                    Box_Colonna bc = (Box_Colonna) b;
                    int numCardUp = bc.getNumCardUp();

                    if (bc.getCard(bc.getNumCardUp() - 1).equals(c))
                        initDrag(c,e);

                    else {

                        int i = 0;
                        dragColonna = new DragColonna();

                        while (!bc.getCard(i).equals(c))
                            i++;

                        while (i < numCardUp) {

                            Card ic = bc.getCard(i);
                            Point posCard = ic.getPos();

                            ic.setLastBox(ic.getInBox());
                            ic.setLastPos(ic.getPos());

                            int dx = e.getX() - posCard.x;
                            int dy = e.getY() - posCard.y;

                            dragColonna.cardList.add(ic);
                            dragColonna.dx.add(dx);
                            dragColonna.dy.add(dy);

                            i++;
                        }
                    }

                } catch (ClassCastException ex) {

                    initDrag(c,e);
                }
            }
        }
    }

	@Override
    public void mouseDragged(MouseEvent e) {

        if (drag != null) {

            int dx = e.getX() - drag.dx;
            int dy = e.getY() - drag.dy;

            Card cur = drag.card;
            cur.setPos(new Point(dx, dy));

            repaint();

        } else if (dragColonna != null) {

            for (int i = 0; i < dragColonna.cardList.size(); i++) {

                int dx = e.getX() - dragColonna.dx.get(i);
                int dy = e.getY() - dragColonna.dy.get(i);

                Card cur = dragColonna.cardList.get(i);
                cur.setPos(new Point(dx,dy));
            }

            repaint();
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {

        if (drag != null) {

            Card curCard = drag.card;

            boolean boxUpNormal = false,
                    boxUpColonna = false,
                    boxUpSemi = false;

            Point p;

            int i=0,j=0,k=0;

            while (i < boxList_Normal.size()) {

                boxUpNormal = boxList_Normal.get(i).isBoxUp(curCard);

                if (boxUpNormal)
                    break;

                else i++;
            }

            while (j < boxList_Semi.size() && !boxUpNormal) {

                boxUpSemi = boxList_Semi.get(j).isBoxUp(curCard);

                if (boxUpSemi)
                    break;

                else j++;
            }

            while (k < boxList_Colonna.size() && !boxUpSemi && !boxUpNormal) {

                boxUpColonna = boxList_Colonna.get(k).isBoxUp(curCard);

                if (boxUpColonna)
                    break;

                else k++;
            }

            if (boxUpNormal) {

                Box_Normal box = boxList_Normal.get(i);

                if (box.getCardUp() == null) {

                    p = box.getPos();

                    curCard.getInBox().removeCardUp();
                    curCard.setInBox(box);
                    box.setCardUp(curCard);
                    curCard.setPos(p);

                    ultimaMossa1.card = curCard;
                    ultimaMossa1.point = curCard.getLastPos();
                    ultimaMossa1.box = curCard.getLastBox();
                    ultimaMossa1.initCur();
                    ultimaMossa1.timer = new Timer(1,ultimaMossa1);
                    ultimaMossa2.resetAnnullaColonna();

                    repaint();

                } else {

                    p = curCard.getInBox().getPos();

                    if (curCard.getInBox().getClass() == Box_Colonna.class)
                        p.translate(0,(((Box_Colonna)curCard.getInBox()).getNumCardUp()-1)*30);

                    animazione2 = new Animazione2(curCard,p,this);
                    animazione2.timer = new Timer(1, animazione2);
                    animazione2.timer.start();
                }

            } else if (boxUpSemi) {

                Box_Semi box = boxList_Semi.get(j);

                if (box.canPutUp(curCard)) {

                    p = box.getPos();
                    curCard.getInBox().removeCardUp();
                    curCard.setInBox(box);
                    box.setCardUp(curCard);

                    curCard.setPos(p);
                    repaint();

                } else {

                    p = curCard.getInBox().getPos();

                    if (curCard.getInBox().getClass() == Box_Colonna.class)
                        p.translate(0,(((Box_Colonna)curCard.getInBox()).getNumCardUp()-1)*30);

                    animazione2 = new Animazione2(curCard,p,this);
                    animazione2.timer = new Timer(1, animazione2);
                    animazione2.timer.start();
                }

            } else if (boxUpColonna) {

                Box_Colonna box = boxList_Colonna.get(k);
                int numCardUp = box.getNumCardUp();

                if (numCardUp == 0) {

                    p = box.getPos();

                    curCard.getInBox().removeCardUp();
                    curCard.setInBox(box);
                    box.setCardUp(curCard);
                    curCard.setPos(p);

                    ultimaMossa1.card = curCard;
                    ultimaMossa1.point = curCard.getLastPos();
                    ultimaMossa1.box = curCard.getLastBox();
                    ultimaMossa1.initCur();
                    ultimaMossa1.timer = new Timer(1,ultimaMossa1);
                    ultimaMossa2.resetAnnullaColonna();

                    repaint();

                } else if (curCard.canPutUp(box.getCard(numCardUp-1))) {

                    p = box.getPos();
                    p.translate(0,numCardUp*30);

                    curCard.getInBox().removeCardUp();
                    curCard.setInBox(box);
                    box.setCardUp(curCard);
                    curCard.setPos(p);

                    ultimaMossa1.card = curCard;
                    ultimaMossa1.point = curCard.getLastPos();
                    ultimaMossa1.box = curCard.getLastBox();
                    ultimaMossa1.initCur();
                    ultimaMossa1.timer = new Timer(1,ultimaMossa1);
                    ultimaMossa2.resetAnnullaColonna();

                    repaint();

                } else {

                    p = curCard.getInBox().getPos();

                    if (curCard.getInBox().getClass() == Box_Colonna.class)
                        p.translate(0,(((Box_Colonna)curCard.getInBox()).getNumCardUp()-1)*30);

                    animazione2 = new Animazione2(curCard,p,this);
                    animazione2.timer = new Timer(1, animazione2);
                    animazione2.timer.start();
                }

            } else {

                p = curCard.getInBox().getPos();

                if (curCard.getInBox().getClass() == Box_Colonna.class)
                    p.translate(0,(((Box_Colonna)curCard.getInBox()).getNumCardUp()-1)*30);

                animazione2 = new Animazione2(curCard,p,this);
                animazione2.timer = new Timer(1, animazione2);
                animazione2.timer.start();
            }

            drag = null;
            isGameWon();

        } else if (dragColonna != null) {

            ArrayList<Card> cards = dragColonna.cardList;
            ArrayList<Point> points = new ArrayList<>();

            int i = 0;

            while (i < boxList_Colonna.size()) {

                if (boxList_Colonna.get(i).isBoxUp(cards.get(0))) {

                    Box_Colonna box = boxList_Colonna.get(i);

                    if (box.getNumCardUp() == 0 || cards.get(0).canPutUp(box.getCard(box.getNumCardUp()-1))) {
                        moveColonna1(cards, box);
                    }

                     else moveColonna2(cards,points,(Box_Colonna) cards.get(0).getInBox());

                    dragColonna = null;
                    return;

                } else i++;
            }

            moveColonna2(cards,points,(Box_Colonna) cards.get(0).getInBox());
            dragColonna = null;

            isGameWon();
        }
    }

    @Override
    public void paint(Graphics g) {

        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        for(Box_Normal b1 : boxList_Normal)
            b1.paint(g2d);

        for(Box_Semi b2 : boxList_Semi)
            b2.paint(g2d);

        for(Box_Colonna b3 : boxList_Colonna)
            b3.paint(g2d);

        if (animazione3 != null)
            for (int i = 0; i < animazione3.getCardsSize(); i++)
                animazione3.getCard(i).paint(g2d);

        if (animazione2 != null)
            animazione2.getCard().paint(g2d);

        for (Animazione i : animazioni)
            if (i != null)
                i.getCard().paint(g2d);

        if (drag != null)
            drag.card.paint(g2d);

        if (dragColonna != null)
            for (Card i : dragColonna.cardList)
                i.paint(g2d);

        if (afp != null)
            for (Card i : cardList)
                i.paint(g2d);

        if (ultimaMossa1.getRepaint())
            ultimaMossa1.card.paint(g2d);

        if (ultimaMossa2.getRepaint())
            for (Card ic : ultimaMossa2.cards)
                ic.paint(g2d);
    }

    // ====================== FUNZIONI DI UTILITA' ============================= \\

    private Card findCardPressed(MouseEvent e) {

        int i;
        Card c = null;

        i = boxList_Normal.size() - 1;
        while (i >= 0 && c == null) {

            Card cur = boxList_Normal.get(i).getCardUp();

            if (cur != null && cur.isIn(e))
                c = cur;

            else i--;
        }

        i = 0;
        while (i < boxList_Colonna.size() && c == null) {

            Box_Colonna curBox = boxList_Colonna.get(i);
            int j = curBox.getNumCardUp() - 1;

            while (j >= 0 && c == null) {

                Card curCard = curBox.getCard(j);

                if (curCard != null && curCard.isIn(e))
                    c = curCard;

                else j--;
            }

            i++;
        }

        return c;
    }

    void setNuovaPartita() {

        cardList = new ArrayList<>();

        Random random = new Random();
        Box_Colonna boxColonna;

        int i,j,k;

        for (i = 0; i < 13; i++)
            cardList.add(new Card(new Point(),null,i+1,Seme.CUORI));

        for (i = 0; i < 13; i++)
            cardList.add(new Card(new Point(),null,i+1,Seme.QUADRI));

        for (i = 0; i < 13; i++)
            cardList.add(new Card(new Point(),null,i+1,Seme.FIORI));

        for (i = 0; i < 13; i++)
            cardList.add(new Card(new Point(),null,i+1,Seme.PICCHE));

        for (i = 0; i < 4; i++) {

            boxColonna = boxList_Colonna.get(i);

            for (j = 0; j < 7; j++) {

                k = random.nextInt(cardList.size());
                Card curCard = cardList.get(k);
                cardList.remove(k);

                Point p = boxColonna.getPos();
                p.translate(0,30*j);

                boxColonna.setCardUp(curCard);
                curCard.setInBox(boxColonna);
                curCard.setPos(p);
            }
        }

        for ( ; i < 8; i++) {

            boxColonna = boxList_Colonna.get(i);

            for (j = 0; j < 6; j++) {

                k = random.nextInt(cardList.size());
                Card curCard = cardList.get(k);
                cardList.remove(k);

                Point p = boxColonna.getPos();
                p.translate(0,30*j);

                boxColonna.setCardUp(curCard);
                curCard.setInBox(boxColonna);
                curCard.setPos(p);
            }
        }

        cardList = null;
        repaint();
    }

    private boolean canMoveCard(Card card) {

        try {

            Box_Colonna box = (Box_Colonna) card.getInBox();
            int num = box.getNumCardUp();

            if (box.getCard(num-1).equals(card))
                return true;

            int indexCard = -1,i=0;
            while (i < num && indexCard == -1) {

                if (card.equals(box.getCard(i)))
                    indexCard = i;

                else i++;
            }

            Card curCard,sucCard;
            while (indexCard < num-1) {

                curCard = box.getCard(indexCard);
                sucCard = box.getCard(indexCard+1);

                if (!sucCard.canPutUp(curCard))
                    return false;

                else indexCard++;
            }

            return true;

        } catch (ClassCastException e) { return true; }
    }

    private void initDrag(Card c, MouseEvent e) {

        c.setLastPos(c.getPos());
        c.setLastBox(c.getInBox());

        drag = new Drag();
        drag.card = c;

        drag.dx = e.getX() - c.getPos().x;
        drag.dy = e.getY() - c.getPos().y;
    }

    private void moveColonna1(ArrayList<Card> cards, Box_Colonna boxNew) {

        ultimaMossa2.resetAnnullaColonna();

        for (Card curCard : cards) {

            Point tmp = boxNew.getPos();
            tmp.translate(0,30*boxNew.getNumCardUp());

            curCard.getInBox().removeCardUp();
            curCard.setInBox(boxNew);
            boxNew.setCardUp(curCard);
            curCard.setPos(tmp);

            ultimaMossa2.cards.add(curCard);
            ultimaMossa2.points.add(curCard.getLastPos());
        }

        ultimaMossa2.box = cards.get(0).getLastBox();
        ultimaMossa2.initCur();
        ultimaMossa2.timer = new Timer(1,ultimaMossa2);
        ultimaMossa1.resetAnnulla();

        repaint();
    }

    private void moveColonna2(ArrayList<Card> cards, ArrayList<Point> points, Box_Colonna box) {

        int size = cards.size();
        int numCardUp = box.getNumCardUp();

        for (int i = 0; i < size; i++) {

            Point p = box.getPos();
            p.translate(0,30*(numCardUp-size+i));
            points.add(p);
        }

        animazione3 = new Animazione3(cards,points,this);
        animazione3.timer = new Timer(1,animazione3);
        animazione3.timer.start();
    }

    void isGameWon() {

        try {

            for (Box_Semi b : boxList_Semi)
                if (b.getCardUp().getValore() != 13)
                    return;

        } catch (NullPointerException e) { return; }

		this.destroyGame();

        afp = new AnimazioneFinePartita(this);
        cardList = afp.getCards();

        afp.timer = new Timer(100,afp);
        afp.timer.start();
    }

    void destroyGame() {

        for (Box_Semi box : boxList_Semi)
            box.removeCardUp();

        for (Box_Normal box : boxList_Normal)
            box.removeCardUp();

        for (Box_Colonna box : boxList_Colonna) {

            int numCardUp = box.getNumCardUp();

            for (int i = 0; i < numCardUp; i++)
                box.removeCardUp();
        }

        ultimaMossa2 = new AnnullaColonna(this);
        ultimaMossa1 = new Annulla(this);
    }

    void annulla() {

        if (ultimaMossa1.timer != null) {

            ultimaMossa1.timer.start();

        } else if (ultimaMossa2.timer != null) {

            ultimaMossa2.timer.start();
        }
    }

    // ========================================================================= \\

	@Override
    public void mouseMoved(MouseEvent e) {}

    @Override
    public void mouseEntered(MouseEvent e) {}

    @Override
    public void mouseExited(MouseEvent e) {}

    @Override
    public void mouseClicked(MouseEvent e) {}
}
