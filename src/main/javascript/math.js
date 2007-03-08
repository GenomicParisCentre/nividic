/*
 * This file contains mathematical methods.
 *
 * @author Laurent Jourdren
 */


/*
 * Calc the median of an array of double
 * @param data the array of double
 * @paran noNaN true if NaN value must be removed from the computation
 * @return nothing
 */
function median(data, noNaN) {

  var nividicNames = JavaImporter();
  nividicNames.importPackage(Packages.fr.ens.transcriptome.nividic.util);
  
  if (noNaN==true) {
    return MathUtils.median(data,true);
  }
  
  return MathUtils.median(data);
}

/*
 * Calc the mean of an array of double
 * @param data the array of double
 * @paran noNaN true if NaN value must be removed from the computation
 * @return nothing
 */
function mean(data, noNaN) {

  var nividicNames = JavaImporter();
  nividicNames.importPackage(Packages.fr.ens.transcriptome.nividic.util);
  
  if (noNaN==true) {
    return MathUtils.mean(data,true);
  }
  
  return MathUtils.mean(data);
}