package maingame.organizmy.rosliny;

import javafx.geometry.Point2D;
import maingame.Swiat;

public class Trawa extends Roslina {
    public Trawa(Point2D pozycja, Swiat swiat) {
        super(',', 0, pozycja, false, swiat);
    }
}
