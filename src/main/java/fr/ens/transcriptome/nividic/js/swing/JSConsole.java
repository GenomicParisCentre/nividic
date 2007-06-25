/*
 *                      Nividic development code
 *
 * This code may be freely distributed and modified under the
 * terms of the GNU Lesser General Public Licence.  This should
 * be distributed with the code.  If you do not have a copy,
 * see:
 *
 *      http://www.gnu.org/copyleft/lesser.html
 *
 * Copyright for this code is held jointly by the microarray platform
 * of the École Normale Supérieure and the individual authors.
 * These should be listed in @author doc comments.
 *
 * For more information on the Nividic project and its aims,
 * or to join the Nividic mailing list, visit the home page
 * at:
 *
 *      http://www.transcriptome.ens.fr/nividic
 *
 */

package fr.ens.transcriptome.nividic.js.swing;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;

import javax.swing.ButtonGroup;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import fr.ens.transcriptome.nividic.js.JSShell;

public class JSConsole extends JFrame implements ActionListener {
  static final long serialVersionUID = 2551225560631876300L;

  private File CWD;
  private JFileChooser dlg;
  private ConsoleTextArea consoleTextArea;

  public String chooseFile() {
    if (CWD == null) {
      String dir = System.getProperty("user.dir");
      if (dir != null) {
        CWD = new File(dir);
      }
    }
    if (CWD != null) {
      dlg.setCurrentDirectory(CWD);
    }
    dlg.setDialogTitle("Select a file to load");
    int returnVal = dlg.showOpenDialog(this);
    if (returnVal == JFileChooser.APPROVE_OPTION) {
      String result = dlg.getSelectedFile().getPath();
      CWD = new File(dlg.getSelectedFile().getParent());
      return result;
    }
    return null;
  }

  public static void main(String args[]) {

    new JSConsole(args);
  }

  public void createFileChooser() {
    dlg = new JFileChooser();
    javax.swing.filechooser.FileFilter filter = new javax.swing.filechooser.FileFilter() {
      public boolean accept(File f) {
        if (f.isDirectory()) {
          return true;
        }
        String name = f.getName();
        int i = name.lastIndexOf('.');
        if (i > 0 && i < name.length() - 1) {
          String ext = name.substring(i + 1).toLowerCase();
          if (ext.equals("js")) {
            return true;
          }
        }
        return false;
      }

      public String getDescription() {
        return "JavaScript Files (*.js)";
      }
    };
    dlg.addChoosableFileFilter(filter);

  }

  public JSConsole(String[] args) {
    super("Rhino JavaScript Console");
    JMenuBar menubar = new JMenuBar();
    createFileChooser();
    String[] fileItems = {"Load...", "Exit"};
    String[] fileCmds = {"Load", "Exit"};
    char[] fileShortCuts = {'L', 'X'};
    String[] editItems = {"Cut", "Copy", "Paste"};
    char[] editShortCuts = {'T', 'C', 'P'};
    String[] plafItems = {"Metal", "Windows", "Motif"};
    boolean[] plafState = {true, false, false};
    JMenu fileMenu = new JMenu("File");
    fileMenu.setMnemonic('F');
    JMenu editMenu = new JMenu("Edit");
    editMenu.setMnemonic('E');
    JMenu plafMenu = new JMenu("Platform");
    plafMenu.setMnemonic('P');
    for (int i = 0; i < fileItems.length; ++i) {
      JMenuItem item = new JMenuItem(fileItems[i], fileShortCuts[i]);
      item.setActionCommand(fileCmds[i]);
      item.addActionListener(this);
      fileMenu.add(item);
    }
    for (int i = 0; i < editItems.length; ++i) {
      JMenuItem item = new JMenuItem(editItems[i], editShortCuts[i]);
      item.addActionListener(this);
      editMenu.add(item);
    }
    ButtonGroup group = new ButtonGroup();
    for (int i = 0; i < plafItems.length; ++i) {
      JRadioButtonMenuItem item = new JRadioButtonMenuItem(plafItems[i],
          plafState[i]);
      group.add(item);
      item.addActionListener(this);
      plafMenu.add(item);
    }
    menubar.add(fileMenu);
    menubar.add(editMenu);
    menubar.add(plafMenu);
    setJMenuBar(menubar);
    consoleTextArea = new ConsoleTextArea(args);
    JScrollPane scroller = new JScrollPane(consoleTextArea);
    setContentPane(scroller);
    consoleTextArea.setRows(24);
    consoleTextArea.setColumns(80);
    addWindowListener(new WindowAdapter() {
      public void windowClosing(WindowEvent e) {
        System.exit(0);
      }
    });
    pack();
    setVisible(true);
    // System.setIn(consoleTextArea.getIn());
    // System.setOut(consoleTextArea.getOut());
    // System.setErr(consoleTextArea.getErr());

    System.setOut(consoleTextArea.getOut());
    System.setErr(consoleTextArea.getErr());

    // BootStrap.bootstrap();

    // System.out.println("out.println(fs.ls()[0])");

    final JSShell shell = new JSShell();

    shell.shell(consoleTextArea.getIn(), consoleTextArea.getOut(),
        consoleTextArea.getErr());

    /*
     * Main.setIn(consoleTextArea.getIn());
     * Main.setOut(consoleTextArea.getOut());
     * Main.setErr(consoleTextArea.getErr()); Main.main();
     */
  }

  public void actionPerformed(ActionEvent e) {
    String cmd = e.getActionCommand();
    String plaf_name = null;
    if (cmd.equals("Load")) {
      String f = chooseFile();
      if (f != null) {
        f = f.replace('\\', '/');
        consoleTextArea.eval("load(\"" + f + "\");");
      }
    } else if (cmd.equals("Exit")) {
      System.exit(0);
    } else if (cmd.equals("Cut")) {
      consoleTextArea.cut();
    } else if (cmd.equals("Copy")) {
      consoleTextArea.copy();
    } else if (cmd.equals("Paste")) {
      consoleTextArea.paste();
    } else {
      if (cmd.equals("Metal")) {
        plaf_name = "javax.swing.plaf.metal.MetalLookAndFeel";
      } else if (cmd.equals("Windows")) {
        plaf_name = "com.sun.java.swing.plaf.windows.WindowsLookAndFeel";
      } else if (cmd.equals("Motif")) {
        plaf_name = "com.sun.java.swing.plaf.motif.MotifLookAndFeel";
      }
      if (plaf_name != null) {
        try {
          UIManager.setLookAndFeel(plaf_name);
          SwingUtilities.updateComponentTreeUI(this);
          consoleTextArea.postUpdateUI();
          // updateComponentTreeUI seems to mess up the file
          // chooser dialog, so just create a new one
          createFileChooser();
        } catch (Exception exc) {
          JOptionPane.showMessageDialog(this, exc.getMessage(), "Platform",
              JOptionPane.ERROR_MESSAGE);
        }
      }
    }

  }

};
