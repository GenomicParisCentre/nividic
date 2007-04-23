/*
 * This file contains methods to manipulate easily BioAssay Objects.
 *
 * @author Laurent Jourdren
 *
 * TODO add function to read/write imagene Files
 */

/*
 * Creating methods
 */

/*
 * Create an empty BioAssay object.
 * @param name The name of the bioAssay (optional)
 * @return a new bioAssay object
 */
function createBioAssay(name) {

  var nividicNames = JavaImporter();
  nividicNames.importPackage(Packages.fr.ens.transcriptome.nividic.om);

  with (nividicNames)  {
  
    var ba = BioAssayFactory.createBioAssay();
    
    if (name!=null) { ba.setName(name); }
    return ba;
  }

}

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
 * Read a bioAssay.
 * @param file File(s) to read
 * @param type Type of the file (gpr,idma...)
 * @param allFields Read all the fields or only default fields
 * @return a BioAssay file
 */
function readBioAssay(file, type, allFields, comma) {

  if (file==null) { return null; }
  
  if (file.constructor==String) { file = sf(file); }  

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
 * Shortcut to read GPR file(s).
 * @param file File(s) to read
 * @param allField read all fields
 * @return A BioAssay Object
 */
function readGPR(file, allFields) {

  return readBioAssay(file, "gpr", allFields, false);
}



/**
 * Shortcut to read IDMA file(s).
 * @param file File(s) to read
 * @param comma true if comma is the decimal separator
 * @return A BioAssay Object
 */
function readIDMA(file, comma) {

  return readBioAssay(file, "idma", false, comma);
}

/**
 * Shortcut to read total.summary file(s).
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

  if (file.constructor==String) { file = sf(file); }

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
 * Write a bioAssay.
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
 * Shortcut to write GPR file.
 * @param file File to write
 * @return nothing
 */
function writeGPR(bioAssay, file) {

  writeBioAssay(bioAssay, file, "gpr", true);
}

/**
 * Shortcut to write IDMA file.
 * @param file File to write
 * @return nothing
 */
function writeIDMA(bioAssay, file) {

  writeBioAssay(bioAssay, file, "idma", true);
}

/**
 * Shortcut to write total.summary file.
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
 * Get a sorter of bioAssay objects.
 * @return the sorter
 */
function createMASorter() {

  return new Packages.fr.ens.transcriptome.nividic.om.filters.BioAssayMASorterComparator();
}

/*
 * BioAssay FieldNames
 */
   
 /** Column name for red data. */
  var FIELD_NAME_RED = "red";
  /** Column name for green data. */
  var FIELD_NAME_GREEN = "green";
  /** Column name for flags data. */
  var FIELD_NAME_FLAG = "flags";
  /** Column name for name data. */
  var FIELD_NAME_ID = "id";
  /** Column name for ratio data. */
  var FIELD_NAME_RATIO = "ratio";
  /** Column name for bright data. */
  var FIELD_NAME_BRIGHT = "bright";
  /** Column name for description data. */
  var FIELD_NAME_DESCRIPTION = "description";
  /** Column name for a coordinate of a MA plot. */
  var FIELD_NAME_A = "a";
  /** Column name for m coordinate of a MA plot. */
  var FIELD_NAME_M = "m";
  /** Column name for the standard deviation of a values. */
  var FIELD_NAME_STD_DEV_A = "stddeva";
  /** Column name for the standard deviation of m values. */
  var FIELD_NAME_STD_DEV_M = "stddevm";

/*
 * Flags values
 */

  /** Flag bad. */
  var FLAG_BAD = -100;
  /** Flag abscent. */
  var FLAG_ABSCENT = -75;
  /** Flag not found. */
  var FLAG_NOT_FOUND = -50;
  /** Flag unflagged. */
  var FLAG_UNFLAGGED = 0;
  /** Flag normalized. */
  var FLAG_NORMALIZED = 1;
  /** Flag good. */
  var FLAG_GOOD = 100;
 

/*
 *  Filter methods
 */


/*
 * Create a filter on values greater or equals to the parameter.
 * @param field Field to use
 * @param threshold
 * @param condition
 */
function createThresholdFilter(field, threshold, condition) {

  if (field==null || threshold==null || condition ==null) return null;

  var nividicNames = JavaImporter();
  nividicNames.importPackage(Packages.fr.ens.transcriptome.nividic.om.filters);

  with(nividicNames) {

    /*obj = { test: function (v) { return !isNaN(v)  && v <= threshold; } };
    return new BioAssayMFilter(obj);*/
    
    return new BioAssayDoubleThresholdFilter(field, threshold, condition);
  }

}


/*
 * Create a filter on values greater or equals to the parameter.
 * @param threshold
 */
function createInfFilter(field,threshold) {

  return createThresholdFilter(field, threshold, "<=");
}

/*
 * Create a filter on values greater or equals to the parameter.
 * @param threshold
 */
function createSupFilter(field, threshold) {

  return createThresholdFilter(field, threshold, ">=");
}

/*
 * Create a filter on values greater or equals to the parameter.
 * @param threshold
 */
function createAInfFilter(threshold) {

  return createInfFilter(FIELD_NAME_A, threshold);
}

/*
 * Create a filter on values greater or equals to the parameter.
 * @param threshold
 */
function createASupFilter(threshold) {

  return createSupFilter(FIELD_NAME_A, threshold);
}


/*
 * Create a filter on values greater or equals to the parameter.
 * @param threshold
 */
function createMSupFilter(threshold) {

  return createSupFilter(FIELD_NAME_M, threshold);
}

/*
 * Create a filter on values greater or equals to the parameter.
 * @param threshold
 */
function createMInfFilter(threshold) {

  return createInfFilter(FIELD_NAME_M, threshold);
}

/*
 * Create a filter on values greater or equals to the parameter.
 * @param threshold
 */
function createAInfFilter(threshold) {

 return createInfFilter(FIELD_NAME_A, threshold);
}


/**
 * Swap the M values of a BioAssay.
 * @param bioAssay The bioAssay to swap
 * @return nothing
 */
function swapBioAssay(bioAssay) {

  var nividicNames = JavaImporter();
  nividicNames.importPackage(Packages.fr.ens.transcriptome.nividic.om);

  with(nividicNames) {
    
    BioAssayUtils.swap(bioAssay);
  }
}





