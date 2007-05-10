/*
 * This file contains methods to print message on console.
 *
 * @author Laurent Jourdren
 */

/*
 * Print a variable on stdout.
 * @param obj variable to print.
 * @return nothing
 */
function print(obj) {

  var nividicNames = JavaImporter();
  nividicNames.importPackage(Packages.fr.ens.transcriptome.nividic.js);


  with(nividicNames) {

    if (obj==null) { 
      return;
    }
    
    if (obj instanceof Function) {
       Echo.out.print("Function");
       return;
    }

    if (obj instanceof Array) {
    
      
      if (obj.length>0 && (obj[0].construtor == Boolean || obj[0].constructor == Number)) {

        Echo.out.print(obj);
        return;
      }
     
     if (isNativeObjectArray(obj)) { 
       Echo.out.printObjectArray(obj); 
       return;
     }
     
     if (isNativePrimitiveArray(obj)) { 
       Echo.out.printPrimitiveArray(obj); 
       return;
     }
      return; 
    } 

    Echo.out.print(obj);
  }
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


