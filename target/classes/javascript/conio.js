/*
 * This file contains methods to print message on console.
 *
 * @author Laurent Jourdren
 */


function write(stream, obj) {

  var nividicNames = JavaImporter();
  nividicNames.importPackage(Packages.fr.ens.transcriptome.nividic.js);


  with(nividicNames) {

    if (obj==null) { 
      return;
    }
    
    if (obj instanceof Function) {
       Echo.print(stream, "Function");
       return;
    }

    if (obj instanceof Array) {
    
      
      if (obj.length>0 && (obj[0].construtor == Boolean || obj[0].constructor == Number)) {

        Echo.print(stream, obj);
        return;
      }
     
     if (isNativeObjectArray(obj)) { 
       Echo.printObjectArray(stream, obj);
      
       return;
     }
     
     if (isNativePrimitiveArray(obj)) { 
       Echo.printPrimitiveArray(stream, obj);
       
       return;
     }
      return; 
    } 

    stream.print(obj);
  }

}

/*
 * Print a variable on stdout.
 * @param obj variable to print.
 * @return nothing
 */
function print(obj) {

  /*var nividicNames = JavaImporter();
  nividicNames.importPackage(Packages.fr.ens.transcriptome.nividic.js);

  with(nividicNames) {*/
  
    write(out, obj);
  /*}*/
  
}

/*
 * Print a variable on stdout with a return carriage at the end of the output.
 * @param obj variable to print.
 * @return nothing
 */
function println(obj) {

  print(obj);
  print("\n");
}


