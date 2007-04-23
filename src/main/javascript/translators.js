/*
 * This file contains translators methods.
 *
 * @author Laurent Jourdren
 */
 
  
 /*
 * Create a translator based on the Description field of the bioAssay
 * @param bioAssay BioAssay to use by the translator
 * @return a new translator
 */
function createTranslatorFromBioAssay(bioAssay) {

  var nividicNames = JavaImporter();
  nividicNames.importPackage(Packages.fr.ens.transcriptome.nividic.om.translators);
  
  with (nividicNames)  {
  
  	return new DescriptionBioAssayTranslator(bioAssay);
  }

}

/*
 * Create a translator based on the Description field of the bioAssay.
 * @param design BioAssay of the design to use by the translator
 * @return a new translator
 */
function createTranslatorFromDesign(design) {

  var nividicNames = JavaImporter();
  nividicNames.importPackage(Packages.fr.ens.transcriptome.nividic.om.translators);
  
  with (nividicNames)  {
  
  	return new DescriptionBioAssayTranslator(design);
  }

}

/*
 * Create a translator based on a annotation file.
 * @param file File to read
 */
function createTranslatorFromAnnotationFile(file) {

  if (file==null) { return null; }

  if (file.constructor==String) { file = sf(file); }

  var nividicNames = JavaImporter();
  nividicNames.importPackage(Packages.fr.ens.transcriptome.nividic.om.io);
  
  with (nividicNames)  {
  
    var reader = new MultiColumnTranslatorReader(file);
  
  	return reader.read();
  }

}
