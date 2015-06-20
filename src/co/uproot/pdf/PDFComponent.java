package co.uproot.pdf;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.InputStream;
import java.net.URL;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

import org.jpedal.PdfDecoder;
import org.jpedal.exception.PdfException;
import org.jpedal.fonts.FontMappings;

public class PDFComponent extends JPanel {

  private final PdfDecoder pdfDecoder;
  private InputStream currentIs;
  private int currentPage = 1;

  private final JLabel pageCounter1 = new JLabel("Page ");
  private final JTextField pageCounter2 = new JTextField(4);

  private final JLabel pageCounter3 = new JLabel("of");

  public PDFComponent() {

    pdfDecoder = new PdfDecoder(true);

    FontMappings.setFontReplacements();
    initializeViewer();

  }

  public void showFile(final InputStream is) {

    try {
      pdfDecoder.closePdfFile();
      pdfDecoder.openPdfFileFromInputStream(is, false);
      if (!checkEncryption()) {
      }
      pdfDecoder.decodePage(currentPage);
      pdfDecoder.setPageParameters(1, 1);
      pdfDecoder.invalidate();
      pageCounter2.setText(String.valueOf(currentPage));
      pageCounter3.setText("of " + pdfDecoder.getPageCount());

    } catch (final Exception e) {

      e.printStackTrace();
    }
    currentIs = is;

  }

  private JScrollPane initPDFDisplay() {
    final JScrollPane currentScroll = new JScrollPane();
    currentScroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
    currentScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

    currentScroll.setViewportView(pdfDecoder);

    return currentScroll;
  }

  private void initializeViewer() {

    setLayout(new BorderLayout());
    final JPanel toolbarPanel = new JPanel();
    final Component[] itemsToAdd = initChangerPanel();

    for (final Component anItemsToAdd : itemsToAdd) {
      toolbarPanel.add(anItemsToAdd);
    }
    add(toolbarPanel, BorderLayout.NORTH);

    final JScrollPane display = initPDFDisplay();
    add(display, BorderLayout.CENTER);

  }

  private boolean checkEncryption() {
    if (pdfDecoder.isEncrypted()) {

      while (!pdfDecoder.isFileViewable()) {

        final String password = JOptionPane.showInputDialog(this, "Please enter password");

        if (password != null) {
          try {
            pdfDecoder.setEncryptionPassword(password);
          } catch (final PdfException e) {
            e.printStackTrace();

          }

        }
      }
      return true;
    }

    return true;
  }

  private Component[] initChangerPanel() {

    final Component[] list = new Component[11];

    /** back to page 1 */
    final JButton start = new JButton();
    start.setBorderPainted(false);
    final URL startImage = getClass().getResource("/org/jpedal/examples/viewer/res/start.gif");
    start.setIcon(new ImageIcon(startImage));
    start.setToolTipText("Rewind to page 1");
    // currentBar1.add(start);
    list[0] = start;
    start.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(final ActionEvent e) {
        if (currentIs != null && currentPage != 1) {
          currentPage = 1;
          try {
            pdfDecoder.decodePage(currentPage);
            pdfDecoder.invalidate();
            repaint();
          } catch (final Exception e1) {
            System.err.println("back to page 1");
            e1.printStackTrace();
          }

          // set page number display
          pageCounter2.setText(String.valueOf(currentPage));
        }
      }
    });

    /** back 10 icon */
    final JButton fback = new JButton();
    fback.setBorderPainted(false);
    final URL fbackImage = getClass().getResource("/org/jpedal/examples/viewer/res/fback.gif");
    fback.setIcon(new ImageIcon(fbackImage));
    fback.setToolTipText("Rewind 10 pages");
    list[1] = fback;
    fback.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(final ActionEvent e) {
        if (currentIs != null && currentPage > 10) {
          currentPage -= 10;
          try {
            pdfDecoder.decodePage(currentPage);
            pdfDecoder.invalidate();
            repaint();
          } catch (final Exception e1) {
            System.err.println("back 10 pages");
            e1.printStackTrace();
          }

          // set page number display
          pageCounter2.setText(String.valueOf(currentPage));
        }
      }
    });

    /** back icon */
    final JButton back = new JButton();
    back.setBorderPainted(false);
    final URL backImage = getClass().getResource("/org/jpedal/examples/viewer/res/back.gif");
    back.setIcon(new ImageIcon(backImage));
    back.setToolTipText("Rewind one page");
    list[2] = back;
    back.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(final ActionEvent e) {
        if (currentIs != null && currentPage > 1) {
          currentPage -= 1;
          try {
            pdfDecoder.decodePage(currentPage);
            pdfDecoder.invalidate();
            repaint();
          } catch (final Exception e1) {
            System.err.println("back 1 page");
            e1.printStackTrace();
          }

          // set page number display
          pageCounter2.setText(String.valueOf(currentPage));
        }
      }
    });

    pageCounter2.setEditable(true);
    pageCounter2.addActionListener(new ActionListener() {

      @Override
      public void actionPerformed(final ActionEvent a) {

        final String value = pageCounter2.getText().trim();
        final int newPage;

        // allow for integer values
        try {
          newPage = Integer.parseInt(value);

          if ((newPage > pdfDecoder.getPageCount()) | (newPage < 1)) {
            return;
          }

          currentPage = newPage;
          try {
            pdfDecoder.decodePage(currentPage);
            pdfDecoder.invalidate();
            repaint();
          } catch (final Exception e) {
            System.err.println("page number entered");
            e.printStackTrace();
          }

        } catch (final Exception e) {
          JOptionPane.showMessageDialog(null,
              '>' + value + "< is Not a valid Value.\nPlease enter a number between 1 and " + pdfDecoder.getPageCount() + ' ' + e);
        }

      }

    });

    /** put page count in middle of forward and back */
    list[3] = pageCounter1;
    list[4] = new JPanel();
    list[5] = pageCounter2;
    list[6] = new JPanel();
    list[7] = pageCounter3;

    /** forward icon */
    final JButton forward = new JButton();
    forward.setBorderPainted(false);
    final URL fowardImage = getClass().getResource("/org/jpedal/examples/viewer/res/forward.gif");
    forward.setIcon(new ImageIcon(fowardImage));
    forward.setToolTipText("forward 1 page");
    list[8] = forward;
    forward.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(final ActionEvent e) {
        if (currentIs != null && currentPage < pdfDecoder.getPageCount()) {
          currentPage += 1;
          try {
            pdfDecoder.decodePage(currentPage);
            pdfDecoder.invalidate();
            repaint();
          } catch (final Exception e1) {
            System.err.println("forward 1 page");
            e1.printStackTrace();
          }

          // set page number display
          pageCounter2.setText(String.valueOf(currentPage));
        }
      }
    });

    /** fast forward icon */
    final JButton fforward = new JButton();
    fforward.setBorderPainted(false);
    final URL ffowardImage = getClass().getResource("/org/jpedal/examples/viewer/res/fforward.gif");
    fforward.setIcon(new ImageIcon(ffowardImage));
    fforward.setToolTipText("Fast forward 10 pages");
    list[9] = fforward;
    fforward.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(final ActionEvent e) {
        if (currentIs != null && currentPage < pdfDecoder.getPageCount() - 9) {
          currentPage += 10;
          try {
            pdfDecoder.decodePage(currentPage);
            pdfDecoder.invalidate();
            repaint();
          } catch (final Exception e1) {
            System.err.println("forward 10 pages");
            e1.printStackTrace();
          }

          // set page number display
          pageCounter2.setText(String.valueOf(currentPage));
        }
      }
    });

    /** goto last page */
    final JButton end = new JButton();
    end.setBorderPainted(false);
    final URL endImage = getClass().getResource("/org/jpedal/examples/viewer/res/end.gif");
    end.setIcon(new ImageIcon(endImage));
    end.setToolTipText("Fast forward to last page");
    list[10] = end;
    end.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(final ActionEvent e) {
        if (currentIs != null && currentPage < pdfDecoder.getPageCount()) {
          currentPage = pdfDecoder.getPageCount();
          try {
            pdfDecoder.decodePage(currentPage);
            pdfDecoder.invalidate();
            repaint();
          } catch (final Exception e1) {
            System.err.println("forward to last page");
            e1.printStackTrace();
          }

          // set page number display
          pageCounter2.setText(String.valueOf(currentPage));
        }
      }
    });

    return list;
  }

}
