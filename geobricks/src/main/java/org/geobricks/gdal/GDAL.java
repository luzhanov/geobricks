/**
 *
 * GeoBricks
 *
 * Copyright (c) 2011 by Kalimaha
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 *
 */
package org.geobricks.gdal;

import java.util.HashMap;
import java.util.Map;

/**
 * 
 * @author <a href="mailto:guido.barbaglia@gmail.com">Guido Barbaglia</a>
 * 
 */
public abstract class GDAL {

	private String script;

	private String inputFilepath;

	private String outputFilepath;

	private boolean help = false;
	
	private Map<String, String> config;

	public GDAL() {

	}

	public GDAL(String script) {
		super();
		this.setScript(script);
	}

	/**
	 * 
	 * @return GDAL command
	 * 
	 *         This function convert the input Java bean into an executable GDAL
	 *         command.
	 */
	public String convert() throws Exception {
		return this.getScript();
	}

	public String getScript() {
		return script;
	}

	public void setScript(String script) {
		this.script = script;
	}

	public String getInputFilepath() {
		return inputFilepath;
	}

	public void setInputFilepath(String inputFilepath) {
		this.inputFilepath = inputFilepath;
	}

	public String getOutputFilepath() {
		return outputFilepath;
	}

	public void setOutputFilepath(String outputFilepath) {
		this.outputFilepath = outputFilepath;
	}

	public boolean showHelp() {
		return help;
	}

	public void help(boolean help) {
		this.help = help;
	}

	public Map<String, String> getConfig() {
		return config;
	}

	public void setConfig(Map<String, String> config) {
		this.config = config;
	}
	
	public void setConfig(String key, String value) {
		if (this.config == null)
			this.config = new HashMap<String, String>();
		this.config.put(key, value);
	}

}