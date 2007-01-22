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

package fr.ens.transcriptome.nividic.js;

import java.io.File;

import fr.ens.transcriptome.nividic.om.BioAssay;
import fr.ens.transcriptome.nividic.om.io.GPRReader;
import fr.ens.transcriptome.nividic.om.io.NividicIOException;

public class BioAssayShortcuts {

  public static final BioAssay readGPR(File file) throws NividicIOException {

    if (file == null)
      throw new RuntimeException("File argument is null");

    GPRReader reader = new GPRReader(file);
    reader.addAllFieldsToRead();

    return reader.read();
  }

  public static final BioAssay readGPR(String filename)
      throws NividicIOException {

    return readGPR(new File(filename));
  }
  
  public static final BioAssay [] readGPR(File [] files) throws NividicIOException {
    
    if (files == null)
      throw new RuntimeException("Files argument is null");
    
    BioAssay [] result = new BioAssay[files.length];
    
    for (int i = 0; i < result.length; i++) 
      result[i]=readGPR(files[i]);
    
    return result;
  }
  
  public static final BioAssay [] readGPR(String [] filenames) throws NividicIOException {
    
    if (filenames == null)
      throw new RuntimeException("Filenames argument is null");
    
    File [] files = new File[filenames.length];
    for (int i = 0; i < files.length; i++) 
      files[i]= new File(filenames[i]);
    
    return readGPR(files);
  }

}
