/*
 * This file contains methods to manipulate easily Design Objects.
 *
 * @author Laurent Jourdren
 */
 
 
/**
 * Shortcut to read Limma design
 * @param file File(s) to read
 * @return A design Object
 */
function readLimmaDesign(file) {

  var nividicNames = JavaImporter();
  nividicNames.importPackage(Packages.fr.ens.transcriptome.nividic.om.io);

  with (nividicNames)  {
  
    var reader = new LimmaDesignReader(file);
    
    return reader.read();
  }

}

/**
 * Shortcut to read goulphar design
 * @param file File(s) to read
 * @param normalizedFiles Datasource are the normalized files
 * @return A design Object
 */
function readGoulpharDesign(file, normalizedFiles) {

  var nividicNames = JavaImporter();
  nividicNames.importPackage(Packages.fr.ens.transcriptome.nividic.om.io);

  with (nividicNames)  {
  
    var reader = new GoulpharDesignReader(file);
    
    if (normalizedFiles==false) {
        reader.setDataSourceNormalized(true);
      }
      
    return reader.read();
  }

}

/*
 * Write a design
 * @param design Design to write
 * @param file File to write
 * @return nothing
 */
function writeLimmaDesign(design, file) {

  var nividicNames = JavaImporter();
  nividicNames.importPackage(Packages.fr.ens.transcriptome.nividic.om.io);

  with (nividicNames)  {

    var writer = new LimmaDesignWriter(file);
    writer.write(design);
  }
}

