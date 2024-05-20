package maingame.organizmy.rosliny;

import javafx.geometry.Point2D;
import maingame.organizmy.Organizm;
import maingame.organizmy.zwierzeta.Zwierze;
import maingame.Swiat;
public class Barszcz extends Roslina {
    public Barszcz(Point2D pozycja, Swiat swiat) {
        super('#', 10, pozycja, false, swiat);
    }

    @Override
    public void akcja() {
        // Sprawdź sąsiednie pola
        Point2D[] ruchy = getAllowedMoves();
        for (Point2D ruch : ruchy) {
            Point2D nowaPozycja = getPozycja().add(ruch);
            if (nowaPozycja.getX() >= 0 && nowaPozycja.getX() < swiat.getSzerokosc() &&
                    nowaPozycja.getY() >= 0 && nowaPozycja.getY() < swiat.getWysokosc()) {
                Organizm sasiad = swiat.getOrganizm(nowaPozycja);
                if (sasiad != null && sasiad instanceof Zwierze && !sasiad.getImmortal()) {
                    swiat.dodajDziennik(this, "Zatrul " + sasiad.getClass().getSimpleName());
                    sasiad.zabij();
                }
            }
        }
    }

    @Override
    public void kolizja(Organizm inny) {
        if (!inny.getImmortal()) {
            String nazwa = inny.getClass().getSimpleName();
            swiat.dodajDziennik(this, "Zatrul " + nazwa);
            inny.zabij();
            this.zabij();
        } else {
            this.zabij();
        }
    }
}
