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
package org.geobricks.gdal.dem.colorrelief;

import org.geobricks.gdal.constant.CONFIG;
import org.geobricks.gdal.dem.GDALDEM;

/**
 * 
 * @author <a href="mailto:guido.barbaglia@gmail.com">Guido Barbaglia</a>
 * 
 *         This command outputs a 3-band (RGB) or 4-band (RGBA) raster with
 *         values are computed from the elevation and a text-based color
 *         configuration file, containing the association between various
 *         elevation values and the corresponding wished color. By default, the
 *         colors between the given elevation values are blended smoothly and
 *         the result is a nice colorized DEM. The -exact_color_entry or
 *         -nearest_color_entry options can be used to avoid that linear
 *         interpolation for values that don't match an index of the color
 *         configuration file.
 * 
 */
public class GDALDEMColorRelief extends GDALDEM {

	/**
	 * text-based color configuration file
	 */
	private String colorConfigurationFile;

	/**
	 * add an alpha channel to the output raster
	 */
	private boolean addAlphaChannel = false;

	/**
	 * use strict matching when searching in the color configuration file. If
	 * none matching color entry is found, the "0,0,0,0" RGBA quadruplet will be
	 * used
	 */
	private boolean extractColorEntry = false;

	/**
	 * use the RGBA quadruplet corresponding to the closest entry in the color
	 * configuration file.
	 */
	private boolean useNearestColorEntry = false;

	public GDALDEMColorRelief(String inputFilepath, String outputFilepath) {
		super(inputFilepath, outputFilepath);
	}

	public String getColorConfigurationFile() {
		return colorConfigurationFile;
	}

	/**
	 * text-based color configuration file
	 */
	public void setColorConfigurationFile(String colorConfigurationFile) {
		this.colorConfigurationFile = colorConfigurationFile;
	}

	public boolean addAlphaChannel() {
		return addAlphaChannel;
	}

	/**
	 * add an alpha channel to the output raster
	 */
	public void addAlphaChannel(boolean addAlphaChannel) {
		this.addAlphaChannel = addAlphaChannel;
	}

	public boolean extractColorEntry() {
		return extractColorEntry;
	}

	/**
	 * use strict matching when searching in the color configuration file. If
	 * none matching color entry is found, the "0,0,0,0" RGBA quadruplet will be
	 * used
	 */
	public void extractColorEntry(boolean extractColorEntry) {
		this.extractColorEntry = extractColorEntry;
	}

	public boolean useNearestColorEntry() {
		return useNearestColorEntry;
	}

	/**
	 * use the RGBA quadruplet corresponding to the closest entry in the color
	 * configuration file.
	 */
	public void useNearestColorEntry(boolean useNearestColorEntry) {
		this.useNearestColorEntry = useNearestColorEntry;
	}
	
	@Override
	public String convert() throws Exception {

		// generic help
		if (this.getScript() != null && !this.getScript().isEmpty()) {
			return this.getScript();
		} else if (this.showHelp()) {
			this.getSB().append("gdalinfo --help");
			return this.getSB().toString();
		}

		// GDALDEMSlope specific
		this.getSB().append("gdaldem color-relief ");
		if (this.getColorConfigurationFile() != null && !this.getColorConfigurationFile().isEmpty()) {
			this.getSB().append(this.getColorConfigurationFile()).append(" ");
		} else {
			throw new Exception("Color configuration file has not been defined.");
		}
		if (this.addAlphaChannel())
			this.getSB().append("-alpha ");
		if (this.extractColorEntry())
			this.getSB().append("-exact_color_entry ");
		if (this.useNearestColorEntry())
			this.getSB().append("-nearest_color_entry ");

		// this section is common to all GDALDEM classes
		if (this.getInputFilepath() != null && !this.getInputFilepath().isEmpty()) {
			this.getSB().append(this.getInputFilepath()).append(" ");
		} else {
			throw new Exception("Input raster has not been defined.");
		}
		if (this.getOutputFilepath() != null && !this.getOutputFilepath().isEmpty()) {
			this.getSB().append(this.getOutputFilepath()).append(" ");
		} else {
			throw new Exception("Output raster has not been defined.");
		}
		if (this.getAlgorithm() != null && !this.getAlgorithm().isEmpty())
			this.getSB().append("-alg ").append(this.getAlgorithm()).append(" ");
		if (this.computeEdges())
			this.getSB().append("-compute_edges ");
		if (this.getBand() != null)
			this.getSB().append("-b ").append(this.getBand()).append(" ");
		if (this.getOutputFormat() != null)
			this.getSB().append("-of ").append(this.getOutputFormat().name()).append(" ");
		if (this.getCreationOption() != null && !this.getCreationOption().isEmpty())
			for (String key : this.getCreationOption().keySet())
				this.getSB().append("-co \"").append(key).append("=").append(this.getCreationOption().get(key)).append("\" ");
		if (this.suppressProgressMonitor())
			this.getSB().append("-q ");

		// configuration options
		if (this.getConfig() != null && !this.getConfig().isEmpty())
			for (CONFIG key : this.getConfig().keySet())
				this.getSB().append("--config ").append(key.name()).append(" ").append(this.getConfig().get(key)).append(" ");

		return this.getSB().toString();
	}

}