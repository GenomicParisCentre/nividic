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

package fr.ens.transcriptome.nividic.util.gui;

import java.awt.BorderLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

/**
 * This class define a widget which allow to add and remove unique elements.
 * This cames from Sun JList tutorial.
 * @author Laurent Jourdren
 */
public class AddRemoveListWidget extends JPanel implements
    ListSelectionListener {

  private JList list;
  private DefaultListModel listModel;

  private static final String addString = "Add";
  private static final String removeString = "Remove";
  private JButton removeButton;
  private JTextField employeeName;

  private static final int TEXTFIELD_LENGTH = 10;
  private static final int MARGIN = 5;

  /**
   * Public constructor.
   */
  public AddRemoveListWidget() {
    this(null);
  }

  /**
   * Public constructor.
   * @param elements elements of the list to add
   */
  public AddRemoveListWidget(final String[] elements) {

    super(new BorderLayout());

    listModel = new DefaultListModel();

    if (elements != null) {

      for (int i = 0; i < elements.length; i++) {

        String e = elements[i];

        if (e != null) {
          e = e.trim();
          if ("".equals(e))
            e = " ";

          if (!listModel.contains(e))
            listModel.addElement(e);
        }
      }

      //If we just wanted to add to the end, we'd do this:
      //listModel.addElement(employeeName.getText());

      // Select the new item and make it visible.
      // list.setSelectedIndex(index);
      // list.ensureIndexIsVisible(index);
    }

    //Create the list and put it in a scroll pane.
    list = new JList(listModel);
    list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    list.setSelectedIndex(0);
    list.addListSelectionListener(this);
    list.setVisibleRowCount(MARGIN);
    JScrollPane listScrollPane = new JScrollPane(list);

    JButton addButton = new JButton(addString);
    HireListener hireListener = new HireListener(addButton);
    addButton.setActionCommand(addString);
    addButton.addActionListener(hireListener);

    addButton.setEnabled(false);

    removeButton = new JButton(removeString);
    removeButton.setActionCommand(removeString);
    removeButton.addActionListener(new FireListener());

    employeeName = new JTextField(TEXTFIELD_LENGTH);
    employeeName.addActionListener(hireListener);
    employeeName.getDocument().addDocumentListener(hireListener);
    //String name = listModel.getElementAt(list.getSelectedIndex()).toString();

    //Create a panel that uses BoxLayout.
    JPanel buttonPane = new JPanel();
    buttonPane.setLayout(new BoxLayout(buttonPane, BoxLayout.LINE_AXIS));
    buttonPane.add(removeButton);
    buttonPane.add(Box.createHorizontalStrut(MARGIN));
    buttonPane.add(new JSeparator(SwingConstants.VERTICAL));
    buttonPane.add(Box.createHorizontalStrut(MARGIN));
    buttonPane.add(employeeName);
    buttonPane.add(addButton);
    buttonPane.setBorder(BorderFactory.createEmptyBorder(MARGIN, MARGIN,
        MARGIN, MARGIN));

    add(listScrollPane, BorderLayout.CENTER);
    add(buttonPane, BorderLayout.PAGE_END);
  }

  class FireListener implements ActionListener {
    public void actionPerformed(final ActionEvent e) {
      //This method can be called only if
      //there's a valid selection
      //so go ahead and remove whatever's selected.
      int index = list.getSelectedIndex();
      listModel.remove(index);

      int size = listModel.getSize();

      if (size == 0) { //Nobody's
        // left,
        // disable
        // firing.
        removeButton.setEnabled(false);

      } else { //Select an index.
        if (index == listModel.getSize()) {
          //removed item in last position
          index--;
        }

        list.setSelectedIndex(index);
        list.ensureIndexIsVisible(index);
      }
    }
  }

  private void addElement(final String element) {

    if (element == null)
      return;

    String e = element.trim();
    if ("".equals(e))
      e = " ";

    // User didn't type in a unique name...
    if (alreadyInList(e)) {
      Toolkit.getDefaultToolkit().beep();
      employeeName.requestFocusInWindow();
      employeeName.selectAll();
      return;
    }

    int index = list.getSelectedIndex(); //get selected

    // index
    if (index == -1) { //no selection, so insert at beginning
      index = 0;
    } else if (index != 0) { //add after the selected item
      index++;
    }

    //listModel.insertElementAt(employeeName.getText(), index);
    listModel.insertElementAt(e, index);
    //If we just wanted to add to the end, we'd do this:
    //listModel.addElement(employeeName.getText());

    // Select the new item and make it visible.
    list.setSelectedIndex(index);
    list.ensureIndexIsVisible(index);

  }

  //This method tests for string equality. You could certainly
  //get more sophisticated about the algorithm. For example,
  //you might want to ignore white space and capitalization.
  protected boolean alreadyInList(final String name) {
    return listModel.contains(name);
  }

  //This listener is shared by the text field and the hire button.
  class HireListener implements ActionListener, DocumentListener {
    private boolean alreadyEnabled;
    private JButton button;

    public HireListener(final JButton button) {
      this.button = button;
    }

    //Required by ActionListener.
    public void actionPerformed(final ActionEvent e) {
      String name = employeeName.getText();

      addElement(name);

      //Reset the text field.
      employeeName.requestFocusInWindow();
      employeeName.setText("");
    }

    //Required by DocumentListener.
    public void insertUpdate(final DocumentEvent e) {
      enableButton();
    }

    //Required by DocumentListener.
    public void removeUpdate(final DocumentEvent e) {
      handleEmptyTextField(e);
    }

    //Required by DocumentListener.
    public void changedUpdate(final DocumentEvent e) {
      if (!handleEmptyTextField(e)) {
        enableButton();
      }
    }

    public void enableButton() {
      if (!alreadyEnabled) {
        button.setEnabled(true);
      }
    }

    private boolean handleEmptyTextField(final DocumentEvent e) {
      if (e.getDocument().getLength() <= 0) {
        button.setEnabled(false);
        alreadyEnabled = false;
        return true;
      }
      return false;
    }
  }

  /**
   * This method is call when a value as changed.
   * @param e ListSelectionEvent
   */
  public void valueChanged(final ListSelectionEvent e) {
    if (!e.getValueIsAdjusting()) {

      if (list.getSelectedIndex() == -1) {
        //No selection, disable fire button.

        removeButton.setEnabled(false);

      } else {
        //Selection, enable the fire button.
        removeButton.setEnabled(true);
      }
    }
  }

  /**
   * Add an array of elements to the list.
   * @param elements elements to add
   */
  public void setElements(final String[] elements) {

    if (elements == null)
      return;

    for (int i = 0; i < elements.length; i++)
      addElement(elements[i].trim());
  }

  /**
   * Get the elements of the list.
   * @return An array of the elements of the list
   */
  public String[] getElements() {

    String[] result = new String[this.listModel.size()];

    for (int i = 0; i < this.listModel.size(); i++) {
      result[i] = (String) this.listModel.elementAt(i);
      if (" ".equals(result[i]))
        result[i] = "";
    }
    return result;
  }

}
