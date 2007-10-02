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
 * @param noHeader true if there is no header for column names
 * @return a new translator
 */
function createTranslatorFromAnnotationFile(file, noheader) {

  if (file==null) { return null; }

  if (file.constructor==String) { file = sf(file); }
  
  if (noheader==undefined) noheaderValue = false;
  else noheaderValue = noheader;

  var nividicNames = JavaImporter();
  nividicNames.importPackage(Packages.fr.ens.transcriptome.nividic.om.translators.io);
  
  
  with (nividicNames)  {
  
    var reader = new MultiColumnTranslatorReader(file, noheaderValue);
  
  	return reader.read();
  }

}

/*
 * Create a translator based on a unique identifer
 * @param ids Identifier to set unique
 * @param translator Translator to use
 * @param translatorField field of the translator to use
 * @param newFieldName the name of new field
 * @return a new translator
 */
function createUniqueIdTranslator(ids, translator, translatorField, newFieldName) {
  
  if (translatorField==undefined) translatorField=null;
  if (newFieldName==undefined) newFieldName=null;
  
  return new Packages.fr.ens.transcriptome.nividic.om.translators
    .UniqueIdentifierTranslator(ids, translator, translatorField, newFieldName);
}

/*
 * Create a translator that add the identifier to translations
 * @param translator Translator to use
 * @param newFieldName the name of new field
 * @return a new translator 
 */
function createAddIdentifierTranslator(translator, newFieldName) {
  
  if (newFieldName==undefined) newFieldName=null;
  
  return new Packages.fr.ens.transcriptome.nividic.om.translators
    .AddIdentifierTranslator(translator, newFieldName);
}

/**
 * Create a translator that can select annotation from another translator.
 * @param translator Translator to use 
 * @param fields Fields to use
 * @return a new translator 
 */
function createSelectAnnotationFieldsTranslator(translator, fields) {
  
  return new Packages.fr.ens.transcriptome.nividic.om.translators
    .SelectAnnotationFieldsTranslator(translator, fields);
}

/**
 * Create a translator that add common links to the annaotations.
 * @param translator Translator to use 
 * @return a new translator 
 */
function createCommonLinksTranslator(translator) {
  
  return new Packages.fr.ens.transcriptome.nividic.om.translators
    .CommonLinksInfoTranslator(translator);
}

/**
 * Create a translator that add fasta sequence to annotation.
 * @param file Fasta file to use 
 * @return a new translator 
 */
function createFastaTranslator(file) {
  
  if (file.constructor==String) { file = sf(file); }
  
  return new Packages.fr.ens.transcriptome.nividic.om.translators.io
    .FastaTranslator(file);
}

/**
 * Create a translator that can concatenat other translators.
 * @param translator1 First translator 
 * @param translator2 Second translator 
 * @return a new translator 
 */
function createConcatTranslator(translator1, translator2) {
  
  if (translator1==undefined && translator2==undefined)
    return new Packages.fr.ens.transcriptome.nividic.om.translators
    .ConcatTranslator();
    
  return new Packages.fr.ens.transcriptome.nividic.om.translators
    .ConcatTranslator(translator1, translator2);
}

/**
 * Create a translator that join two other translators.
 * @param translator1 First translator 
 * @param joinField  
 * @param translator2 Second translator 
 * @return a new translator 
 */
function createJoinTranslator(translator1, joinField, translator2, returnTranslation1IfNoTranslation) {
     
   var rNoTranslation;
   
   if (returnTranslation1IfNoTranslation==undefined) rNoTranslation = false;
   else rNoTranslation = returnTranslation1IfNoTranslation;
     
  return new Packages.fr.ens.transcriptome.nividic.om.translators
    .JoinTranslator(translator1, joinField, translator2, rNoTranslation);
}


