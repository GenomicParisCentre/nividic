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

  public enum HistoryActionType {
    CREATE, FILTER, SORT, REMOVE, ADD, LOAD, SAVE, UNKNOWN
  };

  public enum HistoryActionResult {
    PASS, FAIL, NA, UNKNOWN
  };

  private Date date;
  private HistoryActionType actionType;
  private String actionName;
  private String arguments;
  private HistoryActionResult actionResult;
  private String comments;

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

}
