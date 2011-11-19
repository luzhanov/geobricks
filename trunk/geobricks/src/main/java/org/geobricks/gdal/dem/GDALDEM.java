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
package org.geobricks.gdal.dem;

import java.util.HashMap;
import java.util.Map;

import org.geobricks.gdal.GDAL;
import org.geobricks.gdal.constant.FORMAT;

/**
 * 
 * @author <a href="mailto:guido.barbaglia@gmail.com">Guido Barbaglia</a> 
 *
 */
public abstract class GDALDEM extends GDAL {

	/**
	 * Select the output format. The default is GeoTIFF (GTiff). Use the short
	 * format name.
	 */
	private FORMAT outputFormat;

	/**
	 * (GDAL >= 1.8.0) Do the computation at raster edges and near nodata values
	 */
	private boolean computeEdges = false;

	/**
	 * (GDAL >= 1.8.0) Use Zevenbergen & Thorne formula, instead of Horn's
	 * formula, to compute slope & aspect. The litterature suggests Zevenbergen
	 * & Thorne to be more suited to smooth landscapes, whereas Horn's formula
	 * to perform better on rougher terrain.
	 */
	private String algorithm;

	/**
	 * Select an input band to be processed. Bands are numbered from 1.
	 */
	private Integer band;

	/**
	 * Passes a creation option to the output format driver. Multiple -co
	 * options may be listed. See format specific documentation for legal
	 * creation options for each format.
	 */
	private Map<String, String> creationOption;

	/**
	 * Suppress progress monitor and other non-error output.
	 */
	private boolean suppressProgressMonitor = false;

	public GDALDEM(String inputFilepath, String outputFilepath) {
		super();
		this.setInputFilepath(inputFilepath);
		this.setOutputFilepath(outputFilepath);
	}

	public FORMAT getOutputFormat() {
		return outputFormat;
	}

	/**
	 * Select the output format. The default is GeoTIFF (GTiff). Use the short
	 * format name.
	 */
	public void setOutputFormat(FORMAT outputFormat) {
		this.outputFormat = outputFormat;
	}

	/**
	 * (GDAL >= 1.8.0) Do the computation at raster edges and near nodata values
	 */
	public boolean computeEdges() {
		return computeEdges;
	}

	public void computeEdges(boolean compute) {
		this.computeEdges = compute;
	}

	public String getAlgorithm() {
		return algorithm;
	}

	/**
	 * (GDAL >= 1.8.0) Use Zevenbergen & Thorne formula, instead of Horn's
	 * formula, to compute slope & aspect. The litterature suggests Zevenbergen
	 * & Thorne to be more suited to smooth landscapes, whereas Horn's formula
	 * to perform better on rougher terrain.
	 */
	public void setAlgorithm(String algorithm) {
		this.algorithm = algorithm;
	}

	public Integer getBand() {
		return band;
	}

	/**
	 * Select an input band to be processed. Bands are numbered from 1.
	 */
	public void setBand(Integer band) {
		this.band = band;
	}

	public Map<String, String> getCreationOption() {
		return creationOption;
	}

	/**
	 * Passes a creation option to the output format driver. Multiple -co
	 * options may be listed. See format specific documentation for legal
	 * creation options for each format.
	 */
	public void setCreationOption(Map<String, String> creationOption) {
		this.creationOption = creationOption;
	}
	
	/**
	 * Passes a creation option to the output format driver. Multiple -co
	 * options may be listed. See format specific documentation for legal
	 * creation options for each format.
	 */
	public void addCreationOption(String option, String value) {
		if (this.creationOption == null)
			this.creationOption = new HashMap<String, String>();
		this.creationOption.put(option, value);
	}

	public boolean suppressProgressMonitor() {
		return suppressProgressMonitor;
	}

	/**
	 * Suppress progress monitor and other non-error output.
	 */
	public void suppressProgressMonitor(boolean suppress) {
		this.suppressProgressMonitor = suppress;
	}
	
}