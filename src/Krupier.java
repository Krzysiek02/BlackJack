import java.util.ArrayList;
import java.util.List;

public final class Krupier extends Postacie{
    public int ostatniaKartaValue(){
        Karta karta = taliaKartGracza.get(taliaKartGracza.size() - 1);
        int wartosc = karta.getWartosc();

        if (wartosc == 11) {
            wartosc = 11;
        } else if (wartosc > 11) {
            wartosc = 10;
        }
        return wartosc;
    }
    public List<Karta> getTalia(){
        return taliaKartGracza;
    }
}
