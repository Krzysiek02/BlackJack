import java.util.ArrayList;
import java.util.List;

public final class Gracz extends Postacie{
    private List<Karta> taliaKartGracza2 = new ArrayList<>();
    public void clearTaliaKartGracza2(){
        taliaKartGracza2.clear();
    }
    public void splitTalia(){
        taliaKartGracza2.add(taliaKartGracza.getLast());
        taliaKartGracza.removeLast();
    }
    public Karta ciagnijKarte2(Karta karta){
        taliaKartGracza2.add(karta);
        Karta ciagnietaKarta = taliaKartGracza2.get(0);
        return ciagnietaKarta;
    }
    public Karta ostatniaKarta2(){
        return taliaKartGracza2.get(taliaKartGracza2.size() - 1);
    }
    public int iloscKart2(){
        return taliaKartGracza2.size();
    }
    public int taliaValue2(){
        int taliaValue = 0;
        int liczbaAsow = 0;

        for (int i = 0; i < taliaKartGracza2.size(); i++) {
            Karta karta = taliaKartGracza2.get(i);
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

    private int money = 1000;
    private int currentBet = 0;
    private int currentBet2 = 0;

    public int getMoney(){
        return money;
    }

    public void addMoney(int hajs) {
        money += hajs;
    }
    public boolean odejmijMoney(int hajs) {
        if(hajs <= money){
            money -= hajs;
            return true;
        }
        return false;
    }
    public void setCurrentBet(int hajs){
        currentBet+=hajs;
    }
    public void setCurrentBet2(int hajs){
        currentBet2+=hajs;
    }
    public void clearCurrentBet(){
        currentBet=0;
    }
    public void clearCurrentBet2(){
        currentBet2=0;
    }
    public int getCurrentBet(){
        return currentBet;
    }
    public int getCurrentBet2(){
        return currentBet2;
    }
    public int checkCardValue(int index){

        Karta karta = taliaKartGracza.get(index);
        int wartosc = karta.getWartosc();

        if (wartosc == 11) {
            wartosc = 11;
        } else if (wartosc > 10) {
            wartosc = 10;
        }
        return wartosc;
    }
}
