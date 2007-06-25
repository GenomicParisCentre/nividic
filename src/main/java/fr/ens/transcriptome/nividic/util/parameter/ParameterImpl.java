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

package fr.ens.transcriptome.nividic.util.parameter;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import fr.ens.transcriptome.nividic.util.StringUtils;
import fr.ens.transcriptome.nividic.util.YesNo;

/**
 * Implements Parameter interface.
 * @author Laurent Jourdren
 */
class ParameterImpl implements Parameter {

  private String name;
  private String longName;
  private String description;
  private String longDescription;
  private String[] choices;

  private int type = DATATYPE_STRING;

  private int intValue;
  private double doubleValue;
  private String stringValue;
  private boolean booleanValue;
  private String defaultValue;
  private boolean valueSet;
  private String unit;
  private String equalityType = "=";

  private Set testers = new HashSet();

  // An interface to test the values
  public interface ParamValueTest {
    boolean test(String value);
  }

  private static final class RangeTest implements ParamValueTest {

    private double min;
    private double max;

    public boolean test(final String value) {

      if (value == null)
        return false;
      double v = Double.parseDouble(value);
      double newMin;
      double newMax;

      if (min > max) {
        newMin = max;
        newMax = min;
      } else {
        newMin = min;
        newMax = max;
      }

      return (v >= newMin && v <= newMax);
    }

    public double getMax() {
      return max;
    }

    public double getMin() {
      return min;
    }

    public void setMax(final double i) {
      max = i;
    }

    public void setMin(final double i) {
      min = i;
    }

    public RangeTest(final double min, final double max) {
      setMin(min);
      setMax(max);
    }
  }

  private static final class LowerThanTest implements ParamValueTest {

    private double threshold;

    public boolean test(final String value) {

      if (value == null)
        return false;

      double v = Double.parseDouble(value);

      return v < getThreshold();
    }

    public double getThreshold() {
      return threshold;
    }

    public void setThreshold(final double d) {
      threshold = d;
    }

    public LowerThanTest(final double threshold) {
      setThreshold(threshold);
    }

  }

  private static final class GreaterThanTest implements ParamValueTest {

    private double threshold;

    public boolean test(final String value) {

      if (value == null)
        return false;

      double v = Double.parseDouble(value);

      return v > getThreshold();
    }

    public double getThreshold() {
      return threshold;
    }

    public void setThreshold(final double d) {
      threshold = d;
    }

    public GreaterThanTest(final double threshold) {
      setThreshold(threshold);
    }
  }

  private static final class EqualsTest implements ParamValueTest {

    private String value;

    public boolean test(final String value) {

      if (value == null)
        return false;

      return value.equals(getValue());
    }

    public String getValue() {
      return value;
    }

    public void setValue(final String string) {
      value = string;
    }

    public EqualsTest(final String value) {
      setValue(value);
    }

  }

  public String getName() {
    return this.name;
  }

  public String getLongName() {
    return this.longName;
  }

  public String getDescription() {
    return this.description;
  }

  public String getLongDescription() {
    return this.longDescription;
  }

  public int getType() {
    return this.type;
  }

  public String getUnit() {
    return this.unit;
  }

  public String getEqualityType() {
    return this.equalityType;
  }

  //
  // Private methods
  //

  public boolean testParameter(final String value) {

    Iterator it = this.testers.iterator();
    if (!it.hasNext())
      return true;

    while (it.hasNext()) {
      ParamValueTest t = (ParamValueTest) it.next();
      if (t.test(value))
        return true;
    }

    return false;
  }

  //
  // Public methods
  //

  public void setValue(final String value) throws ParameterException {
    setValue(value, true);
  }

  private void setValue(final String value, final boolean valueSet)
      throws ParameterException {

    if (value == null)
      throw new ParameterException("Value is null");

    final int type = getType();

    if (type >= DATATYPE_INTEGER && type <= DATATYPE_ARRAY_STRING) {
      switch (type) {

      case (DATATYPE_INTEGER):
        final int iv;
        try {
          iv = Integer.parseInt(value);
        } catch (NumberFormatException e) {
          throw new ParameterException("Invalid integer");
        }
        if (!testParameter(value))
          throw new ParameterException("Invalid integer parameter value ("
              + getName() + "): " + value);
        this.intValue = iv;
        break;

      case (DATATYPE_DOUBLE):
        final double dv;
        try {
          dv = Double.parseDouble(value);
        } catch (NumberFormatException e) {
          throw new ParameterException("Invalid double");
        }
        if (!testParameter(value))
          throw new ParameterException("Invalid double parameter value");
        this.doubleValue = dv;
        break;

      case (DATATYPE_BOOLEAN):
        boolean bv;
        try {
          bv = Boolean.valueOf(value).booleanValue();
        } catch (NumberFormatException e) {
          throw new ParameterException("Invalid boolean");
        }
        if (!testParameter(value))
          throw new ParameterException("Invalid boolean parameter value");
        this.booleanValue = bv;
        break;

      case (DATATYPE_YESNO):

        try {
          bv = new YesNo(value).toBoolean();
        } catch (NumberFormatException e) {
          throw new ParameterException("Invalid yesno value");
        }
        if (!testParameter(value))
          throw new ParameterException("Invalid yesno parameter value");
        this.booleanValue = bv;

        break;

      case (DATATYPE_STRING):

        if (!testParameter(value))
          throw new ParameterException("Invalid string parameter value ("
              + getName() + "): " + value);
        this.stringValue = value;
        break;

      case (DATATYPE_ARRAY_STRING):
        final String[] asv = StringUtils.stringToArrayString(value);

        for (int i = 0; i < asv.length; i++) {
          if (!testParameter(asv[i]))
            throw new ParameterException("Invalid string parameter value");
        }

        this.stringValue = value;
        break;

      default:
        break;
      }
    }
    this.valueSet = valueSet;
  }

  /**
   * Get the value of the parameter in a string.
   * @return A String value
   * @throws ParameterException if parameter is'nt a boolean
   */
  public String getValue() throws ParameterException {

    switch (getType()) {

    case Parameter.DATATYPE_BOOLEAN:
      return "" + getBooleanValue();

    case Parameter.DATATYPE_DOUBLE:
      return "" + getDoubleValue();

    case Parameter.DATATYPE_INTEGER:
      return "" + getIntValue();

    case Parameter.DATATYPE_STRING:
      return "" + getStringValue();

    case Parameter.DATATYPE_YESNO:
      return getBooleanValue() ? "yes" : "no";

    case Parameter.DATATYPE_ARRAY_BOOLEAN:
    case Parameter.DATATYPE_ARRAY_DOUBLE:
    case Parameter.DATATYPE_ARRAY_STRING:
    case Parameter.DATATYPE_ARRAY_INTEGER:
    case Parameter.DATATYPE_ARRAY_YESNO:
      return this.stringValue == null ? (getDefaultValue() == null ? ""
          : getDefaultValue()) : this.stringValue;

    default:
      return null;
    }

  }

  public boolean getBooleanValue() throws ParameterException {

    if (!(getType() == DATATYPE_BOOLEAN || getType() == DATATYPE_YESNO))
      throw new ParameterException("Invalid type : not a boolean");

    if (!valueSet) {
      if (this.defaultValue == null)
        this.defaultValue = "false";
      setValue(this.defaultValue, false);
    }

    return this.booleanValue;
  }

  public int getIntValue() throws ParameterException {

    if (getType() != DATATYPE_INTEGER)
      throw new ParameterException("Invalid type : not an integer");
    if (!valueSet) {

      if (this.defaultValue == null)
        this.defaultValue = "0";
      setValue(this.defaultValue, false);
    }
    return this.intValue;
  }

  private String[] getArrayValues() throws ParameterException {
    if (!valueSet) {
      if (this.defaultValue == null)
        this.defaultValue = "";
      setValue(this.defaultValue, false);
    }
    return StringUtils.stringToArrayString(this.stringValue);
  }

  public String[] getArrayStringValues() throws ParameterException {

    if (getType() != DATATYPE_ARRAY_STRING)
      throw new ParameterException("Invalid type : not an array string");

    return getArrayValues();
  }

  public int[] getArrayIntegerValues() throws ParameterException {

    if (getType() != DATATYPE_ARRAY_INTEGER)
      throw new ParameterException("Invalid type : not an array string");

    String[] values = getArrayValues();

    int[] result = new int[values.length];

    for (int i = 0; i < values.length; i++) {
      result[i] = Integer.parseInt(values[i]);
    }
    return result;
  }

  public double[] getArrayDoubleValues() throws ParameterException {

    if (getType() != DATATYPE_ARRAY_DOUBLE)
      throw new ParameterException("Invalid type : not an array string");

    String[] values = getArrayValues();

    double[] result = new double[values.length];

    for (int i = 0; i < values.length; i++) {
      result[i] = Double.parseDouble(values[i]);
    }
    return result;
  }

  public boolean[] getArrayBooleanValues() throws ParameterException {

    if (getType() != DATATYPE_ARRAY_BOOLEAN)
      throw new ParameterException("Invalid type : not an array string");

    String[] values = getArrayValues();

    boolean[] result = new boolean[values.length];

    for (int i = 0; i < values.length; i++) {
      result[i] = Boolean.getBoolean(values[i]);
    }
    return result;
  }

  public boolean[] getArrayYesNoValues() throws ParameterException {

    if (getType() != DATATYPE_ARRAY_YESNO)
      throw new ParameterException("Invalid type : not an array string");

    String[] values = getArrayValues();

    boolean[] result = new boolean[values.length];

    for (int i = 0; i < values.length; i++) {
      result[i] = Boolean.getBoolean(values[i]);
    }
    return result;
  }

  public String getStringValue() throws ParameterException {

    if (getType() != DATATYPE_STRING)
      throw new ParameterException("Invalid type : not a string");

    if (!valueSet) {
      if (this.defaultValue == null)
        this.defaultValue = "";
      setValue(this.defaultValue, false);
    }
    return this.stringValue;
  }

  public double getDoubleValue() throws ParameterException {

    if (getType() != DATATYPE_DOUBLE)
      throw new ParameterException("Invalid type : not a double");

    if (!valueSet) {
      if (this.defaultValue == null)
        this.defaultValue = "0.0";
      setValue(this.defaultValue, false);
    }
    return doubleValue;
  }

  public String[] getChoices() {
    return this.choices;
  }

  public String getDefaultValue() {
    return this.defaultValue;
  }

  //
  // Package methods
  //

  void setDescription(final String description) {
    this.description = description;
  }

  void setLongDescription(final String longDescription) {
    this.longDescription = longDescription;
  }

  void setName(final String name) {
    this.name = name;
  }

  void setLongName(final String longName) {
    this.longName = longName;
  }

  void setType(final int i) throws ParameterException {

    if (this.type == Parameter.DATATYPE_STRING
        && i != Parameter.DATATYPE_STRING && this.stringValue != null) {
      type = i;
      setValue(this.stringValue);
    } else
      type = i;
  }

  void setUnit(final String unit) {
    this.unit = unit;
  }

  void setEqualityType(final String equality) {

    if ("=".equals(equality) || "!=".equals(equality) || "<".equals(equality)
        || "<=".equals(equality) || ">".equals(equality)
        || ">=".equals(equality))

      this.equalityType = equality;
  }

  void setChoices(final String[] choices) {
    if (choices == null)
      return;

    this.choices = choices;

    for (int i = 0; i < choices.length; i++) {
      addTester(this.makeEqualsTest(choices[i]));
    }
  }

  void setDefaultValue(final String value) {
    this.defaultValue = value;
  }

  void addTester(final ParamValueTest tester) {

    if (tester == null)
      return;
    this.testers.add(tester);
  }

  ParamValueTest makeRangeTest(final double min, final double max) {
    return new RangeTest(min, max);
  }

  ParamValueTest makeGreaterThanTest(final double threshold) {
    return new GreaterThanTest(threshold);
  }

  ParamValueTest makeLowerThanTest(final double threshold) {
    return new LowerThanTest(threshold);
  }

  ParamValueTest makeEqualsTest(final String value) {
    return new EqualsTest(value);
  }

}