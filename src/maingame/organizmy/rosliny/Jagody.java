package maingame.organizmy.rosliny;

import javafx.geometry.Point2D;
import maingame.organizmy.Organizm;import maingame.Swiat;
public class Jagody extends Roslina {

    public Jagody(Point2D pozycja, Swiat swiat) {
        super('%', 99, pozycja, false, swiat);
    }

    @Override
    public void kolizja(Organizm inny) {
        if (!inny.getImmortal()) {
            String nazwa = inny.getClass().getSimpleName();
            swiat.dodajDziennik(this, "Smiertelnie otruly " + nazwa);
            this.zabij();
            inny.zabij();
        } else {
            this.zabij();
        }
    }

}