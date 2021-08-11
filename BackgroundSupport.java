import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;

public class BackgroundSupport {

    private final DrawPanel drawPanel;
    private final SimpleFileChooser fileChooser;
    private final JCheckBoxMenuItem gradientOverlayCheckbox;
    String[] bkOptions = {"Mandelbrot", "Earthrise", "Sunset", "Cloud", "Eagle_nebula"};
    String[] extraBkOptions = {"Custom...", "Color..."};

    public BackgroundSupport(DrawPanel drawPanel, SimpleFileChooser fileChooser, JCheckBoxMenuItem overlayCheckbox) {
        this.drawPanel = drawPanel;
        this.fileChooser = fileChooser;
        this.gradientOverlayCheckbox = overlayCheckbox;
    }

    JMenu makeMenu() {
        JMenu menu = new JMenu("Background");
        for (String opt : bkOptions) {
            menu.add(new ChooseBackgroundAction(opt));
        }
        menu.addSeparator();
        for (String extraOpt : extraBkOptions) {
            menu.add(new ChooseBackgroundAction(extraOpt));
            menu.addSeparator();
        }
        menu.add(gradientOverlayCheckbox);
        gradientOverlayCheckbox.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                if (gradientOverlayCheckbox.isSelected())
                    drawPanel.setGradientOverlayColor(Color.WHITE);
                else
                    drawPanel.setGradientOverlayColor(null);
            }
        });
        return menu;
    }

    // a toolbar method that creates a toolbar with actions of type ChooseBackgroundAction to the toolbar
    // this is done for the menu in MakeBackgroundMenu

    public JToolBar makeToolbar() {
        JToolBar backgroundToolbar = new JToolBar(JToolBar.HORIZONTAL);
        for (String opt : bkOptions) {
            backgroundToolbar.add(new ChooseBackgroundAction(opt));
        }
        backgroundToolbar.addSeparator();
        for (String extraOpt : extraBkOptions) {
            backgroundToolbar.add(new ChooseBackgroundAction(extraOpt));
            backgroundToolbar.addSeparator(new Dimension(15, 0));
        }
        return backgroundToolbar;
    }

    /**
     * An object of type ChooseBackgroundAction represents an action through which the
     * user selects the background of the picture.  There are three types of background:
     * solid color background ("Color..." command), an image selected by the user from
     * the file system ("Custom..." command), and four built-in image resources
     * (Mandelbrot, Earthrise, Sunset, and Eagle_nebula).
     */
    private class ChooseBackgroundAction extends AbstractAction {
        String text;

        ChooseBackgroundAction(String text) {
            super(text);
            this.text = text;

            if (!text.equals("Custom...") && !text.equals("Color...")) {
                putValue(Action.SMALL_ICON,
                        Util.iconFromResource("resources/images/" + text.toLowerCase() + "_thumbnail.jpeg"));
            }
            if (text.equals("Color...")) {
                putValue(Action.SMALL_ICON, new ImageIcon(makeColorIcon()));
                putValue(Action.SHORT_DESCRIPTION, "<html>Use a solid color for background<br>instead of an image.</html>");
            } else if (text.equals("Custom...")) {
                putValue(Action.SMALL_ICON,
                        Util.iconFromResource("resources/action_icons/fileopen.png"));
            } else {
                putValue(Action.SHORT_DESCRIPTION, "Use this image as the background.");
            }
        }

        private Color randomColor() {
            return new Color((int) (Math.random() * 255),
                    (int) (Math.random() * 255),
                    (int) (Math.random() * 255));
        }

        private BufferedImage makeColorIcon() {
            BufferedImage icon = new BufferedImage(32, 32, BufferedImage.TYPE_INT_RGB);
            Graphics g = icon.createGraphics();
            g.setColor(Color.WHITE);
            for (int x = 0; x < 33; x += 4) {
                for (int y = 0; y < 33; y += 4) {
                    g.fillRect(x, y, 4, 4);
                    g.setColor(randomColor());
                }
            }
            g.dispose();
            return icon;
        }

        public void actionPerformed(ActionEvent evt) {
            if (text.equals("Custom...")) {
                File inputFile = fileChooser.getInputFile(drawPanel, "Select Background Image");
                if (inputFile != null) {
                    try {
                        BufferedImage img = ImageIO.read(inputFile);
                        if (img == null)
                            throw new Exception();
                        drawPanel.setBackgroundImage(img);
                    } catch (Exception e) {
                        JOptionPane.showMessageDialog(drawPanel, "Sorry, couldn't read the file.");
                    }
                }
            } else if (text.equals("Color...")) {
                Color c = JColorChooser.showDialog(drawPanel, "Select Color for Background", drawPanel.getBackground());
                if (c != null) {
                    drawPanel.setBackground(c);
                    drawPanel.setBackgroundImage(null);
                }
            } else {
                Image bg = Util.getImageResource("resources/images/" + text.toLowerCase() + ".jpeg");
                drawPanel.setBackgroundImage(bg);
            }
        }
    }
}
