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
package org.geobricks.gdal.info;

import org.geobricks.gdal.GDAL;

/**
 * 
 * @author <a href="mailto:guido.barbaglia@gmail.com">Guido Barbaglia</a>
 * 
 */
public class GDALInfo extends GDAL {

	/**
	 * Force computation of the actual min/max values for each band in the
	 * dataset.
	 */
	private boolean minMax = false;

	/**
	 * Read and display image statistics. Force computation if no statistics are
	 * stored in an image.
	 */
	private boolean statistics = false;

	/**
	 * Report histogram information for all bands.
	 */
	private boolean histogram = false;

	/**
	 * Suppress ground control points list printing. It may be useful for
	 * datasets with huge amount of GCPs, such as L1B AVHRR or HDF4 MODIS which
	 * contain thousands of them.
	 */
	private boolean suppressGroundControlPoints = false;

	/**
	 * Suppress metadata printing. Some datasets may contain a lot of metadata
	 * strings.
	 */
	private boolean suppressMetadata = false;

	/**
	 * Force computation of the checksum for each band in the dataset.
	 */
	private boolean checksum = false;

	/**
	 * Suppress printing of color table.
	 */
	private boolean suppressColorTable = false;

	/**
	 * Report metadata for the specified domain
	 */
	private String domain;

	public GDALInfo() {
		super();
	}

	public GDALInfo(String script) {
		super(script);
	}

	@Override
	public String convert() throws Exception {
//		StringBuilder sb = new StringBuilder();
//		if (this.getScript() != null && !this.getScript().isEmpty()) {
//			return this.getScript();
//		} else if (this.showHelp()) {
//			this.getSB().append("gdalinfo --help");
//			return this.getSB().toString();
//		} else {
			this.getSB().append("gdalinfo ");
			if (this.getInputFilepath() != null && !this.getInputFilepath().isEmpty())
				this.getSB().append(this.getInputFilepath()).append(" ");
			if (this.showChecksum())
				this.getSB().append("-checksum ");
			if (this.suppressGroundControlPoints())
				this.getSB().append("-nogcp ");
			if (this.showHistogram())
				this.getSB().append("-hist ");
			if (this.suppressMetadata())
				this.getSB().append("-nomd ");
			if (this.suppressColorTable())
				this.getSB().append("-noct ");
			if (this.forceMinMax())
				this.getSB().append("-mm ");
			if (this.showStatistics())
				this.getSB().append("-stats ");
			if (this.getDomain() != null && !this.getDomain().isEmpty())
				this.getSB().append("-mdd " + this.getDomain());
			if (this.getConfig() != null && !this.getConfig().isEmpty())
				for (String key : this.getConfig().keySet())
					this.getSB().append("--config ").append(key).append(" ").append(this.getConfig().get(key)).append(" ");
			return this.getSB().toString();
//		}
	}

	private boolean forceMinMax() {
		return minMax;
	}

	/**
	 * Force computation of the actual min/max values for each band in the
	 * dataset.
	 */
	public void minMax(boolean force) {
		this.minMax = force;
	}

	private boolean showStatistics() {
		return statistics;
	}

	/**
	 * Read and display image statistics. Force computation if no statistics are
	 * stored in an image.
	 */
	public void statistics(boolean show) {
		this.statistics = show;
	}

	private boolean showHistogram() {
		return histogram;
	}

	/**
	 * Report histogram information for all bands.
	 */
	public void histogram(boolean show) {
		this.histogram = show;
	}

	private boolean suppressGroundControlPoints() {
		return suppressGroundControlPoints;
	}

	/**
	 * Suppress ground control points list printing. It may be useful for
	 * datasets with huge amount of GCPs, such as L1B AVHRR or HDF4 MODIS which
	 * contain thousands of them.
	 */
	public void groundControlPoints(boolean suppress) {
		this.suppressGroundControlPoints = suppress;
	}

	private boolean suppressMetadata() {
		return suppressMetadata;
	}

	/**
	 * Suppress metadata printing. Some datasets may contain a lot of metadata
	 * strings.
	 */
	public void metadata(boolean suppress) {
		this.suppressMetadata = suppress;
	}

	private boolean showChecksum() {
		return checksum;
	}

	/**
	 * Force computation of the checksum for each band in the dataset.
	 */
	public void checksum(boolean forceComputation) {
		this.checksum = forceComputation;
	}

	private boolean suppressColorTable() {
		return suppressColorTable;
	}

	/**
	 * Suppress printing of color table.
	 */
	public void colorTable(boolean suppress) {
		this.suppressColorTable = suppress;
	}

	private String getDomain() {
		return domain;
	}

	/**
	 * Report metadata for the specified domain
	 */
	public void setDomain(String domain) {
		this.domain = domain;
	}

}