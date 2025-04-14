import javax.swing.*;
import java.awt.*;
import java.util.Objects;

public final class ObslugaZdjec {
    public static ImageIcon dodawanieZdjecia(Karta karta, Talia talia){
        ImageIcon zdjecie = talia.getZdjecieKarty(karta);
        Image przeskalowanezdjecie = zdjecie.getImage().getScaledInstance(100, 150, Image.SCALE_SMOOTH);
        return new ImageIcon(przeskalowanezdjecie);
    }

    public static ImageIcon dodawanieZdjeciaZakrytej() {
//        java.net.URL url = getClass().getResource("images/4_Karo.png");
        ImageIcon zakrytaKarta = new ImageIcon(Objects.requireNonNull(ObslugaZdjec.class.getResource("images/black_joker.png")));
        Image przeskalowanezdjecie = zakrytaKarta.getImage().getScaledInstance(100, 150, Image.SCALE_SMOOTH);
        return new ImageIcon(przeskalowanezdjecie);
    }
}
