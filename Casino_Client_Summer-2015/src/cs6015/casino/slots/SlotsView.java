package cs6015.casino.slots;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.GroupLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import cs6015.casino.blackjack.BlackJackController;
import cs6015.casino.blackjack.BlackJackView;
import cs6015.casino.common.MasterView;
import cs6015.casino.serializables.BlackJackMessage.BlackJackEnum;

public class SlotsView extends MasterView{
	private JButton spinButton;
	private JButton quitButton;
	
	private JPanel slotsTablePanel;
	private GroupLayout slotsTableLayout; 
	
	private JLabel tableCard1;
	private JLabel tableCard2;
	private JLabel tableCard3;

	private JLabel blackJackTableTitle;

	private JLabel messageLabel;
	private JLabel welcomeLabel;
	private JLabel userNameLabel;
	private JLabel winLabel;
	private JLabel accountMessageLabel;
	private JLabel accountMoneyLabel;

	private JTextField betTextField;
	private final SlotsController controller;

	public SlotsView(SlotsController slotsController)
	{
		super();
		controller = slotsController;
		init();
	}

	public SlotsView()
	{
		super();
		controller = new SlotsController(this);
		init();
	}

	private void init()
	{
		ButtonActionListener listener = new ButtonActionListener();
		spinButton = createButton("SPIN", listener, true);
		quitButton = createButton("QUIT", listener, true);
		betTextField = new JTextField("20", 7);

		setUpButtonPanel();

		tableCard1 = createImageLabel("81");
		tableCard2 = createImageLabel("82");
		tableCard3 = createImageLabel("83");

		blackJackTableTitle = createLabel("SLOT MACHINE", 36);

		slotsTablePanel = new JPanel();
		slotsTableLayout = new GroupLayout(slotsTablePanel);
		slotsTablePanel.setLayout(slotsTableLayout);
		slotsTablePanel.setBackground(TABLE_PANEL_COLOR);
		slotsTablePanel.setBorder(blackLineBorder);

		messageLabel = createLabel("", 24);
		welcomeLabel = createLabel("Welcome,", 24);
		userNameLabel = createLabel("", 24);
		winLabel = createLabel("", 24);
		accountMessageLabel = createLabel("Balance:", 24);
		accountMoneyLabel = createLabel("", 24);

		setUpTablePanel();

	}

	public void updateButtonStatus(boolean betButtonVisibility, boolean betTextFieldVisibility)
	{
		spinButton.setEnabled(betButtonVisibility);
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

	/*************End**************/

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
						.addGroup(GroupLayout.Alignment.CENTER, buttonPanelLayout.createSequentialGroup()
								.addContainerGap(50, Short.MAX_VALUE)
								.addComponent(spinButton, 10, 100, 100)
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
						.addGap(60)
						.addComponent(spinButton, 10, 50, 50))
						.addGroup(buttonPanelLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
								.addGap(70)
								.addComponent(betTextField, 10, 30, 30))
								.addGroup(buttonPanelLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
										.addComponent(quitButton, 10, 50, 50))
										.addContainerGap(250, Short.MAX_VALUE));
	}

	private void setUpPokerTable()
	{

		slotsTableLayout.setHorizontalGroup(slotsTableLayout.createParallelGroup()
				.addGroup(slotsTableLayout.createSequentialGroup()	
						.addContainerGap(5, Short.MAX_VALUE)
						.addComponent(blackJackTableTitle, 0, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE)
						.addContainerGap(5, Short.MAX_VALUE))
						.addGroup(slotsTableLayout.createSequentialGroup()
								.addContainerGap(30, Short.MAX_VALUE)
								.addGap(10).addComponent(tableCard1, 0, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE)
								.addGap(10).addComponent(tableCard2, 0, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE)
								.addGap(10).addComponent(tableCard3, 0, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE)
								.addContainerGap(30, Short.MAX_VALUE)));

		slotsTableLayout.setVerticalGroup(slotsTableLayout.createSequentialGroup()
				.addContainerGap(30, Short.MAX_VALUE)
				.addComponent(blackJackTableTitle, 0, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE)
				.addGap(30)
				.addGroup(slotsTableLayout.createParallelGroup()
						.addComponent(tableCard1, 0, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE)
						.addComponent(tableCard2, 0, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE)
						.addComponent(tableCard3, 0, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE))
				.addContainerGap(30, Short.MAX_VALUE));
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
										.addComponent(slotsTablePanel)
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
								.addComponent(slotsTablePanel)
								.addGap(50))
								.addGap(100)
								.addContainerGap(50, 50));
	}

	/*
	 * Private Class - ButtonActionListener
	 */
	private class ButtonActionListener implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			Object src = e.getSource();

			if(src.equals(spinButton))
			{
				SlotsView.this.controller.setBetMoney(Integer.valueOf(betTextField.getText()));
				SlotsView.this.controller.spin();
			}
			else if(src.equals(quitButton))
			{
				SlotsView.this.controller.quit(SlotsView.this);
			}
		}
	} // End of ButtonListener

	@Override
	public void closingAction() {
		this.controller.closingAction();		
	}
} // End of SlotsView