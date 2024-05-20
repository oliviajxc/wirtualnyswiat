package maingame.organizmy.rosliny;

import javafx.geometry.Point2D;
import maingame.organizmy.Organizm;import maingame.Swiat;
public class Guarana extends Roslina {

    public Guarana(Point2D pozycja, Swiat swiat) {
        super('@', 0, pozycja, false, swiat);
    }

    @Override
    public void kolizja(Organizm inny) {
        String nazwa = inny.getClass().getSimpleName();
        swiat.dodajDziennik(this, "Wzmocnila " + nazwa);
        inny.wzmocnij(3);
        this.zabij();
    }

}