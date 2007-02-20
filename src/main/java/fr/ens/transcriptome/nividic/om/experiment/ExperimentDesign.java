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

package fr.ens.transcriptome.nividic.om.experiment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.commons.collections.BidiMap;
import org.apache.commons.collections.bidimap.DualHashBidiMap;

import fr.ens.transcriptome.nividic.NividicException;
import fr.ens.transcriptome.nividic.om.Annotatable;
import fr.ens.transcriptome.nividic.om.Annotation;
import fr.ens.transcriptome.nividic.om.AnnotationFactory;

/**
 * This class defines an experiement design.
 * @author Laurent Jourdren
 */
public class ExperimentDesign implements Annotable, ExperimentDesignListener {

  private String name;
  private BidiMap parameters = new DualHashBidiMap();
  private BidiMap conditions = new DualHashBidiMap();
  private Map values = new HashMap();
  private Annotation annotation = AnnotationFactory.createAnnotation();
  private ArrayList listeners = new ArrayList();

  //
  // Getters
  //

  /**
   * Should return the associated annotation object.
   * @return an Annotation object, never null
   */
  public Annotation getAnnotation() {
    return this.annotation;
  }

  /**
   * Get the name of the experiement design.
   * @return Returns the name
   */
  String getName() {
    return name;
  }

  //
  // Setters
  //

  /**
   * Set the name of the experiement design.
   * @param name The name to set
   */
  void setName(final String name) {
    this.name = name;
  }

  //
  // Other methods
  //

  /**
   * Add a parameter to the experimental design
   * @param parameter Parameter to add
   * @throws NividicException if parameter is null, parameter name is null or if
   *                 the parameter already exists.
   */
  public void addParameter(final ExperimentParameter parameter)
      throws NividicException {

    if (parameter == null)
      throw new NividicException("Parameter is null");
    if (parameter.getName() == null)
      throw new NividicException("Parameter name is null");
    if (isParameter(parameter))
      throw new NividicException("Parameter already exists");

    if (parameter.getExperimentDesign() != null
        && parameter.getExperimentDesign() != this)
      throw new NividicException(
          "Parameter is already a member of another experiment design");

    parameter.setExperimentDesign(this);

    this.parameters.put(parameter.getName(), parameter);
    String value = parameter.getType().getDefaultValue();

    Iterator it = this.values.keySet().iterator();
    while (it.hasNext()) {
      String conditionName = (String) it.next();
      set(conditionName, parameter, value);
    }

    sendEvent(new SimpleExperimentDesignEvent(this,
        ExperimentDesignEvent.PARAMETER_ADD_EVENT, parameter));
  }

  /**
   * Add a condition to the experimental design.
   * @param condition condition to add
   * @throws NividicException if condition is null or the condition already exists
   */
  public void addCondition(final Condition condition) throws NividicException {

    if (condition == null)
      throw new NividicException("condition is null");
    if (this.conditions.containsKey(condition.getName()))
      throw new NividicException("condition already exists");

    if (condition.getExperimentDesign() != null
        && condition.getExperimentDesign() != this)
      throw new NividicException(
          "condition is already a member of another experiment design");

    condition.setExperimentDesign(this);
    condition.addListener(this);
    this.conditions.put(condition.getName(), condition);

    Map values = new HashMap();
    this.values.put(condition.getName(), values);

    Iterator it = parameterIterator();
    while (it.hasNext()) {
      ExperimentParameter ep = (ExperimentParameter) it.next();

      ExperimentParameterType ept = ep.getType();

      set(condition, ep, ept == null ? null : ept.getDefaultValue());
    }

    sendEvent(new SimpleExperimentDesignEvent(this,
        ExperimentDesignEvent.CONDITION_ADD_EVENT, condition));
  }

  /**
   * Add a condition to the experimental design.
   * @param conditionName condition to add
   * @throws NividicException if condition is null or the condition already exists
   */
  public void addCondition(final String conditionName) throws NividicException {

    addCondition(new Condition(conditionName));
  }

  /**
   * Add a condition to the experimental design.
   * @param conditionName condition to add
   * @param descriotionCondition description of the condition to add
   * @throws NividicException if condition is null or the condition already exists
   */
  public void addCondition(final String conditionName,
      final String descriotionCondition) throws NividicException {

    addCondition(new Condition(conditionName, descriotionCondition));
  }

  /**
   * Remove a parameter.
   * @param parameterName Parameter to remove
   */
  public void removeParameter(final String parameterName) {

    if (parameterName == null)
      return;

    removeParameter((ExperimentParameter) this.parameters.get(parameterName));
  }

  /**
   * Remove a parameter.
   * @param parameter Parameter to remove
   */
  public void removeParameter(final ExperimentParameter parameter) {

    if (parameter == null || !isParameter(parameter))
      return;

    this.parameters.remove(parameter.getName());

    Iterator it = conditionIterator();
    while (it.hasNext()) {
      String condition = (String) it.next();
      Map params = (Map) this.values.get(condition);
      params.remove(parameter.getName());
    }

    sendEvent(new SimpleExperimentDesignEvent(this,
        ExperimentDesignEvent.PARAMETER_REMOVE_EVENT, parameter));

  }

  /**
   * Remove a condition.
   * @param conditionName Parameter to remove
   */
  public void removeCondition(final String conditionName) {

    if (conditionName == null)
      return;

    removeCondition((Condition) this.conditions.get(conditionName));
  }

  /**
   * Remove a condition.
   * @param condition Condition to remove
   */
  public void removeCondition(final Condition condition) {

    this.conditions.remove(condition.getName());

    sendEvent(new SimpleExperimentDesignEvent(this,
        ExperimentDesignEvent.CONDITION_REMOVE_EVENT, condition));
    condition.removeListener(this);
  }

  /**
   * Rename a condition
   * @param condition Condition to rename
   */
  private void rename(final Condition condition) {

    if (condition == null)
      return;

    if (!this.conditions.containsValue(condition))
      return;

    if (isCondition(condition))
      return;

    String conditionName = (String) this.conditions.getKey(condition);
    String newName = condition.getName();

    Map map = (Map) this.values.get(conditionName);
    this.values.remove(conditionName);
    this.values.put(newName, map);

    this.conditions.remove(conditionName);
    this.conditions.put(newName, condition);
  }

  /**
   * Rename a parameter
   * @param parameter Condition to rename
   */
  private void rename(final ExperimentParameter parameter) {

    if (parameter == null)
      return;

    if (!this.parameters.containsValue(parameter))
      return;

    if (isParameter(parameter))
      return;

    String parameterName = (String) this.parameters.getKey(parameter);
    String newName = parameter.getName();

    Iterator it = this.values.keySet().iterator();
    while (it.hasNext()) {
      String conditionName = (String) it.next();
      Map map = (Map) this.values.get(conditionName);

      String value = (String) map.get(parameterName);
      map.remove(parameterName);
      map.put(newName, value);
    }

    this.parameters.remove(parameterName);
    this.parameters.put(newName, parameter);
  }

  /**
   * Set a value of the experiemental design.
   * @param condition Condition to set
   * @param parameter Parameter to set
   * @param value Value to set
   * @throws NividicException if the condition or the parameter doesn't exists in
   *                 the experimental design
   */
  public void set(final String condition, final String parameter,
      final String value) throws NividicException {

    ExperimentParameter ep = getParameter(parameter);
    set(condition, ep, value);
  }

  /**
   * Set a value of the experiemental design.
   * @param condition Condition to set
   * @param parameter Parameter to set
   * @param value Value to set
   * @throws NividicException if the condition or the parameter doesn't exists in
   *                 the experimental design
   */
  public void set(final Condition condition, final String parameter,
      final String value) throws NividicException {

    if (condition == null)
      throw new NividicException("The condition is null");

    ExperimentParameter ep = getParameter(parameter);
    set(condition.getName(), ep, value);
  }

  /**
   * Set a value of the experiemental design.
   * @param condition Condition to set
   * @param parameter Parameter to set
   * @param value Value to set
   * @throws NividicException if the condition or the parameter doesn't exists in
   *                 the experimental design
   */
  public void set(final Condition condition,
      final ExperimentParameter parameter, final String value)
      throws NividicException {

    if (!isCondition(condition))
      throw new NividicException("The condition doesn't exists");

    set(condition.getName(), parameter, value);

  }

  /**
   * Set a value of the experiemental design.
   * @param conditionName Condition to set
   * @param parameter Parameter to set
   * @param value Value to set
   * @throws NividicException if the condition or the parameter doesn't exists in
   *                 the experimental design
   */
  public void set(final String conditionName,
      final ExperimentParameter parameter, final String value)
      throws NividicException {

    if (!isCondition(conditionName))
      throw new NividicException("The condition doesn't exits");
    if (!isParameter(parameter))
      throw new NividicException("The parameter doesn't exists");

    if (!parameter.getType().isCorrectValue(value))
      throw new NividicException("Incorrect value");

    String parameterName = parameter.getName();

    Map params = (Map) this.values.get(conditionName);

    params.put(parameterName, value);

    sendEvent(new SimpleExperimentDesignEvent(this,
        ExperimentDesignEvent.VALUE_MODIFY_EVENT, null));
  }

  /**
   * Get a value of the experimental design.
   * @param conditionName The condition to get
   * @param parameterName The parameter to get
   * @return the value of exeperimental for the couple parameter/condition
   * @throws NividicException if the condition or the parameter doesn't exists in
   *                 the experimental design
   */
  public String get(final String conditionName, final String parameterName)
      throws NividicException {

    if (!isCondition(conditionName))
      throw new NividicException("The condition doesn't exits");
    if (!isParameter(parameterName))
      throw new NividicException("The parameter doesn't exists");

    Map params = (Map) this.values.get(conditionName);

    return (String) params.get(parameterName);

  }

  /**
   * Get a value of the experimental design.
   * @param condition The condition to get
   * @param parameter The parameter to get
   * @return the value of exeperimental for the couple parameter/condition
   * @throws NividicException if the condition or the parameter doesn't exists in
   *                 the experimental design
   */
  public String get(final Condition condition,
      final ExperimentParameter parameter) throws NividicException {

    if (!isCondition(condition))
      throw new NividicException("The condition doesn't exits");
    if (!isParameter(parameter))
      throw new NividicException("The parameter doesn't exits");

    return get(condition.getName(), parameter.getName());
  }

  /**
   * Get a value of the experimental design.
   * @param conditionName The condition to get
   * @param parameter The parameter to get
   * @return the value of exeperimental for the couple parameter/condition
   * @throws NividicException if the condition or the parameter doesn't exists in
   *                 the experimental design
   */
  public String get(final String conditionName,
      final ExperimentParameter parameter) throws NividicException {

    return get(getCondition(conditionName), parameter);
  }

  /**
   * Test if a condition exists in the experiemental design
   * @param conditionName Condition to test
   * @return true if the condition exists in the experiemental design
   */
  public boolean isCondition(final String conditionName) {
    return this.conditions.containsKey(conditionName);
  }

  /**
   * Test if a condition exists in the experiemental design
   * @param condition ConditionName to test
   * @return true if the condition exists in the experiemental design
   */
  public boolean isCondition(final Condition condition) {
    if (condition == null || condition.getExperimentDesign() != this)
      return false;
    return isCondition(condition.getName());
  }

  /**
   * Test if a parameter exists in the experiemental design
   * @param parameterName Parameter to test
   * @return true if the parameter exists in the experiemental design
   */
  public boolean isParameter(final String parameterName) {
    return this.parameters.containsKey(parameterName);
  }

  /**
   * Test if a parameter exists in the experiemental design
   * @param parameter Parameter to test
   * @return true if the parameter exists in the experiemental design
   */
  public boolean isParameter(final ExperimentParameter parameter) {

    if (parameter == null)
      return false;
    if (parameter.getExperimentDesign() != null
        && parameter.getExperimentDesign() != this)
      return false;

    return isParameter(parameter.getName());
  }

  /**
   * Returns an iterator over the parameters
   * @return An iterator over the parameters
   */
  public Iterator parameterIterator() {
    return this.parameters.values().iterator();
  }

  /**
   * Returns an iterator over the conditions
   * @return An iterator over the conditions
   */
  public Iterator conditionIterator() {
    return this.conditions.keySet().iterator();
  }

  /**
   * Return an array of parameters with all the parameters of the experiement
   * design.
   * @return An array of ExperimentParameter with all the parameters of the
   *               experiement design
   */
  public ExperimentParameter[] getParameters() {

    ExperimentParameter[] result = new ExperimentParameter[this.parameters
        .size()];
    this.parameters.values().toArray(result);

    return result;
  }

  /**
   * Return an array of string with all the parameters of the experiement
   * design.
   * @return An array of ExperimentParameter with all the parameters of the
   *               experiement design
   */
  public String[] getParametersNames() {

    String[] result = new String[this.parameters.size()];
    this.parameters.keySet().toArray(result);

    return result;
  }

  /**
   * Return an array of conditions with all the conditions of the experiement
   * design.
   * @return An array of conditions with all the conditions of the experiement
   *               design
   */
  public Condition[] getConditions() {
    Condition[] result = new Condition[this.conditions.size()];
    this.conditions.values().toArray(result);

    return result;
  }

  /**
   * Return an array of string with all the name of the conditions of the
   * experiement design.
   * @return An array of string with all the conditions of the experiement
   *               design
   */
  public String[] getConditionsNames() {
    String[] result = new String[this.conditions.size()];
    this.conditions.keySet().toArray(result);

    return result;
  }

  /**
   * Return the number of the conditions in the experiment design.
   * @return The number of the conditions in the experiment design.
   */
  public int conditionsSize() {
    return this.conditions.size();
  }

  /**
   * Return the number of the conditions in the experiment design.
   * @return The number of the conditions in the experiment design.
   */
  public int parametersSize() {
    return this.parameters.size();
  }

  /**
   * Get the parameter object from the experiment design object.
   * @param parameterName Name of the parameter
   * @return A parameter object
   */
  public ExperimentParameter getParameter(final String parameterName) {
    return (ExperimentParameter) this.parameters.get(parameterName);
  }

  /**
   * Get the condition object from the experiment design object.
   * @param conditionName Name of the condition
   * @return A parameter object
   */
  public Condition getCondition(final String conditionName) {

    return (Condition) this.conditions.get(conditionName);
  }

  //
  // Listener management
  //

  /**
   * Add a listener.
   * @param listener Listener to add.
   */
  public void addListener(final ExperimentDesignListener listener) {

    if (listener == null || this.listeners.contains(listener))
      return;
    this.listeners.add(listener);
  }

  /**
   * Remove a listener.
   * @param listener Listener to remove
   */
  public void removeListener(final ExperimentDesignListener listener) {
    if (listener == null || !this.listeners.contains(listener))
      return;

    this.listeners.remove(listener);
  }

  /**
   * Send event to all the listeners.
   * @param event a AlgorithmEvent object
   */
  private void sendEvent(final ExperimentDesignEvent event) {

    if (event == null)
      return;

    Iterator it = this.listeners.iterator();
    while (it.hasNext())
      ((ExperimentDesignListener) it.next())
          .experimentDesignStateChanged(event);
  }

  /**
   * Invoked when the target of the listener has changed its state.
   * @param event a WorkflowEvent object
   */
  public void experimentDesignStateChanged(final ExperimentDesignEvent event) {

    if (event == null)
      return;

    switch (event.getId()) {

    case ExperimentDesignEvent.CONDITION_RENAME_EVENT:
      Condition c = (Condition) event.getObjectValue();
      if (c == null)
        break;
      rename(c);
      break;

    case ExperimentDesignEvent.PARAMETER_RENAME_EVENT:
      ExperimentParameter p = (ExperimentParameter) event.getObjectValue();
      rename(p);
      break;

    default:
      break;
    }

  }

  //
  // Constructors
  //

  /**
   * Public constructor.
   */
  public ExperimentDesign() {
  }

  /**
   * Public constructor.
   * @param name Name of the experiement design
   */
  public ExperimentDesign(final String name) {
    setName(name);
  }

}