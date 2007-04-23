/*
 * This file contains methods to manipulate easily Biological list Objects.
 *
 * @author Laurent Jourdren
 */


/*
 * Creating methods
 */

/*
 * Create a new Biological List.
 * @return new Biological list object
 */
function createBiologicalList() {

  var nividicNames = JavaImporter();
  nividicNames.importPackage(Packages.fr.ens.transcriptome.nividic.om);

  with (nividicNames)  {

    return BiologicalListFactory.createBiologicalList();
  }

}

/*
 * Reader methods
 */

/*
 * read a biological list.
 * @param file File to read
 * @param trim trim readed entries
 * @return the biological list object readed
 */
function readBiologicalList(file, trim) {

  var nividicNames = JavaImporter();
  nividicNames.importPackage(Packages.fr.ens.transcriptome.nividic.om.io);


  if (file instanceof Array) {

    var result = new Array;

    for (var i=0; i<file.length;i++) {
      
      with (nividicNames)  {
      
        var f = file[i];
        
        if (f.constructor==String) { f = sf(file); }  

        var reader = new SimpleBiologicalListReader(f);
  
        if (trim == true) {
          reader.setTrim(true);
        }
  
        result[i]=reader.read();
        result[i].setName(file[i].getName());
      }
    }
    return result;
  }
 
  with (nividicNames)  {
    var reader = new SimpleBiologicalListReader(file);
  
    if (trim == true) {
      reader.setTrim(true);
    }
  
    var result = reader.read();
    result.setName(file.getName());
    return result;
  }
}

/*
 * Writer methods
 */

/*
 * Write a biological list.
 * @param list List to write
 * @param file Write to write
 * @return nothing
 */ 
function writeBiologicalList(list, file) {

  if (file.constructor==String) { file = sf(file); }  

  var nividicNames = JavaImporter();
  nividicNames.importPackage(Packages.fr.ens.transcriptome.nividic.om.io);

  with (nividicNames)  {

    var writer = new SimpleBiologicalListWriter(file);
    writer.write(list);
  }
}

