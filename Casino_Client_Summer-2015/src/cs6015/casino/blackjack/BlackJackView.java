package cs6015.casino.blackjack;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.GroupLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import cs6015.casino.common.MasterView;
import cs6015.casino.serializables.BlackJackMessage.BlackJackEnum;


@SuppressWarnings("serial")
public class BlackJackView extends MasterView{

	private JButton betButton;
	private JButton surrenderButton;
	private JButton hitButton;	
	private JButton doubleButton;
	private JButton standButton;
	private JButton quitButton;
	private JButton newGameButton;

	private JPanel pokerTablePanel;
	private JPanel player1Panel;
	private GroupLayout pokerTableLayout; 
	private GroupLayout player1Layout;
	private JLabel tableCard1;
	private JLabel tableCard2;
	private JLabel tableCard3;
	private JLabel tableCard4;
	private JLabel tableCard5;

	private JLabel Player1_FirstCard;
	private JLabel Player1_SecondCard;
	private JLabel Player1_ThirdCard;
	private JLabel Player1_FourthCard;
	private JLabel Player1_FifthCard;

	private JLabel Player1_Name;
	private JLabel Player1_Money;

	private JLabel blackJackPanelTitle;
	private JLabel potMoney;
	private JLabel potName;
	private JLabel blackJackTableTitle;

	private JLabel messageLabel;
	private JLabel welcomeLabel;
	private JLabel userNameLabel;
	private JLabel winLabel;
	private JLabel accountMessageLabel;
	private JLabel accountMoneyLabel;

	private JTextField betTextField;
	private final BlackJackController controller;

	public BlackJackView(BlackJackController pokerController)
	{
		super();
		controller = pokerController;
		init();
	}

	public BlackJackView()
	{
		super();
		controller = null;
		init();
	}

	private void init()
	{
		ButtonActionListener listener = new ButtonActionListener();
		betButton = createButton("BET", listener, true);
		surrenderButton = createButton("SURRENDER", listener, true);
		hitButton = createButton("HIT", listener, true);
		doubleButton = createButton("DOUBLE", listener, true);
		standButton = createButton("STAND", listener, true);
		quitButton = createButton("QUIT", listener, true);
		newGameButton = createButton("NEW GAME", listener, true);
		newGameButton.setEnabled(false);
		betTextField = new JTextField("20", 7);

		setUpButtonPanel();

		tableCard1 = createImageLabel();
		tableCard2 = createImageLabel();
		tableCard3 = createImageLabel();
		tableCard4 = createImageLabel();
		tableCard5 = createImageLabel();

		Player1_FirstCard = createImageLabel();
		Player1_SecondCard = createImageLabel();
		Player1_ThirdCard = createImageLabel();
		Player1_FourthCard = createImageLabel();
		Player1_FifthCard = createImageLabel();

		Player1_Name = createLabel("Player", 20);
		Player1_Money = createLabel("Money:", 20);

		blackJackPanelTitle = new JLabel();
		potMoney = createLabel("0", 24);
		potName = createLabel("POT MONEY:", 24);
		blackJackTableTitle = createLabel("BLACK JACK TABLE", 36);

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

		messageLabel = createLabel("", 24);
		welcomeLabel = createLabel("Welcome,", 24);
		userNameLabel = createLabel("", 24);
		winLabel = createLabel("", 24);
		accountMessageLabel = createLabel("Balance:", 24);
		accountMoneyLabel = createLabel("", 24);

		setUpTablePanel();

	}

	public void updateButtonStatus(boolean betButtonVisibility, boolean surrenderButtonVisibility, boolean hitButtonVisibility, boolean doubleButtonVisibility 
			, boolean standButtonVisibility, boolean betTextFieldVisibility)
	{
		betButton.setEnabled(betButtonVisibility);
		surrenderButton.setEnabled(surrenderButtonVisibility);
		hitButton.setEnabled(hitButtonVisibility);
		doubleButton.setEnabled(doubleButtonVisibility);
		standButton.setEnabled(standButtonVisibility);
		betTextField.setEnabled(betTextFieldVisibility);
	}

	/*************Setters for card images**************/


	public void setNewGameButtonStatus(boolean value) {
		this.newGameButton.setEnabled(value);;
	}

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

	public void setPlayer1_ThirdCard(ImageIcon icon) {
		Player1_ThirdCard.setIcon(icon); 
	}

	public void setPlayer1_FourthCard(ImageIcon icon) {
		Player1_FourthCard.setIcon(icon); 
	}

	public void setPlayer1_FifthCard(ImageIcon icon) {
		Player1_FifthCard.setIcon(icon); 
	}

	/*************End**************/

	public void setPlayer1_Name(String player1_Name) {
		Player1_Name.setText(player1_Name);
	}

	public void setPlayer1_Money(String player1_Money) {
		Player1_Money.setText("Money: "+player1_Money);
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
								.addComponent(newGameButton, 10, 150, 150)
								.addContainerGap(50, Short.MAX_VALUE))
								.addGroup(buttonPanelLayout.createSequentialGroup()
										.addContainerGap(50, Short.MAX_VALUE)
										.addComponent(hitButton, 10, 100, 100)
										.addContainerGap(50, Short.MAX_VALUE)) 
										.addGroup(GroupLayout.Alignment.CENTER, buttonPanelLayout.createSequentialGroup()
												.addContainerGap(50, Short.MAX_VALUE)
												.addComponent(doubleButton, 10, 150, 150)
												.addContainerGap(50, Short.MAX_VALUE))	
												.addGroup(GroupLayout.Alignment.CENTER, buttonPanelLayout.createSequentialGroup()
														.addContainerGap(50, Short.MAX_VALUE)
														.addComponent(standButton, 10, 150, 150)
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
																				.addComponent(surrenderButton, 10, 200, 200)
																				.addContainerGap(50, Short.MAX_VALUE))	
																				.addGroup(GroupLayout.Alignment.CENTER, buttonPanelLayout.createSequentialGroup()
																						.addContainerGap(50, Short.MAX_VALUE)
																						.addComponent(quitButton, 10, 100, 100)
																						.addContainerGap(50, Short.MAX_VALUE)));

		buttonPanelLayout.setVerticalGroup(buttonPanelLayout.createSequentialGroup()
				.addContainerGap(250, Short.MAX_VALUE)
				.addGroup(buttonPanelLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
						.addComponent(newGameButton, 10, 50, 50)
						.addGap(80))
						.addGroup(buttonPanelLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
								.addComponent(hitButton, 10, 50, 50)
								.addGap(80))
								.addGroup(buttonPanelLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
										.addGap(80)
										.addComponent(doubleButton, 10, 50, 50))
										.addGroup(buttonPanelLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
												.addGap(80)
												.addComponent(standButton, 10, 50, 50))
												.addGroup(buttonPanelLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
														.addGap(60)
														.addComponent(betButton, 10, 50, 50))
														.addGroup(buttonPanelLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
																.addGap(70)
																.addComponent(betTextField, 10, 30, 30))
																.addGroup(buttonPanelLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
																		.addGap(80)
																		.addComponent(surrenderButton, 10, 50, 50))
																		.addGroup(buttonPanelLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
																				.addComponent(quitButton, 10, 50, 50))
																				.addContainerGap(250, Short.MAX_VALUE));
	}

	private void setUpPokerTable()
	{

		pokerTableLayout.setHorizontalGroup(pokerTableLayout.createParallelGroup()
				.addGroup(pokerTableLayout.createSequentialGroup()	
						.addContainerGap(5, Short.MAX_VALUE)
						.addComponent(blackJackTableTitle, 0, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE)
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
				.addComponent(blackJackTableTitle, 0, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE)
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

		createPlayerLayout(player1Layout, Player1_Name, Player1_Money, Player1_FirstCard, Player1_SecondCard
				, Player1_ThirdCard, Player1_FourthCard, Player1_FifthCard);
	}

	private void createPlayerLayout(GroupLayout playerLayout, JLabel playerName, JLabel playerMoney, JLabel playerFirstCard, JLabel playerSecondCard
			, JLabel playerThirdCard, JLabel playerFourthCard, JLabel playerFifthCard)
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
								.addGap(20)
								.addComponent(playerThirdCard, 0, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE)
								.addGap(20)
								.addComponent(playerFourthCard, 0, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE)
								.addGap(20)
								.addComponent(playerFifthCard, 0, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE)
								.addContainerGap(3, 3)));

		playerLayout.setVerticalGroup(playerLayout.createSequentialGroup()
				.addContainerGap(10, 10)
				.addGroup(playerLayout.createParallelGroup(GroupLayout.Alignment.CENTER)
						.addComponent(playerName, 0, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE)
						.addComponent(playerMoney, 0, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE))
						.addGap(20)
						.addGroup(playerLayout.createParallelGroup(GroupLayout.Alignment.CENTER)
								.addComponent(playerFirstCard, 0, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE)
								.addComponent(playerSecondCard, 0, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE)
								.addComponent(playerThirdCard, 0, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE)
								.addComponent(playerFourthCard, 0, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE)
								.addComponent(playerFifthCard, 0, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE))
								.addContainerGap(10, 10));
	}

	public void setUpTablePanel()
	{
		setUpPokerTable();
		tablePanelLayout.setHorizontalGroup(tablePanelLayout.createSequentialGroup()
				.addContainerGap(10, Short.MAX_VALUE)
				.addGroup(tablePanelLayout.createParallelGroup(GroupLayout.Alignment.CENTER)
						.addGroup(tablePanelLayout.createSequentialGroup()
								.addComponent(welcomeLabel) .addGap(10) .addComponent(userNameLabel)))
								.addGap(150)	
								.addGroup(tablePanelLayout.createParallelGroup(GroupLayout.Alignment.CENTER)
										.addComponent(player1Panel)
										.addComponent(pokerTablePanel)
										.addComponent(winLabel)
										.addComponent(messageLabel))
										.addGap(150)	
										.addGroup(tablePanelLayout.createParallelGroup(GroupLayout.Alignment.CENTER)
												.addGroup(tablePanelLayout.createSequentialGroup()
														.addComponent(accountMessageLabel) .addGap(10) .addComponent(accountMoneyLabel)))
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
								.addGap(50)	
								.addComponent(pokerTablePanel)
								.addGap(50))
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
				//				if(bet < BlackJackView.this.controller.minimumBet)
				//				{
				//					BlackJackView.this.setMessageLabel("The Minimum bet is "+ BlackJackView.this.controller.minimumBet + "Please Bet again.");
				//					return;
				//				}
				BlackJackView.this.controller.sendBetMoney(bet);
			}
			else if(src.equals(hitButton))
			{
				BlackJackView.this.controller.sendMessageToServer(BlackJackEnum.HIT.toString());
			}
			else if(src.equals(doubleButton))
			{
				BlackJackView.this.controller.sendMessageToServer(BlackJackEnum.DOUBLE.toString());
			}	
			else if(src.equals(surrenderButton))
			{
				BlackJackView.this.controller.sendMessageToServer(BlackJackEnum.SURRENDER.toString());
			}
			else if(src.equals(standButton))
			{
				BlackJackView.this.controller.sendMessageToServer(BlackJackEnum.STAND.toString());
			}
			else if(src.equals(newGameButton))
			{
				BlackJackView.this.controller.sendMessageToServer(BlackJackEnum.NEW.toString());
			}
			else if(src.equals(quitButton))
			{
				BlackJackView.this.controller.quit(BlackJackView.this);
			}
		}
	}

	@Override
	public void closingAction() {
		this.controller.closingAction();		
	}
}






