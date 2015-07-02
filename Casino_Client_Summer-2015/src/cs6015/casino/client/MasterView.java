package cs6015.casino.client;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.GroupLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;


/*
 * References:-
 * https://docs.oracle.com/javase/tutorial/uiswing/
 */
public abstract class MasterView extends JPanel
{
	protected final Color TABLE_PANEL_COLOR = new Color(0,153,0);
	protected final Color BUTTON_PANEL_COLOR = new Color(192,192,192);
	protected final Color BACKGROUND_PANEL_COLOR = new Color(254,254,254);
	protected final Dimension TABLE_PANEL_DIMENSION = new Dimension(1400,900);
	protected final Dimension BUTTON_PANEL_DIMENSION = new Dimension(400,900);
	
	private CasinoFrame parentFrame;
	protected JPanel mainPanel;
	protected JPanel tablePanel;
	protected JPanel buttonPanel;
	
	private GroupLayout masterPanelLayout;
	protected GroupLayout tablePanelLayout;
	protected GroupLayout buttonPanelLayout;
	
	private Border tablePanelBorder;
	private Border buttonPanelBorder;
	protected Border bevelBorder, blackLineBorder;
	
	protected CardsImagesResource cardImageHandler;

	
	public MasterView()
	{
		parentFrame = CasinoFrame.getInstance();
		cardImageHandler = CardsImagesResource.getInstance();
		initialize();
	}
	
	public MasterView(boolean noCardsNeeded)
	{
		parentFrame = CasinoFrame.getInstance();
		initialize();
	}
	
	public void initialize()
	{
		this.setBackground(BACKGROUND_PANEL_COLOR);
		
		bevelBorder = BorderFactory.createCompoundBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED), 
																	BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
		blackLineBorder = BorderFactory.createLineBorder(Color.BLACK, 5, true);
		
		tablePanelBorder = BorderFactory.createCompoundBorder(blackLineBorder, bevelBorder);
		buttonPanelBorder = BorderFactory.createCompoundBorder(blackLineBorder, bevelBorder);
		
		tablePanel = new JPanel();
		tablePanel.setPreferredSize(TABLE_PANEL_DIMENSION);
		tablePanel.setBackground(TABLE_PANEL_COLOR);
		tablePanel.setBorder(tablePanelBorder);
		
		buttonPanel = new JPanel();
		buttonPanel.setPreferredSize(BUTTON_PANEL_DIMENSION);
		buttonPanel.setBackground(BUTTON_PANEL_COLOR);
		buttonPanel.setBorder(buttonPanelBorder);		
		
		masterPanelLayout = new GroupLayout(this);
		this.setLayout(masterPanelLayout);
		masterPanelLayout.setAutoCreateGaps(true);
		masterPanelLayout.setAutoCreateContainerGaps(true);
		
		masterPanelLayout.setHorizontalGroup(masterPanelLayout.createSequentialGroup()
				 .addComponent(tablePanel, 0, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
					.addComponent(buttonPanel, 0, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE));
		
	
		masterPanelLayout.setVerticalGroup(masterPanelLayout.createParallelGroup()	
				.addComponent(tablePanel)
					.addComponent(buttonPanel));
		
		tablePanelLayout = new GroupLayout(tablePanel);
		tablePanel.setLayout(tablePanelLayout);
		
		buttonPanelLayout = new GroupLayout(buttonPanel);
		buttonPanel.setLayout(buttonPanelLayout);
		buttonPanelLayout.setAutoCreateGaps(true);
	}
	
	protected JButton createButton(String text, ActionListener listener, boolean enabled) 
	{
        JButton button = new JButton(text);
        button.setEnabled(enabled);
        button.setFont(new Font("Serif", Font.BOLD, 16));
        button.addActionListener(listener);
        return button;
    }
	
	protected JLabel createLabel(String text, int fontSize)
	{
		JLabel returnLabel = new JLabel(text);
		returnLabel.setFont(new Font("Serif", Font.BOLD, fontSize));
		returnLabel.setForeground(Color.BLACK);
		//returnLabel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1, true));
		return returnLabel;
	}
	
	protected JLabel createImageLabel()
	{
		ImageIcon basecard = cardImageHandler.getCardImage(0);
		JLabel returnlabel = new JLabel(basecard);
		returnlabel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1, true));
		return returnlabel;
	}
	
	public void updateView()
	{
		parentFrame.updatePanel(this);
	}
	
	

}