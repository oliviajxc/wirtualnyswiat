package maingame.organizmy.zwierzeta;
import java.util.Random;
import javafx.geometry.Point2D;
import maingame.Swiat;
public class Lis extends Zwierze {
    private static final Random rand = new Random();

    public Lis(Point2D pozycja, Swiat swiat) {
        super('L', 3, 7, pozycja, swiat);
    }

    @Override
    public void akcja() {
        postarz(1);
        Point2D[] allowedMoves = getAllowedMoves();
        for(int move = rand.nextInt(allowedMoves.length - 1); move<=getAllowedMoves().length*2; move++){
            if(!swiat.isInBounds(getPozycja().add(allowedMoves[move%getAllowedMoves().length])))
                continue;
            if(swiat.getOrganizm(getPozycja().add(allowedMoves[move%getAllowedMoves().length]))==null){
                setPozycja(getPozycja().add(allowedMoves[move%getAllowedMoves().length]),true,false);
                return;
            }
            if(swiat.getOrganizm(getPozycja().add(allowedMoves[move%getAllowedMoves().length])).getSila()<=getSila()){
                setPozycja(getPozycja().add(allowedMoves[move%getAllowedMoves().length]),true,false);
                return;
            }
        }
    }

}
