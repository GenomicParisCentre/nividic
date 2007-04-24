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

package fr.ens.transcriptome.nividic.om;

import java.util.Date;

/**
 * This class define a history entry.
 * @author Laurent Jourdren
 */
public class HistoryEntry {

  /**
   * This enum describe all the actions of an event log in a history entry.
   * @author Laurent Jourdren
   */
  public enum HistoryActionType {
    CREATE, FILTER, SORT, REMOVE, ADD, LOAD, SAVE, UNKNOWN
  };

  /**
   * This enum describe all the results of an event log in a history entry.
   * @author Laurent Jourdren
   */
  public enum HistoryActionResult {
    PASS, FAIL, NA, UNKNOWN
  };

  private Date date;
  private HistoryActionType actionType;
  private String actionName;
  private String arguments;
  private HistoryActionResult actionResult;
  private String comments = "";

  //
  // Getters
  //

  /**
   * Get the action name.
   * @return Returns the actionName
   */
  public String getActionName() {

    return actionName;
  }

  /**
   * Get the action type.
   * @return Returns the actionType
   */
  public HistoryActionType getActionType() {

    return actionType;
  }

  /**
   * Get the arguments.
   * @return Returns the arguments
   */
  public String getArguments() {

    return arguments;
  }

  /**
   * Get the comments.
   * @return Returns the comments
   */
  public String getComments() {

    return comments;
  }

  /**
   * Get the result of the action.
   * @return Returns the actionResult
   */
  public HistoryActionResult getActionResult() {
    return actionResult;
  }

  /**
   * Get the date of the action.
   * @return Returns the date
   */
  public Date getDate() {

    return date;
  }

  //
  // Setters
  //

  /**
   * Set the comments of the entry.
   * @param comments The comments to set
   */
  public void setComments(final String comments) {

    this.comments = comments != null ? comments : "";
  }

  private void setActionName(final String actionName) {

    this.actionName = actionName != null ? actionName : "Unknown action";
  }

  private void setActionResult(final HistoryActionResult actionResult) {

    this.actionResult = actionResult != null ? actionResult
        : HistoryActionResult.UNKNOWN;
  }

  private void setActionType(final HistoryActionType actionType) {

    this.actionType = actionType != null ? actionType
        : HistoryActionType.UNKNOWN;
  }

  private void setArguments(final String arguments) {

    this.arguments = arguments != null ? arguments : "";
  }

  //
  // Other method
  //

  /**
   * Override toString() method.
   * @return a description of the entry
   */
  public String toString() {

    StringBuffer sb = new StringBuffer();

    sb.append("Date=");
    sb.append(getDate());
    sb.append(" Type=");
    sb.append(getActionType());
    sb.append(" Action=");
    sb.append(getActionName());
    sb.append(" Result=");
    sb.append(getActionResult());
    sb.append(" Arguments=");
    sb.append(getArguments());
    sb.append(" Comments=");
    sb.append(getComments());

    return sb.toString();
  }

  //
  // Constructors
  //

  /**
   * Constructor.
   * @param actionName the name of the action entry
   * @param actionType the type of the action
   * @param arguments arguments
   * @param actionResult the result of the action
   */
  public HistoryEntry(final String actionName,
      final HistoryActionType actionType, final String arguments,
      final HistoryActionResult actionResult) {

    setActionName(actionName);
    setActionType(actionType);
    setArguments(arguments);
    setActionResult(actionResult);

    this.date = new Date(System.currentTimeMillis());
  }

  /**
   * Constructor for creating a copy of an entry.
   * @param entry Entry to copy
   */
  public HistoryEntry(final HistoryEntry entry) {

    if (entry == null)
      throw new NullPointerException("History entry is null");

    this.date = entry.date;
    this.actionType = entry.actionType;
    this.actionName = entry.actionName;
    this.arguments = entry.arguments;
    this.actionResult = entry.actionResult;
    this.comments = entry.comments;
  }

}
