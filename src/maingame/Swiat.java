package maingame;

import java.io.*;
import java.util.*;
import javafx.geometry.Point2D;
import maingame.organizmy.Organizm;
import maingame.organizmy.rosliny.*;
import maingame.organizmy.zwierzeta.*;
import java.util.stream.Collectors;


public class Swiat {
    private int szerokosc;
    private int wysokosc;
    private ArrayList<Organizm> organizmy;
    private int tura;
    private Czlowiek czlowiek;
    private int wpisyLogow;
    private List<String> logi;

    private static Swiat instance;

    private Swiat(int szerokosc, int wysokosc) {
        this.szerokosc = szerokosc;
        this.wysokosc = wysokosc;
        organizmy = new ArrayList<>();
        logi = new ArrayList<>();
        tura = 0;
        czlowiek = null;
        wpisyLogow = 0;
    }

    private static Swiat getInstance() {
        return instance;
    }

    private static Swiat getInstance(FileInputStream plik) {
        instance = null;
        Scanner scanner = new Scanner(plik);
        String firstLine = scanner.nextLine(); // odczytanie pierwszej linii jako string
        String[] firstLineParts = firstLine.split(";"); // podzielenie pierwszej linii na części

        int tura = Integer.parseInt(firstLineParts[0]);
        int szerokosc = Integer.parseInt(firstLineParts[1]);
        int wysokosc = Integer.parseInt(firstLineParts[2]);

        Swiat swiat = new Swiat(szerokosc, wysokosc);
        swiat.tura = tura;

        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            if (line.isEmpty())
                continue;

            String[] parts = line.split(";");
            String nazwa = parts[0];
            double x = Double.parseDouble(parts[1]);
            double y = Double.parseDouble(parts[2]);
            int sila = Integer.parseInt(parts[3]);
            int wiek = Integer.parseInt(parts[4]);

            Organizm obj = null;
            switch (nazwa) {
                case "maingame.organizmy.zwierzeta.Czlowiek":
                    obj = new Czlowiek(new Point2D(x, y), swiat);
                    swiat.czlowiek = (Czlowiek) obj;
                    int umiejetnosc = Integer.parseInt(parts[5]);
                    swiat.czlowiek.setUmiejetnosc(umiejetnosc);
                    break;
                case "maingame.organizmy.zwierzeta.Antylopa":
                    obj = new Antylopa(new Point2D(x, y), swiat);
                    break;
                case "maingame.organizmy.rosliny.Trawa":
                    obj = new Trawa(new Point2D(x, y), swiat);
                    break;
                case "maingame.organizmy.zwierzeta.Owca":
                    obj = new Owca(new Point2D(x, y), swiat);
                    break;
                case "maingame.organizmy.zwierzeta.Wilk":
                    obj = new Wilk(new Point2D(x, y), swiat);
                    break;
                case "maingame.organizmy.rosliny.Mlecz":
                    obj = new Mlecz(new Point2D(x, y), swiat);
                    break;
                case "maingame.organizmy.rosliny.Guarana":
                    obj = new Guarana(new Point2D(x, y), swiat);
                    break;
                case "maingame.organizmy.rosliny.Barszcz":
                    obj = new Barszcz(new Point2D(x, y), swiat);
                    break;
                case "maingame.organizmy.rosliny.Jagody":
                    obj = new Jagody(new Point2D(x, y), swiat);
                    break;
                case "maingame.organizmy.zwierzeta.Lis":
                    obj = new Lis(new Point2D(x, y), swiat);
                    break;
                case "maingame.organizmy.zwierzeta.Zolw":
                    obj = new Zolw(new Point2D(x, y), swiat);
                    break;
            }

            if (obj != null) {
                obj.wzmocnij(sila - obj.getSila());
                obj.postarz(wiek);
                swiat.organizmy.add(obj);
            }
        }

        scanner.close();
        return swiat;
    }

    private void wyczyscDziennik() {
        logi.clear();
    }
    public static Swiat getInstance(int szerokosc, int wysokosc) {
        if (instance != null)
            instance = null;
        instance = new Swiat(szerokosc, wysokosc);
        return instance;
    }
    public void poczatkowaPopulacja() {
        Swiat swiat = Swiat.getInstance();

        Czlowiek czlowiek = new Czlowiek(new Point2D(5, 5), swiat);
        swiat.czlowiek = czlowiek;
        new Antylopa(new Point2D(10, 10), swiat);
        new Trawa(new Point2D(2, 0), swiat);
        new Owca(new Point2D(19, 19), swiat);
        new Owca(new Point2D(16, 17), swiat);
        new Wilk(new Point2D(13, 3), swiat);
        new Wilk(new Point2D(3, 7), swiat);
        new Mlecz(new Point2D(3, 9), swiat);
        new Guarana(new Point2D(6, 5), swiat);
        new Barszcz(new Point2D(18, 17), swiat);
        new Jagody(new Point2D(10, 12), swiat);
        new Lis(new Point2D(8, 11), swiat);
        new Zolw(new Point2D(0, 3), swiat);

    }
    public void wykonajTure() {
        wyczyscDziennik();
        tura++;
        organizmy = organizmy.stream().filter(o -> o.getSila() >= 0).collect(Collectors.toCollection(ArrayList::new));
        organizmy.sort(Organizm::pierwszenstwo);
        int size = organizmy.size();
        for (int i = 0; i < size; i++) {
            if(organizmy.get(i).getSila()>=0)
                organizmy.get(i).akcja();
        }
    }

    public void zapiszStan(String nazwaPliku) {
        try {
            PrintWriter plik = new PrintWriter(nazwaPliku + ".csv");
            plik.println(tura + ";" + szerokosc + ";" + wysokosc);
            for (Organizm o : organizmy) {
                if (o.getSila() > -1) {
                    plik.println(o);
                }
            }
            plik.close();
            System.out.println("Zapisano!");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void wczytajStan(String nazwaPliku) {
        try {
            FileInputStream fis = new FileInputStream(nazwaPliku + ".csv");
            Swiat swiat = getInstance(fis);
            this.tura = swiat.tura;
            this.szerokosc = swiat.szerokosc;
            this.wysokosc = swiat.wysokosc;
            this.organizmy = swiat.organizmy;
            this.logi = swiat.logi;
            this.czlowiek = swiat.czlowiek;
            System.out.println("Wczytano!");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }


    public void dodajOrganizm(Organizm organizm) {
        organizmy.add(organizm);
    }
    public void dodajOrganizm(String name, Point2D position){
        Organizm o = Organizm.spawn(this, name, position);
        organizmy.add(o);
        assert o != null;
        dodajDziennik(o, " narodzil sie na ["+o.getpointX()+","+o.getpointY()+"]");
    }
    public Organizm getOrganizm(Point2D pozycja) {
        if (pozycja.getX() < 0 || pozycja.getX() >= szerokosc ||
                pozycja.getY() < 0 || pozycja.getY() >= wysokosc)
            return null;
        for (Organizm o : organizmy) {
            if ((o.getpointX() == pozycja.getX()) && o.getpointY() == pozycja.getY()) {
                return o;
            }
        }
        return null;
    }

    public List<Organizm> getOrganizmy() {
        return Collections.unmodifiableList(organizmy);
    }
    public Czlowiek getCzlowiek() {
        return czlowiek;
    }

    public List<String> getLogi() {
        return Collections.unmodifiableList(logi);
    }

    public void dodajDziennik(Organizm zrodlo, String log) {
        String nazwa = zrodlo.getClass().getSimpleName();
        wpisyLogow++;
        logi.add(nazwa + log);
    }

    public int getSzerokosc() {
        return szerokosc;
    }

    public int getWysokosc() {
        return wysokosc;
    }
    public Boolean isInBounds(Point2D pozycja){
        return pozycja.getX()>=0 && pozycja.getX()<szerokosc && pozycja.getY()>=0 && pozycja.getY()<wysokosc;
    }
    public void resetujSwiat() {
        this.organizmy.clear();
        this.logi.clear();
        this.czlowiek = null;
    }
}
