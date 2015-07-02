package cs6015.casino.common;
import java.awt.BorderLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.JFrame;
import javax.swing.JPanel;


@SuppressWarnings("serial")
public class CasinoFrame extends JFrame {
	
	private static CasinoFrame instance;
	private static MasterView currentPanelInParentFrame;
	
	private CasinoFrame()
	{
		super("Online Casino");
		this.addWindowListener(new WindowAdapter(){
			 public void windowClosing(WindowEvent windowEvent)
			 {
				 if(currentPanelInParentFrame == null)
					 System.exit(0);
				 else
				 {
					 currentPanelInParentFrame.closingAction();
					 try {
						this.finalize();
					} catch (Throwable e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				 }
		     } 
		});
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
		this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		//setExtendedState(JFrame.MAXIMIZED_BOTH); 
		this.pack();
		this.setVisible(true);	
	}

	public static JPanel getCurrentPanelInParentFrame() {
		return currentPanelInParentFrame;
	}

	public static void setCurrentPanelInParentFrame(MasterView currentPanelInParentFrame) {
		CasinoFrame.currentPanelInParentFrame = currentPanelInParentFrame;
	}
}
