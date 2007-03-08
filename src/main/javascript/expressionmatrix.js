 /*
 * This file contains methods to manipulate easily Expression matrix Objects.
 *
 * @author Laurent Jourdren
 */
 
 /**
 * Shortcut to read an expression matrix
 * @param file File(s) to read
 * @return An expression matrix Object
 */
function readExpressionMatrix(file) {

  var nividicNames = JavaImporter();
  nividicNames.importPackage(Packages.fr.ens.transcriptome.nividic.om.io);

  with (nividicNames)  {
  
    var reader = new SimpleExpressionMatrixReader(file);
    
    return reader.read();
  }

}

/*
 * Write a expression matrix
 * @param em Expression matrix to write
 * @param file File to write
 * @translator Translator to use
 * @return nothing
 */
function writeExpressionMatrix(em, file, translator) {

  var nividicNames = JavaImporter();
  nividicNames.importPackage(Packages.fr.ens.transcriptome.nividic.om.io);

  with (nividicNames)  {

    var writer = new SimpleExpressionMatrixWriter(file);
    
    if (translator != null) {
      writer.setTranslator(translator);
    }
    
    writer.write(em);
  }
}

/*
 * Write a expression matrix at the standford format
 * @param em Expression matrix to write
 * @param file File to write
 * @translator Translator to use
 * @return nothing
 */
function writeStandfordExpressionMatrix(em, file, translator) {

  var nividicNames = JavaImporter();
  nividicNames.importPackage(Packages.fr.ens.transcriptome.nividic.om.io);

  with (nividicNames)  {

    var writer = new StandfordExpressionMatrixWriter(file);
    
    if (translator != null) {
      writer.setTranslator(translator);
    }
    
    writer.write(em);
  }
}

/**
 * Short to create a matrix.
 * @param the name of the matrix
 * @return a new matrix Object
 */
function createMatrix(name) {

  var nividicNames = JavaImporter();
  nividicNames.importPackage(Packages.fr.ens.transcriptome.nividic.om);
  
  with (nividicNames)  {
  
  	return ExpressionMatrixFactory.createExpressionMatrix(name);
  
  }

}

/**
 * Short to create a matrix with M and A dimensions.
 * @param the name of the matrix
 * @return a new matrix Object
 */
function createMAMatrix(name) {

  var nividicNames = JavaImporter();
  nividicNames.importPackage(Packages.fr.ens.transcriptome.nividic.om);
  
  with (nividicNames)  {

    var matrix = createMatrix(name);
    matrix.addDimension(BioAssay.FIELD_NAME_A);
    
    return matrix;
  }
}
