package cs6015.casino.login;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import cs6015.casino.common.MasterView;


@SuppressWarnings("serial")
public class LoginView extends MasterView{

	private JButton LoginButton;
	private JButton RegisterButton;
	
	private JLabel welcomeLabel;
	private JLabel loginLabel;
	private JPanel loginPanel;
	private GroupLayout loginPanelLayout;
	private JTextField loginTextField;
	private LoginController controller; 
	
	public LoginView()
	{
		super(true);
		init();
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
		public void actionPerformed(ActionEvent e) {
			Object src = e.getSource();
			
			if(src.equals(LoginButton))
			{
				String loginName = loginTextField.getText();
				if(!loginName.isEmpty())
				{
					 LoginView.this.controller = LoginController.getInstance(loginName, LoginView.this);
				}
				else
				{
					JOptionPane.showMessageDialog(null, "Please enter userid and login again", "ONLINE CASINO",
							JOptionPane.OK_OPTION);
					//System.exit(0);
				}
			}
			else if(src.equals(RegisterButton))
			{
				String loginName = loginTextField.getText();
				if(!loginName.isEmpty())
				{
					 LoginView.this.controller = LoginController.getInstance(loginName, LoginView.this);
					 LoginView.this.controller.setRegister(true);
				}
				else
				{
					JOptionPane.showMessageDialog(null, "Please enter userid and login again", "ONLINE CASINO",
							JOptionPane.OK_OPTION);
					//System.exit(0);
				}
			}
			
		}
	}// End of ButtonListener

	public void invalidUserDialog()
	{
		JOptionPane.showMessageDialog(null, "Invalid User name. Please login again.", "ONLINE CASINO",
				JOptionPane.OK_OPTION);
		System.exit(0);
	}

	@Override
	public void closingAction() {
		if(this.controller == null)
			System.exit(0);
		this.controller.closingAction();
	}
	
}// End of Login view
