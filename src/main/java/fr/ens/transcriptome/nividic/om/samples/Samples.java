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
import java.util.HashMap;
import java.util.Map;

import fr.ens.transcriptome.nividic.NividicRuntimeException;

/**
 * This class allow to describe samples
 * @author Laurent Jourdren
 */
public class Samples implements Serializable {
	static final long serialVersionUID = -2395732709762059275L;

	private Map<String, SampleParameter> parameters = new HashMap<String, SampleParameter>();
	private Map<String, Integer> samplesIds = new HashMap<String, Integer>();
	private Map<String, String> data = new HashMap<String, String>();

	private int samplesCount;

	/**
	 * Test if the parameter exists.
	 * @param parameterName Parameter to test
	 * @return true if the parameter exists
	 */
	public boolean isParameter(final String parameterName) {

		if (parameterName == null)
			return false;

		return this.parameters.containsKey(parameterName);
	}

	/**
	 * Add a parameter.
	 * @param parameterName Name of the parameter to add
	 * @param type Type of the parameter to add
	 */
	public void addParameter(final String parameterName,
			final SampleParameterType type) {

		if (parameterName == null)
			throw new NullPointerException("Parameter name is null");
		if (type == null)
			throw new NullPointerException("Type name is null");

		if (isParameter(parameterName))
			throw new NullPointerException("Parameter already exists");

		final SampleParameter sp = new SampleParameter(parameterName, type);
		this.parameters.put(parameterName, sp);
	}

	/**
	 * Remove a parameter.
	 * @param oldName Old parameter name
	 * @param newName New parameter name
	 */
	public void renameParameter(final String oldName, final String newName) {

		if (oldName == null || newName == null)
			throw new NullPointerException("One of the parameter is null");

		if (!isParameter(oldName))
			throw new NividicRuntimeException(
					"The parameter to rename doesn't exists");

		if (isParameter(newName))
			throw new NividicRuntimeException(
					"The new name of the parameter already exists");

		final SampleParameter sp = this.parameters.get(oldName);
		sp.setName(newName);

		this.parameters.remove(oldName);
		this.parameters.put(newName, sp);
	}

	/**
	 * Test if a sample exists.
	 * @param sampleName Name of the sample to test
	 * @return true if the sample exists
	 */
	public boolean isSample(final String sampleName) {

		if (sampleName == null)
			return false;

		return this.samplesIds.containsKey(sampleName);
	}

	/**
	 * Add a sample to the samples.
	 * @param sampleName Sample name
	 */
	public void addSample(final String sampleName) {

		if (sampleName == null)
			throw new NullPointerException("Sample name is null");

		if (isSample(sampleName))
			throw new NullPointerException("Sample already exists");

		this.samplesIds.put(sampleName, samplesCount++);
	}

	/**
	 * Rename a sample.
	 * @param oldName Old name of the sample
	 * @param newName New name of the sample
	 */
	public void renameSample(final String oldName, final String newName) {

		if (oldName == null || newName == null)
			throw new NullPointerException("One of the parameter is null");

		if (!isSample(oldName))
			throw new NividicRuntimeException("The sample to rename doesn't exists");

		if (isSample(newName))
			throw new NividicRuntimeException(
					"The new name of the sample already exists");

		int id = this.samplesIds.get(oldName);
		this.samplesIds.put(newName, id);
	}

	private String getKey(final String sampleName, final String parameterName) {

		final int sampleId = this.samplesIds.get(sampleName);
		final int paramId = this.parameters.get(parameterName).getId();

		return sampleId + "-" + paramId;
	}

	/**
	 * Set the value of a paramater for a sample
	 * @param sampleName Name of the sample
	 * @param parameterName Name of the parameter
	 * @param value Value to Set
	 */
	public void set(final String sampleName, final String parameterName,
			final String value) {

		if (!isSample(sampleName))
			throw new NividicRuntimeException("The sample doesnt exists");
		if (!isParameter(parameterName))
			throw new NividicRuntimeException("The parameter doesnt exists");

		final SampleParameterType type = this.parameters.get(parameterName)
		.getType();

		if (!type.isCorrectValue(value))
			throw new NividicRuntimeException("Invalid value");

		this.data.put(getKey(sampleName, parameterName), value);
	}

	/**
	 * Get the value of a paramater for a sample.
	 * @param sampleName Name of the sample
	 * @param parameterName Name of the parameter
	 * @return The value of a paramater for a sample
	 */
	public String get(final String sampleName, final String parameterName) {

		if (!isSample(sampleName))
			throw new NividicRuntimeException("The sample doesnt exists");
		if (!isParameter(parameterName))
			throw new NividicRuntimeException("The parameter doesnt exists");

		final String key = getKey(sampleName, parameterName);

		String result = this.data.get(key);

		if (result == null) {

			final SampleParameterType type = this.parameters.get(parameterName)
			.getType();

			result = type.getDefaultValue();
			this.data.put(key, result);
		}

		return result;
	}

	/**
	 * Get the unique identifier of the sample.
	 * @param sampleName Name of the sample
	 * @return the unique identifier of the sample
	 */
	public int getSampleId(final String sampleName) {

		return this.samplesIds.get(sampleName);
	}

	/**
	 * Get the name of the sample from the unique identifier of the sample.
	 * @param sampleId Sample identifier
	 * @return The name of the sample
	 */
	public String getSampleName(final int sampleId) {

		for (String s : this.samplesIds.keySet())

			if (this.samplesIds.get(s) == sampleId)
				return s;

		return null;
	}

	/**
	 * Get the number of samples.
	 * @return The number of samples
	 */
	public int getSamplesCount() {

		return this.samplesIds.size();
	}

	/**
	 * Get the number of parameters.
	 * @return The number of parameters
	 */
	public int getParameterCount() {

		return this.parameters.size();
	}

	/**
	 * Clear the samples.
	 */
	public void clear() {

		this.parameters.clear();
		this.samplesIds.clear();
		this.data.clear();
	}

	//
	// Constructor
	//

	/**
	 * Package constructor.
	 */
	Samples() {
	}

}
