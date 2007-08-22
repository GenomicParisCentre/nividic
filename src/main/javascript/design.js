/*
 * This file contains methods to manipulate easily Design Objects.
 *
 * @author Laurent Jourdren
 */
 
 
/*
 * Creating methods
 */

/*
 * Create a standard design for 2 colors experiments
 * @return a new Design
 */
function create2ColorsDesign() {

  var nividicNames = JavaImporter();
  nividicNames.importPackage(Packages.fr.ens.transcriptome.nividic.om.design);

  with (nividicNames)  {

    return DesignFactory.create2ColorsDesign();
  }

}

/*
 * Create a design without targets.
 * @return a new Design
 */
function createEmptyDesign() {

  var nividicNames = JavaImporter();
  nividicNames.importPackage(Packages.fr.ens.transcriptome.nividic.om.design);

  with (nividicNames)  {

    return DesignFactory.create2ColorsDesign();
  }

}

/*
 * Print methods
 */
 
/*
 * Show a design.
 * @param design Design to show
 * @return nothing
 */
 function showDesign(design) {

  var nividicNames = JavaImporter();
  nividicNames.importPackage(Packages.fr.ens.transcriptome.nividic.om.design);

  with (nividicNames)  {
  
    return DesignUtils.showDesign(design);
  }

}
  
 /*
 * Convert all the DataSource of a design to FileDataSources.
 * @param design Design to use
 * @param baseDir Base directory of the BioAssays files of the design
 * @return nothing
 */
 function convertAllDataSourceToFileDataSources(design, baseDir) {

  var nividicNames = JavaImporter();
  nividicNames.importPackage(Packages.fr.ens.transcriptome.nividic.om.design);

  with (nividicNames)  {
  
    return DesignUtils.convertAllDataSourceToFileDataSources(design, baseDir);
  }

}
  
 
 /*
 * Reader methods
 */
 
/**
 * Shortcut to read Limma design
 * @param file File(s) to read
 * @return A design Object
 */
function readLimmaDesign(file) {



  if (file.constructor==String) { file = sf(file); }

  var nividicNames = JavaImporter();
  nividicNames.importPackage(Packages.fr.ens.transcriptome.nividic.om.design.io);

  with (nividicNames)  {
  
    var reader = new LimmaDesignReader(file);
    
    var design =  reader.read();
    design.setName(file.getName());
      
    return design;
  }

}

/**
 * Shortcut to read goulphar design
 * @param file File(s) to read
 * @param normalizedFiles Datasource are the normalized files
 * @return A design Object
 */
function readGoulpharDesign(file, normalizedFiles) {

  if (file.constructor==String) { file = sf(file); }

  var nividicNames = JavaImporter();
  nividicNames.importPackage(Packages.fr.ens.transcriptome.nividic.om.design.io);

  with (nividicNames)  {
  
    var reader = new GoulpharDesignReader(file);
    
    if (normalizedFiles==false) {
        reader.setDataSourceNormalized(true);
      }
     
    var design =  reader.read();
    design.setName(file.getName());
      
    return design;
  }

}

/**
 * Shortcut to read design from the lims
 * @param slideSerialNumbers Serial numbers of the slide of the design
 * @return A design Object
 */
function readDesignFromLims(slideSerialNumbers) {

  var nividicNames = JavaImporter();
  nividicNames.importPackage(Packages.fr.ens.transcriptome.nividic.sgdb.io);

  with (nividicNames)  {
  
    var reader = new SGDBLimsDesignReader(slideSerialNumbers);
    
        
    var design =  reader.read();      
    return design;
  }

}


/*
 * Writer methods
 */

/*
 * Write a design
 * @param design Design to write
 * @param file File to write
 * @return nothing
 */
function writeLimmaDesign(design, file) {

  if (file.constructor==String) { file = sf(file); }

  var nividicNames = JavaImporter();
  nividicNames.importPackage(Packages.fr.ens.transcriptome.nividic.om.design.io);

  with (nividicNames)  {

    var writer = new LimmaDesignWriter(file);
    writer.write(design);
  }
}

