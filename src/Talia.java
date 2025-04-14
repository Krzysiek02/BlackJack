import javax.swing.*;
import java.util.*;
import java.util.List;

public class Talia implements TypyKarty{
    private final Map<String, ImageIcon> taliaZdjecia = new HashMap<>();
    private final List<Karta> talia = new ArrayList<>();

    public Talia() {
        for (String kolor : KOLORY) {
            for (int figura : FIGURY) {
                Karta nowaKarta = new Karta(kolor , figura);
                talia.add(nowaKarta);

                String klucz = figura + "_" + kolor;
                String sciezka = "images/" + klucz + ".png";
                java.net.URL url = getClass().getResource(sciezka);
                ImageIcon ikona = new ImageIcon(url);
                taliaZdjecia.put(klucz, ikona);
            }
        }
        Collections.shuffle(talia);
    }
    public List<Karta> getTalia(){
        return talia;
    }
    public List<Karta> shuffleTalia(){
        Collections.shuffle(talia);
        return talia;
    }
    public Karta usunKarte(){
        Karta ciagnietaKarta = talia.get(0);
        talia.remove(0);
        return ciagnietaKarta;
    }
    public ImageIcon getZdjecieKarty(Karta karta) {
        String klucz = karta.getWartosc() + "_" + karta.getKolor();
        return taliaZdjecia.get(klucz);
    }

}
