package cs6015.casino.poker;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.GroupLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import cs6015.casino.common.MasterView;


@SuppressWarnings("serial")
public class PokerView extends MasterView{
	
	private JButton betButton;
	private JButton foldButton;
    private JButton checkButton;	
    private JButton quitButton;
    
    private JPanel pokerTablePanel;
    private JPanel player1Panel;
    private JPanel player2Panel;
    private JPanel player3Panel;
    
    private GroupLayout pokerTableLayout; 
    private GroupLayout player1Layout;
    private GroupLayout player2Layout;
    private GroupLayout player3Layout;
    
    private JLabel tableCard1;
    private JLabel tableCard2;
    private JLabel tableCard3;
    private JLabel tableCard4;
    private JLabel tableCard5;
    
    private JLabel Player1_FirstCard;
    private JLabel Player1_SecondCard;
    private JLabel Player2_FirstCard;
    private JLabel Player2_SecondCard;
    private JLabel Player3_FirstCard;
    private JLabel Player3_SecondCard;

    private JLabel Player1_Name;
    private JLabel Player2_Name;
    private JLabel Player3_Name;
    
    private JLabel Player1_Money;
    private JLabel Player2_Money;
    private JLabel Player3_Money;
    
    private JLabel potMoney;
    private JLabel potName;
    private JLabel pokerTableTitle;
    
    private JLabel messageLabel;
    private JLabel welcomeLabel;
    private JLabel userNameLabel;
    private JLabel winLabel;
    private JLabel accountMessageLabel;
    private JLabel accountMoneyLabel;
    
    private JTextField betTextField;
    private final PokerController controller;
    
	public PokerView(PokerController pokerController)
	{
		super();
		controller = pokerController;
		init();
	}
	
	private void init()
	{
		ButtonActionListener listener = new ButtonActionListener();
		betButton = createButton("BET", listener, true);
		foldButton = createButton("FOLD", listener, true);
		checkButton = createButton("CHECK", listener, true);
		quitButton = createButton("QUIT", listener, true);
		betTextField = new JTextField("20", 7);
		
		setUpButtonPanel();
		
		tableCard1 = createImageLabel();
	    tableCard2 = createImageLabel();
	    tableCard3 = createImageLabel();
	    tableCard4 = createImageLabel();
	    tableCard5 = createImageLabel();
	    
	    Player1_FirstCard = createImageLabel();
	    Player1_SecondCard = createImageLabel();
	    Player2_FirstCard = createImageLabel();
	    Player2_SecondCard = createImageLabel();
	    Player3_FirstCard = createImageLabel();
	    Player3_SecondCard = createImageLabel();
	   
	    Player1_Name = createLabel("Player-1", 20);
	    Player2_Name = createLabel("Player-2", 20);
	    Player3_Name = createLabel("Player-3", 20);
	    
	    Player1_Money = createLabel("Money:", 20);
	    Player2_Money = createLabel("Money:", 20);
	    Player3_Money = createLabel("Money:", 20);
	    
	    potMoney = createLabel("0", 24);
	    potName = createLabel("POT MONEY:", 24);
	    pokerTableTitle = createLabel("POKER TABLE", 36);
		
	    pokerTablePanel = new JPanel();
		pokerTableLayout = new GroupLayout(pokerTablePanel);
		pokerTablePanel.setLayout(pokerTableLayout);
		pokerTablePanel.setBackground(TABLE_PANEL_COLOR);
		pokerTablePanel.setBorder(blackLineBorder);
		
		player1Panel = new JPanel();
		player1Layout = new GroupLayout(player1Panel);
		player1Panel.setLayout(player1Layout);
		player1Panel.setBackground(TABLE_PANEL_COLOR);
		player1Panel.setBorder(blackLineBorder);
		
		player2Panel = new JPanel();
		player2Layout = new GroupLayout(player2Panel);
		player2Panel.setLayout(player2Layout);
		player2Panel.setBackground(TABLE_PANEL_COLOR);
		player2Panel.setBorder(blackLineBorder);
		
		player3Panel = new JPanel();
		player3Layout = new GroupLayout(player3Panel);
		player3Panel.setLayout(player3Layout);
		player3Panel.setBackground(TABLE_PANEL_COLOR);
		
		player3Panel.setBorder(blackLineBorder);
		
		messageLabel = createLabel("", 24);
	    welcomeLabel = createLabel("Welcome,", 24);
	    userNameLabel = createLabel("", 24);
	    winLabel = createLabel("", 24);
	    accountMessageLabel = createLabel("Balance:", 24);
	    accountMoneyLabel = createLabel("", 24);

		setUpTablePanel();
		
	}
	
	public void updateButtonStatus(boolean betButtonVisibility, boolean foldButtonVisibility, boolean checkButtonVisibility, boolean betTextFieldVisibility)
	{
		betButton.setEnabled(betButtonVisibility);
		foldButton.setEnabled(foldButtonVisibility);
		checkButton.setEnabled(checkButtonVisibility);
		betTextField.setEnabled(betTextFieldVisibility);
	}
	
	/*************Setters for card images**************/
	
	
	public void setTableCard1(ImageIcon icon) {
		this.tableCard1.setIcon(icon); 
	}

	public void setBetTextField(int value) {
		this.betTextField.setText(Integer.toString(value));
	}

	public void setTableCard2(ImageIcon icon) {
		this.tableCard2.setIcon(icon);
	}

	public void setTableCard3(ImageIcon icon) {
		this.tableCard3.setIcon(icon); 
	}

	public void setTableCard4(ImageIcon icon) {
		this.tableCard4.setIcon(icon); 
	}

	public void setTableCard5(ImageIcon icon) {
		this.tableCard5.setIcon(icon);
	}

	public void setPlayer1_FirstCard(ImageIcon icon) {
		Player1_FirstCard.setIcon(icon);
	}

	public void setPlayer1_SecondCard(ImageIcon icon) {
		Player1_SecondCard.setIcon(icon); 
	}

	public void setPlayer2_FirstCard(ImageIcon icon) {
		Player2_FirstCard.setIcon(icon); 
	}

	public void setPlayer2_SecondCard(ImageIcon icon) {
		Player2_SecondCard.setIcon(icon); 
	}

	public void setPlayer3_FirstCard(ImageIcon icon) {
		Player3_FirstCard.setIcon(icon); 
	}

	public void setPlayer3_SecondCard(ImageIcon icon) {
		Player3_SecondCard.setIcon(icon); 
	}

	/*************End**************/
	
	public void setPlayer1_Name(String player1_Name) {
		Player1_Name.setText(player1_Name);
	}

	public void setPlayer2_Name(String player2_Name) {
		Player2_Name.setText(player2_Name);
	}

	public void setPlayer3_Name(String player3_Name) {
		Player3_Name.setText(player3_Name);
	}

	public void setPlayer1_Money(String player1_Money) {
		Player1_Money.setText("Money: "+player1_Money);
	}

	public void setPlayer2_Money(String player2_Money) {
		Player2_Money.setText("Money: "+player2_Money);
	}

	public void setPlayer3_Money(String player3_Money) {
		if(player3_Money.equals("1000999"))
			Player3_Money.setText("Folded");
		else
			Player3_Money.setText("Money: "+player3_Money);
	}

	public void setPotMoney(String potMoney) {
		this.potMoney.setText(potMoney);
	}

	public void setMessageLabel(String messageLabel) {
		this.messageLabel.setText(messageLabel);
	}

	public void setUserNameLabel(String userNameLabel) {
		this.userNameLabel.setText(userNameLabel);
	}

	public void setWinLabel(String winLabel) {
		this.winLabel.setText(winLabel);
	}

	public void setAccountMoneyLabel(String accountMoneyLabel) {
		this.accountMoneyLabel.setText(accountMoneyLabel);
	}

	public void setUpButtonPanel()
	{
		buttonPanelLayout.setHorizontalGroup(buttonPanelLayout.createParallelGroup()
				.addGroup(buttonPanelLayout.createSequentialGroup()
						.addGap(100))
				.addGroup(buttonPanelLayout.createSequentialGroup()
						.addContainerGap(50, Short.MAX_VALUE)
							.addComponent(foldButton, 10, 100, 100)
								.addGap(50)
									.addComponent(checkButton, 10, 100, 100)
										.addContainerGap(50, Short.MAX_VALUE))
				.addGroup(GroupLayout.Alignment.CENTER, buttonPanelLayout.createSequentialGroup()
						.addContainerGap(50, Short.MAX_VALUE)
							.addComponent(betButton, 10, 100, 100)
								.addContainerGap(50, Short.MAX_VALUE))
				.addGroup(GroupLayout.Alignment.CENTER, buttonPanelLayout.createSequentialGroup()
						.addContainerGap(50, Short.MAX_VALUE)
							.addComponent(betTextField, 10, 200, 200)
								.addContainerGap(50, Short.MAX_VALUE))
				.addGroup(GroupLayout.Alignment.CENTER, buttonPanelLayout.createSequentialGroup()
						.addContainerGap(50, Short.MAX_VALUE)
							.addComponent(quitButton, 10, 100, 100)
								.addContainerGap(50, Short.MAX_VALUE)));
		
		buttonPanelLayout.setVerticalGroup(buttonPanelLayout.createSequentialGroup()
				.addContainerGap(250, Short.MAX_VALUE)
				.addGroup(buttonPanelLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
						.addComponent(foldButton, 10, 50, 50)
							.addComponent(checkButton, 10, 50, 50)
								.addGap(80))
				.addGroup(buttonPanelLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
						.addGap(50)
							.addComponent(betButton, 10, 50, 50))
				.addGroup(buttonPanelLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
						.addGap(80)
							.addComponent(betTextField, 10, 30, 30))
				.addGroup(buttonPanelLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
						.addComponent(quitButton, 10, 50, 50))
				.addContainerGap(250, Short.MAX_VALUE));
	}
	
	private void setUpPokerTable()
	{

		pokerTableLayout.setHorizontalGroup(pokerTableLayout.createParallelGroup()
				.addGroup(pokerTableLayout.createSequentialGroup()	
						.addContainerGap(5, Short.MAX_VALUE)
							.addComponent(pokerTableTitle, 0, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE)
								.addContainerGap(5, Short.MAX_VALUE))
					.addGroup(pokerTableLayout.createSequentialGroup()
						.addContainerGap(5, Short.MAX_VALUE)
						.addGap(10).addComponent(tableCard1, 0, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE)
							.addGap(10).addComponent(tableCard2, 0, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE)
								.addGap(10).addComponent(tableCard3, 0, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE)
									.addGap(10).addComponent(tableCard4, 0, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE)
										.addGap(10).addComponent(tableCard5, 0, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE)
										   .addContainerGap(5, Short.MAX_VALUE))
					.addGroup(pokerTableLayout.createSequentialGroup()
							.addContainerGap(5, Short.MAX_VALUE)
							.addComponent(potName, 0, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE)
								.addGap(5)
									.addComponent(potMoney, 0, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE)
										.addContainerGap(5, Short.MAX_VALUE)));
		
		
		pokerTableLayout.setVerticalGroup(pokerTableLayout.createSequentialGroup()
					.addContainerGap(10, Short.MAX_VALUE)
					.addComponent(pokerTableTitle, 0, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE)
					.addGap(30)
					.addGroup(pokerTableLayout.createParallelGroup()
						.addComponent(tableCard1, 0, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE)
							.addComponent(tableCard2, 0, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE)
								.addComponent(tableCard3, 0, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE)
									.addComponent(tableCard4, 0, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE)
										.addComponent(tableCard5, 0, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE))
					.addGap(30)
					.addGroup(pokerTableLayout.createParallelGroup()
							.addComponent(potName, 0, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE)
									.addComponent(potMoney, 0, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE))
					.addContainerGap(10, Short.MAX_VALUE));
		
		createPlayerLayout(player1Layout, Player1_Name, Player1_Money, Player1_FirstCard, Player1_SecondCard);
		createPlayerLayout(player2Layout, Player2_Name, Player2_Money, Player2_FirstCard, Player2_SecondCard);
		createPlayerLayout(player3Layout, Player3_Name, Player3_Money, Player3_FirstCard, Player3_SecondCard);
	}
	
	private void createPlayerLayout(GroupLayout playerLayout, JLabel playerName, JLabel playerMoney, JLabel playerFirstCard, JLabel playerSecondCard)
	{
		
		playerLayout.setHorizontalGroup(playerLayout.createParallelGroup()
				.addGroup(playerLayout.createSequentialGroup()
						.addContainerGap(3, 3)
						.addComponent(playerName, 0, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE)
						.addGap(10)
						.addComponent(playerMoney, 0, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE)
						.addContainerGap(3, 3))
				.addGroup(playerLayout.createSequentialGroup()
						.addContainerGap(10, 10)
						.addComponent(playerFirstCard, 0, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE)
						.addGap(20)
						.addComponent(playerSecondCard, 0, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE)
						.addContainerGap(3, 3)));
		
		playerLayout.setVerticalGroup(playerLayout.createSequentialGroup()
				.addContainerGap(10, 10)
				.addGroup(playerLayout.createParallelGroup(GroupLayout.Alignment.CENTER)
						.addComponent(playerName, 0, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE)
						.addComponent(playerMoney, 0, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE))
				.addGap(20)
				.addGroup(playerLayout.createParallelGroup(GroupLayout.Alignment.CENTER)
						.addComponent(playerFirstCard, 0, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE)
						.addComponent(playerSecondCard, 0, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE))
						.addContainerGap(10, 10));
	}
	
	public void setUpTablePanel()
	{
		setUpPokerTable();
		tablePanelLayout.setHorizontalGroup(tablePanelLayout.createSequentialGroup()
				.addContainerGap(10, Short.MAX_VALUE)
				.addGroup(tablePanelLayout.createParallelGroup(GroupLayout.Alignment.CENTER)
						.addGroup(tablePanelLayout.createSequentialGroup()
							.addComponent(welcomeLabel) .addGap(10) .addComponent(userNameLabel))
						.addComponent(player2Panel))
				.addGap(150)	
				.addGroup(tablePanelLayout.createParallelGroup(GroupLayout.Alignment.CENTER)
					.addComponent(player1Panel)
						.addComponent(pokerTablePanel)
							.addComponent(winLabel)
								.addComponent(messageLabel))
				.addGap(150)	
				.addGroup(tablePanelLayout.createParallelGroup(GroupLayout.Alignment.CENTER)
						.addGroup(tablePanelLayout.createSequentialGroup()
							.addComponent(accountMessageLabel) .addGap(10) .addComponent(accountMoneyLabel))
						.addComponent(player3Panel))
				.addContainerGap(30, Short.MAX_VALUE));
		
		tablePanelLayout.setVerticalGroup(tablePanelLayout.createSequentialGroup()
				.addContainerGap(10, 30)
				.addGroup(tablePanelLayout.createParallelGroup(GroupLayout.Alignment.CENTER, true)
						.addComponent(winLabel)
							.addComponent(welcomeLabel)
								.addComponent(userNameLabel)
								.addComponent(accountMessageLabel) .addComponent(accountMoneyLabel))
				
				.addGap(100)
				.addComponent(messageLabel)
				.addGap(40)
				.addGroup(tablePanelLayout.createParallelGroup(GroupLayout.Alignment.CENTER)
					.addComponent(player2Panel)	
					.addGap(50)	
					.addComponent(pokerTablePanel)
					.addGap(50)
					.addComponent(player3Panel))
				.addGap(100)
				.addComponent(player1Panel)
				.addContainerGap(50, 50));
	}
	
	
	
	/*
	 * Private Class - ButtonActionListener
	 */
	private class ButtonActionListener implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			Object src = e.getSource();
			
			if(src.equals(betButton))
			{
				int bet = Integer.valueOf(betTextField.getText());
				if(bet < PokerView.this.controller.minimumBet)
				{
					PokerView.this.setMessageLabel("The Minimum bet is "+ PokerView.this.controller.minimumBet + "Please Bet again.");
					return;
				}
				else if(bet < 1){
					PokerView.this.setMessageLabel("Bet cannot be zero. Please Bet again.");
					return;
				}
				PokerView.this.controller.sendBetMoney(bet);
			}
			else if(src.equals(checkButton))
			{
				PokerView.this.controller.sendBetMoney(PokerView.this.controller.minimumBet);
			}
			else if(src.equals(foldButton))
			{
				PokerView.this.controller.fold();
			}
			else if(src.equals(quitButton))
			{
				PokerView.this.controller.quit(PokerView.this);
			}
			
		}
	}

	@Override
	public void closingAction() {
		this.controller.closingAction();		
	}
}






