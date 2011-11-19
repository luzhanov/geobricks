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
package org.geobricks.gdal.dem.hillshade;

import org.geobricks.gdal.constant.CONFIG;
import org.geobricks.gdal.dem.GDALDEM;

/**
 * 
 * @author <a href="mailto:guido.barbaglia@gmail.com">Guido Barbaglia</a>
 * 
 *         Generate a shaded relief map from any GDAL-supported elevation raster
 * 
 */
public class GDALDEMHillshade extends GDALDEM {

	/**
	 * vertical exaggeration used to pre-multiply the elevations
	 */
	private Double zFactor;

	/**
	 * ratio of vertical units to horizontal. If the horizontal unit of the
	 * source DEM is degrees (e.g Lat/Long WGS84 projection), you can use
	 * scale=111120 if the vertical units are meters (or scale=370400 if they
	 * are in feet)
	 */
	private Double scale;

	/**
	 * azimuth of the light, in degrees. 0 if it comes from the top of the
	 * raster, 90 from the east, ... The default value, 315, should rarely be
	 * changed as it is the value generally used to generate shaded maps.
	 */
	private Double azimuth;

	/**
	 * altitude of the light, in degrees. 90 if the light comes from above the
	 * DEM, 0 if it is raking light.
	 */
	private Double altitude;
	
	public GDALDEMHillshade(String inputFilepath, String outputFilepath) {
		super(inputFilepath, outputFilepath);
	}

	public Double getzFactor() {
		return zFactor;
	}

	/**
	 * vertical exaggeration used to pre-multiply the elevations
	 */
	public void setzFactor(Double zFactor) {
		this.zFactor = zFactor;
	}

	public Double getScale() {
		return scale;
	}

	/**
	 * ratio of vertical units to horizontal. If the horizontal unit of the
	 * source DEM is degrees (e.g Lat/Long WGS84 projection), you can use
	 * scale=111120 if the vertical units are meters (or scale=370400 if they
	 * are in feet)
	 */
	public void setScale(Double scale) {
		this.scale = scale;
	}

	public Double getAzimuth() {
		return azimuth;
	}

	/**
	 * azimuth of the light, in degrees. 0 if it comes from the top of the
	 * raster, 90 from the east, ... The default value, 315, should rarely be
	 * changed as it is the value generally used to generate shaded maps.
	 */
	public void setAzimuth(Double azimuth) {
		this.azimuth = azimuth;
	}

	public Double getAltitude() {
		return altitude;
	}

	/**
	 * altitude of the light, in degrees. 90 if the light comes from above the
	 * DEM, 0 if it is raking light.
	 */
	public void setAltitude(Double altitude) {
		this.altitude = altitude;
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
		
		// GDALDEMHillshade specific
		this.getSB().append("gdaldem hillshade ");
		if (this.getzFactor() != null)
			this.getSB().append("-z ").append(this.getzFactor()).append(" ");
		if (this.getScale() != null)
			this.getSB().append("-s ").append(this.getScale()).append(" ");
		if (this.getAzimuth() != null)
			this.getSB().append("-az ").append(this.getAzimuth()).append(" ");
		if (this.getAltitude() != null)
			this.getSB().append("-alt ").append(this.getAltitude()).append(" ");

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