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

package fr.ens.transcriptome.nividic.taverna;

public class Defaults {

  /** The name of the application. */
  public static final String APP_NAME = "Nividic";
  
  /** The name of the application in lowercase. */
  public static final String APP_NAME_LOWERCASE = APP_NAME.toLowerCase();
  
  /** The description of the application. */
  public static final String APP_DESCRIPTION = "Microarray Development Kit";
  
  public static void main(final String [] args ){
    
    int [] array = new int [] { 1,2,3,4 };
    
    System.out.println(array.getClass().getCanonicalName());
  
  }
  
}
