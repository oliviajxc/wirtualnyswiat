package maingame.organizmy.zwierzeta;

import java.util.Random;
import javafx.geometry.Point2D;
import maingame.organizmy.Organizm;import maingame.Swiat;

public class Zolw extends Zwierze {
    private static final Random rand = new Random();
    public Zolw(Point2D pozycja, Swiat swiat) {
        super('Z', 2, 1, pozycja, swiat);
    }

    @Override
    public void akcja() {
        boolean robiRuch = rand.nextInt(100) >= 75; // 25% chance to move
        if (robiRuch) {
            super.akcja();
        } else {
            postarz(1);
        }
    }

    @Override
    public void kolizja(Organizm inny) {
        if (inny.getSila() < 5 && !(inny instanceof Zolw)) {
            swiat.dodajDziennik(this, "Odparł atak");
            inny.cofnijRuch();
        } else {
            super.kolizja(inny);
        }
    }

}