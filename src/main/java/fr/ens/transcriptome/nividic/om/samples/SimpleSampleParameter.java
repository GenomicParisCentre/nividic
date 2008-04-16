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
 * of the �cole Normale Sup�rieure and the individual authors.
 * These should be listed in @author doc comments.
 *
 * For more information on the Nividic project and its aims,
 * or to join the Nividic mailing list, visit the home page
 * at:
 *
 *      http://www.transcriptome.ens.fr/nividic
 *
 */

package fr.ens.transcriptome.nividic.om.samples;

import java.io.Serializable;

public class SimpleSampleParameter implements SampleParameterType, Serializable {
	static final long serialVersionUID = 296845652434328174L;

	private String name = "SimpleParameter-" + count++;
	private String defaultValue = "";
	private Class classType;
	private static int count;

	//
	// Getter
	//

	/**
	 * Get the name of the experiment parameter type
	 * @return Returns the name of the experiement parameter
	 */
	public String getName() {
		return name;
	}

	/**
	 * Get the default value of the parameter.
	 * @return The default value of the parameter
	 */
	public String getDefaultValue() {
		return this.defaultValue;
	}

	/**
	 * Get the type (integer, double, string) of the value.
	 * @return The type of the value
	 */
	public Class getValueType() {
		return this.classType;
	}

	//
	// Setters
	//

	/**
	 * Set the name of the experiment parameter type
	 * @param name The name to set. Must be not null
	 */
	public void setName(final String name) {
		if (name != null)
			this.name = name;
	}

	/**
	 * Set the default value of the parameter.
	 * @param defaultValue The default value of the parameter. Must be not null.
	 */
	public void setDefaultValue(final String defaultValue) {
		if (defaultValue != null)
			this.defaultValue = defaultValue;
	}

	/**
	 * Get the type (integer, double, string) of the value.
	 * @param classType The class type of the value
	 */
	public void setValueType(final Class classType) {
		this.classType = classType;
	}

	//
	// Other methods
	//

	/**
	 * Test if the value of parameter is correct.
	 * @param value Value to test
	 * @return true if the value of the parameter is correct
	 */
	public boolean isCorrectValue(final String value) {
		return true;
	}

}
