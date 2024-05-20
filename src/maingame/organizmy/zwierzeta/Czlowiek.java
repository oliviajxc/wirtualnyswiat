package maingame.organizmy.zwierzeta;

import javafx.geometry.Point2D;
import java.util.Random;
import maingame.Swiat;
import maingame.organizmy.Organizm;
public class Czlowiek extends Zwierze {
    private int umiejetnosc;
    private Point2D kierunek;
    private void setNiesmiertelnosc(boolean wartosc) {
        this.immortal = wartosc;
    }
    public Czlowiek(Point2D pozycja, Swiat swiat) {
        super('C', 4, 4, pozycja, swiat);
        this.umiejetnosc = -5;
        this.kierunek = new Point2D(0, 0);
    }

    @Override
    public void akcja() {
        postarz(1);
        int predkosc = 1;
        if (umiejetnosc > -5) {
            umiejetnosc--;
        }
        this.setNiesmiertelnosc(umiejetnosc > 0);
        setPozycja(new Point2D(getpointX() + kierunek.getX() * predkosc, getpointY() + kierunek.getY() * predkosc), false, kierunek.getX() == 0 && kierunek.getY() == 0);
        kierunek = new Point2D(0, 0);
    }

    @Override
    public void kolizja(Organizm inny) {
        if (this == inny) {
            return;
        }
        if (umiejetnosc > 0 && getSila() <= inny.getSila()) {
            Random rand = new Random();
            Point2D[] allowedMoves = getAllowedMoves();
            int move = rand.nextInt(4);
            while (!setPozycja(getPozycja().add(allowedMoves[move]), false, false)) {
                move = (move + 1) % 4;
            }
            swiat.dodajDziennik(this, " uwaga! Czlowiek jest niesmiertelny!");
        } else {
            super.kolizja(inny);
        }
    }

    public void setKierunek(Point2D kierunek) {
        if ((getpointX() + kierunek.getX()) < 0 || (getpointX() + kierunek.getX()) >= swiat.getSzerokosc() ||
                (getpointY() + kierunek.getY()) < 0 || (getpointY() + kierunek.getY()) >= swiat.getWysokosc()) {
            return;
        }
        this.kierunek = kierunek;
    }

    public void aktywujUmiejetnosc() {
        if (umiejetnosc == -5) {
            swiat.dodajDziennik(this, " aktywowano: Niesmiertelnosc");
            this.setNiesmiertelnosc(true);
            umiejetnosc = 5;
        }
    }

    public void setUmiejetnosc(int umiejetnosc) {
        this.umiejetnosc = umiejetnosc;
    }

    @Override
    public boolean czyUcieczka() {
        return this.getImmortal();
    }

    @Override
    public String toString() {
        String dane = super.toString();
        dane += umiejetnosc + ";";
        return dane;
    }
}
