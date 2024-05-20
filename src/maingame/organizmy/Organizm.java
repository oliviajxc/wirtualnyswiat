package maingame.organizmy;

import javafx.geometry.Point2D;
import maingame.Swiat;

import java.lang.reflect.InvocationTargetException;

public abstract class Organizm {
    protected final Swiat swiat;
    private final char ikona;
    private int sila;
    private int inicjatywa;
    private int wiek;
    protected boolean immortal;
    private Point2D pozycja;
    private Point2D poprzedniaPozycja;
    private int getInicjatywa() {
        return inicjatywa;
    }
    protected abstract void kolizja(Organizm inny);
    protected Organizm(char ikona, int sila, int inicjatywa, Point2D pozycja, boolean immortal, Swiat swiat) {
        this.swiat = swiat;
        this.ikona = ikona;
        this.sila = sila;
        this.inicjatywa = inicjatywa;
        this.pozycja = pozycja;
        this.wiek = 0;
        this.immortal = immortal;
        swiat.dodajOrganizm(this);
    }
    protected Point2D getPozycja() {
        return pozycja;
    }

    protected boolean setPozycja(Point2D pozycja, boolean musiBycPuste, boolean rozmnozenie) {
        if (pozycja.getX() < 0 || pozycja.getX() >= swiat.getSzerokosc() ||
                pozycja.getY() < 0 || pozycja.getY() >= swiat.getWysokosc()) {
            return false;
        }
        if (musiBycPuste && swiat.getOrganizm(pozycja) != null) {
            return false;
        }
        poprzedniaPozycja = this.pozycja;
        Organizm inny = swiat.getOrganizm(pozycja);
        this.pozycja = pozycja;
        if (!rozmnozenie) {
            swiat.dodajDziennik(this, " Rusza z [" + poprzedniaPozycja.getX() + ";" + poprzedniaPozycja.getY() + "] do [" + pozycja.getX() + ";" + pozycja.getY() + "]");
        }
        if (inny != null) {
            inny.kolizja(this);
        }
        return true;
    }

    public boolean czyUcieczka() {
        return false;
    }

    public static Organizm spawn(Swiat swiat, String name, Point2D position) {
        try {
            if (!name.contains("maingame.organizmy.")) {
                name = "maingame.organizmy." + name;
            }
            return (Organizm) Class.forName(name)
                    .getConstructor(Point2D.class, Swiat.class)
                    .newInstance(position, swiat);
        } catch (ClassNotFoundException | NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public String toString() {
        return this.getClass().getName() + ";" +
                pozycja.getX() + ";" +
                pozycja.getY() + ";" +
                sila + ";" +
                wiek + ";";
    }

    public int getSila() {
        return sila;
    }
    public int getWiek() {
        return wiek;
    }

    public char getIkona() {
        return ikona;
    }

    public void postarz(int wartosc) {
        wiek += wartosc;
    }

    public void zabij() {
        pozycja = new Point2D(-1, -1);
        sila = -1;
        inicjatywa = -1;
    }

    public void wzmocnij(int wartosc) {
        sila += wartosc;
    }

    public abstract void akcja();



    public double getpointX() {
        return pozycja.getX();
    }

    public double getpointY() {
        return pozycja.getY();
    }

    public void cofnijRuch() {
        setPozycja(poprzedniaPozycja, false, false);
    }

    public static int pierwszenstwo(Organizm a, Organizm b) {
        if (a.getInicjatywa() > b.getInicjatywa()) {
            return 1;
        } else if (a.getInicjatywa() < b.getInicjatywa()) {
            return 0;
        } else {
            return a.getWiek() > b.getWiek() ? 1 : 0;
        }
    }

    public boolean getImmortal() {
        return immortal;
    }
}
