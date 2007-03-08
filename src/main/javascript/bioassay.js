/*
 * This file contains methods to manipulate easily BioAssay Objects.
 *
 * @author Laurent Jourdren
 *
 * TODO add function to read/write imagene Files
 */

/*
 * Reader methods
 */

/*
 * Internal function.
 * Get the bioassay reader object
 * @param file File to read
 * @param type Type of the file to read
 * @return a BioAssayReader object
 */
function _getBioAssayReader(file, type) {

  var nividicNames = JavaImporter();
  nividicNames.importPackage(Packages.fr.ens.transcriptome.nividic.om.io);

  with (nividicNames)  {
    switch(type) {
  
      case "gpr": 
        return new GPRReader(file);

      case "idma": 
        return new IDMAReader(file);

      case "total.summary":
        return new TotalSummaryReader(file);
    }
  }

  return null;
}

/*
 * Read a bioAssay
 * @param file File(s) to read
 * @param type Type of the file (gpr,idma...)
 * @param allFields Read all the fields or only default fields
 * @return a BioAssay file
 */
function readBioAssay(file, type, allFields, comma) {


  if (file instanceof Array) {

    var result = new Array;
    for (i=0; i<file.length;i++) {
      
      var reader = _getBioAssayReader(file[i], type);

      if (allFields==true) { 
        reader.addAllFieldsToRead(); 
      }

      if (comma==true) {
        reader.setCommaDecimalSeparator(true);
      }

      result[i]=reader.read();
      result[i].setName(file[i].getName());
    }

    return result;
  } 
  else {

      var reader = _getBioAssayReader(file, type);

      if (allFields==true) { 
        reader.addAllFieldsToRead(); 
      }

      if (comma==true) {
        reader.setCommaDecimalSeparator(true);
      }

      var ba = reader.read();
      ba.setName(file.getName());
      return ba;
  }
}

/**
 * Shortcut to read GPR file(s)
 * @param file File(s) to read
 * @param allField read all fields
 * @return A BioAssay Object
 */
function readGPR(file, allFields) {

  return readBioAssay(file, "gpr", allFields, false);
}



/**
 * Shortcut to read IDMA file(s)
 * @param file File(s) to read
 * @param comma true if comma is the decimal separator
 * @return A BioAssay Object
 */
function readIDMA(file, comma) {

  return readBioAssay(file, "idma", false, comma);
}

/**
 * Shortcut to read total.summary file(s)
 * @param file File(s) to read
 * @param comma true if comma is the decimal separator
 * @return A BioAssay Object
 */
function readTotalSummary(file, comma) {

  return readBioAssay(file, "total.summary", false, comma);
}

/*
 * Writer methods
 */

/*
 * Internal function.
 * Get the bioassay reader object
 * @param file File to read
 * @param type Type of the file to read
 * @return a BioAssayReader object
 */
function _getBioAssayWriter(file, type) {

  var nividicNames = JavaImporter();
  nividicNames.importPackage(Packages.fr.ens.transcriptome.nividic.om.io);

  with (nividicNames)  {
    switch(type) {
  
      case "gpr": 
        return new GPRWriter(file);

      case "idma": 
        return new IDMAWriter(file);

      case "total.summary":
        return new TotalSummaryWriter(file);
    }
  }

  return null;
}

/*
 * Write a bioAssay
 * @param bioAssay BioAssay to write
 * @param file File(s) to write
 * @param type Type of the file (gpr,idma...)
 * @param allFields Write all the fields or only default fields
 * @return nothing
 */
function writeBioAssay(bioAssay, file, type, allFields) {

  var writer = _getBioAssayWriter(file, type);

  if (allFields==true) { 
    writer.addAllFieldsToWrite(); 
  }

  writer.write(bioAssay);
}

/**
 * Shortcut to write GPR file
 * @param file File to write
 * @return nothing
 */
function writeGPR(bioAssay, file) {

  writeBioAssay(bioAssay, file, "gpr", true);
}

/**
 * Shortcut to write IDMA file
 * @param file File to write
 * @return nothing
 */
function writeIDMA(bioAssay, file) {

  writeBioAssay(bioAssay, file, "idma", true);
}

/**
 * Shortcut to write total.summary file
 * @param file File to write
 * @return nothing
 */
function writeTotalSummary(bioAssay, file) {

  writeBioAssay(bioAssay, file, "total.summary", true);
}

/*
 *  Sort methods
 */

/*
 * Get a sorter of bioAssay objects
 * @return the sorter
 */
function createMASorter() {

  return new Packages.fr.ens.transcriptome.nividic.om.filters.BioAssayMASorterComparator();
}

/*
 *  Filter methods
 */

/*
 * Create a filter on values greater or equals to the parameter
 * @param threshold
 */
function createMSupFilter(threshold) {

  var nividicNames = JavaImporter();
  nividicNames.importPackage(Packages.fr.ens.transcriptome.nividic.om.filters);

  with(nividicNames) {

    obj = { test: function (v) { return !isNaN(v)  && v >= threshold;  } };
    return new BioAssayMFilter(obj);
  }
}

/*
 * Create a filter on values greater or equals to the parameter
 * @param threshold
 */
function createMInfFilter(threshold) {

  var nividicNames = JavaImporter();
  nividicNames.importPackage(Packages.fr.ens.transcriptome.nividic.om.filters);

  with(nividicNames) {

    obj = { test: function (v) { return !isNaN(v)  && v <= threshold; } };
    return new BioAssayMFilter(obj);
  }
}

