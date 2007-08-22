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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import fr.ens.transcriptome.nividic.om.io.GPRReader;
import fr.ens.transcriptome.nividic.om.io.NividicIOException;

import junit.framework.TestCase;

public class SerializationTest extends TestCase {

  public void testBioAssay1() throws FileNotFoundException, IOException,
      ClassNotFoundException {

    BioAssay b = BioAssayFactory.createBioAssay();

    File f = new File("/tmp/bioassaySerialized");

    ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(f));
    oos.writeObject(b);
    oos.close();

    ObjectInputStream ois = new ObjectInputStream(new FileInputStream(f));
    BioAssay copy = (BioAssay) ois.readObject();
    ois.close();

    assertEquals(b.getBiologicalId(), copy.getBiologicalId());

    f.delete();
  }

  public void testBioAssay2() throws FileNotFoundException, IOException,
      ClassNotFoundException, NividicIOException {

    InputStream is = this.getClass().getResourceAsStream("/files/testGPR3.gpr");
    GPRReader gprr = new GPRReader(is);
    BioAssay b = gprr.read();

    File f = new File("/tmp/bioassaySerialized");

    ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(f));
    oos.writeObject(b);
    oos.close();

    ObjectInputStream ois = new ObjectInputStream(new FileInputStream(f));
    BioAssay copy = (BioAssay) ois.readObject();
    ois.close();

    assertEquals(b.getBiologicalId(), copy.getBiologicalId());

    f.delete();
  }

}
