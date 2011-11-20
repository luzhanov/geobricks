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
package org.geobricks.gdal.dem.contour;

import java.util.ArrayList;
import java.util.List;

import org.geobricks.gdal.GDAL;
import org.geobricks.gdal.constant.CONFIG;
import org.geobricks.gdal.constant.FORMAT;

/**
 * 
 * @author <a href="mailto:guido.barbaglia@gmail.com">Guido Barbaglia</a>
 * 
 *         This program generates a vector contour file from the input raster
 *         elevation model (DEM). Starting from version 1.7 the contour
 *         line-strings will be oriented consistently. The high side will be on
 *         the right, i.e. a line string goes clockwise around a top.
 * 
 */
public class GDALContour extends GDAL {

	/**
	 * picks a particular band to get the DEM from. Defaults to band 1.
	 */
	private Integer band;

	/**
	 * provides a name for the attribute in which to put the elevation. If not
	 * provided no elevation attribute is attached.
	 */
	private String attribute;

	/**
	 * Force production of 3D vectors instead of 2D. Includes elevation at every
	 * vertex.
	 */
	private boolean force3D = false;

	/**
	 * Ignore any nodata value implied in the dataset - treat all values as
	 * valid.
	 */
	private boolean ignoreNoData = false;

	/**
	 * Input pixel value to treat as "nodata".
	 */
	private String noData;

	/**
	 * create output in a particular format, default is shapefiles.
	 */
	private FORMAT outputFormat;

	/**
	 * elevation interval between contours.
	 */
	private Double interval;

	/**
	 * Offset from zero relative to which to interpret intervals.
	 */
	private Double offset;

	/**
	 * Name one or more "fixed levels" to extract.
	 */
	private List<String> fixedLevels;

	/**
	 * Provide a name for the output vector layer. Defaults to "contour".
	 */
	private String outputLayerName;
	
	public GDALContour(String inputFilepath, String outputFilepath) {
		this.setInputFilepath(inputFilepath);
		this.setOutputFilepath(outputFilepath);
	}

	public Integer getBand() {
		return band;
	}

	/**
	 * picks a particular band to get the DEM from. Defaults to band 1.
	 */
	public void setBand(Integer band) {
		this.band = band;
	}

	public String getAttribute() {
		return attribute;
	}

	/**
	 * provides a name for the attribute in which to put the elevation. If not
	 * provided no elevation attribute is attached.
	 */
	public void setAttribute(String attribute) {
		this.attribute = attribute;
	}

	public boolean force3D() {
		return force3D;
	}

	/**
	 * Force production of 3D vectors instead of 2D. Includes elevation at every
	 * vertex.
	 */
	public void force3D(boolean force3d) {
		force3D = force3d;
	}

	public boolean ignoreNoData() {
		return ignoreNoData;
	}

	/**
	 * Ignore any nodata value implied in the dataset - treat all values as
	 * valid.
	 */
	public void ignoreNoData(boolean ignoreNoData) {
		this.ignoreNoData = ignoreNoData;
	}

	public String getNoData() {
		return noData;
	}

	/**
	 * Input pixel value to treat as "nodata".
	 */
	public void setNoData(String noData) {
		this.noData = noData;
	}

	public FORMAT getOutputFormat() {
		return outputFormat;
	}

	/**
	 * create output in a particular format, default is shapefiles.
	 */
	public void setOutputFormat(FORMAT outputFormat) {
		this.outputFormat = outputFormat;
	}

	public Double getInterval() {
		return interval;
	}

	/**
	 * elevation interval between contours.
	 */
	public void setInterval(Double interval) {
		this.interval = interval;
	}

	public Double getOffset() {
		return offset;
	}

	/**
	 * Offset from zero relative to which to interpret intervals.
	 */
	public void setOffset(Double offset) {
		this.offset = offset;
	}

	public List<String> getFixedLevels() {
		return fixedLevels;
	}

	/**
	 * Name one or more "fixed levels" to extract.
	 */
	public void setFixedLevels(List<String> fixedLevels) {
		this.fixedLevels = fixedLevels;
	}
	
	/**
	 * Name one or more "fixed levels" to extract.
	 */
	public void addFixedLevel(String levelName) {
		if (this.fixedLevels == null)
			this.fixedLevels = new ArrayList<String>();
		this.fixedLevels.add(levelName);
	}

	public String getOutputLayerName() {
		return outputLayerName;
	}

	/**
	 * Provide a name for the output vector layer. Defaults to "contour".
	 */
	public void setOutputLayerName(String outputLayerName) {
		this.outputLayerName = outputLayerName;
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

		this.getSB().append("gdal_contour ");
		if (this.getBand() != null)
			this.getSB().append("-b ").append(this.getBand()).append(" ");
		if (this.getAttribute() != null && !this.getAttribute().isEmpty())
			this.getSB().append("-a ").append(this.getAttribute()).append(" ");
		if (this.force3D())
			this.getSB().append("-3d ");
		if (this.ignoreNoData())
			this.getSB().append("-inodata ");
		if (this.getNoData() != null && !this.getNoData().isEmpty())
			this.getSB().append("-snodata ").append(this.getNoData()).append(" ");
		if (this.getOutputFormat() != null)
			this.getSB().append("-f ").append(this.getOutputFormat().name()).append(" ");
		if (this.getInterval() != null)
			this.getSB().append("-i ").append(this.getInterval()).append(" ");
		if (this.getOffset() != null)
			this.getSB().append("-off ").append(this.getOffset()).append(" ");
		if (this.getFixedLevels() != null && !this.getFixedLevels().isEmpty()) {
			this.getSB().append("-fl ");
			for (String fl : this.getFixedLevels())
				this.getSB().append(fl).append(" ");
		}
		if (this.getOutputLayerName() != null)
			this.getSB().append("-nln ").append(this.getOutputLayerName()).append(" ");
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

		// configuration options
		if (this.getConfig() != null && !this.getConfig().isEmpty())
			for (CONFIG key : this.getConfig().keySet())
				this.getSB().append("--config ").append(key.name()).append(" ").append(this.getConfig().get(key)).append(" ");

		return this.getSB().toString();
	}

}