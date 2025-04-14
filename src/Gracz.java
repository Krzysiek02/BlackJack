public final class Gracz extends Postacie{

    private int money = 1000;
    private int currentBet = 0;

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
        currentBet=hajs;
    }
    public void clearCurrentBet(){
        currentBet=0;
    }
    public int getCurrentBet(){
        return currentBet;
    }

}
