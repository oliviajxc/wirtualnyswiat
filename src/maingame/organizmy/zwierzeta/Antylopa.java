package maingame.organizmy.zwierzeta;

import java.util.Random;
import javafx.geometry.Point2D;
import maingame.Swiat;

public class Antylopa extends Zwierze {
    private static final Random rand = new Random();
    private static final int[][] ruchy = {{1, 0}, {0, 1}, {-1, 0}, {0, -1}};

    public Antylopa(Point2D pozycja, Swiat swiat) {
        super('A', 4, 4, pozycja, swiat);
    }

    @Override
    public void akcja() {
        postarz(1);
        int move = rand.nextInt(4);
        while (!setPozycja(new Point2D(getpointX() + 2 * ruchy[move][0], getpointY() + 2 * ruchy[move][1]), false, false)) {
            move = (move + 1) % 4;
        }
    }

    @Override
    public boolean czyUcieczka() {
        boolean ucieczka = rand.nextBoolean();
        if (ucieczka) {
            int move = 0;
            while (!setPozycja(new Point2D(getpointX() + ruchy[move][0], getpointY() + ruchy[move][1]), true, false)) {
                move++;
                if (move == 8) {
                    swiat.dodajDziennik(this, " nie zdazyla uciec");
                    return false;
                }
            }
            swiat.dodajDziennik(this, " uciekla!");
            return true;
        }
        return false;
    }

}
