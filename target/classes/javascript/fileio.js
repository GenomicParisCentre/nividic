/*
 * This file contains methods to read and write file.
 *
 * @author Laurent Jourdren
 */
 
 /*
  * Open a file in write mode.
  * @param file The file to open
  * @return a stream
  */
 function openReader(file) {
 
  if (file==null) { 
    return;
  }
    
  var javaIONames = JavaImporter();
  javaIONames.importPackage(Packages.java.io);
    
  with(javaIONames) { 
    return new PrintWriter(BufferedWriter(new OutputStreamWriter(new FileOutputStream(file))));
  }
 }
 
 
 /*
  * Close a stream.
  * @param stream the stream to close
  * @return nothing
  */
 function close(stream) {
 
   stream.close();
 }
