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

package fr.ens.transcriptome.nividic.sgdb.agilent;

class AnnotationEntry {

  private String probeId;
  private String transcriptId;
  private String geneSymbol;
  private String mgi;
  private String description;
  private String chromosome;
  private String entrezId;

  private int score = -1;

  //
  // Getters
  //  

  /**
   * @return Returns the chromosome
   */
  String getChromosome() {
    return chromosome == null ? "" : chromosome;
  }

  /**
   * @return Returns the description
   */
  String getDescription() {
    return description == null ? "" : description;
  }

  /**
   * @return Returns the entrezID
   */
  String getEntrezId() {
    return entrezId == null ? "" : entrezId;
  }

  /**
   * @return Returns the probeId
   */
  String getProbeId() {
    return probeId == null ? "" : probeId;
  }

  /**
   * @return Returns the transcriptId
   */
  String getTranscriptId() {
    return transcriptId == null ? "" : transcriptId;
  }

  /**
   * @return Returns the geneSymbol
   */
  String getGeneSymbol() {
    return geneSymbol == null ? "" : geneSymbol;
  }

  /**
   * @return Returns the mgi
   */
  String getMgi() {
    return mgi == null ? "" : mgi;
  }

  //
  // Setters
  //

  /**
   * @param chromosome The chromosome to set
   */
  void setChromosome(final String chromosome) {
    this.chromosome = chromosome;
  }

  /**
   * @param description The description to set
   */
  void setDescription(final String description) {
    this.description = description;
  }

  /**
   * @param entrezID The entrezID to set
   */
  void setEntrezId(final String entrezID) {
    entrezId = entrezID;
  }

  /**
   * @param probeId The probeId to set
   */
  void setProbeId(final String probeId) {
    this.probeId = probeId;
  }

  /**
   * @param transcriptId The transcriptId to set
   */
  void setTranscriptId(final String transcriptId) {
    this.transcriptId = transcriptId;
  }

  /**
   * @param geneSymbol The geneSymbol to set
   */
  void setGeneSymbol(String geneSymbol) {
    this.geneSymbol = geneSymbol;
  }

  /**
   * @param mgi The mgi to set
   */
  void setMgi(String mgi) {
    this.mgi = mgi;
  }

  //
  // Other methods
  //

  public boolean isEntrezId() {

    return !"".equals(getEntrezId());
  }

  public void set(final String line) {

    if (line == null)
      return;

    String[] fields = line.split("\t");

    if (fields.length > 0)
      setProbeId(fields[0]);
    if (fields.length > 1)
      setTranscriptId(fields[1]);
    if (fields.length > 2)
      setGeneSymbol(fields[2]);
    if (fields.length > 3)
      setMgi(fields[3]);
    if (fields.length > 4)
      setDescription(fields[4]);
    if (fields.length > 5)
      setChromosome(fields[5]);
    if (fields.length > 6)
      setEntrezId(fields[6]);
  }

  public int score() {

    if (this.score != -1)
      return this.score;

    int result = 0;

    if (!"".equals(getProbeId()))
      result++;
    if (!"".equals(getTranscriptId()))
      result++;
    if (!"".equals(getGeneSymbol()))
      result += 3;
    if (!"".equals(getMgi()))
      result++;
    if (!"".equals(getDescription())
        || !"No associated gene".equals(getDescription()))
      result++;
    if (!"".equals(getChromosome()))
      result++;
    if (!"".equals(getEntrezId()))
      result += 3;

    this.score = result;
    return this.score;
  }

  public String toString() {

    // ProbeID TranscriptID GeneSymbol MGI Description Chromosome EntrezID

    return getProbeId() + "\t" + getGeneSymbol() + "\t" + getTranscriptId()
        + "\t" + getMgi() + "\t" + getDescription() + "\t" + getChromosome()
        + "\t" + getEntrezId();
  }

  //
  // Constructor
  //

  public AnnotationEntry(final String line) {

    set(line);
  }

}
