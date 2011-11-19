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
package org.geobricks.gdal.merge;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.geobricks.gdal.GDAL;
import org.geobricks.gdal.constant.CONFIG;
import org.geobricks.gdal.constant.FORMAT;
import org.geobricks.gdal.general.OutputBounds;

/**
 * 
 * @author <a href="mailto:guido.barbaglia@gmail.com">Guido Barbaglia</a>
 * 
 *         This utility will automatically mosaic a set of images. All the
 *         images must be in the same coordinate system and have a matching
 *         number of bands, but they may be overlapping, and at different
 *         resolutions. In areas of overlap, the last image will be copied over
 *         earlier ones.
 * 
 */
public class GDALMerge extends GDAL {

	/**
	 * The source file name(s).
	 */
	private List<String> inputFilepaths;

	/**
	 * Output format, defaults to GeoTIFF (GTiff).
	 */
	private FORMAT outputFormat;

	/**
	 * Creation option for output file. Multiple options can be specified.
	 */
	private Map<String, String> creationOption;

	/**
	 * Force the output image bands to have a specific type. Use type names (ie.
	 * Byte, Int16,...)
	 */
	private String outputBandsType;

	/**
	 * Pixel size to be used for the output file. If not specified the
	 * resolution of the first input file will be used.
	 */
	private PixelSize outputPixelSize;

	private boolean targetAlignedPixels = false;

	/**
	 * The extents of the output file. If not specified the aggregate extents of
	 * all input files will be used.
	 */
	private OutputBounds outputExtents;

	/**
	 * Generate verbose output of mosaicing operations as they are done.
	 */
	private boolean verbose = false;

	/**
	 * Place each input file into a separate stacked band.
	 */
	private boolean separate = false;

	/**
	 * Grab a pseudocolor table from the first input image, and use it for the
	 * output. Merging pseudocolored images this way assumes that all input
	 * files use the same color table.
	 */
	private boolean pseudoColorTable = false;

	/**
	 * Ignore pixels from files being merged in with this pixel value.
	 */
	private String noDataValue;

	/**
	 * (GDAL >= 1.9.0) Assign a specified nodata value to output bands.
	 */
	private String outputBandsNoDataValue;

	/**
	 * Pre-initialize the output image bands with these values. However, it is
	 * not marked as the nodata value in the output file. If only one value is
	 * given, the same value is used in all the bands.
	 */
	private List<String> outputBandsInitValues;

	/**
	 * The output file is created (and potentially pre-initialized) but no input
	 * image data is copied into it.
	 */
	private boolean createOnly = false;

	public GDALMerge(List<String> inputFilepaths, String outputFilepath) {
		super();
		this.setInputFilepaths(inputFilepaths);
		this.setOutputFilepath(outputFilepath);
	}

	public GDALMerge(String inputFilepath, String outputFilepath) {
		super();
		this.addInputFilepath(inputFilepath);
		this.setOutputFilepath(outputFilepath);
	}

	public List<String> getInputFilepaths() {
		return inputFilepaths;
	}

	public void setInputFilepaths(List<String> inputFilepaths) {
		this.inputFilepaths = inputFilepaths;
	}

	public void addInputFilepath(String inputFilepath) {
		if (this.inputFilepaths == null)
			this.inputFilepaths = new ArrayList<String>();
		this.inputFilepaths.add(inputFilepath);
	}

	public FORMAT getOutputFormat() {
		return outputFormat;
	}

	public void setOutputFormat(FORMAT outputFormat) {
		this.outputFormat = outputFormat;
	}

	public Map<String, String> getCreationOption() {
		return creationOption;
	}

	public void setCreationOption(Map<String, String> creationOption) {
		this.creationOption = creationOption;
	}

	public String getOutputBandsType() {
		return outputBandsType;
	}

	public void setOutputBandsType(String outputBandsType) {
		this.outputBandsType = outputBandsType;
	}

	public PixelSize getOutputPixelSize() {
		return outputPixelSize;
	}

	public void setOutputPixelSize(PixelSize outputPixelSize) {
		this.outputPixelSize = outputPixelSize;
	}

	public boolean targetAlignedPixels() {
		return targetAlignedPixels;
	}

	public void targetAlignedPixels(boolean targetAlignedPixels) {
		this.targetAlignedPixels = targetAlignedPixels;
	}

	public OutputBounds getOutputExtents() {
		return outputExtents;
	}

	public void setOutputExtents(OutputBounds outputExtents) {
		this.outputExtents = outputExtents;
	}

	public boolean verbose() {
		return verbose;
	}

	public void verbose(boolean verbose) {
		this.verbose = verbose;
	}

	public boolean separate() {
		return separate;
	}

	public void separate(boolean separate) {
		this.separate = separate;
	}

	public boolean pseudoColorTable() {
		return pseudoColorTable;
	}

	public void pseudoColorTable(boolean pseudoColorTable) {
		this.pseudoColorTable = pseudoColorTable;
	}

	public String getNoDataValue() {
		return noDataValue;
	}

	public void setNoDataValue(String noDataValue) {
		this.noDataValue = noDataValue;
	}

	public String getOutputBandsNoDataValue() {
		return outputBandsNoDataValue;
	}

	public void setOutputBandsNoDataValue(String outputBandsNoDataValue) {
		this.outputBandsNoDataValue = outputBandsNoDataValue;
	}

	public List<String> getOutputBandsInitValues() {
		return outputBandsInitValues;
	}

	public void setOutputBandsInitValues(List<String> outputBandsInitValues) {
		this.outputBandsInitValues = outputBandsInitValues;
	}

	public void addOutputBandInitValue(String outputBandInitValue) {
		if (this.outputBandsInitValues == null)
			this.outputBandsInitValues = new ArrayList<String>();
		this.outputBandsInitValues.add(outputBandInitValue);
	}

	public boolean createOnly() {
		return createOnly;
	}

	public void createOnly(boolean createOnly) {
		this.createOnly = createOnly;
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
		
		this.getSB().append("gdal_merge.py ");
		if (this.getOutputFilepath() != null && !this.getOutputFilepath().isEmpty())
			this.getSB().append("-o ").append(this.getOutputFilepath()).append(" ");
		if (this.getOutputFormat() != null)
			this.getSB().append("-of ").append(this.getOutputFormat().name()).append(" ");
		if (this.getCreationOption() != null && !this.getCreationOption().isEmpty())
			for (String key : this.getCreationOption().keySet())
				this.getSB().append("-co \"").append(key).append("=").append(this.getCreationOption().get(key)).append("\" ");
		if (this.getOutputPixelSize() != null)
			this.getSB().append("-ps ").append(this.getOutputPixelSize()).append(" ");
		if (this.targetAlignedPixels())
			this.getSB().append("-tap ");
		if (this.getOutputExtents() != null)
			this.getSB().append("-ul_lr ").append(this.getOutputExtents()).append(" ");
		if (this.verbose())
			this.getSB().append("-v ");
		if (this.separate())
			this.getSB().append("-separate ");
		if (this.pseudoColorTable())
			this.getSB().append("-pct ");
		if (this.getNoDataValue() != null && !this.getNoDataValue().isEmpty())
			this.getSB().append("-n ").append(this.getNoDataValue()).append(" ");
		if (this.getOutputBandsNoDataValue() != null && !this.getOutputBandsNoDataValue().isEmpty())
			this.getSB().append("-a_nodata ").append(this.getOutputBandsNoDataValue()).append(" ");
		if (this.getOutputBandsInitValues() != null && !this.getOutputBandsInitValues().isEmpty()) {
			this.getSB().append("-init \"");
			for (String v : this.getOutputBandsInitValues())
				this.getSB().append(v).append(" ");
			this.getSB().append("\" ");
		}
		if (this.createOnly())
			this.getSB().append("-createonly ");

		// configuration options
		if (this.getConfig() != null && !this.getConfig().isEmpty())
			for (CONFIG key : this.getConfig().keySet())
				this.getSB().append("--config ").append(key.name()).append(" ").append(this.getConfig().get(key)).append(" ");

		return this.getSB().toString();
	}

}
