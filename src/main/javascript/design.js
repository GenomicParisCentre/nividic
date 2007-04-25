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
  nividicNames.importPackage(Packages.fr.ens.transcriptome.nividic.om);

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
  nividicNames.importPackage(Packages.fr.ens.transcriptome.nividic.om);

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
  nividicNames.importPackage(Packages.fr.ens.transcriptome.nividic.om);

  with (nividicNames)  {
  
    return DesignUtils.showDesign(design);
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
  nividicNames.importPackage(Packages.fr.ens.transcriptome.nividic.om.io);

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
  nividicNames.importPackage(Packages.fr.ens.transcriptome.nividic.om.io);

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
  nividicNames.importPackage(Packages.fr.ens.transcriptome.nividic.om.io);

  with (nividicNames)  {

    var writer = new LimmaDesignWriter(file);
    writer.write(design);
  }
}

