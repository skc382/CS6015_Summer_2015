package cs6015.casino.client;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;


public class LoginView extends MasterView{

	JButton LoginButton;
	JButton RegisterButton;
	
	JLabel welcomeLabel;
	JLabel loginLabel;
	JPanel loginPanel;
	GroupLayout loginPanelLayout;
	JTextField loginTextField;
	
	public LoginView()
	{
		super(true);
		init();
		updateView();
	}
	
	public static void main(String[] args) 
	{
		new LoginView();
	}
	
	private void init()
	{
		ButtonActionListener listener = new ButtonActionListener();
		loginTextField = new JTextField();
		LoginButton = createButton("LOGIN", listener, true);
		RegisterButton = createButton("REGISTER", listener, true);
		
		buttonPanelLayout = new GroupLayout(buttonPanel);
		buttonPanel.setLayout(buttonPanelLayout);
		buttonPanelLayout.setAutoCreateGaps(true);
		setUpButtonPanel();
		
		welcomeLabel = createLabel("Welcome to the Casino", 42);
		loginLabel = createLabel("Enter your username", 24);
		loginTextField = new JTextField();
		
		loginPanel = new JPanel();
		loginPanelLayout = new GroupLayout(loginPanel);
		loginPanel.setLayout(loginPanelLayout);
		loginPanel.setBackground(TABLE_PANEL_COLOR);
		loginPanel.setBorder(blackLineBorder);
		setUpTablePanel();
	}
	

	
	private void setUpButtonPanel()
	{
		buttonPanelLayout.setHorizontalGroup(buttonPanelLayout.createSequentialGroup()
				.addContainerGap(50, Short.MAX_VALUE)
				.addGroup(buttonPanelLayout.createParallelGroup(GroupLayout.Alignment.CENTER)
							.addComponent(LoginButton, 10, 200, 200)
									.addComponent(RegisterButton, 10, 200, 200))
				.addContainerGap(50, Short.MAX_VALUE));
		
		buttonPanelLayout.setVerticalGroup(buttonPanelLayout.createSequentialGroup()
				.addContainerGap(220, Short.MAX_VALUE)
				.addGroup(buttonPanelLayout.createSequentialGroup()
						.addComponent(LoginButton, 10, 50, 50)
							.addGap(50)
							   .addComponent(RegisterButton, 10, 50, 50))
				.addContainerGap(280, Short.MAX_VALUE));
	}
	
	private void setupLoginPanel()
	{
		loginPanelLayout.setHorizontalGroup(loginPanelLayout.createParallelGroup(GroupLayout.Alignment.CENTER)

				.addComponent(welcomeLabel, GroupLayout.Alignment.CENTER)
				.addGroup(loginPanelLayout.createSequentialGroup()
						.addContainerGap(30, Short.MAX_VALUE)
						.addComponent(loginLabel, 0, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE)
						.addGap(30)
						.addComponent(loginTextField, 10, 200, 200)
						.addContainerGap(30, Short.MAX_VALUE)));
		
		loginPanelLayout.setVerticalGroup(loginPanelLayout.createSequentialGroup()
				.addContainerGap(20, Short.MAX_VALUE)
				.addComponent(welcomeLabel)
				.addGroup(loginPanelLayout.createParallelGroup(GroupLayout.Alignment.CENTER)
						.addComponent(loginLabel)
						.addGap(100)
						.addComponent(loginTextField, 10, 30, 30))
				.addContainerGap(20, Short.MAX_VALUE));
	}
	
	private void setUpTablePanel()
	{
		setupLoginPanel();
		tablePanelLayout.setHorizontalGroup(tablePanelLayout.createSequentialGroup()
				.addContainerGap(5, Short.MAX_VALUE)
				.addComponent(loginPanel)
				.addContainerGap(5, Short.MAX_VALUE));
		
		tablePanelLayout.setVerticalGroup(tablePanelLayout.createSequentialGroup()
				.addContainerGap(5, Short.MAX_VALUE)
				.addComponent(loginPanel)
				.addContainerGap(5, Short.MAX_VALUE));
		
	}
	

	
	
	/*
	 * Private Class - ButtonActionListener
	 */
	private class ButtonActionListener implements ActionListener
	{

		@Override
		public void actionPerformed(ActionEvent arg0) {
			// TODO Auto-generated method stub
			
		}
	}// End of ButtonListener

}
