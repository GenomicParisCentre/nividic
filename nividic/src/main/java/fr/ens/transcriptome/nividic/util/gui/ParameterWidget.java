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

import java.awt.Font;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JTextPane;

import fr.ens.transcriptome.nividic.util.StringUtils;
import fr.ens.transcriptome.nividic.util.YesNo;
import fr.ens.transcriptome.nividic.util.parameter.Parameter;
import fr.ens.transcriptome.nividic.util.parameter.ParameterException;

/**
 * This class define a widget to edit a parameter
 * @author Laurent Jourdren
 */
public class ParameterWidget {

  private JPanel panel;
  private JLabel fieldLabel;
  private JTextField valueField;
  private JComboBox valueFieldComboBox;
  private AddRemoveListWidget addRemoveListWidget;
  private JLabel descriptionLabel;
  private JTextPane descriptionField;

  private Parameter parameter;

  //
  // Getters
  //

  /**
   * Get the parameter.
   * @return Returns the paramater
   */
  public Parameter getParameter() {
    return parameter;
  }

  //
  // Setters
  //

  /**
   * Set the parameter.
   * @param parameter The parameter to set
   */
  private void setParameter(final Parameter parameter) {
    this.parameter = parameter;
  }

  /**
   * Set the panel.
   * @param panel
   */
  private void setPanel(final JPanel panel) {
    this.panel = panel;
  }

  //
  // Other methods
  //

  /**
   * This method initializes this.
   */
  private void initialize() {

    //java.awt.GridBagConstraints gridBagConstraints;

    final Parameter p = getParameter();

    String name;
    if (p.getLongName() != null)
      name = p.getLongName();
    else
      name = p.getName();

    if (p.getUnit() != null)
      name = name + " (" + p.getUnit() + ")";
    name = name + ":";

    fieldLabel = new JLabel(name);

    fieldLabel.setVerticalAlignment(javax.swing.SwingConstants.TOP);
    fieldLabel.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);
    this.panel.add(fieldLabel);

    switch (p.getType()) {
    case Parameter.DATATYPE_BOOLEAN:

      boolean booleanValue;

      try {
        booleanValue = p.getBooleanValue();
      } catch (ParameterException e) {
        booleanValue = false;
      }

      valueFieldComboBox = new JComboBox();
      valueFieldComboBox.addItem(Boolean.TRUE);
      valueFieldComboBox.addItem(Boolean.FALSE);
      valueFieldComboBox.setSelectedItem(booleanValue ? Boolean.TRUE
          : Boolean.FALSE);

      this.panel.add(valueFieldComboBox);

      break;

    case Parameter.DATATYPE_YESNO:
      boolean yesNoValue;

      try {
        yesNoValue = p.getBooleanValue();
      } catch (ParameterException e) {
        yesNoValue = false;
      }

      valueFieldComboBox = new JComboBox();
      valueFieldComboBox.addItem(YesNo.YES_STRING);
      valueFieldComboBox.addItem(YesNo.NO_STRING);
      valueFieldComboBox.setSelectedItem(yesNoValue ? YesNo.YES_STRING
          : YesNo.NO_STRING);

      this.panel.add(valueFieldComboBox);
      break;

    case Parameter.DATATYPE_ARRAY_INTEGER:
    case Parameter.DATATYPE_ARRAY_DOUBLE:
    case Parameter.DATATYPE_ARRAY_BOOLEAN:
    case Parameter.DATATYPE_ARRAY_YESNO:
    case Parameter.DATATYPE_ARRAY_STRING:

      String[] values;

      try {
        values = StringUtils.stringToArrayString(p.getValue());
      } catch (ParameterException e) {
        values = null;
      }

      this.addRemoveListWidget = new AddRemoveListWidget(values);

      this.panel.add(this.addRemoveListWidget);
      break;

    default:

      final String[] choices = p.getChoices();
      if (choices == null) {

        String value;

        try {
          value = p.getValue();
        } catch (ParameterException e) {
          value = "";
        }

        valueField = new JTextField(value);
        this.panel.add(valueField);
      } else {

        String stringValue;

        try {
          stringValue = p.getValue();
        } catch (ParameterException e) {
          stringValue = "";
        }

        valueFieldComboBox = new JComboBox();
        for (int i = 0; i < choices.length; i++) {
          valueFieldComboBox.addItem(choices[i]);
        }

        valueFieldComboBox.setSelectedItem(stringValue);

        this.panel.add(valueFieldComboBox);

      }

      break;
    }

    descriptionLabel = new JLabel("Description: ");

    this.panel.add(descriptionLabel);

    descriptionField = new JTextPane();
    descriptionField.setFont(new Font("SansSerif", Font.PLAIN, descriptionField
        .getFont().getSize()));
    descriptionField.setText(p.getDescription());
    descriptionField.setEditable(false);

    this.panel.add(descriptionField);

  }

  /**
   * Set the value in the field in the parameter object.
   * @throws ParameterException if an error occurs when setting the parameter.
   */
  public void putValueInParameter() throws ParameterException {

    final int type = getParameter().getType();
    String value;

    switch (type) {
    case Parameter.DATATYPE_BOOLEAN:
      value = ((Boolean) this.valueFieldComboBox.getSelectedItem()).toString();
      //getParameter().setValue(value);
      break;

    case Parameter.DATATYPE_YESNO:
      value = (String) this.valueFieldComboBox.getSelectedItem();
      //getParameter().setValue(value);
      break;

    case Parameter.DATATYPE_ARRAY_STRING:

      String[] elements = this.addRemoveListWidget.getElements();
      if (elements == null)
        value = "";
      value = StringUtils.arrayStringtoString(elements);
      getParameter().setValue(value);
      break;

    default:
      if (getParameter().getChoices() == null)
        value = this.valueField.getText();
      else
        value = this.valueFieldComboBox.getSelectedItem().toString();
      break;
    }

    getParameter().setValue(value);
  }

  /**
   * Get the maximum size of the labels.
   * @return the maximum size of the labels
   */
  public int getMaxSizeLabels() {

    return Math.max(this.descriptionLabel.getWidth(), this.fieldLabel
        .getWidth());
  }

  /**
   * Get the maximum size of the values.
   * @return the maximum size of the values
   */
  public int getMaxSizeValues() {

    int type = getParameter().getType();

    if (type == Parameter.DATATYPE_BOOLEAN || type == Parameter.DATATYPE_YESNO)
      return Math.max(this.descriptionField.getWidth(), this.valueFieldComboBox
          .getWidth());

    return Math.max(this.descriptionField.getWidth(), this.valueField
        .getWidth());
  }

  /**
   * Set the size values.
   * @param size1 first size
   * @param size2 second size
   */
  public void setSizeValues(final int size1, final int size2) {
    this.descriptionLabel.setSize(size1, this.descriptionLabel.getHeight());
    this.fieldLabel.setSize(size1, this.fieldLabel.getHeight());

    this.descriptionField.setSize(size2, this.descriptionField.getHeight());
    int type = getParameter().getType();

    if (type == Parameter.DATATYPE_BOOLEAN || type == Parameter.DATATYPE_YESNO)
      this.valueFieldComboBox.setSize(size2, this.valueFieldComboBox
          .getHeight());
    else
      this.valueField.setSize(size2, this.valueField.getHeight());
  }

  //
  // Constructor
  //

  /**
   * This is the default constructor
   * @param panel the panel
   * @param parameter Parameter of the widget
   */
  public ParameterWidget(final JPanel panel, final Parameter parameter) {
    super();

    setPanel(panel);
    setParameter(parameter);
    initialize();
  }

}