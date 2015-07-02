package cs6015.casino.client;
import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;


public class CasinoFrame extends JFrame{
	
	private static CasinoFrame instance;
	
	private CasinoFrame()
	{
		super("Online Casino");
		
	}
	
	public static synchronized CasinoFrame getInstance()
	{
		if(instance == null)
			instance = new CasinoFrame();
		
		return instance;
	}
	
	public synchronized void updatePanel(JPanel inputPanel)
	{
		this.getContentPane().add(inputPanel, BorderLayout.CENTER);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		//setExtendedState(JFrame.MAXIMIZED_BOTH); 
		this.pack();
		this.setVisible(true);	
	}

}
