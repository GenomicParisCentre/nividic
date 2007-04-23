/*
 * This file contains methods to handle the filesystem.
 *
 * @author Laurent Jourdren
 */

/*
 * Return an array of File objects in a directory
 * @param dir path of the directory to list
 * @return an array of File object
 */
function ls(dir) {

  if (dir==null) return fs.ls();
  return fs.ls(dir);
}

/*
 * Return the path of the current directory
 * @return the path of the current directory
 */
function cwd() {

  return fs.cwd().getCanonicalFile();
}

/*
 * Return the path of the current directory
 * @return the path of the current directory
 */
function pwd() {

  println(cwd());
}


/*
 * Change the current directory
 * @param dir path of the new directory
 * @return nothing
 */
function chdir(dir) {

  return fs.chdir(dir);
}

/*
 * Change the current directory
 * @param dir path of the new directory
 * @return nothing
 */
function cd(dir) {

  return chdir(dir);
}


/*
 * Move a file
 * @param a File to move
 * @param b New path (file) of the file
 * @return nothing
 */
function move(a, b) {

  return fs.move(a, b);
}

/*
 * Move a file
 * @param a File to move
 * @param b New path (file) of the file
 * @return nothing
 */
function mv(a, b) {

  return move(a, b);
}


/*
 * Remove a file
 * @param file File to remove
 * @return nothing
 */
function unlink(file) {

  fs.unlink(file);
}

/*
 * Remove a file
 * @param file File to remove
 * @return nothing
 */
function rm(file) {

  unlink(file);
}

/*
 * Create a directory
 * @param dir path of the new directory
 * @return nothing
 */
function mkdir(dir) {

  fs.mkdir(dir);
}

/*
 * Remove a directory
 * @param dir Directory to remove
 * @return nothing
 */
function rmdir(dir) {

  fs.rmdir(dir);
}

/*
 * Select a file (even if doesn't exists)
 * @parm file file to select
 * @return a File object
 */
function sf(file) {

  return fs.sf(file);
}

/*
 * Return an array of the filenames of a directory.
 * @param dir
 * @return an array of the filenames of the directory
 */
function dir(dir) {

  var files = ls(dir);
  
  var a = new Array;
  
  for (var i=0; i<files.length;i++) {
      a[i]=files[i].getName();
  }
  
  return a;
}