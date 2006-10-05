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

import fr.ens.transcriptome.nividic.util.event.GenericEvent;

/**
 * Interface for ExperimentDesign events.
 * @author Laurent Jourdren
 */
public interface ExperimentDesignEvent extends GenericEvent {

  /** Condition add event. */
  int CONDITION_ADD_EVENT = 1;

  /** Condition remove event. */
  int CONDITION_REMOVE_EVENT = 2;

  /** Condition rename event. */
  int CONDITION_RENAME_EVENT = 3;

  /** Condition modify event. */
  int CONDITION_MODIFY_EVENT = 4;

  /** Condition add event. */
  int PARAMETER_ADD_EVENT = 5;

  /** Condition remove event. */
  int PARAMETER_REMOVE_EVENT = 6;

  /** Condition rename event. */
  int PARAMETER_RENAME_EVENT = 7;

  /** Condition modify event. */
  int PARAMETER_MODIFY_EVENT = 8;

  /** Value modify event. */
  int VALUE_MODIFY_EVENT = 9;

  /**
   * Get the source of the event.
   * @return Returns the source
   */
  ExperimentDesign getSource();

}
