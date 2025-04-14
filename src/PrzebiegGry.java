import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

public final class PrzebiegGry {
    private Talia talia = new Talia();
    private final Krupier krupier = new Krupier();
    private JFrame ramka;
    private JPanel playersBoard, krupierBoardCards, informacjeboard, krupierBoard;
    private JLabel informacjeJLabel, krupierValue;
    int liczbaGraczy = 1;
    int MAX_GRACZY = 4;
    private Karta kartaDoOdkrycia;
    private JButton newRoundButton;
    private final ArrayList<Gracz> graczeList = new ArrayList<>();
//    private final ArrayList<JPanel> graczBoardsList = new ArrayList<>();
    private final ArrayList<JPanel> graczBoardsCardsList = new ArrayList<>();
    private final ArrayList<JLabel> graczValueLabelList = new ArrayList<>();
    private final ArrayList<JButton> hitButtonsList = new ArrayList<>();
    private final ArrayList<JButton> standButtonsList = new ArrayList<>();
    private final ArrayList<JLabel> moneyLabelList = new ArrayList<>();
    private final ArrayList<JLabel> BetLabelList = new ArrayList<>();

    int turaGracza = 0;
    public void StartGra(){
        String input = JOptionPane.showInputDialog("Podaj liczbę graczy 1-4: ");
        try {
            int inputParsed = Integer.parseInt(input);
            if (inputParsed > 0 && inputParsed <= MAX_GRACZY) {
                liczbaGraczy = inputParsed;
            }
        } catch (NumberFormatException e) {
            throw new RuntimeException(e);
        }
        frame();
        betowanie();
    }

    public void frame() {
        ramka = new JFrame();
        ramka.setTitle("BlackJack");
        ramka.setSize(1900, 1000);
        ramka.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        playersBoard = new JPanel();

        //dodawanie graczy
        for (int i =0; i< liczbaGraczy; i++){
            dodawanieGraczy(i);
        }
        //dodawanie Krupiera
        dodanieKrupiera();

        informacjeboard = new JPanel();
        informacjeboard.setLayout(new BoxLayout(informacjeboard,BoxLayout.Y_AXIS));
        informacjeboard.add(Box.createRigidArea(new Dimension(0, 120)));
        informacjeJLabel = new JLabel("Rozdawanie Kart", JLabel.CENTER);
        informacjeJLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        informacjeJLabel.setFont(new Font("serif", Font.BOLD, 30));
        informacjeboard.add(informacjeJLabel);
        newRoundButton = new JButton("Nowa Runda");
        newRoundButton.setEnabled(false);
        newRoundButton.addActionListener(new NewRoundButtonListener());
        newRoundButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        informacjeboard.add(newRoundButton);


        ramka.add(krupierBoard, BorderLayout.NORTH);
        ramka.add(informacjeboard, BorderLayout.CENTER);
        ramka.add(playersBoard, BorderLayout.SOUTH);
        ramka.setVisible(true);
    }
    public class NewRoundButtonListener implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e) {
            resetBoard();
            betowanie();
            newRoundButton.setEnabled(false);
        }
    }

    public void betowanie(){
        int i = 0;
        informacjeJLabel.setText("BETOWANIE");
        for (Gracz gracz : graczeList) {
            boolean valid = false;
            while (!valid) {
                int aktualnyGracz=i+1;
                String input = JOptionPane.showInputDialog("Gracz " + aktualnyGracz + " ma " + gracz.getMoney() + " zł.\n Podaj zakład:");
                try {
                    int bet = Integer.parseInt(input);
                    if (bet > 0 && gracz.odejmijMoney(bet)) {
                        gracz.setCurrentBet(bet);
                        valid = true;
                        moneyLabelList.get(i).setText("Stan konta: " + gracz.getMoney() + " zł");
                        BetLabelList.get(i).setText("Bet: " + gracz.getCurrentBet() + " zł");
                        i++;
                    } else {
                        JOptionPane.showMessageDialog(ramka, "Masz za malo pieniedzy");
                    }
                } catch (NumberFormatException e) {
                    JOptionPane.showMessageDialog(ramka, "Nieprawidłowa liczba.");
                }
            }
        }
        rozdawanieKart();
    }

    public void resetBoard(){
        talia = new Talia();
        for (Gracz gracz: graczeList){
            gracz.clearTaliaKartGracza();
        }
        for (JPanel panel : graczBoardsCardsList){
            panel.removeAll();
            panel.revalidate();
            panel.repaint();
        }
        for (JLabel valueLabel : graczValueLabelList){
            valueLabel.setText("0");
        }
        for(JLabel betValueLabel : BetLabelList){
            betValueLabel.setText("Bet: 0 zł");
        }
        int i = 0;
        for(Gracz gracz : graczeList){
            moneyLabelList.get(i).setText("Stan konta: " + gracz.getMoney() + " zł");
            i++;
        }
        krupier.clearTaliaKartGracza();
        krupierBoardCards.removeAll();
        krupierValue.setText("0");
        krupierBoardCards.revalidate();
        krupierBoardCards.repaint();
        turaGracza = 0;
    }
    public void dodawanieGraczy(int i){
        Gracz gracz = new Gracz();
        graczeList.add(gracz);
        Border border = BorderFactory.createLineBorder(Color.BLACK, 1);
        JPanel graczBoard = new JPanel(new BorderLayout());
        graczBoard.setPreferredSize(new Dimension(400, 380));
        graczBoard.setBorder(border);

        //KARTY
        JPanel graczBoardCards = new JPanel(null);
        graczBoardCards.setPreferredSize(new Dimension(150, 160));
        graczBoardCards.setBorder(border);



        JPanel bottomPanel = new JPanel(new BorderLayout());
        //info
        JPanel graczInfoPanel = new JPanel(new GridLayout(3, 1));
        JLabel graczValue = new JLabel(String.valueOf(0), JLabel.CENTER);
        graczValue.setFont(new Font("serif", Font.BOLD, 30));
        JLabel moneyLabel = new JLabel("Stan konta: " + gracz.getMoney() + " zł", JLabel.CENTER);
        moneyLabel.setFont(new Font("serif", Font.BOLD, 20));
        JLabel BetLabel = new JLabel("Bet: " + gracz.getCurrentBet() + " zł", JLabel.CENTER);
        BetLabel.setFont(new Font("serif", Font.BOLD, 20));
        graczInfoPanel.add(graczValue);
        graczInfoPanel.add(moneyLabel);
        graczInfoPanel.add(BetLabel);


        //buttons
        JPanel graczBoardButtons = new JPanel(new FlowLayout(FlowLayout.CENTER));
        graczBoardButtons.setBorder(border);
        JButton hit = new JButton("HIT");
        hit.setPreferredSize(new Dimension(150, 50));
        graczBoardButtons.add(hit);
        hit.addActionListener(new HitListener(i));

        JButton stand = new JButton("STAND");
        stand.setPreferredSize(new Dimension(150, 50));
        graczBoardButtons.add(stand);
        stand.addActionListener(new StandListener(i));

        bottomPanel.add(graczInfoPanel, BorderLayout.NORTH);
        bottomPanel.add(graczBoardButtons, BorderLayout.SOUTH);

        graczBoard.add(graczBoardCards, BorderLayout.CENTER);
        graczBoard.add(bottomPanel, BorderLayout.SOUTH);

        playersBoard.add(graczBoard);

        //Listy
        moneyLabelList.add(moneyLabel);
        BetLabelList.add(BetLabel);
        graczBoardsCardsList.add(graczBoardCards);
//        graczBoardsList.add(graczBoard);
        hitButtonsList.add(hit);
        standButtonsList.add(stand);
        graczValueLabelList.add(graczValue);

        //stany
        hit.setEnabled(false);
        stand.setEnabled(false);
    }
    public void dodanieKrupiera(){
        krupierBoard = new JPanel();
        krupierBoard.setLayout(new BoxLayout(krupierBoard,BoxLayout.Y_AXIS));
        krupierBoardCards = new JPanel();
        krupierBoardCards.setPreferredSize(new Dimension(400, 160));

        krupierValue = new JLabel(String.valueOf(krupier.taliaValue()), JLabel.CENTER);
        krupierValue.setFont(new Font("serif", Font.BOLD, 30));
        JPanel krupierValuePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));

        krupierValuePanel.add(krupierValue);
        krupierBoard.add(krupierBoardCards);
        krupierBoard.add(krupierValuePanel);




    }
    public void rozdawanieKart() {
        Timer timer = new Timer(1000, null);
        timer.addActionListener(new ActionListener() {
            int i = 0;
            @Override
            public void actionPerformed(ActionEvent e) {
                informacjeJLabel.setText("Rozdawanie Kart");
                if(i < liczbaGraczy){
                    GraczCiagnie(i);
                }else if (i == liczbaGraczy){
                    krupierCiagnie();
                }else if (i < 2 * liczbaGraczy + 1){
                    GraczCiagnie(i-liczbaGraczy-1);
                }else if (i == 2 * liczbaGraczy + 1){
                    krupierCiagnie();
                }else{
                    int aktualnyGracz = turaGracza+1;
                    informacjeJLabel.setText("Tura gracza " + aktualnyGracz);
                    if(graczeList.get(turaGracza).taliaValue()==21){
                        nastepnyGracz();
                    }else{
                        hitButtonsList.get(0).setEnabled(true);
                        standButtonsList.get(0).setEnabled(true);
                    }
                    ((Timer) e.getSource()).stop();
                }
                i++;
            }
        });
        timer.setInitialDelay(1000);
        timer.start();

    }
    public void krupierCiagnie() {

        JLabel kartaLabel2;
        kartaDoOdkrycia = krupier.ciagnijKarte(talia.usunKarte());
        if (krupier.iloscKart() == 1) {
            ImageIcon przeskalowanaKarta = ObslugaZdjec.dodawanieZdjeciaZakrytej();
            kartaLabel2 = new JLabel(przeskalowanaKarta);
        } else {
            ImageIcon przeskalowanaKarta = ObslugaZdjec.dodawanieZdjecia(krupier.ostatniaKarta(),talia);
            kartaLabel2 = new JLabel(przeskalowanaKarta);
            krupierValue.setText(String.valueOf(krupier.taliaValue()));
        }
        if (krupier.iloscKart()==2){
            krupierValue.setText(String.valueOf(krupier.ostatniaKartaValue()));
        }
        krupierBoardCards.add(kartaLabel2);
        krupierBoardCards.revalidate();
        krupierBoardCards.repaint();

    }

    public void GraczCiagnie(int nrGracza) {
        Gracz gracz = graczeList.get(nrGracza);
        JPanel board = graczBoardsCardsList.get(nrGracza);
        JLabel graczValue = graczValueLabelList.get(nrGracza);

        int przesuniecieX = gracz.iloscKart() * 50;
        gracz.ciagnijKarte(talia.usunKarte());
        ImageIcon przeskalowanaKarta = ObslugaZdjec.dodawanieZdjecia(gracz.ostatniaKarta(),talia);
        JLabel kartaLabel = new JLabel(przeskalowanaKarta);
//        graczBoardsCardsList.get(0).getWidth()/3
        kartaLabel.setBounds(przesuniecieX+30, 25, 100, 150);
        board.add(kartaLabel,0);

        graczValue.setText(String.valueOf(gracz.taliaValue()));
        board.revalidate();
        board.repaint();
    }


    public class HitListener implements ActionListener{
        private final int nrGracza;
        public HitListener(int nrGracza){
            this.nrGracza = nrGracza;
        }
        @Override
        public void actionPerformed(ActionEvent e) {
            GraczCiagnie(nrGracza);
            if(graczeList.get(nrGracza).taliaValue() >= 21){
                hitButtonsList.get(nrGracza).setEnabled(false);
                standButtonsList.get(nrGracza).setEnabled(false);
                nastepnyGracz();
            }
        }
    }
    public class StandListener implements ActionListener{
        private final int nrGracza;
        public StandListener(int nrGracza){
            this.nrGracza = nrGracza;
        }
        @Override
        public void actionPerformed(ActionEvent e) {
            hitButtonsList.get(nrGracza).setEnabled(false);
            standButtonsList.get(nrGracza).setEnabled(false);
            nastepnyGracz();
        }
    }
    public void nastepnyGracz(){
        turaGracza++;
        if(turaGracza<liczbaGraczy){
            if(graczeList.get(turaGracza).taliaValue()==21){
                nastepnyGracz();
            }
            int aktualnyGracz = turaGracza+1;
            informacjeJLabel.setText("Tura gracza " + aktualnyGracz);
            hitButtonsList.get(turaGracza).setEnabled(true);
            standButtonsList.get(turaGracza).setEnabled(true);
        }else{
            informacjeJLabel.setText("Tura krupiera");
            ruchKrupiera();
        }
    }

    public void ruchKrupiera() {
        ImageIcon przeskalowanaKarta = ObslugaZdjec.dodawanieZdjecia(kartaDoOdkrycia,talia);
        JLabel kartaLabel = new JLabel(przeskalowanaKarta);
        krupierBoardCards.remove(0);
        krupierBoardCards.add(kartaLabel,0);
        krupierBoardCards.revalidate();
        krupierBoardCards.repaint();
        krupierValue.setText(String.valueOf(krupier.taliaValue()));
        Timer krupierTimer = new Timer(2000, null);
        krupierTimer.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (krupier.taliaValue() < 17) {
                    krupierCiagnie();
                } else {
                    ((Timer) e.getSource()).stop();
                    checkWin();
                }
            }
        });
        krupierTimer.setInitialDelay(2000);
        krupierTimer.start();
    }

    public void checkWin() {
        int krupierWartosc = krupier.taliaValue();

        for(int i = 0; i<liczbaGraczy; i++){
            Gracz gracz = graczeList.get(i);
            JLabel graczValue = graczValueLabelList.get(i);
            int graczWartosc = gracz.taliaValue();
            if(graczWartosc>21 || graczWartosc<krupierWartosc && (krupier.taliaValue()>16 && krupier.taliaValue()<22)){
                graczValue.setText("PRZEGRAŁEŚ");
                gracz.clearCurrentBet();
            }else if(krupierWartosc>21 && graczWartosc<22 || graczWartosc>krupierWartosc && krupier.taliaValue()>16){
                graczValue.setText("WYGRAŁEŚ!");
                if(graczWartosc==21){
                    gracz.addMoney(gracz.getCurrentBet()*3);
                }else{
                    gracz.addMoney(gracz.getCurrentBet()*2);
                    gracz.clearCurrentBet();
                }
            }
            if(krupierWartosc==graczWartosc && krupierWartosc<22){
                graczValue.setText("REMIS!");
                gracz.addMoney(gracz.getCurrentBet());
                gracz.clearCurrentBet();
            }
        }
        newRoundButton.setEnabled(true);
    }
}
