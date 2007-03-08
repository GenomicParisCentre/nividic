/*
 * This file contains methods to automate usual analysis.
 *
 * @author Laurent Jourdren
 */


/*
 * Create up and down list from bioAssay
 * @param ba BioAssay
 * @param threshold absolute value of the threshold
 * @param fileUp File used to save the up list
 * @param fileDown File used to save the down list
 * @return nothing
 */
function createLists(ba, threshold, fileUp, fileDown) {

  var baf1 = ba.filter(createMSupFilter(threshold));
  var baf2 = ba.filter(createMInfFilter(-threshold));

  var l1 = newBiologicalList();
  l1.add(baf1.getIds());
  println("Up list count: "+l1.size());

  var l2 = newBiologicalList();
  l2.add(baf2.getIds());
  println("Down list count: "+l2.size());

  writeBiologicalList(l1, fileUp);
  writeBiologicalList(l2, fileDown);
}

/*
 * Sort a total summary bioAssay by descending M, and 
 * then by descending A. The result bioAssay is writen in a file.
 * @param ba The bioAssay
 * @param file File to write
 * @return nothing
 */
function sortTotalSummary(ba, file) {

  writeTotalSummary(ts.sort(createMASorter()), file);
}

/*
 * Show the number of NA values and empty spots in the M field in a bioAssay.
 * @param ba The bioAssay
 * @return nothing
 */
function countNA(ba) {

  var it = ba.iterator();
  var na = 0;
  var nane = 0;
  var ne = 0;


  while (it.hasNext()) {

    it.next();

    if (isNaN(it.getM())) { na++; }
    if (isNaN(it.getM()) && !it.isEmpty()) { nane++; }
    if (!it.isEmpty()) { ne++; }

  }

  println("NaN: " + na + "/" + ba.size() + " (" + (na/ba.size()*100).toFixed(2) + "%)");
  println("NaN without empty spots: " + nane + "/" + ne + " (" + (na/ba.size()*100).toFixed(2) + "%)");

}

