import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class Dodawanie {
    private final ArrayList<Gracz> graczeList = new ArrayList<>();
    private final ArrayList<JPanel> graczBoardsList = new ArrayList<>();
    private final ArrayList<JPanel> graczBoardsCardsList = new ArrayList<>();
    private final ArrayList<JLabel> graczValueLabelList = new ArrayList<>();
    private final ArrayList<JButton> hitButtonsList = new ArrayList<>();
    private final ArrayList<JButton> standButtonsList = new ArrayList<>();
    private final ArrayList<JLabel> moneyLabelList = new ArrayList<>();
    private final ArrayList<JLabel> BetLabelList = new ArrayList<>();
    private JPanel playersBoard;
    public void dodawanieGraczy(int i){
        Gracz gracz = new Gracz();
        graczeList.add(gracz);
        Border border = BorderFactory.createLineBorder(Color.BLACK, 1);
        JPanel graczBoard = new JPanel(new BorderLayout());
        graczBoard.setPreferredSize(new Dimension(400, 380));

        //KARTY
        JPanel graczBoardCards = new JPanel(null);
        graczBoardCards.setPreferredSize(new Dimension(150, 160));
        graczBoardCards.setBorder(border);

        graczBoard.add(graczBoardCards, BorderLayout.CENTER);

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
        bottomPanel.add(graczInfoPanel, BorderLayout.NORTH);


        //buttons
        JPanel graczBoardButtons = new JPanel(new FlowLayout(FlowLayout.CENTER));
        graczBoardButtons.setBorder(border);
        JButton hit = new JButton("HIT");
        hit.setPreferredSize(new Dimension(150, 50));
        graczBoardButtons.add(hit);
//        hit.addActionListener(new PrzebiegGry.HitListener(i));

        JButton stand = new JButton("STAND");
        stand.setPreferredSize(new Dimension(150, 50));
        graczBoardButtons.add(stand);
//        stand.addActionListener(new PrzebiegGry.StandListener(i));

        bottomPanel.add(graczBoardButtons, BorderLayout.SOUTH);

        graczBoard.add(bottomPanel, BorderLayout.SOUTH);
        graczBoard.setBorder(border);
        playersBoard.add(graczBoard);

        //Listy
        moneyLabelList.add(moneyLabel);
        BetLabelList.add(BetLabel);
        graczBoardsCardsList.add(graczBoardCards);
        graczBoardsList.add(graczBoard);
        hitButtonsList.add(hit);
        standButtonsList.add(stand);
        graczValueLabelList.add(graczValue);

        //stany
        hit.setEnabled(false);
        stand.setEnabled(false);
    }

}
