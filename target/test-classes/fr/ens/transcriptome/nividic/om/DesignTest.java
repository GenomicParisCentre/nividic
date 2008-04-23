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
 * of the �cole Normale Sup�rieure and the individual authors.
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

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.text.ParseException;
import java.util.List;

import junit.framework.TestCase;
import fr.ens.transcriptome.nividic.om.design.Design;
import fr.ens.transcriptome.nividic.om.design.DesignFactory;
import fr.ens.transcriptome.nividic.om.design.Slide;
import fr.ens.transcriptome.nividic.om.design.SlideDescription;
import fr.ens.transcriptome.nividic.om.design.io.DesignWriter;
import fr.ens.transcriptome.nividic.om.design.io.GoulpharDesignReader;
import fr.ens.transcriptome.nividic.om.design.io.LimmaDesignReader;
import fr.ens.transcriptome.nividic.om.design.io.LimmaDesignWriter;
import fr.ens.transcriptome.nividic.om.io.NividicIOException;
import fr.ens.transcriptome.nividic.om.samples.Samples;

public class DesignTest extends TestCase {

  private Design createDesign() {

    return DesignFactory.create2ColorsDesign();
  }

  public void testRead1() throws NividicIOException {

    InputStream is =
        this.getClass().getResourceAsStream("/files/SwirlSample.txt");
    LimmaDesignReader ldr = new LimmaDesignReader(is);

    Design d = ldr.read();

    Slide s = d.getSlide(0);
    // System.out.println("id: " + s.getName());
    // System.out.println("s/n: "+s.getDescription().getSerialNumber());

    ByteArrayOutputStream os = new ByteArrayOutputStream();

    DesignWriter dw = new LimmaDesignWriter(os);
    dw.write(d);

    // System.out.println(os.toString());
  }

  public void testRead2() throws NividicIOException {

    InputStream is =
        this.getClass().getResourceAsStream("/files/ApoAITargets.txt");
    LimmaDesignReader ldr = new LimmaDesignReader(is);

    Design d = ldr.read();

    Slide s = d.getSlide(0);
    // System.out.println("id: " + s.getName());
    // System.out.println("s/n: " + s.getDescription().getSerialNumber());

    ByteArrayOutputStream os = new ByteArrayOutputStream();

    DesignWriter dw = new LimmaDesignWriter(os);
    dw.write(d);

    // System.out.println(os.toString());
  }

  public void testGlobal() throws ParseException, NividicIOException {

    Design d = createDesign();

    Samples samples = d.getSamples();
    samples.addSample("WT");
    samples.addSample("Swirl");

    d.addSlide("81");
    d.addSlide("82");
    d.addSlide("93");
    d.addSlide("94");

    d.setDescription("81", SlideDescription.SLIDE_NUMBER_FIELD, "l81");
    d.setDescription("82", SlideDescription.SLIDE_NUMBER_FIELD, "l82");
    d.setDescription("93", SlideDescription.SLIDE_NUMBER_FIELD, "l93");
    d.setDescription("94", SlideDescription.SLIDE_NUMBER_FIELD, "l94");

    d.setTarget("81", PhysicalConstants.GREEN_FIELD, "Swirl");
    d.setTarget("81", PhysicalConstants.RED_FIELD, "WT");
    d.setSource("81", "swirl.spot1");
    d.getSlideDescription("81").setDate("20/09/2001");

    d.setTarget("82", PhysicalConstants.GREEN_FIELD, "WT");
    d.setTarget("82", PhysicalConstants.RED_FIELD, "Swirl");
    d.setSource("82", "swirl.spot2");
    d.getSlideDescription("82").setDate("20/09/2001");

    d.setTarget("93", PhysicalConstants.GREEN_FIELD, "Swirl");
    d.setTarget("93", PhysicalConstants.RED_FIELD, "WT");
    d.setSource("93", "swirl.spot3");
    d.getSlideDescription("93").setDate("8/11/2001");

    d.setTarget("94", PhysicalConstants.GREEN_FIELD, "WT");
    d.setTarget("94", PhysicalConstants.RED_FIELD, "Swirl");
    d.setSource("94", "swirl.spot4");
    d.getSlideDescription("94").setDate("8/11/2001");

    ByteArrayOutputStream os = new ByteArrayOutputStream();

    DesignWriter dw = new LimmaDesignWriter(os);
    dw.write(d);

    // System.out.println(os.toString());

  }

  public void testGoulpharAroma() throws NividicIOException {

    InputStream is = this.getClass().getResourceAsStream("/files/param.dat");
    GoulpharDesignReader ldr = new GoulpharDesignReader(is);
    ldr.setDataSourceNormalized(false);

    Design d = ldr.read();

    ByteArrayOutputStream os = new ByteArrayOutputStream();

    DesignWriter dw = new LimmaDesignWriter(os);
    dw.write(d);

    // System.out.println(os.toString());

  }

  public void testGoulpha() throws NividicIOException {

    InputStream is =
        this.getClass().getResourceAsStream("/files/param_goulphar.dat");
    GoulpharDesignReader ldr = new GoulpharDesignReader(is);
    ldr.setDataSourceNormalized(false);

    Design d = ldr.read();

    ByteArrayOutputStream os = new ByteArrayOutputStream();

    DesignWriter dw = new LimmaDesignWriter(os);
    dw.write(d);

    // System.out.println(os.toString());

  }

  public void testGetSamples() {

    Design d = createDesign();

    Samples samples = d.getSamples();

    assertNotNull(samples);

    samples.addSample("WT");
    samples.addSample("Mutant");

    assertEquals(2, samples.getSamplesCount());

  }

  public void testIsLabel() {

    Design d = createDesign();

    assertTrue(d.isLabel(PhysicalConstants.GREEN_FIELD));
    assertTrue(d.isLabel(PhysicalConstants.RED_FIELD));
    assertFalse(d.isLabel("Target"));

  }

  public void testGetLabelCount() {

    Design d = createDesign();
    assertEquals(2, d.getLabelCount());

  }

  public void testAddLabel() {

    Design d = createDesign();
    assertEquals(2, d.getLabelCount());
    d.addLabel("Cy9");
    assertEquals(3, d.getLabelCount());
    assertTrue(d.isLabel("Cy9"));

  }

  public void testRenameLabel() {
    // fail("Not yet implemented");
  }

  public void testGetLabelsNames() {

    Design d = createDesign();
    List<String> labels = d.getLabelsNames();

    for (String l : labels)
      assertTrue(d.isLabel(l));

  }

  public void testRemoveLabel() {
    // fail("Not yet implemented");
  }

  public void testIsSlide() {
    // fail("Not yet implemented");
  }

  public void testAddSlide() {
    // fail("Not yet implemented");
  }

  public void testGetSlideCount() {
    // fail("Not yet implemented");
  }

  public void testRenameSlide() {
    // fail("Not yet implemented");
  }

  public void testGetSlides() {
    // fail("Not yet implemented");
  }

  public void testRemoveSlide() {
    // fail("Not yet implemented");
  }

  public void testSetTarget() {
    // fail("Not yet implemented");
  }

  public void testGetTarget() {
    // fail("Not yet implemented");
  }

  public void testGetSlideDescription() {
    // fail("Not yet implemented");
  }

  public void testSetSourceStringDataSource() {
    // fail("Not yet implemented");
  }

  public void testSetSourceStringString() {
    // fail("Not yet implemented");
  }

  public void testSetSourceFormatStringBioAssayFormat() {
    // fail("Not yet implemented");
  }

  public void testSetSourceFormatStringString() {
    // fail("Not yet implemented");
  }

  public void testGetSource() {
    // fail("Not yet implemented");
  }

  public void testGetSourceInfo() {
    // fail("Not yet implemented");
  }

  public void testGetFormat() {
    // fail("Not yet implemented");
  }

}
