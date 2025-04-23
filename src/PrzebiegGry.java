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
    private JButton newRoundButton;
    private final ArrayList<Gracz> graczeList = new ArrayList<>();
//    private final ArrayList<JPanel> graczBoardsList = new ArrayList<>();
    private final ArrayList<JPanel> graczBoardsCardsList = new ArrayList<>();
    private final ArrayList<JLabel> graczValueLabelList = new ArrayList<>();
    private final ArrayList<JLabel> graczValue2LabelList = new ArrayList<>();
    private final ArrayList<JButton> hitButtonsList = new ArrayList<>();
    private final ArrayList<JButton> splitButtonsList = new ArrayList<>();
    private final ArrayList<JButton> standButtonsList = new ArrayList<>();
    private final ArrayList<JButton> hit2ButtonsList = new ArrayList<>();
    private final ArrayList<JButton> stand2ButtonsList = new ArrayList<>();
    private final ArrayList<JLabel> moneyLabelList = new ArrayList<>();
    private final ArrayList<JLabel> BetLabelList = new ArrayList<>();
    private final ArrayList<JLabel> Bet2LabelList = new ArrayList<>();
    private final ArrayList<Component> spacerList = new ArrayList<>();

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
            gracz.clearTaliaKartGracza2();
        }
        for (JPanel panel : graczBoardsCardsList){
            panel.removeAll();
            panel.revalidate();
            panel.repaint();
        }
        for (JLabel valueLabel : graczValueLabelList){
            valueLabel.setText("0");
        }
        for (JLabel valueLabel : graczValue2LabelList){
            valueLabel.setText("0");
            valueLabel.setVisible(false);
        }
        for(JLabel betValueLabel : BetLabelList){
            betValueLabel.setText("Bet: 0 zł");
        }
        for(JLabel betValueLabel : Bet2LabelList){
            betValueLabel.setText("Bet: 0 zł");
            betValueLabel.setVisible(false);
        }

        for (JButton przycisk : hit2ButtonsList){
            przycisk.setVisible(false);
        }
        for (JButton przycisk : stand2ButtonsList){
            przycisk.setVisible(false);
        }
        for (JButton przycisk : hitButtonsList){
            przycisk.setPreferredSize(new Dimension(100,50));
        }
        for (JButton przycisk : standButtonsList){
            przycisk.setPreferredSize(new Dimension(100,50));
        }
        for (JButton przycisk : splitButtonsList){
            przycisk.setVisible(true);
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
        JPanel graczValuePanel = new JPanel();
        JLabel graczValue = new JLabel(String.valueOf(0), JLabel.CENTER);
        graczValue.setFont(new Font("serif", Font.BOLD, 30));
        JLabel graczValue2 = new JLabel(String.valueOf(0), JLabel.CENTER);
        graczValue2.setFont(new Font("serif", Font.BOLD, 30));
        graczValue2.setVisible(false);

        JPanel betPanel = new JPanel();
        JLabel betLabel = new JLabel("Bet: " + gracz.getCurrentBet() + " zł", JLabel.CENTER);
        betLabel.setFont(new Font("serif", Font.BOLD, 20));
        JLabel bet2Label = new JLabel("Bet: " + gracz.getCurrentBet2() + " zł", JLabel.CENTER);
        bet2Label.setFont(new Font("serif", Font.BOLD, 20));
        bet2Label.setVisible(false);

        JLabel moneyLabel = new JLabel("Stan konta: " + gracz.getMoney() + " zł", JLabel.CENTER);
        moneyLabel.setFont(new Font("serif", Font.BOLD, 20));

        Component spacer = Box.createRigidArea(new Dimension(160, 0)); // odstęp
        spacer.setVisible(false);
        betPanel.add(betLabel);
        betPanel.add(spacer);
        betPanel.add(bet2Label);

        graczValuePanel.add(graczValue);
        graczValuePanel.add(spacer);
        graczValuePanel.add(graczValue2);

        graczInfoPanel.add(graczValuePanel);
        graczInfoPanel.add(betPanel);
        graczInfoPanel.add(moneyLabel);

        //buttons
        JPanel graczBoardButtons = new JPanel(new FlowLayout(FlowLayout.CENTER));
        graczBoardButtons.setBorder(border);

        JButton hit = new JButton("HIT");
        hit.setPreferredSize(new Dimension(100, 50));
        graczBoardButtons.add(hit);
        hit.addActionListener(new HitListener(i));

        JButton stand = new JButton("STAND");
        stand.setPreferredSize(new Dimension(100, 50));
        graczBoardButtons.add(stand);
        stand.addActionListener(new StandListener(i));

        JButton split = new JButton("SPLIT");
        split.setPreferredSize(new Dimension(100, 50));
        graczBoardButtons.add(split);
        split.addActionListener(new SplitListener(i));

        JButton hit2 = new JButton("HIT");
        hit2.setPreferredSize(new Dimension(70, 50));
        hit2.setVisible(false);
        graczBoardButtons.add(hit2);
        hit2.addActionListener(new Hit2Listener(i));

        JButton stand2 = new JButton("STAND");
        stand2.setPreferredSize(new Dimension(85, 50));
        stand2.setVisible(false);
        graczBoardButtons.add(stand2);
        stand2.addActionListener(new Stand2Listener(i));

        bottomPanel.add(graczInfoPanel, BorderLayout.NORTH);
        bottomPanel.add(graczBoardButtons, BorderLayout.SOUTH);

        graczBoard.add(graczBoardCards, BorderLayout.CENTER);
        graczBoard.add(bottomPanel, BorderLayout.SOUTH);

        playersBoard.add(graczBoard);

        //Listy
        moneyLabelList.add(moneyLabel);
        BetLabelList.add(betLabel);
        Bet2LabelList.add(bet2Label);
        spacerList.add(spacer);
        graczBoardsCardsList.add(graczBoardCards);
//        graczBoardsList.add(graczBoard);
        hitButtonsList.add(hit);
        standButtonsList.add(stand);
        splitButtonsList.add(split);
        hit2ButtonsList.add(hit2);
        stand2ButtonsList.add(stand2);
        graczValueLabelList.add(graczValue);
        graczValue2LabelList.add(graczValue2);
        //stany
        hit.setEnabled(false);
        hit2.setEnabled(false);
        stand.setEnabled(false);
        stand2.setEnabled(false);
        split.setEnabled(false);
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
                    GraczCiagnie(i,1);
                }else if (i == liczbaGraczy){
                    krupierCiagnie();
                }else if (i < 2 * liczbaGraczy + 1){
                    GraczCiagnie(i-liczbaGraczy-1,1);
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

                        if(graczeList.get(0).checkCardValue(0)==graczeList.get(0).checkCardValue(1) && checkSplit()){
                            splitButtonsList.get(0).setEnabled(true);
                        }
                    }
                    ((Timer) e.getSource()).stop();
                }
                i++;
            }
        });
        timer.setInitialDelay(1000);
        timer.start();

    }
    public void rozdawanieKart2() {
        Timer timer = new Timer(1000, null);
        timer.addActionListener(new ActionListener() {
            int i = 0;
            @Override
            public void actionPerformed(ActionEvent e) {
                informacjeJLabel.setText("Rozdawanie Kart");
                if(i ==1){
                    GraczCiagnie(turaGracza,1);
                }else if (i == 2){
                    GraczCiagnie(turaGracza,2);
                    hit2ButtonsList.get(turaGracza).setEnabled(true);
                    stand2ButtonsList.get(turaGracza).setEnabled(true);
                    informacjeJLabel.setText("Tura gracza " + turaGracza);
                    ((Timer) e.getSource()).stop();
                }
                i++;
            }
        });
        timer.setInitialDelay(1000);
        timer.start();

    }
    public boolean checkSplit(){
        if(graczeList.get(turaGracza).getMoney()-graczeList.get(turaGracza).getCurrentBet()*2>=0){
            return true;
        }
        return false;
    }
    public void krupierCiagnie() {

        JLabel kartaLabel2;
        krupier.ciagnijKarte(talia.usunKarte());
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

    public void GraczCiagnie(int nrGracza,int aktualnaReka) {
        if(aktualnaReka==1){
            Gracz gracz = graczeList.get(nrGracza);
            JPanel board = graczBoardsCardsList.get(nrGracza);
            JLabel graczValue = graczValueLabelList.get(nrGracza);

            int przesuniecieX = gracz.iloscKart() * 20;
            gracz.ciagnijKarte(talia.usunKarte());
            ImageIcon przeskalowanaKarta = ObslugaZdjec.dodawanieZdjecia(gracz.ostatniaKarta(),talia);
            JLabel kartaLabel = new JLabel(przeskalowanaKarta);

            kartaLabel.setBounds(przesuniecieX+10, 10, 100, 150);
            board.add(kartaLabel,0);

            graczValue.setText(String.valueOf(gracz.taliaValue()));
            board.revalidate();
            board.repaint();
        }else{
            Gracz gracz = graczeList.get(nrGracza);
            JPanel board = graczBoardsCardsList.get(nrGracza);
            JLabel graczValue = graczValue2LabelList.get(nrGracza);

            gracz.ciagnijKarte2(talia.usunKarte());
            int przesuniecieX = gracz.iloscKart2() * 20+170;
            ImageIcon przeskalowanaKarta = ObslugaZdjec.dodawanieZdjecia(gracz.ostatniaKarta2(),talia);
            JLabel kartaLabel = new JLabel(przeskalowanaKarta);

            kartaLabel.setBounds(przesuniecieX+10, 10, 100, 150);
            board.add(kartaLabel,0);

            graczValue.setText(String.valueOf(gracz.taliaValue2()));
            board.revalidate();
            board.repaint();
        }

    }
    public class Hit2Listener implements ActionListener{
        private final int nrGracza;
        public Hit2Listener(int nrGracza){
            this.nrGracza = nrGracza;
        }
        @Override
        public void actionPerformed(ActionEvent e) {
            GraczCiagnie(nrGracza,2);
            if(graczeList.get(nrGracza).taliaValue2() >= 21){
                hit2ButtonsList.get(nrGracza).setEnabled(false);
                stand2ButtonsList.get(nrGracza).setEnabled(false);
                hitButtonsList.get(nrGracza).setEnabled(true);
                standButtonsList.get(nrGracza).setEnabled(true);
            }
        }
    }
    public class Stand2Listener implements ActionListener{
        private final int nrGracza;
        public Stand2Listener(int nrGracza){
            this.nrGracza = nrGracza;
        }
        @Override
        public void actionPerformed(ActionEvent e) {
            hit2ButtonsList.get(nrGracza).setEnabled(false);
            stand2ButtonsList.get(nrGracza).setEnabled(false);
            hitButtonsList.get(nrGracza).setEnabled(true);
            standButtonsList.get(nrGracza).setEnabled(true);
        }
    }
    public class HitListener implements ActionListener{
        private final int nrGracza;
        public HitListener(int nrGracza){
            this.nrGracza = nrGracza;
        }
        @Override
        public void actionPerformed(ActionEvent e) {
            splitButtonsList.get(nrGracza).setEnabled(false);
            GraczCiagnie(nrGracza,1);
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
            splitButtonsList.get(nrGracza).setEnabled(false);
            hit2ButtonsList.get(nrGracza).setEnabled(false);
            stand2ButtonsList.get(nrGracza).setEnabled(false);
            nastepnyGracz();
        }
    }
    public class SplitListener implements ActionListener{
        private final int nrGracza;
        public SplitListener(int nrGracza){
            this.nrGracza = nrGracza;
        }
        @Override
        public void actionPerformed(ActionEvent e) {
            Gracz gracz = graczeList.get(nrGracza);
            splitButtonsList.get(nrGracza).setEnabled(false);
            hitButtonsList.get(nrGracza).setEnabled(false);
            standButtonsList.get(nrGracza).setEnabled(false);
            hitButtonsList.get(nrGracza).setPreferredSize(new Dimension(70,50));
            standButtonsList.get(nrGracza).setPreferredSize(new Dimension(85,50));
            splitButtonsList.get(nrGracza).setVisible(false);
            hit2ButtonsList.get(nrGracza).setVisible(true);
            stand2ButtonsList.get(nrGracza).setVisible(true);

            gracz.splitTalia();
            graczValueLabelList.get(nrGracza).setText(String.valueOf(gracz.taliaValue()));
            graczValue2LabelList.get(nrGracza).setText(String.valueOf(gracz.taliaValue2()));
            graczValue2LabelList.get(nrGracza).setVisible(true);
            spacerList.get(nrGracza).setVisible(true);
            graczBoardsCardsList.get(nrGracza).remove(0);
            ImageIcon przeskalowanaKarta = ObslugaZdjec.dodawanieZdjecia(gracz.ostatniaKarta2(),talia);
            JLabel kartaLabel = new JLabel(przeskalowanaKarta);
            int przesuniecieX = gracz.iloscKart2() * 20+170;
            kartaLabel.setBounds(przesuniecieX+10, 10, 100, 150);
            graczBoardsCardsList.get(nrGracza).add(kartaLabel,0);
            int bet = graczeList.get(nrGracza).getCurrentBet();
            gracz.odejmijMoney(bet);
            graczeList.get(nrGracza).setCurrentBet2(bet);

            moneyLabelList.get(nrGracza).setText("Stan konta: " + gracz.getMoney() + " zł");
            Bet2LabelList.get(nrGracza).setText("Bet: " + gracz.getCurrentBet2() + " zł");
            Bet2LabelList.get(nrGracza).setVisible(true);
            playersBoard.revalidate();
            playersBoard.repaint();
            rozdawanieKart2();

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
            if(graczeList.get(turaGracza).checkCardValue(0)==graczeList.get(turaGracza).checkCardValue(1) && checkSplit()){
                splitButtonsList.get(turaGracza).setEnabled(true);
            }
        }else{
            informacjeJLabel.setText("Tura krupiera");
            ruchKrupiera();
        }
    }

    public void ruchKrupiera() {
        ImageIcon przeskalowanaKarta = ObslugaZdjec.dodawanieZdjecia(krupier.getTalia().get(0),talia);
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
                graczValue.setText("Przegrałeś");
                gracz.clearCurrentBet();
            }else if(krupierWartosc>21 && graczWartosc<22 || graczWartosc>krupierWartosc && krupier.taliaValue()>16){
                graczValue.setText("Wygrałeś");
                if(graczWartosc==21){
                    gracz.addMoney(gracz.getCurrentBet()*3);
                    gracz.clearCurrentBet();
                }else{
                    gracz.addMoney(gracz.getCurrentBet()*2);
                    gracz.clearCurrentBet();
                }
            }
            if(krupierWartosc==graczWartosc && krupierWartosc<22){
                graczValue.setText("Remis");
                gracz.addMoney(gracz.getCurrentBet());
                gracz.clearCurrentBet();
            }
        }
        //split
        for(int i = 0; i<liczbaGraczy; i++){
            Gracz gracz = graczeList.get(i);
            JLabel graczValue = graczValue2LabelList.get(i);
            spacerList.get(i).setVisible(false);
            int graczWartosc = gracz.taliaValue2();
            if(graczWartosc>21 || graczWartosc<krupierWartosc && (krupier.taliaValue()>16 && krupier.taliaValue()<22)){
                graczValue.setText("Przegrałeś");
                gracz.clearCurrentBet2();
            }else if(krupierWartosc>21 && graczWartosc<22 || graczWartosc>krupierWartosc && krupier.taliaValue()>16){
                graczValue.setText("Wygrałeś!");
                if(graczWartosc==21){
                    gracz.addMoney(gracz.getCurrentBet2()*3);
                    gracz.clearCurrentBet2();
                }else{
                    gracz.addMoney(gracz.getCurrentBet2()*2);
                    gracz.clearCurrentBet2();
                }
            }
            if(krupierWartosc==graczWartosc && krupierWartosc<22){
                graczValue.setText("Remis!");
                gracz.addMoney(gracz.getCurrentBet2());
                gracz.clearCurrentBet2();
            }
        }
        int i = 0;
        for(Gracz gracz : graczeList){
            moneyLabelList.get(i).setText("Stan konta: " + gracz.getMoney() + " zł");
            i++;
        }
        newRoundButton.setEnabled(true);
    }
}
