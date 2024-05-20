package maingame.organizmy.zwierzeta;

import javafx.geometry.Point2D;
import maingame.Swiat;

public class Owca extends Zwierze {

    public Owca(Point2D pozycja, Swiat swiat) {
        super('O', 4, 4, pozycja, swiat);
    }
}