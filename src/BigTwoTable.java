import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

import javax.swing.*;


/**
 * The BigTwoTable class implements the CardGameTable interface. It is used to build a GUI for the Big Two card game and handle all user actions. Below is a detailed description for the BigTwoTable class.
 * @author Vinit Miranda
 *
 */
public class BigTwoTable implements CardGameTable{

	private CardGame game;
	private boolean[] selected;
	private int activePlayer;
	private JFrame frame;
	private JPanel bigTwoPanel;
	private JButton playButton;
	private JButton passButton;
	private JTextArea msgArea;
	private Image[][] cardImages;
	private Image cardBackImage;
	private Image[] avatars;
	
	private JTextField chatInput;
	private JTextArea chatArea; 
	private JMenuItem connect;
	/**
	 * a constructor for creating a BigTwoTable. The parameter game is a reference to a card game associates with this table.
	 * @param game
	 * 		game is an object of type CardGame
	 */
	public BigTwoTable(CardGame game){
		selected = new boolean[13];
		this.game = game;
		frame = new JFrame("Big Two");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(800, 620);
		frame.setResizable(true);
		frame.setLayout(new GridLayout(1, 2, 2, 2));
		
		//Adding the Panel
		Color color = new Color(34, 139, 34);
		bigTwoPanel = new BigTwoPanel();
		bigTwoPanel.setBackground(color);
		bigTwoPanel.addMouseListener((BigTwoPanel)bigTwoPanel);
		
		//Adding the JTextArea
		JPanel textPanel = new JPanel();
		textPanel.setLayout(new GridLayout(2, 1, 2, 2));
		JPanel panel = new JPanel(new BorderLayout());
		msgArea = new JTextArea();
		msgArea.setFont(new Font ("Arial", Font.PLAIN, 14));
		msgArea.setEditable(false);
		msgArea.setLineWrap(true);
		JScrollPane scroller = new JScrollPane(msgArea);
		scroller.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		scroller.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		panel.add(scroller, BorderLayout.CENTER);
		textPanel.add(panel);
		
		// sets up a text area for showing incoming messages
		JPanel messagePanel = new JPanel(new BorderLayout());
		JPanel sendPanel = new JPanel();
		//FlowLayout flow = new FlowLayout();
		//flow.setAlignment(FlowLayout.LEFT);
		//sendPanel.setLayout(flow);
		sendPanel.add(new JLabel("Message"));
		chatInput = new JTextField(20);
		chatInput.addKeyListener(new EnterKeyListener());
		sendPanel.add(chatInput);
		JButton submit = new JButton("Submit");
		submit.addActionListener(new SubmitButtonListener());
		sendPanel.add(submit);
		messagePanel.add(sendPanel, BorderLayout.SOUTH);
		chatArea = new JTextArea();
		messagePanel.add(chatArea);
		textPanel.add(messagePanel);
		
		
		//Adding the MENU
		JMenuBar menuBar = new JMenuBar();
		JMenu menu = new JMenu("Game");
		connect = new JMenuItem("Connect");
		JMenuItem quit = new JMenuItem("Quit");
		connect.addActionListener(new ConnectMenuItemListener());
		quit.addActionListener(new QuitMenuItemListener());
		menu.add(connect);
		menu.add(quit);
		menuBar.add(menu);
		frame.setJMenuBar(menuBar);
		
		//Adding the Two Buttons
		bigTwoPanel.setLayout(new BorderLayout());
		JPanel buttonPanel = new JPanel();
		//buttonPanel.setBackground();
		playButton = new JButton("Play");
		passButton = new JButton("Pass");
		playButton.addActionListener(new PlayButtonListener());
		buttonPanel.add(playButton);
		passButton.addActionListener(new PassButtonListener());
		buttonPanel.add(passButton);
		bigTwoPanel.add(buttonPanel, BorderLayout.PAGE_END);
		
		
		frame.add(bigTwoPanel);
		frame.add(textPanel);
     	frame.setVisible(true);
     	frame.repaint();
     	
     	
     	//ITs JOPTION TIME
     	
     	String name = new String();
     	String serverInput = new String(); 
     	while(name.equals("")){
     		name = JOptionPane.showInputDialog("Enter Name: ");
     		if(name == null){
     			System.exit(0);
     		}
       	}
     	while(serverInput.equals("")){
     		serverInput = JOptionPane.showInputDialog("Enter ServerIP: ");
     		if (serverInput == null){
     			System.exit(0);
     		}
     	}
     	((BigTwoClient)game).setPlayerName(name);
     	((BigTwoClient)game).setServerIP(serverInput.substring(0, serverInput.lastIndexOf(':')));
 		((BigTwoClient)game).setServerPort(Integer.parseInt(serverInput.substring(serverInput.lastIndexOf(':') + 1)));
 	
	}
	@Override
	public void setActivePlayer(int activePlayer) {
		this.activePlayer = activePlayer;
	}
	@Override
	public int[] getSelected() {
		int[] cardIdx = null;
		int count = 0;
		for (int j = 0; j < selected.length; j++) {
			if (selected[j]) {
				count++;
			}
		}
		if (count != 0) {
			cardIdx = new int[count];
			count = 0;
			for (int j = 0; j < selected.length; j++) {
				if (selected[j]) {
					cardIdx[count] = j;
					count++;
				}
			}
		}
		return cardIdx;
	}
	@Override
	public void resetSelected() {
		selected = new boolean[13];
	}
	@Override
	public void repaint() {
		frame.repaint();
	}
	@Override
	public void printMsg(String msg) {
		msgArea.append(msg);
	}
	public void printChatMsg(String msg) {
		chatArea.append(msg);
	}
	@Override
	public void clearMsgArea() {
		msgArea.setText(null);
	}
	@Override
	public void reset() {
		BigTwoDeck bigTwoDeck = new BigTwoDeck();
		bigTwoDeck.initialize();
		bigTwoDeck.shuffle();
		//Resetting the flag variables
		BigTwoClient bigTwo = (BigTwoClient) game;
		bigTwo.reset();
		//Enabling the GUI
		enable();
		//Reset Selected
		resetSelected();
		//Clear message Area
		clearMsgArea();
		game.start(bigTwoDeck);
		
	}
	public void showEnd(String content){
		JOptionPane.showMessageDialog(frame, content);
	}
	public void disableConnect(){
		connect.setEnabled(false);
	}
	@Override
	public void enable() {
		passButton.setEnabled(true);
		playButton.setEnabled(true);		
	}
	@Override
	public void disable() {
		passButton.setEnabled(false);
		playButton.setEnabled(false);
	}
	/**
	 * an inner class that extends the JPanel class and implements the
	 * MouseListener interface. Overrides the paintComponent() method inherited from the
	 * JPanel class to draw the card game table. Implements the mouseClicked() method from
     * the MouseListener interface to handle mouse click events.
	 * @author Vinit Miranda
	 *
	 */
	class BigTwoPanel extends JPanel implements MouseListener{
	
		private static final long serialVersionUID = 1L;
		
		public void paintComponent(Graphics g){
			super.paintComponent(g);
			//Graphics2D g2d = (Graphics2D) g;
			ArrayList<CardGamePlayer> players = game.getPlayerList();
			avatars = new Image[4];
			cardImages = new Image[4][13];
			Font font = new Font ("Calibri", Font.BOLD , 20);
			g.setFont(font);
			String[] avatarNames={"flash_64.png", "batman_64.png", "superman_64.png", "green_lantern_64.png"};
			cardBackImage = new ImageIcon("src/cards/b.gif").getImage();
			//g.drawString(Integer.toString((game.getNumOfPlayers())), 90,490);
			for (int i = 0; i < game.getNumOfPlayers(); i++){
				
				if (((BigTwoClient)game).getPlayerList().get(i).getName() != null){
					g.drawString(((BigTwoClient)game).getPlayerList().get(i).getName(), 0, 35 + (i * 100));
					avatars[i] = new ImageIcon("src/003-Character/png/64/" + avatarNames[i]).getImage();
					g.drawImage(avatars[i], 0, 40 + (i * 100), this);
				}
			}
			g.drawString("Table", 5, 500);
			
			char[] suits = { 'd', 'c', 'h', 's'};
			char[] ranks = { 'a', '2', '3', '4', '5', '6', '7', '8', '9', 't', 'j', 'q', 'k' };
			
			//Printing Cards
			char rank;
			char suit;
			int x, y;
			
			for (int i = 0; i < game.getNumOfPlayers(); i++){
				for (int j = 0; j < players.get(i).getCardsInHand().size(); j++){
					x = 70 + (j * 15);
					y = 15 + (i * 100);
					if(i == activePlayer){
						rank = ranks[players.get(i).getCardsInHand().getCard(j).getRank()];
						suit = suits[players.get(i).getCardsInHand().getCard(j).getSuit()];
						cardImages[i][j] = new ImageIcon("src/cards/" + rank + suit + ".gif").getImage();
						if(!selected[j])
							g.drawImage(cardImages[i][j], x, y, this);
						else
							g.drawImage(cardImages[i][j], x, y - 15, this);
					}
					else{
						g.drawImage(cardBackImage, x, y, this);
					}
				}
			}
			if (game.getHandsOnTable().size() != 0){
				for (int i = 0; i < game.getHandsOnTable().get(game.getHandsOnTable().size() - 1).size(); i++){
					rank = ranks[game.getHandsOnTable().get(game.getHandsOnTable().size() - 1).getCard(i).getRank()];
					suit = suits[game.getHandsOnTable().get(game.getHandsOnTable().size() - 1).getCard(i).getSuit()];
					g.drawImage(new ImageIcon("src/cards/" + rank + suit + ".gif").getImage()
							, 70+ (i*15), 420, this);
				}
			}
			/*
			g.drawLine(0, 150, 450, 150);
			g.drawLine(0, 250, 450, 250);
			g.drawLine(0, 350, 450, 350);
			g.drawLine(0, 450, 450, 450);
			g.drawLine(0, 550, 450, 550);
			*/
		
		}
		@Override
		public void mouseClicked(MouseEvent e) {
			int mousex = e.getX();
			int mousey = e.getY();
			int x, y;
		
			for (int i = 0; i < game.getPlayerList().size(); i++){
				if(i == ((BigTwoClient)game).getPlayerID() && i == game.getCurrentIdx()){
					for (int j = 0; j < game.getPlayerList().get(i).getCardsInHand().size(); j++){
						int ctr = j == game.getPlayerList().get(i).getCardsInHand().size() - 1 ? j : j + 1; 
						if (selected[j] && selected[ctr]){
							x = 70 + (j * 15);
							y = i * 100;
							if (j == game.getPlayerList().get(i).getCardsInHand().size() - 1){
								if (x <= mousex && mousex <= x + 70
									&& y <= mousey && mousey <= y + 95){
									selected[j] = false;
								}
							}
							else{
								if (x <= mousex && mousex < x + 15 && y <= mousey && mousey <= y + 95){
									selected[j] = false;
								}
							}
						}
						
						else if (selected[j] && !selected[ctr]){
							x = 70 + (j * 15);
							y = i * 100;
							if (j == game.getPlayerList().get(i).getCardsInHand().size() - 1){
								if (x <= mousex && mousex <= x + 70
									&& y <= mousey && mousey <= y + 95){
									selected[j] = false;
								}
							}
							else{
								if ((x <= mousex && mousex <= x + 70 && y <= mousey && mousey <= y + 15)
										|| (x <= mousex && mousex < x + 15 && y <= mousey && mousey <= y + 95)){
									selected[j] = false;
								}
							}
						}
						else{
							x = 70 + (j * 15);
							y = 15 + (i * 100);
							if (j == game.getPlayerList().get(i).getCardsInHand().size() - 1){
								if (x <= mousex && mousex <= x + 70
									&& y <= mousey && mousey <= y + 95){
									selected[j] = true;
								}
							}
							else{
								if (x <= mousex && mousex < x + 15
										&& y <= mousey && mousey <= y + 95){
									selected[j] = true;
								}
							}
						}
					}
				}
			}
			frame.repaint();
		}
		@Override
		public void mousePressed(MouseEvent e) {
		}
		@Override
		public void mouseReleased(MouseEvent e) {
		}
		@Override
		public void mouseEntered(MouseEvent e) {	
		}
		@Override
		public void mouseExited(MouseEvent e) {
		}
	}
	class EnterKeyListener implements KeyListener{
		@Override
		public void keyTyped(KeyEvent e) {
		}
		@Override
		public void keyPressed(KeyEvent e) {
			if(e.getKeyCode() == KeyEvent.VK_ENTER){
				String s = chatInput.getText();
				CardGameMessage mess = new CardGameMessage(CardGameMessage.MSG, activePlayer, s);
				((BigTwoClient)game).sendMessage(mess);
			}
		}
		@Override
		public void keyReleased(KeyEvent e) {
		}
	
	}
	class SubmitButtonListener implements ActionListener{
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			
			String s = chatInput.getText();
			CardGameMessage mess = new CardGameMessage(CardGameMessage.MSG, activePlayer, s);
			((BigTwoClient)game).sendMessage(mess);
		}
	}
	/**
	 * an inner class that implements the ActionListener
	 * interface. Implements the actionPerformed() method from the ActionListener interface
	 * to handle button-click events for the “Play” button.
	 * @author Vinit Miranda
	 *
	 */
	class PlayButtonListener implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent e) {
			if(game.getCurrentIdx() == ((BigTwoClient)game).getPlayerID()){
				int moves[] = getSelected();
				if (moves != null)
					game.makeMove(game.getCurrentIdx(), moves);
			}
		}
	}
	
	/**
	 * an inner class that implements the ActionListener
	 * interface. Implements the actionPerformed() method from the ActionListener interface
	 * to handle button-click events for the “Pass” button.
	 * @author Vinit Miranda
	 *
	 */
	class PassButtonListener implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent e) {
			if(game.getCurrentIdx() == ((BigTwoClient)game).getPlayerID()){
				int moves[] = getSelected();
				if (moves == null)
					game.makeMove(game.getCurrentIdx(), moves);
			}
		}
	}
	/**
	 * an inner class that implements the ActionListener
	 * interface. Implements the actionPerformed() method from the ActionListener interface
	 * to handle menu-item-click events for the “Restart” menu item.
	 * @author Vinit Miranda
	 *
	 */
	class ConnectMenuItemListener implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent e) {
			String name = new String();
	     	String serverInput = new String(); 
	     	while(name.equals("")){
	     		name = JOptionPane.showInputDialog("Enter Name: ");
	     		if(name == null){
	     			name = new String();
	     		}
	       	}
	     	((BigTwoClient)game).setPlayerName(name);
	     	while(serverInput.equals("")){
	     		serverInput = JOptionPane.showInputDialog("Enter ServerIP: ");
	     		if (serverInput == null){
	     			serverInput = new String();
	     		}
	     	}
	     	((BigTwoClient)game).setServerIP(serverInput.substring(0, serverInput.lastIndexOf(':')));
	 		((BigTwoClient)game).setServerPort(Integer.parseInt(serverInput.substring(serverInput.lastIndexOf(':') + 1)));
	 		((BigTwoClient)game).makeConnection();
		}
	}
	/**
	 * an inner class that implements the ActionListener
	 * interface. Implements the actionPerformed() method from the ActionListener interface
	 * to handle menu-item-click events for the “Quit” menu item.
	 * @author Vinit Miranda
	 *
	 */
	class QuitMenuItemListener implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent e) {
			System.exit(0);
		}
	}
}
