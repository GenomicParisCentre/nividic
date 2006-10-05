/*
 *                      Nividic development code
 *
 * This code may be freely distributed and modified under the
 * terms of the GNU Lesser General Public Licence.  This should
 * be distributed with the code.  If you do not have a copy,
 * see:
 *
 *      http://www.gnu.org/copyleft/lesser.html
 *
 * Copyright for this code is held jointly by the microarray platform
 * of the École Normale Supérieure and the individual authors.
 * These should be listed in @author doc comments.
 *
 * For more information on the Nividic project and its aims,
 * or to join the Nividic mailing list, visit the home page
 * at:
 *
 *      http://www.transcriptome.ens.fr/nividic
 *
 */

package fr.ens.transcriptome.nividic.sgdb.lims;

import javax.xml.rpc.ServiceException;

import fr.ens.transcriptome.nividic.sgdb.lims.ws.LimsBindingStub;
import fr.ens.transcriptome.nividic.sgdb.lims.ws.LimsPort_PortType;
import fr.ens.transcriptome.nividic.sgdb.lims.ws.LimsService;
import fr.ens.transcriptome.nividic.sgdb.lims.ws.LimsServiceLocator;

/**
 * This class define a connection to the webservices interface of the ENS sgdb
 * lims.
 * @author Laurent Jourdren
 */
public class LimsConnection {

  private final static String DEFAULT_PORT = "http://skadi.ens.fr/lims/webservices/lims.php";

  private String limsPortEndpointAddress = DEFAULT_PORT;
  private String username;
  private String password;

  //
  // Getters
  //

  /**
   * Get the address of the port of the webservice.
   * @return the address of the port of the webservice
   */
  public String getLimsPortEndpointAddress() {
    return limsPortEndpointAddress;
  }

  /**
   * Get the password for the connection.
   * @return The password for the connection
   */
  public String getPassword() {
    return password;
  }

  /**
   * Get the username for the connection.
   * @return the username for the connection
   */
  public String getUsername() {
    return username;
  }

  //
  // Setters
  //

  /**
   * Set the address of the port of the webservice.
   * @param limsPortEndpointAddress the address of the port of the webservice
   */
  public void setLimsPortEndpointAddress(final String limsPortEndpointAddress) {
    this.limsPortEndpointAddress = limsPortEndpointAddress;
  }

  /**
   * Set the password for the connection.
   * @param password The password for the connection
   */
  public void setPassword(final String password) {
    this.password = password;
  }

  /**
   * Set the username for the connection.
   * @param username the username for the connection
   */
  public void setUsername(final String username) {
    this.username = username;
  }

  //
  // Other methods
  //

  /**
   * Get the port of the connection.
   * @return the port of the connection
   * @throws ServiceException if an error occurs while creating the connection
   */
  public LimsPort_PortType getPort() throws ServiceException {

    // Make a service
    LimsService service = new LimsServiceLocator();

    // Get the port
    LimsPort_PortType port = service.getLimsPort();

    // set the user and password of the http connection

    final String username = getUsername();
    final String password = getPassword();

    if (username != null || password != null) {

      LimsBindingStub stub = (LimsBindingStub) port;

      stub.setUsername(username);
      stub.setPassword(password);
    }

    return port;
  }

  //
  // Constructor
  //

  /**
   * Default constructor.
   * @param limsPortEndpointAddress the address of the port of the webservice
   * @param username the username for the connection
   * @param password The password for the connection
   */
  public LimsConnection(final String limsPortEndpointAddress,
      final String username, final String password) {

    setLimsPortEndpointAddress(limsPortEndpointAddress);
    setUsername(username);
    setPassword(password);
  }

  /**
   * Default constructor.
   * @param limsPortEndpointAddress the address of the port of the webservice
   */
  public LimsConnection(final String limsPortEndpointAddress) {

    setLimsPortEndpointAddress(limsPortEndpointAddress);
    setUsername(username);
    setPassword(password);
  }

  /**
   * Default constructor.
   */
  public LimsConnection() {
  }

}
