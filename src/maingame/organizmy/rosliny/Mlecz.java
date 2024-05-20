package maingame.organizmy.rosliny;

import javafx.geometry.Point2D;
import maingame.Swiat;
public class Mlecz extends Roslina {

    public Mlecz(Point2D pozycja, Swiat swiat) {
        super('*', 0, pozycja, false, swiat);
    }

    @Override
    public void akcja() {
        for (int i = 0; i < 3; i++) {
            super.akcja();
        }
    }
}