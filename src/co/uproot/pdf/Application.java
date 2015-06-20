package co.uproot.pdf;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.filechooser.FileNameExtensionFilter;

public class Application {

  protected PDFComponent c = new PDFComponent();
  private final JTextField jtf = new JTextField(25);

  public void init() {
    final JFrame f = new JFrame();
    f.getContentPane().setLayout(new BoxLayout(f.getContentPane(), BoxLayout.Y_AXIS));
    final JPanel buttons = new JPanel();
    buttons.setLayout(new BoxLayout(buttons, BoxLayout.X_AXIS));
    f.add(buttons);
    f.add(c);
    buttons.add(initOpen());
    buttons.add(jtf);
    buttons.add(openURL());
    f.pack();
    final Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
    f.setSize(screen.width, screen.height);
    f.setLocationRelativeTo(null);
    f.setVisible(true);
    jtf.setMaximumSize(jtf.getPreferredSize());
    f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
  }

  public JButton openURL() {
    final JButton openurl = new JButton();

    openurl.setIcon(new ImageIcon(getClass().getResource("/org/jpedal/examples/viewer/res/open.gif"))); //$NON-NLS-1$
    openurl.setText("Open");
    openurl.setToolTipText("Open a URL");
    openurl.setBorderPainted(false);

    openurl.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(final ActionEvent e) {

        try {
          final URL u = new URL(jtf.getText());
          final URLConnection uc = u.openConnection();
          final InputStream is = uc.getInputStream();
          c.showFile(is);
        } catch (final MalformedURLException e1) {
          e1.printStackTrace();
        } catch (final IOException e1) {
          e1.printStackTrace();
        }

      }

    });
    return openurl;
  }

  public JButton initOpen() {
    final JButton open = new JButton();

    open.setIcon(new ImageIcon(getClass().getResource("/org/jpedal/examples/viewer/res/open.gif"))); //$NON-NLS-1$
    open.setText("OpenFile");
    open.setToolTipText("Open a file");
    open.setBorderPainted(false);

    open.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(final ActionEvent e) {

        selectFile();

      }

    });
    return open;
  }

  public void selectFile() {

    final JFileChooser open = new JFileChooser(".");
    open.setFileSelectionMode(JFileChooser.FILES_ONLY);
    open.addChoosableFileFilter(new FileNameExtensionFilter("PDF Documents", "pdf"));
    open.setAcceptAllFileFilterUsed(true);
    open.showOpenDialog(open);
    final File currentFile = new File(open.getSelectedFile().getAbsolutePath());

    try {
      final InputStream is = new FileInputStream(currentFile);
      c.showFile(is);
    } catch (final FileNotFoundException e) {
      e.printStackTrace();
    }

  }

  public static void main(final String[] args) {
    final Application a = new Application();
    a.init();
  }

}
