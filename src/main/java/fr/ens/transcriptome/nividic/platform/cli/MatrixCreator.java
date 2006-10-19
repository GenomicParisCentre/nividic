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

package fr.ens.transcriptome.nividic.platform.cli;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import fr.ens.transcriptome.nividic.om.BioAssay;
import fr.ens.transcriptome.nividic.om.ExpressionMatrix;
import fr.ens.transcriptome.nividic.om.ExpressionMatrixFactory;
import fr.ens.transcriptome.nividic.om.filters.BioAssayRemoveIdentifiersFilter;
import fr.ens.transcriptome.nividic.om.filters.BioAssayReplicateFilter;
import fr.ens.transcriptome.nividic.om.io.IDMAReader;
import fr.ens.transcriptome.nividic.om.io.NividicIOException;
import fr.ens.transcriptome.nividic.om.io.SimpleExpressionMatrixWriter;
import fr.ens.transcriptome.nividic.om.translators.DescriptionBioAssayTranslator;
import fr.ens.transcriptome.nividic.om.translators.Translator;
import fr.ens.transcriptome.nividic.sgdb.lims.LimsConnection;
import fr.ens.transcriptome.nividic.sgdb.lims.ReplaceSpotIdsByProbeIdsFilter;

/**
 * This class define a method to easily create matrix from GPR file from dna
 * chip of the ENS microarray platform.
 * @author Laurent Jourdren
 */
public class MatrixCreator {

  private ArrayList bioAssays = new ArrayList();
  private LimsConnection limsConnection;

  private Set emptyIdentifiers = new HashSet();
  private boolean mergeReplicate;
  private boolean replaceSpotIdsByProbeIds;
  private boolean withIntensities;
  private boolean withDescriptions;

  //
  // Getters
  //

  /**
   * Test if the merge replicate is enable
   * @return true if merge replicate is enable
   */
  public boolean isMergeReplicate() {
    return mergeReplicate;
  }

  /**
   * Test if identifers (spotIds) must be replace by probeIds.
   * @return if identifers (spotIds) must be replace by probeIds
   */
  public boolean isReplaceSpotIdsByProbeIds() {
    return replaceSpotIdsByProbeIds;
  }

  /**
   * Get the empty identifiers
   * @return The empty identifiers
   */
  public String[] getEmptyIdentifier() {

    String[] result = new String[this.emptyIdentifiers.size()];

    this.emptyIdentifiers.toArray(result);

    return result;
  }

  /**
   * Get the connection to the lims.
   * @return Returns the limsConnection
   */
  public LimsConnection getLimsConnection() {
    return limsConnection;
  }

  /**
   * Test if the matrix must containts descriptions
   * @return Returns the withDescriptions
   */
  public boolean isWithDescriptions() {
    return withDescriptions;
  }

  /**
   * Test if the matrix must contains the intensiteis
   * @return Returns the withIntensities
   */
  public boolean isWithIntensities() {
    return withIntensities;
  }

  //
  // Setters
  //

  /**
   * Set the connection to the lims
   * @param limsConnection The limsConnection to set
   */
  public void setLimsConnection(final LimsConnection limsConnection) {
    this.limsConnection = limsConnection;
  }

  /**
   * Set the merge replicate enable or not.
   * @param mergeReplicate the value
   */
  public void setMergeReplicate(final boolean mergeReplicate) {
    this.mergeReplicate = mergeReplicate;
  }

  /**
   * Enable or not the replacement of spotIds by probeIds
   * @param enable the value
   */
  public void setReplaceSpotIdsByProbeIds(final boolean enable) {
    this.replaceSpotIdsByProbeIds = enable;
  }

  /**
   * Add an empty identifier
   * @param id Identifier to add
   */
  public void addEmptyIdentifier(final String id) {
    this.emptyIdentifiers.add(id);
  }

  /**
   * Add empty identifiers
   * @param ids Identifiers to add
   */
  public void addEmptyIdentifier(final String[] ids) {

    if (ids != null)
      for (int i = 0; i < ids.length; i++)
        addEmptyIdentifier(ids[i]);
  }

  /**
   * Enable the descriptions in the matrix
   * @param withDescriptions The withDescriptions to set
   */
  public void setWithDescriptions(final boolean withDescriptions) {
    this.withDescriptions = withDescriptions;
  }

  /**
   * Enable the intensities in the matrix
   * @param withIntensities The withIntensities to set
   */
  public void setWithIntensities(final boolean withIntensities) {
    this.withIntensities = withIntensities;
  }

  //
  // Other methods
  //

  /**
   * Read a GPR file.
   * @param file File to read
   * @throws FileNotFoundException if the file is not found
   * @throws NividicIOException if an error occurs while reading the file
   */
  private static BioAssay readGPRFile(final File file)
      throws FileNotFoundException, NividicIOException {

    InputStream is = new FileInputStream(file);

    IDMAReader idmar = new IDMAReader(is);

    return idmar.read();

  }

  /**
   * Get the name of a bioAssay from its filename.
   * @param file File to extract the filename
   * @return the name of the bioAssay
   */
  private static String getBioAssayName(final File file) {

    if (file == null)
      return null;
    String f = file.getName();
    int index = f.indexOf('.');
    if (index == -1)
      return f;
    return f.substring(0, index);
  }

  /**
   * Add a BioAssay to the matrix.
   * @param bioAssay The BioAssay to Add
   */
  public void addBioAssay(final BioAssay bioAssay) {

    if (bioAssay == null)
      return;

    BioAssay bioAssayToAdd = bioAssay;

    if (this.emptyIdentifiers.size() != 0) {

      BioAssayRemoveIdentifiersFilter baeif = new BioAssayRemoveIdentifiersFilter();
      baeif.addIdentifers(getEmptyIdentifier());

      bioAssayToAdd = baeif.filter(bioAssayToAdd);
    }

    if (isReplaceSpotIdsByProbeIds()) {
      System.out.println("probeIDs");
      ReplaceSpotIdsByProbeIdsFilter rsibpi = new ReplaceSpotIdsByProbeIdsFilter();
      rsibpi.setRemoveUnknownSpotId(true);
      bioAssayToAdd = rsibpi.filter(bioAssayToAdd);
    }

    if (isMergeReplicate()) {

      BioAssayReplicateFilter barf = new BioAssayReplicateFilter();
      bioAssayToAdd = barf.filter(bioAssayToAdd);
    }

    this.bioAssays.add(bioAssayToAdd);
  }

  /**
   * Add a GPR file to the matrix
   * @param file GPR file to add
   * @throws FileNotFoundException if the file is not found
   * @throws NividicIOException if an error occurs while reading the file
   */
  public void addGPR(final File file) throws FileNotFoundException,
      NividicIOException {

    BioAssay bioAssay = readGPRFile(file);

    final String name = getBioAssayName(file);
    bioAssay.setName(name);
    System.out.println(name);

    addBioAssay(bioAssay);
  }

  /**
   * Add a GPR file to the matrix
   * @param filename GPR filename to add
   * @throws FileNotFoundException if the file is not found
   * @throws NividicIOException if an error occurs while reading the file
   */
  public void addGPR(final String filename) throws FileNotFoundException,
      NividicIOException {

    addGPR(new File(filename));
  }

  /**
   * Write the matrix.
   * @param os The output stream
   * @throws NividicIOException if an error occurs while writing the matrix
   */
  public void createMatrix(final OutputStream os) throws NividicIOException {

    Translator annotations = null;

    BioAssay[] bas = new BioAssay[this.bioAssays.size()];

    for (int i = 0; i < this.bioAssays.size(); i++)
      bas[i] = (BioAssay) this.bioAssays.get(i);

    if (isWithDescriptions())
      annotations = new DescriptionBioAssayTranslator(bas);

    ExpressionMatrix matrix = ExpressionMatrixFactory.createExpressionMatrix();

    if (isWithIntensities())
      matrix.addDimension(BioAssay.FIELD_NAME_A);

    for (int i = 0; i < this.bioAssays.size(); i++) {
      BioAssay ba = bas[i];
      matrix.addBioAssay(ba);
    }

    SimpleExpressionMatrixWriter mw = new SimpleExpressionMatrixWriter(os);
    mw.setTranslator(annotations);
    mw.write(matrix);

  }
}
