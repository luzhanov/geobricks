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
package org.geobricks.gdal.beans;

/**
 * 
 * @author <a href="mailto:guido.barbaglia@gmail.com">Guido Barbaglia</a>
 * 
 */
public class GDALInfo extends GDAL {

	private boolean minMax = false;

	private boolean statistics = false;

	private boolean histogram = false;

	private boolean groundControlPoints = true;

	private boolean metadata = true;

	private boolean checksum = false;

	private boolean colorTable = false;

	private String domain = "";

	public GDALInfo() {
		super();
	}

	public boolean showMinMax() {
		return minMax;
	}

	/**
	 * @param show
	 * 
	 *            Force computation of the actual min/max values for each band
	 *            in the dataset.
	 */
	public void minMax(boolean show) {
		this.minMax = show;
	}

	public boolean showStatistics() {
		return statistics;
	}

	/**
	 * @param show
	 * 
	 *            Read and display image statistics. Force computation if no
	 *            statistics are stored in an image.
	 */
	public void statistics(boolean show) {
		this.statistics = show;
	}

	public boolean showHistogram() {
		return histogram;
	}

	/**
	 * @param show
	 * 
	 *            Report histogram information for all bands.
	 */
	public void histogram(boolean show) {
		this.histogram = show;
	}

	public boolean showGroundControlPoints() {
		return groundControlPoints;
	}

	/**
	 * @param show
	 * 
	 *            Show ground control points list printing. It may be useful for
	 *            datasets with huge amount of GCPs, such as L1B AVHRR or HDF4
	 *            MODIS which contain thousands of them.
	 */
	public void groundControlPoints(boolean show) {
		this.groundControlPoints = show;
	}

	public boolean showMetadata() {
		return metadata;
	}

	/**
	 * @param show
	 * 
	 *            Show metadata printing. Some datasets may contain a lot of
	 *            metadata strings.
	 */
	public void metadata(boolean show) {
		this.metadata = show;
	}

	public boolean showChecksum() {
		return checksum;
	}

	/**
	 * @param show
	 * 
	 *            Force computation of the checksum for each band in the
	 *            dataset.
	 */
	public void checksum(boolean show) {
		this.checksum = show;
	}

	public String showDomain() {
		return domain;
	}

	/**
	 * @param show
	 * 
	 *            Report metadata for the specified domain
	 */
	public void domain(String show) {
		this.domain = show;
	}

	public boolean showColorTable() {
		return colorTable;
	}

	/**
	 * @param show
	 * 
	 *            Suppress printing of color table.
	 */
	public void colorTable(boolean show) {
		this.colorTable = show;
	}

}