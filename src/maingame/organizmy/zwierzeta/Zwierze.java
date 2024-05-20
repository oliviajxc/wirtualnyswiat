package maingame.organizmy.zwierzeta;
import javafx.geometry.Point2D;
import maingame.organizmy.Organizm;
import java.util.Random;
import maingame.Swiat;
public class Zwierze extends Organizm {
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

    Zwierze(char ikona, int sila, int inicjatywa, Point2D pozycja, Swiat swiat) {
        super(ikona, sila, inicjatywa, pozycja, false, swiat);
    }

    @Override
    public void akcja() {
        postarz(1);
        Point2D[] allowedMoves = getAllowedMoves();
        int ruch = rand.nextInt(4);
        while (true) {
            Point2D newPozycja = getPozycja().add(allowedMoves[ruch]);
            if (swiat.isInBounds(newPozycja)) {
                setPozycja(newPozycja, false, false);
                break;
            } else {
                ruch = (ruch + 1) % 4;
            }
        }
    }

    @Override
    public void kolizja(Organizm inny) {
        if (this == inny) {
            return;
        }
        if (this.getClass() == inny.getClass()) {
            if (getWiek() < 4 || inny.getWiek() < 4) {
                return;
            }
            Point2D[] allowedMoves = getAllowedMoves();
            swiat.dodajDziennik(this, " rozmnozyl/a sie");
            int ruch = rand.nextInt(4);
            while(!swiat.isInBounds(getPozycja().add(allowedMoves[ruch]))||swiat.getOrganizm(getPozycja().add(allowedMoves[ruch]))!=null) {
                ruch++;
                if (ruch > 8) {
                    swiat.dodajDziennik(this, " nie ma miejsca na rozmnozenie");
                    return;
                }
            }
            cofnijRuch();
            swiat.dodajOrganizm(this.getClass().getCanonicalName(), getPozycja().add(allowedMoves[ruch]));
        } else if (czyUcieczka() || inny.czyUcieczka()) {
        } else if (getSila() > inny.getSila()) {
            swiat.dodajDziennik(this, " wygral/a walke");
            inny.zabij();
        } else if (getSila() < inny.getSila()) {
            swiat.dodajDziennik(this, " przegral/a walke");
            zabij();
        } else {
            swiat.dodajDziennik(this, " sily sa rowne");
            inny.zabij();
        }
    }

    Point2D[] getAllowedMoves(){
        return ruchy;
    }
}
