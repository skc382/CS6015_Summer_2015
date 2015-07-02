package cs6015.casino.dashboard;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import cs6015.casino.client.Controller;
import cs6015.casino.common.MasterView;

@SuppressWarnings("serial")
public class DashBoardView extends MasterView{
	
	private JButton playPokerButton;
	private JButton playBlackjackButton;
    private JButton playSlotsButton;	
    private JButton quitButton;
    
    private JPanel dashBoardTablePanel;
    private GroupLayout dashBoardTableLayout; 
    
    private JLabel dashBoardTableTitle;
    private JLabel welcomeLabel;
    private JLabel userNameLabel;
    private JLabel accountMessageLabel;
    private JLabel accountMoneyLabel;
    
    private Controller controller;
	
	public DashBoardView(Controller controller)
	{
		super(true);
		this.controller = controller;
		init();
	}

	private void init()
	{
		ButtonActionListener listener = new ButtonActionListener();
		playPokerButton = createButton("PLAY POKER", listener, true);
		playBlackjackButton = createButton("PLAY BLACK JACK", listener, true);
		playSlotsButton = createButton("PLAY SLOTS", listener, true);
		quitButton = createButton("QUIT", listener, true);
		setUpButtonPanel();
		
	    dashBoardTableTitle = createLabel("DASH BOARD", 36);
		
	    dashBoardTablePanel = new JPanel();
		dashBoardTableLayout = new GroupLayout(dashBoardTablePanel);
		dashBoardTablePanel.setLayout(dashBoardTableLayout);
		dashBoardTablePanel.setBackground(TABLE_PANEL_COLOR);
		dashBoardTablePanel.setBorder(blackLineBorder);
		
	    welcomeLabel = createLabel("Welcome,", 36);
	    userNameLabel = createLabel("", 36);
	    accountMessageLabel = createLabel("Your Money:", 30);
	    accountMoneyLabel = createLabel("", 30);
		
	    setUpTablePanel();
	}
	
	public void updateDashBoardFields(String userName, String money)
	{
		userNameLabel.setText(userName);
		accountMoneyLabel.setText(money);
		this.refreshView();
	}
	
	public void setUpButtonPanel()
	{
		buttonPanelLayout.setHorizontalGroup(buttonPanelLayout.createParallelGroup()			
				.addGroup(GroupLayout.Alignment.CENTER, buttonPanelLayout.createSequentialGroup()
						.addContainerGap(50, Short.MAX_VALUE)
							.addComponent(playPokerButton, 10, 250, 250)
								.addContainerGap(50, Short.MAX_VALUE))
				.addGroup(GroupLayout.Alignment.CENTER, buttonPanelLayout.createSequentialGroup()
						.addContainerGap(50, Short.MAX_VALUE)
							.addComponent(playBlackjackButton, 10, 250, 250)
								.addContainerGap(50, Short.MAX_VALUE))
				.addGroup(GroupLayout.Alignment.CENTER, buttonPanelLayout.createSequentialGroup()
						.addContainerGap(50, Short.MAX_VALUE)
							.addComponent(playSlotsButton, 10, 250, 250)
								.addContainerGap(50, Short.MAX_VALUE))
				.addGroup(GroupLayout.Alignment.CENTER, buttonPanelLayout.createSequentialGroup()
						.addContainerGap(50, Short.MAX_VALUE)
							.addComponent(quitButton, 10, 250, 250)
								.addContainerGap(50, Short.MAX_VALUE)));
		
		buttonPanelLayout.setVerticalGroup(buttonPanelLayout.createSequentialGroup()
				.addContainerGap(250, Short.MAX_VALUE)
				.addGroup(buttonPanelLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
						.addGap(100)
							.addComponent(playPokerButton, 10, 80, 80))
				.addGroup(buttonPanelLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
						.addGap(100)
							.addComponent(playBlackjackButton, 10, 80, 80))
				.addGroup(buttonPanelLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
						.addGap(100)
							.addComponent(playSlotsButton, 10, 80, 80))
				.addGroup(buttonPanelLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
						.addGap(100)
							.addComponent(quitButton, 10, 80, 80))
				.addContainerGap(250, Short.MAX_VALUE));
	}
	
	private void setUpDashBoardTable()
	{
		dashBoardTableLayout.setHorizontalGroup(dashBoardTableLayout.createParallelGroup()
				.addGroup(dashBoardTableLayout.createSequentialGroup()	
						.addContainerGap(5, Short.MAX_VALUE)
							.addComponent(dashBoardTableTitle, 0, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE)
								.addContainerGap(5, Short.MAX_VALUE)));
		
		
		dashBoardTableLayout.setVerticalGroup(dashBoardTableLayout.createSequentialGroup()
					.addContainerGap(10, Short.MAX_VALUE)
					.addComponent(dashBoardTableTitle, 0, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE)
					.addContainerGap(10, Short.MAX_VALUE));
	}
	
	public void setUpTablePanel()
	{
		setUpDashBoardTable();
		tablePanelLayout.setHorizontalGroup(tablePanelLayout.createSequentialGroup()
				.addContainerGap(5, Short.MAX_VALUE)
				.addGroup(tablePanelLayout.createParallelGroup(GroupLayout.Alignment.CENTER)
						.addGroup(tablePanelLayout.createSequentialGroup()
							.addComponent(welcomeLabel) .addGap(10) .addComponent(userNameLabel)))
				.addGap(150)	
				.addGroup(tablePanelLayout.createParallelGroup(GroupLayout.Alignment.CENTER)
						.addComponent(dashBoardTablePanel))
				.addGap(150)	
				.addGroup(tablePanelLayout.createParallelGroup(GroupLayout.Alignment.CENTER)
						.addGroup(tablePanelLayout.createSequentialGroup()
							.addComponent(accountMessageLabel) .addGap(10) .addComponent(accountMoneyLabel)))
				.addContainerGap(10, Short.MAX_VALUE));
		
		tablePanelLayout.setVerticalGroup(tablePanelLayout.createSequentialGroup()
				.addContainerGap(5, 5)
				.addGroup(tablePanelLayout.createParallelGroup(GroupLayout.Alignment.CENTER, true)
							.addComponent(welcomeLabel)
								.addComponent(userNameLabel)
								.addComponent(accountMessageLabel) .addComponent(accountMoneyLabel))
				.addGap(100)
				.addGroup(tablePanelLayout.createParallelGroup(GroupLayout.Alignment.CENTER)
					.addComponent(dashBoardTablePanel))
				.addContainerGap(100, Short.MAX_VALUE));
	}
	
	
	/*
	 * Private Class - ButtonActionListener
	 */
	private class ButtonActionListener implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			Object src = e.getSource();
			
			if(src.equals(playPokerButton))
			{
				DashBoardView.this.controller.registerPokerController();
			}
			else if(src.equals(playBlackjackButton))
			{
				DashBoardView.this.controller.registerBlackJackController();
			}
			else if(src.equals(playSlotsButton))
			{
				DashBoardView.this.controller.registerSlotsController();
			}
			else if(src.equals(quitButton))
			{
				DashBoardView.this.controller.closingServerConnection();
			}
			
		}
	}


	@Override
	public void closingAction() {
		this.controller.closingServerConnection();
	}
	
}
