package maingame.organizmy.rosliny;

import javafx.geometry.Point2D;
import maingame.organizmy.Organizm;
import java.util.Random;
import maingame.Swiat;
public class Roslina extends Organizm {
    private static final Point2D[] ruchy = {
            new Point2D(1, 0),
            new Point2D(0, 1),
            new Point2D(-1, 0),
            new Point2D(0, -1),
            new Point2D(-1, -1),
            new Point2D(1, -1),
            new Point2D(-1, 1),
            new Point2D(1, -1)
    };
    private static final Random rand = new Random();
    Roslina(char ikona, int sila, Point2D pozycja, boolean immortal, Swiat swiat) {
        super(ikona, sila, 0, pozycja, immortal, swiat);
    }

    @Override
    public void akcja() {
        boolean rozsianie = (Math.random() < 0.05);  // Placeholder for randomness.
        if (rozsianie) {
            Point2D[] allowedMoves = getAllowedMoves();
            int randomMove = rand.nextInt(allowedMoves.length);
            for (int i = 0; i < allowedMoves.length; i++) {
                Point2D move = allowedMoves[(randomMove + i) % allowedMoves.length];
                Point2D newPosition = getPozycja().add(move);
                if (!swiat.isInBounds(newPosition)) {
                    continue;
                }
                if (swiat.getOrganizm(newPosition) == null) {
                    try {
                        swiat.dodajOrganizm(this.getClass().getCanonicalName(), newPosition);
                     //   swiat.dodajDziennik(this, "Rozsianie");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    return;
                }
            }
        }
    }

    @Override
    public void kolizja(Organizm inny) {
        if (this == inny) {
            return;
        }
        if (getSila() > inny.getSila()) {
            inny.zabij();
        } else if (getSila() < inny.getSila()) {
            zabij();
        } else {
            zabij();
            inny.zabij();
        }
    }
    Point2D[] getAllowedMoves(){
        return ruchy;
    }
}