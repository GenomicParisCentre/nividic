 /*
 * This file contains methods to manipulate easily Expression matrix Objects.
 *
 * @author Laurent Jourdren
 */
 
 
 /*
 * Creating methods
 */
 
 /**
 * Shortcut to create a matrix.
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
 * Shortcut to create a matrix with M and A dimensions.
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

/**
 * Shortcut to create an expression matrix merger
 * @return a new ExpressionMatrixMerger Object
 */
function createMatrixMerger() {

  var nividicNames = JavaImporter();
  nividicNames.importPackage(Packages.fr.ens.transcriptome.nividic.om.filters);
  
  with (nividicNames)  {
  
    return new ExpressionMatrixMerger();
  }
}

/*
 * Show methods
 */

/*
 * Show a matrix
 * @param matrix Matrix to show
 * @return nothing
 */
function showMatrix(matrix) {

  var nividicNames = JavaImporter();
  nividicNames.importPackage(Packages.fr.ens.transcriptome.nividic.om);

  with (nividicNames)  {
  
    ExpressionMatrixUtils.printExpressionMatrix(matrix);
  }

}

/*
 * Reader methods
 */
 
 /**
 * Shortcut to read an expression matrix
 * @param file File(s) to read
 * @return An expression matrix Object
 */
function readExpressionMatrix(file) {

  if (file.constructor==String) { file = sf(file); }

  var nividicNames = JavaImporter();
  nividicNames.importPackage(Packages.fr.ens.transcriptome.nividic.om.io);

  with (nividicNames)  {
  
    var reader = new SimpleExpressionMatrixReader(file);
    
    return reader.read();
  }

}

/*
 * Writer methods
 */

/*
 * Write a expression matrix
 * @param em Expression matrix to write
 * @param file File to write
 * @translator Translator to use
 * @return nothing
 */
function writeExpressionMatrix(em, file, translator) {

  if (file.constructor==String) { file = sf(file); }

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

  if (file.constructor==String) { file = sf(file); }

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

/*
 * Other methods
 */
 
/*
 * Swap the M values of a column of an expression matrix
 * @param matrix The matrix to use
 * @param column the name of column
 * @return nothing
 */
function swapExpressionMatrix(matrix, column) {

  var nividicNames = JavaImporter();
  nividicNames.importPackage(Packages.fr.ens.transcriptome.nividic.om);

  with(nividicNames) {
    
    ExpressionMatrixUtils.swap(matrix,column);
  }
}


/*
 * Filters
 */
 
/*
 * Create an ExpressionMatrix row filter from a BioAssayFilter
 * @param bioAssayFilter Filter to adapt
 * @param threshold to reject the row
 */
function createMatrixRowFilterAdapter(bioAssayFilter, threshold) {

  var nividicNames = JavaImporter();
  nividicNames.importPackage(Packages.fr.ens.transcriptome.nividic.om.filters);

  with(nividicNames) {
    
    return new ExpressionMatrixRowFilterBioAssayFilterAdapter(bioAssayFilter, threshold);
  }
}

/*
 * Create an ExpressionMatrix column filter from a BioAssayFilter
 * @param bioAssayFilter Filter to adapt
 * @param threshold to reject the row
 * @return a new ExpressionMatrixFilter
 */
function createMatrixColumnFilterAdapter(bioAssayFilter, threshold) {

  var nividicNames = JavaImporter();
  nividicNames.importPackage(Packages.fr.ens.transcriptome.nividic.om.filters);

  with(nividicNames) {
    
    return new ExpressionMatrixColumnFilterBioAssayFilterAdapter(bioAssayFilter, threshold);
  }
}
