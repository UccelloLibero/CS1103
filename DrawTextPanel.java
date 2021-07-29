import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.Random;
import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.JColorChooser;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.KeyStroke;

/**
 * A panel that contains a large drawing area where strings
 * can be drawn.  The strings are represented by objects of
 * type DrawTextItem.  An input box under the panel allows
 * the user to specify what string will be drawn when the
 * user clicks on the drawing area.
 */
public class DrawTextPanel extends JPanel  {

	// As it now stands, this class can only show one string at at
	// a time!  The data for that string is in the DrawTextItem object
	// named stringList.  (If it's null, nothing is shown.  This
	// variable should be replaced by a variable of type
	// ArrayList<DrawStringItem> that can store multiple items.

	// Declaring a list
	private ArrayList<DrawTextItem> stringList = new ArrayList<DrawTextItem>();  // change to an ArrayList<DrawTextItem> !


	private Color currentTextColor = Color.BLACK;  // Color applied to new strings.

	private Canvas canvas;  // the drawing area.
	private JTextField input;  // where the user inputs the string that will be added to the canvas
	private SimpleFileChooser fileChooser;  // for letting the user select files
	private JMenuBar menuBar; // a menu bar with command that affect this panel
	private MenuHandler menuHandler; // a listener that responds whenever the user selects a menu command
	private JMenuItem undoMenuItem;  // the "Remove Item" command from the edit menu


	/**
	 * An object of type Canvas is used for the drawing area.
	 * The canvas simply displays all the DrawTextItems that
	 * are stored in the ArrayList, strings.
	 */
	private class Canvas extends JPanel {


		Canvas() {
			setPreferredSize( new Dimension(800,600) );
			setBackground(Color.LIGHT_GRAY);
			setFont( new Font( "Serif", Font.BOLD, 24 ));
		}
		protected void paintComponent(Graphics g) {
			super.paintComponent(g);
			((Graphics2D)g).setRenderingHint(RenderingHints.KEY_ANTIALIASING,
					RenderingHints.VALUE_ANTIALIAS_ON);

			// iterate over the list and for each DrawTextItem object invoke the draw() method
			for (DrawTextItem dti : stringList) {
				if (dti != null) {
					dti.draw(g);
				}
			}
		}
	}

	/**
	 * An object of type MenuHandler is registered as the ActionListener
	 * for all the commands in the menu bar.  The MenuHandler object
	 * simply calls doMenuCommand() when the user selects a command
	 * from the menu.
	 */
	private class MenuHandler implements ActionListener {
		public void actionPerformed(ActionEvent evt) {
			doMenuCommand( evt.getActionCommand());
		}
	}

	/**
	 * Creates a DrawTextPanel.  The panel has a large drawing area and
	 * a text input box where the user can specify a string.  When the
	 * user clicks the drawing area, the string is added to the drawing
	 * area at the point where the user clicked.
	 */
	public DrawTextPanel() {
		fileChooser = new SimpleFileChooser();
		undoMenuItem = new JMenuItem("Remove Item");
		undoMenuItem.setEnabled(false);
		menuHandler = new MenuHandler();
		setLayout(new BorderLayout(3,3));
		setBackground(Color.BLACK);
		setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
		canvas = new Canvas();
		add(canvas, BorderLayout.CENTER);
		JPanel bottom = new JPanel();
		bottom.add(new JLabel("Text to add: "));
		input = new JTextField("Hello World!", 40);
		bottom.add(input);
		add(bottom, BorderLayout.SOUTH);
		canvas.addMouseListener( new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				doMousePress( e );
			}
		} );
	}

	/**
	 * This method is called when the user clicks the drawing area.
	 * A new string is added to the drawing area.  The center of
	 * the string is at the point where the user clicked.
	 * @param e the mouse event that was generated when the user clicked
	 */
	public void doMousePress( MouseEvent e ) {
		String text = input.getText().trim();
		if (text.length() == 0) {
			input.setText("Hello World!");
			text = "Hello World!";
		}
		DrawTextItem s = new DrawTextItem( text, e.getX(), e.getY() );
		s.setTextColor(currentTextColor);  // Default is null, meaning default color of the canvas (black).

//   SOME OTHER OPTIONS THAT CAN BE APPLIED TO TEXT ITEMS:
//		s.setFont( new Font( "Serif", Font.ITALIC + Font.BOLD, 12 ));  // Default is null, meaning font of canvas.
//		s.setMagnification(3);  // Default is 1, meaning no magnification.
//		s.setBorder(true);  // Default is false, meaning don't draw a border.
//		s.setRotationAngle(25);  // Default is 0, meaning no rotation.
//		s.setTextTransparency(0.3); // Default is 0, meaning text is not at all transparent.
//		s.setBackground(Color.BLUE);  // Default is null, meaning don't draw a background area.
//		s.setBackgroundTransparency(0.7);  // Default is 0, meaning background is not transparent.

		// stringList = s;  // Set this string as the ONLY string to be drawn on the canvas!

		stringList.add(s); // adding the input string to be the only string painted to the canvas as a DrawTextItem object
		undoMenuItem.setEnabled(true);
		canvas.repaint();
	}

	/**
	 * Returns a menu bar containing commands that affect this panel.  The menu
	 * bar is meant to appear in the same window that contains this panel.
	 */
	public JMenuBar getMenuBar() {
		if (menuBar == null) {
			menuBar = new JMenuBar();

			String commandKey; // for making keyboard accelerators for menu commands
			if (System.getProperty("mrj.version") == null)
				commandKey = "control ";  // command key for non-Mac OS
			else
				commandKey = "meta ";  // command key for Mac OS

			JMenu fileMenu = new JMenu("File");
			menuBar.add(fileMenu);
			JMenuItem saveItem = new JMenuItem("Save...");
			saveItem.setAccelerator(KeyStroke.getKeyStroke(commandKey + "N"));
			saveItem.addActionListener(menuHandler);
			fileMenu.add(saveItem);
			JMenuItem openItem = new JMenuItem("Open...");
			openItem.setAccelerator(KeyStroke.getKeyStroke(commandKey + "O"));
			openItem.addActionListener(menuHandler);
			fileMenu.add(openItem);
			fileMenu.addSeparator();
			// adding a feature that supports creating and generating a random collage as a way to improve program
			JMenuItem randomCollageItem = new JMenuItem("Random Collage...");
			randomCollageItem.addActionListener(menuHandler);
			fileMenu.add(randomCollageItem);

			JMenuItem saveImageItem = new JMenuItem("Save Image...");
			saveImageItem.addActionListener(menuHandler);
			fileMenu.add(saveImageItem);

			JMenu editMenu = new JMenu("Edit");
			menuBar.add(editMenu);
			undoMenuItem.addActionListener(menuHandler); // undoItem was created in the constructor
			undoMenuItem.setAccelerator(KeyStroke.getKeyStroke(commandKey + "Z"));
			editMenu.add(undoMenuItem);
			editMenu.addSeparator();
			JMenuItem clearItem = new JMenuItem("Clear");
			clearItem.addActionListener(menuHandler);
			editMenu.add(clearItem);

			JMenu optionsMenu = new JMenu("Options");
			menuBar.add(optionsMenu);
			JMenuItem colorItem = new JMenuItem("Set Text Color...");
			colorItem.setAccelerator(KeyStroke.getKeyStroke(commandKey + "T"));
			colorItem.addActionListener(menuHandler);
			optionsMenu.add(colorItem);
			JMenuItem bgColorItem = new JMenuItem("Set Background Color...");
			bgColorItem.addActionListener(menuHandler);
			optionsMenu.add(bgColorItem);

		}
		return menuBar;
	}

	/**
	 * Carry out one of the commands from the menu bar.
	 * @param command the text of the menu command.
	 */
	private void doMenuCommand(String command) {
		if (command.equals("Save...")) { // save all the string info to a file
			// JOptionPane.showMessageDialog(this, "Sorry, the Save command is not implemented.");


			// adding code to support writing data to file and saving the collage
			// code that supports the save command
			try {

				StringBuilder sb = new StringBuilder();
				Color c = canvas.getBackground();

				// store the background color RGB values and number of strings on the first line
				sb.append(c.getRed()).append(" ");
				sb.append(c.getGreen()).append(" ");
				sb.append(c.getBlue()).append(" ");

				// store the number of strings, prep with new row
				sb.append(stringList.size()).append("\n");

				// store RGB values and X, Y coordinates on the first line
				// followed by the text value
				for (DrawTextItem item : stringList) {
					c = item.getTextColor();

					// store true RGB value of text
					sb.append(c.getRed()).append(" ");
					sb.append(c.getGreen()).append(" ");
					sb.append(c.getBlue()).append(" ");

					// store X, Y coordinates
					sb.append(item.getX()).append(" ");
					sb.append(item.getY()).append(" ");

					// store the string
					sb.append(getString()).append("\n");

				}

				// write the data to a file

				File textFile = fileChooser.getOutputFile(this, "Select Text File Name", "textimage.txt");

				if (textFile == null) {
					return;
				}

				PrintWriter pw = new PrintWriter(textFile);
				pw.write(sb.toString());

				// close file
				pw.close();
			}

			catch (Exception e) {

				JOptionPane.showMessageDialog(this, "Sorry, but an error occurred while trying to save the image:\n" + e);
			}
		}


		else if (command.equals("Open...")) { // read a previously saved file, and reconstruct the list of strings
			// JOptionPane.showMessageDialog(this, "Sorry, the Open command is not implemented.");

			// adding code to support the open file command
			File openedFile = fileChooser.getInputFile(this, "Select Text File Name");

			Color backgroundColor = null;

			try {

				Scanner sc = new Scanner(openedFile);

				int r, g, b = 0;
				Color textColor;

				int num = 0;

				// read the background color RGB values
				r = sc.nextInt();
				g = sc.nextInt();
				b = sc.nextInt();

				backgroundColor = new Color(r, g, b);

				// read the number of strings on the collage
				num = sc.nextInt();
				stringList.clear();

				// iterate over and then create the DrawTextItem objects
				for (int i = 0; i < 1; i++) {
					// extract RGB
					r = sc.nextInt();
					g = sc.nextInt();
					b = sc.nextInt();

					// extract X and Y coordinates
					int x = sc.nextInt();
					int y = sc.nextInt();

					// skip to a new line after previous integers
					sc.skip("[\r\n]+");

					// extract string
					String s =  sc.nextLine();

					// update data with retrieved data
					textColor = new Color(r, g, b);

					// create new data with extracted X, Y coordinates and the string
					DrawTextItem item = new DrawTextItem(s, x, y);
					item.setTextColor(textColor);
					stringList.add(item);
				}

				// close file
				sc.close();
			}

			catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			// update canvas background color
			canvas.setBackground(backgroundColor);

			canvas.repaint(); // (you'll need this to make the new list of strings take effect)
		}
		else if (command.equals("Random Collage...")) { // generate a random collage in the drawing area

			// list of some words that will be used by the random collage
			List<String> words = Array.asList(
			"happy",
			"sad",
			"sunshine",
			"mountains",
			"ego",
			"soul",
			"howdy",
			"pika",
			"marmot",
			"badger",
			"birds",
			"clouds",
			"scree",
			"gentle",
			"meaning",
			"clueless",
			"brave",
			"STRONG",
			"gully",
			"elevation",
			"dizzy",
			"euphoria",
			"LOVE");

			// randomize the generating of the words from the list of words
			java.util.Collections.shuffle(words);

			Random rand = new Random();

			// use only a few words from the list
			int n = rand.nextInt(words.size());

			// background color of the collage
			Color backgroundColor = null;
			int r, g, b = 0;

			// text color of the collage
			Color textColor = null;

			// generate the background color RGB values randomly
			r = rand.nextInt(255);
			g = rand.nextInt(255);
			b = rand.nextInt(255);

			backgroundColor = new Color(r, g, b);

			stringList.clear();

			// iterate until the strings are present and new DrawTextItem object has been created
			for (int i =0; i < n; i++) {
				// generate text color RGB randomly
				r = rand.nextInt(255);
				g = rand.nextInt(255);
				b = rand.nextInt(255);

				textColor = new Color(r, g, b);

				// generate X and Y coordinates for text randomly
				int x = rand.nextInt(this.getWidth());
				int y = rand.nextInt(this.getHeight());

				// read the string from the list of strings
				String s = words.get(i);

				DrawTextItem item = new DrawTextItem(s, x, y);
				item.setTextColor(textColor);
				stringList.add(item);
			}

			// update background color
			canvas.setBackground(backgroundColor);
			// random list of string can take effect now
			canvas.repaint();

		}
		else if (command.equals("Clear")) {  // remove all strings
			stringList = null;   // Remove the ONLY string from the canvas.
			undoMenuItem.setEnabled(false);
			canvas.repaint();
		}
		else if (command.equals("Remove Item")) { // remove the most recently added string
			stringList = null;   // Remove the ONLY string from the canvas.
			undoMenuItem.setEnabled(false);
			canvas.repaint();
		}
		else if (command.equals("Set Text Color...")) {
			Color c = JColorChooser.showDialog(this, "Select Text Color", currentTextColor);
			if (c != null)
				currentTextColor = c;
		}
		else if (command.equals("Set Background Color...")) {
			Color c = JColorChooser.showDialog(this, "Select Background Color", canvas.getBackground());
			if (c != null) {
				canvas.setBackground(c);
				canvas.repaint();
			}
		}
		else if (command.equals("Save Image...")) {  // save a PNG image of the drawing area
			File imageFile = fileChooser.getOutputFile(this, "Select Image File Name", "textimage.png");
			if (imageFile == null)
				return;
			try {
				// Because the image is not available, I will make a new BufferedImage and
				// draw the same data to the BufferedImage as is shown in the panel.
				// A BufferedImage is an image that is stored in memory, not on the screen.
				// There is a convenient method for writing a BufferedImage to a file.
				BufferedImage image = new BufferedImage(canvas.getWidth(),canvas.getHeight(),
						BufferedImage.TYPE_INT_RGB);
				Graphics g = image.getGraphics();
				g.setFont(canvas.getFont());
				canvas.paintComponent(g);  // draws the canvas onto the BufferedImage, not the screen!
				boolean ok = ImageIO.write(image, "PNG", imageFile); // write to the file
				if (ok == false)
					throw new Exception("PNG format not supported (this shouldn't happen!).");
			}
			catch (Exception e) {
				JOptionPane.showMessageDialog(this,
						"Sorry, an error occurred while trying to save the image:\n" + e);
			}
		}
	}

}
