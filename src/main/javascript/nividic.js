/*
 * This file contains tests methods.
 *
 * @author Laurent Jourdren
 */
 
 /*
 * Show information about nividic
 * @return nothing
 */
function about() {

  var nividicNames = JavaImporter();
  nividicNames.importPackage(Packages.fr.ens.transcriptome.nividic);
  
  with (nividicNames)  {
  
  	println(Globals.APP_NAME +" version " + Globals.VERSION+"\n"+Globals.COPYRIGHT);
  }

}

/*
 * Load and execute a javascript file.
 * @return nothing
 */
function source(file) {

  if (file instanceof Array) {

    for (var i=0; i<file.length;i++) 
      source(file[i]);
      
    return;  
  } 
      
  if (file.constructor ==  String) {
  
    engine.source(fs.sf(file));
    return;  
  }
  
  engine.source(file);   
}

/*
 * Edit a file
 * @param file file to edit
 * @return nothing
 */
function edit(file) {

   if (file==null) {
     engine.edit(null);
     return;
   }

   if (file.constructor ==  String) {
  
    engine.edit(fs.sf(file));
    return;  
  }
  
  engine.edit(file);   
}

/*
 * Execute a javascript command and return the time needed to execute a
 * Javascript command.
 * @param cmd command to execute
 */
function time(cmd) {

	engine.time(cmd);
}

/*
 * Execute a system command.
 * @param cmd command to execute
 * @throws IOException if an error occurs while executing the command
 */
function system(cmd) {

  engine.system(cmd);
}


/*
 * Get the current time in milliseconds since the Unix epoch.
 * @return The current time in milliseconds since the Unix epoch
 */
function currentTimeMillis() {

  return engine.currentTimeMillis();
}

/*
 * Show help information.
 *@return nothing
 */
function help() {

  println("");
  println("Command                Description");
  println("=======                ===========");
  println("help()                 Display usage and help messages. ");
  println("source(file)           Load JavaScript source file.");
  println("edit(file)             Edit a file.");
  println("print([expr ...])      Evaluate and print expressions. ");
  println("about()                Show information about this application.");
  println("system(cmd)            Execute a shell command.");
  println("time([expr])           Get the running time of a expression.");
  println("quit()                 Quit the shell. ");
  println("");
  

}

