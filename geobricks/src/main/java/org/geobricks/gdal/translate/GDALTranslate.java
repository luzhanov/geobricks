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
package org.geobricks.gdal.translate;

import java.util.Map;

import org.geobricks.gdal.GDAL;
import org.geobricks.gdal.constant.EXPAND;
import org.geobricks.gdal.constant.FORMAT;

/**
 * 
 * @author <a href="mailto:guido.barbaglia@gmail.com">Guido Barbaglia</a>
 * 
 */
public class GDALTranslate extends GDAL {

	/**
	 * For the output bands to be of the indicated data type.
	 */
	private String outputType = "";

	/**
	 * Do'nt be forgiving of mismatches and lost data when translating to the
	 * output format.
	 */
	private boolean strict = false;

	/**
	 * Select the output format. The default is GeoTIFF (GTiff). Use the short
	 * format name.
	 */
	private FORMAT outputFormat = FORMAT.GTiff;

	/**
	 * Select an input band band for output. Bands are numbered from 1. Multiple
	 * -b switches may be used to select a set of input bands to write to the
	 * output file, or to reorder bands. Starting with GDAL 1.8.0, band can also
	 * be set to "mask,1" (or just "mask") to mean the mask band of the 1st band
	 * of the input dataset.
	 */
	private String[] band;

	/**
	 * (GDAL >= 1.8.0) Select an input band band to create output dataset mask
	 * band. Bands are numbered from 1. band can be set to "none" to avoid
	 * copying the global mask of the input dataset if it exists. Otherwise it
	 * is copied by default ("auto"), unless the mask is an alpha channel, or if
	 * it is explicitely used to ben a regular band of the output dataset
	 * ("-b mask"). band can also be set to "mask,1" (or just "mask") to mean
	 * the mask band of the 1st band of the input dataset.
	 */
	private String mask = "";

	/**
	 * (From GDAL 1.6.0) To expose a dataset with 1 band with a color table as a
	 * dataset with 3 (RGB) or 4 (RGBA) bands. Usefull for output drivers such
	 * as JPEG, JPEG2000, MrSID, ECW that don't support color indexed datasets.
	 * The 'gray' value (from GDAL 1.7.0) enables to expand a dataset with a
	 * color table that only contains gray levels to a gray indexed dataset.
	 */
	private EXPAND expand = EXPAND.rgb;

	/**
	 * Set the size of the output file. Outsize is in pixels and lines unless ''
	 * is attached in which case it is as a fraction of the input image size.
	 */
	private OutputSize outputSize = null;

	/**
	 * Rescale the input pixels values from the range src_min to src_max to the
	 * range dst_min to dst_max. If omitted the output range is 0 to 255. If
	 * omitted the input range is automatically computed from the source data.
	 */
	private Scale scale;

	/**
	 * Apply the scale/offset metadata for the bands to convert scaled values to
	 * unscaled values. It is also often necessary to reset the output datatype
	 * with the -ot switch.
	 */
	private boolean unscale = false;

	/**
	 * Selects a subwindow from the source image for copying based on pixel/line
	 * location.
	 */
	private SubWindowPixels subWindowPixels;

	/**
	 * Selects a subwindow from the source image for copying (like -srcwin) but
	 * with the corners given in georeferenced coordinates.
	 */
	private SubWindowCorners subWindowCorners;

	/**
	 * Override the projection for the output file. The srs_def may be any of
	 * the usual GDAL/OGR forms, complete WKT, PROJ.4, EPSG:n or a file
	 * containing the WKT.
	 */
	private String outputProjection = "";

	/**
	 * Assign/override the georeferenced bounds of the output file. This assigns
	 * georeferenced bounds to the output file, ignoring what would have been
	 * derived from the source file.
	 */
	private OutputBounds outputBounds;

	/**
	 * Assign a specified nodata value to output bands. Starting with GDAL
	 * 1.8.0, can be set to none to avoid setting a nodata value to the output
	 * file if one exists for the source file
	 */
	private String noDataValue;

	/**
	 * Passes a metadata key and value to set on the output dataset if possible.
	 */
	private Map<String, String> metadataOutput;

	/**
	 * Passes a creation option to the output format driver. Multiple -co
	 * options may be listed. See format specific documentation for legal
	 * creation options for each format.
	 */
	private Map<String, String> creationOption;

	/**
	 * Add the indicated ground control point to the output dataset. This option
	 * may be provided multiple times to provide a set of GCPs.
	 */
	private GroundControlPoint groundControlPoint;

	/**
	 * Suppress progress monitor and other non-error output.
	 */
	private boolean suppressProgressMonitor = false;

	/**
	 * Copy all subdatasets of this file to individual output files. Use with
	 * formats like HDF or OGDI that have subdatasets.
	 */
	private boolean subDatasets2IndividualOutputs = false;

	/**
	 * (GDAL >= 1.8.0) Force (re)computation of statistics.
	 */
	private boolean statistics = false;

	public GDALTranslate() {
		super();
	}

	public GDALTranslate(String script) {
		super(script);
	}

	@Override
	public String convert() {
		return super.convert();
	}

}