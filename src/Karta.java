public class Karta {
    private String kolor;
    private int wartosc;

    public Karta(String kolor, int wartosc) {
        this.kolor = kolor;
        this.wartosc = wartosc;
    }
    public int getWartosc() {
        return wartosc;
    }
    public String getKolor() {
        return kolor;
    }


    @Override
    public String toString() {
        return switch (wartosc) {
            case 11 -> "As";
            case 12 -> "Walet";
            case 13 -> "Dama";
            case 14 -> "Krol";
            default -> String.valueOf(wartosc);
        };
    }
}
