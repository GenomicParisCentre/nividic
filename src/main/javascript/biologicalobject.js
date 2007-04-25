/*
 * This file contains utility methods.
 *
 * @author Laurent Jourdren
 */
 
/*
 * Print methods
 */
 
/*
 * Show history of biological objects.
 * @param bo A BiologicalObject
 * @return nothing
 */
 function showHistory(bo) {

  var nividicNames = JavaImporter();
  nividicNames.importPackage(Packages.fr.ens.transcriptome.nividic.om);

  with (nividicNames)  {
  
    return HistoryUtils.showHistory(bo);
  }

}

/*
 * Writer methods
 */

/*
 * Write a history object
 * @param bo BiologicalObject to write
 * @param file File to write
 * @return nothing
 */
function writeHistory(bo, file) {

  if (file.constructor==String) { file = sf(file); }

  var nividicNames = JavaImporter();
  nividicNames.importPackage(Packages.fr.ens.transcriptome.nividic.om.io);

  with (nividicNames)  {

    var writer = new SimpleHistoryWriter(file);
    
    writer.write(bo.getHistory());
  }
}