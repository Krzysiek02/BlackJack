import java.util.ArrayList;
import java.util.List;

public abstract class Postacie {
    List<Karta> taliaKartGracza = new ArrayList<>();


    public void clearTaliaKartGracza(){
        taliaKartGracza.clear();
    }

    public Karta ciagnijKarte(Karta karta){
        taliaKartGracza.add(karta);
        Karta ciagnietaKarta = taliaKartGracza.get(0);
        return ciagnietaKarta;
    }
    public Karta ostatniaKarta(){
        return taliaKartGracza.get(taliaKartGracza.size() - 1);
    }

    public int iloscKart(){
        return taliaKartGracza.size();
    }
    public int taliaValue(){
        int taliaValue = 0;
        int liczbaAsow = 0;

        for (int i = 0; i < taliaKartGracza.size(); i++) {
            Karta karta = taliaKartGracza.get(i);
            int wartosc = karta.getWartosc();

            if (wartosc == 11) {
                taliaValue += 11;
                liczbaAsow++;
            } else if (wartosc > 10) {
                taliaValue += 10;
            } else {
                taliaValue += wartosc;
            }
        }

        while (taliaValue > 21 && liczbaAsow > 0) {
            taliaValue -= 10;
            liczbaAsow--;
        }

        return taliaValue;
    }

}
