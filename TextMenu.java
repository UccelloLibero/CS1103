package guidemo;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;;

/**
 * A menu full of commands that affect the text shown
 * in a DrawPanel.
 */
public class TextMenu extends JMenu {

	private final DrawPanel panel;    // the panel whose text is controlled by this menu

	private JCheckBoxMenuItem bold;   // controls whether the text is bold or not.
	private JCheckBoxMenuItem italic; // controls whether the text is italic or not.
	private JMenu justify; // controls the justification of text.
	/**
	 * Constructor creates all the menu commands and adds them to the menu.
	 * @param owner the panel whose text will be controlled by this menu.
	 */
	public TextMenu(DrawPanel owner) {
		super("Text");
		this.panel = owner;
		final JMenuItem change = new JMenuItem("Change Text...");
		change.addActionListener( new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				String currentText = panel.getTextItem().getText();
				String newText = GetTextDialog.showDialog(panel,currentText);
				if (newText != null && newText.trim().length() > 0) {
					panel.getTextItem().setText(newText);
					panel.repaint();
				}
			}
		});
		final JMenuItem size = new JMenuItem("Set Size...");
		size.addActionListener( new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				int currentSize = panel.getTextItem().getFontSize();
				String s = JOptionPane.showInputDialog(panel, "What font size do you want to use?",currentSize);
				if (s != null && s.trim().length() > 0) {
					try {
						int newSize = Integer.parseInt(s.trim()); // can throw NumberFormatException
						panel.getTextItem().setFontSize(newSize); // can throw IllegalArgumentException
						panel.repaint();
					}
					catch (Exception e) {
						JOptionPane.showMessageDialog(panel, s + " is not a legal text size.\n"
								+"Please enter a positive integer.");
					}
				}
			}
		});

		// adding code that supports line height setup in order to support text justification
		// and also personalizing the program
		final JMenuItem lineHeight = new JMenuItem("Set Line Height...");
		lineHeight.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				int currentLineHeight = (int) panel.getTextItem().getLineHeightMultiplier();
				String s = JOptionPane.showInputDialog(panel, "Please choose a integer value for your prefered line height.", currentLineHeight);
				if (s != null && s.trim().length() > 0) {
					try {
						int newLineHeight = Integer.parseInt(s.trim());
						panel.getTextItem().setLineHeightMultiplier(newLineHeight);
						panel.repaint();
					}
					catch (Exception e) {
						JOptionPane.showMessageDialog(panel, s + " is not a legal line height size.\n" + "Please enter a positive integer.");
					}
				}
			}
		});

		final JMenuItem color = new JMenuItem("Set Color...");
		color.addActionListener( new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				Color currentColor = panel.getTextItem().getColor();
				Color newColor = JColorChooser.showDialog(panel, "Select Text Color", currentColor);
				if (newColor != null) {
					panel.getTextItem().setColor(newColor);
					panel.repaint();
				}
			}
		});
		italic = new JCheckBoxMenuItem("Italic");
		italic.addActionListener( new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				panel.getTextItem().setItalic(italic.isSelected());
				panel.repaint();
			}
		});
		bold = new JCheckBoxMenuItem("Bold");
		bold.addActionListener( new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				panel.getTextItem().setBold(bold.isSelected());
				panel.repaint();
			}
		});

		// preparing for adding makeJustifyMenu() method;
		justify = makeJustifyMenu();

		add(change);
		addSeparator();
		add(size);
		add(lineHeight);
		add(color);
		add(italic);
		add(bold);
		addSeparator();
		add(justify);
		add(makeFontNameSubmenu());
	}

	/**
	 * Reset the state of the menu to reflect the default settings for text
	 * in a DrawPanel.  (Sets the italic and bold checkboxes to unselected.)
	 * This method is called by the main program when the user selects the
	 * "New" command, to make sure that the menu state reflects the contents
	 * of the panel.
	 */
	public void setDefaults() {
		italic.setSelected(false);
		bold.setSelected(false);
		// adding functionality to support justify button logic
		justify.getItem(0).setSelected(true);
	}

	/**
	 * Adding a submenu with a group of three buttons that can be used to
	 * control the text justification.
	 * Using a switch statement with two cases and default for one option:
	 * case 1 left, case 2 is center and default is right.
	*/
	private JMenu makeJustifyMenu() {
        ActionListener justifyContentAction = evt -> {
        	String cmd = evt.getActionCommand();
        	switch (cmd) {
				case "Left":
					panel.getTextItem().setJustify(TextItem.LEFT);
					break;
				case "Center":
					panel.getTextItem().setJustify(TextItem.CENTER);
					break;
				default:
					panel.getTextItem().setJustify(TextItem.RIGHT);
			}
			panel.repaint();
		};

		// adding action listener to each button
		ButtonGroup group = new ButtonGroup();
    JMenu menu = new JMenu("Justify");
    String[] basic = {"Left", "Center", "Right"};
    for (String label : basic) {
			JRadioButtonMenuItem item = new JRadioButtonMenuItem(label);
      item.setActionCommand(label);
      item.addActionListener(justifyContentAction);
      item.setFont(new Font(label, Font.PLAIN, 12));
      menu.add(item);
      group.add(item);
		}
    return menu;
    }

	/**
	 * Create a menu containing a list of all available fonts.
	 * (It turns out this can be very messy, at least on Linux, but
	 * it does show the use what is available and lets the user try
	 * everything!)
	 */
	private JMenu makeFontNameSubmenu() {
		ActionListener setFontAction = new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				panel.getTextItem().setFontName(evt.getActionCommand());
				panel.repaint();
			}
		};
		JMenu menu = new JMenu("Font Name");
		String[] basic = { "Serif", "SansSerif", "Monospace" };
		for (String f : basic) {
			JMenuItem m = new JMenuItem(f+ " Default");
			m.setActionCommand(f);
			m.addActionListener(setFontAction);
			m.setFont(new Font(f,Font.PLAIN,12));
			menu.add(m);
		}
		menu.addSeparator();
		String[] fonts = GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();
		if (fonts.length <= 20) {
			for (String f : fonts) {
				JMenuItem m = new JMenuItem(f);
				m.addActionListener(setFontAction);
				m.setFont(new Font(f,Font.PLAIN,12));
				menu.add(m);
			}
		}
		else { //Too many items for one menu; divide them into several sub-sub-menus.
			char ch1 = 'A';
			char ch2 = 'A';
			JMenu m = new JMenu();
			int i = 0;
			while (i < fonts.length) {
				while (i < fonts.length && (Character.toUpperCase(fonts[i].charAt(0)) <= ch2 || ch2 == 'Z')) {
					JMenuItem item = new JMenuItem(fonts[i]);
					item.addActionListener(setFontAction);
					item.setFont(new Font(fonts[i],Font.PLAIN,12));
					m.add(item);
					i++;
				}
				if (i == fonts.length || (m.getMenuComponentCount() >= 12 && i < fonts.length-4)) {
					if (ch1 == ch2)
						m.setText("" + ch1);
					else
						m.setText(ch1 + " to " + ch2);
					menu.add(m);
					if (i < fonts.length)
						m = new JMenu();
					ch2++;
					ch1 = ch2;
				}
				else
					ch2++;
			}
		}
		return menu;
	}


}
