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
package org.geobricks.gdal.buildvrt;

import java.util.ArrayList;
import java.util.List;

import org.geobricks.gdal.GDAL;
import org.geobricks.gdal.constant.CONFIG;
import org.geobricks.gdal.constant.RESOLUTION;
import org.geobricks.gdal.general.GeoreferencedExtents;

/**
 * 
 * @author <a href="mailto:guido.barbaglia@gmail.com">Guido Barbaglia</a>
 * 
 *         This program builds a VRT (Virtual Dataset) that is a mosaic of the
 *         list of input gdal datasets. The list of input gdal datasets can be
 *         specified at the end of the command line, or put in a text file (one
 *         filename per line) for very long lists, or it can be a MapServer
 *         tileindex (see gdaltindex utility). In the later case, all entries in
 *         the tile index will be added to the VRT. With -separate, each files
 *         goes into a separate stacked band in the VRT band. Otherwise, the
 *         files are considered as tiles of a larger mosaic and the VRT file has
 *         as many bands as one of the input files. If one GDAL dataset is made
 *         of several subdatasets and has 0 raster bands, all the subdatasets
 *         will be added to the VRT rather than the dataset itself. gdalbuildvrt
 *         does some amount of checks to assure that all files that will be put
 *         in the resulting VRT have similar characteristics : number of bands,
 *         projection, color interpretation... If not, files that do not match
 *         the common characteristics will be skipped. (This is only true in the
 *         default mode, and not when using the -separate option) If there is
 *         some amount of spatial overlapping between files, the order may
 *         depend on the order they are inserted in the VRT file, but this
 *         behaviour should not be relied on. This utility is somehow equivalent
 *         to the gdal_vrtmerge.py utility and is build by default in GDAL
 *         1.6.1.
 * 
 */
public class GDALBuildVRT extends GDAL {

	/**
	 * Use the specified value as the tile index field, instead of the default
	 * value with is 'location'.
	 */
	private String tileIndex;

	/**
	 * In case the resolution of all input files is not the same, the
	 * -resolution flag enables the user to control the way the output
	 * resolution is computed. 'average' is the default. 'highest' will pick the
	 * smallest values of pixel dimensions within the set of source rasters.
	 * 'lowest' will pick the largest values of pixel dimensions within the set
	 * of source rasters. 'average' will compute an average of pixel dimensions
	 * within the set of source rasters. 'user' is new in GDAL 1.7.0 and must be
	 * used in combination with the -tr option to specify the target resolution.
	 */
	private RESOLUTION resolution;

	/**
	 * (starting with GDAL 1.7.0) set target resolution. The values must be
	 * expressed in georeferenced units. Both must be positive values.
	 * Specifying those values is of course incompatible with
	 * highest|lowest|average values for -resolution option.
	 */
	private TargetResolution targetResolution;

	/**
	 * (GDAL >= 1.8.0) (target aligned pixels) align the coordinates of the
	 * extent of the output file to the values of the -tr, such that the aligned
	 * extent includes the minimum extent.
	 */
	private boolean targetAlignedPoints = false;

	/**
	 * (starting with GDAL 1.7.0) set georeferenced extents of VRT file. The
	 * values must be expressed in georeferenced units. If not specified, the
	 * extent of the VRT is the minimum bounding box of the set of source
	 * rasters.
	 */
	private GeoreferencedExtents georeferencedExtents;

	/**
	 * (starting with GDAL 1.7.0) Adds an alpha mask band to the VRT when the
	 * source raster have none. Mainly useful for RGB sources (or grey-level
	 * sources). The alpha band is filled on-the-fly with the value 0 in areas
	 * without any source raster, and with value 255 in areas with source
	 * raster. The effect is that a RGBA viewer will render the areas without
	 * source rasters as transparent and areas with source rasters as opaque.
	 * This option is not compatible with -separate.
	 */
	private boolean addAlpha = false;

	/**
	 * (starting with GDAL 1.7.0) Even if any band contains nodata value, giving
	 * this option makes the VRT band not report the NoData. Useful when you
	 * want to control the background color of the dataset. By using along with
	 * the -addalpha option, you can prepare a dataset which doesn't report
	 * nodata value but is transparent in areas with no data.
	 */
	private boolean hideNoData = false;

	/**
	 * (starting with GDAL 1.7.0) Set nodata values for input bands (different
	 * values can be supplied for each band). If more than one value is supplied
	 * all values should be quoted to keep them together as a single operating
	 * system argument. If the option is not specified, the instrinsic nodata
	 * settings on the source datasets will be used (if they exist). The value
	 * set by this option is written in the NODATA element of each ComplexSource
	 * element. Use a value of None to ignore intrinsic nodata settings on the
	 * source datasets.
	 */
	private List<Integer> inputNoDataValues;

	/**
	 * (starting with GDAL 1.7.0) Set nodata values at the VRT band level
	 * (different values can be supplied for each band). If more than one value
	 * is supplied all values should be quoted to keep them together as a single
	 * operating system argument. If the option is not specified, instrinsic
	 * nodata settings on the first dataset will be used (if they exist). The
	 * value set by this option is written in the NoDataValue element of each
	 * VRTRasterBand element. Use a value of None to ignore intrinsic nodata
	 * settings on the source datasets.
	 */
	private List<Integer> outputNoDataValues;

	/**
	 * (starting with GDAL 1.7.0) Place each input file into a separate stacked
	 * band. In that case, only the first band of each dataset will be placed
	 * into a new band. Contrary to the default mode, it is not required that
	 * all bands have the same datatype.
	 */
	private boolean separate = false;

	/**
	 * (starting with GDAL 1.7.0) When this option is specified, the utility
	 * will accept to make a VRT even if the input datasets have not the same
	 * projection. Note: this does not mean that they will be reprojected. Their
	 * projection will just be ignored.
	 */
	private boolean allowProjectionDifference = false;

	/**
	 * To specify a text file with an input filename on each line
	 */
	private String inputFileList;

	/**
	 * (starting with GDAL 1.7.0) To disable the progress bar on the console
	 */
	private boolean quiet = false;

	/**
	 * Overwrite the VRT if it already exists.
	 */
	private boolean overwrite = false;
	
	private List<String> inputFilepaths;

	public GDALBuildVRT(String outputFilepath) {
		this.setOutputFilepath(outputFilepath);
	}
	
	public GDALBuildVRT(String inputFilepath, String outputFilepath) {
		this.setInputFilepath(inputFilepath);
		this.setOutputFilepath(outputFilepath);
	}
	
	public GDALBuildVRT(List<String> inputFilepaths, String outputFilepath) {
		this.setInputFilepaths(inputFilepaths);
		this.setOutputFilepath(outputFilepath);
	}

	public String getTileIndex() {
		return tileIndex;
	}

	/**
	 * Use the specified value as the tile index field, instead of the default
	 * value with is 'location'.
	 */
	public void setTileIndex(String tileIndex) {
		this.tileIndex = tileIndex;
	}

	public RESOLUTION getResolution() {
		return resolution;
	}

	/**
	 * @param resolution
	 * 
	 *            In case the resolution of all input files is not the same, the
	 *            -resolution flag enables the user to control the way the
	 *            output resolution is computed. 'average' is the default.
	 *            'highest' will pick the smallest values of pixel dimensions
	 *            within the set of source rasters. 'lowest' will pick the
	 *            largest values of pixel dimensions within the set of source
	 *            rasters. 'average' will compute an average of pixel dimensions
	 *            within the set of source rasters. 'user' is new in GDAL 1.7.0
	 *            and must be used in combination with the -tr option to specify
	 *            the target resolution.
	 */
	public void setResolution(RESOLUTION resolution) {
		this.resolution = resolution;
	}

	public TargetResolution getTargetResolution() {
		return targetResolution;
	}

	/**
	 * @param targetResolution
	 * 
	 *            (starting with GDAL 1.7.0) set target resolution. The values
	 *            must be expressed in georeferenced units. Both must be
	 *            positive values. Specifying those values is of course
	 *            incompatible with highest|lowest|average values for
	 *            -resolution option.
	 */
	public void setTargetResolution(TargetResolution targetResolution) {
		this.targetResolution = targetResolution;
	}

	public boolean targetAlignedPoints() {
		return targetAlignedPoints;
	}

	/**
	 * @param targetAlignedPoints
	 * 
	 *            (GDAL >= 1.8.0) (target aligned pixels) align the coordinates
	 *            of the extent of the output file to the values of the -tr,
	 *            such that the aligned extent includes the minimum extent.
	 */
	public void targetAlignedPoints(boolean targetAlignedPoints) {
		this.targetAlignedPoints = targetAlignedPoints;
	}

	public GeoreferencedExtents getGeoreferencedExtents() {
		return georeferencedExtents;
	}

	/**
	 * @param georeferencedExtents
	 * 
	 *            (starting with GDAL 1.7.0) set georeferenced extents of VRT
	 *            file. The values must be expressed in georeferenced units. If
	 *            not specified, the extent of the VRT is the minimum bounding
	 *            box of the set of source rasters.
	 */
	public void setGeoreferencedExtents(GeoreferencedExtents georeferencedExtents) {
		this.georeferencedExtents = georeferencedExtents;
	}

	public boolean addAlpha() {
		return addAlpha;
	}

	/**
	 * @param addAlpha
	 * 
	 *            (starting with GDAL 1.7.0) Adds an alpha mask band to the VRT
	 *            when the source raster have none. Mainly useful for RGB
	 *            sources (or grey-level sources). The alpha band is filled
	 *            on-the-fly with the value 0 in areas without any source
	 *            raster, and with value 255 in areas with source raster. The
	 *            effect is that a RGBA viewer will render the areas without
	 *            source rasters as transparent and areas with source rasters as
	 *            opaque. This option is not compatible with -separate.
	 */
	public void addAlpha(boolean addAlpha) {
		this.addAlpha = addAlpha;
	}

	public boolean hideNoData() {
		return hideNoData;
	}

	/**
	 * @param hideNoData
	 * 
	 *            (starting with GDAL 1.7.0) Even if any band contains nodata
	 *            value, giving this option makes the VRT band not report the
	 *            NoData. Useful when you want to control the background color
	 *            of the dataset. By using along with the -addalpha option, you
	 *            can prepare a dataset which doesn't report nodata value but is
	 *            transparent in areas with no data.
	 */
	public void hideNoData(boolean hideNoData) {
		this.hideNoData = hideNoData;
	}

	public List<Integer> getInputNoDataValues() {
		return inputNoDataValues;
	}

	/**
	 * @param inputNoDataValues
	 * 
	 *            (starting with GDAL 1.7.0) Set nodata values for input bands
	 *            (different values can be supplied for each band). If more than
	 *            one value is supplied all values should be quoted to keep them
	 *            together as a single operating system argument. If the option
	 *            is not specified, the instrinsic nodata settings on the source
	 *            datasets will be used (if they exist). The value set by this
	 *            option is written in the NODATA element of each ComplexSource
	 *            element. Use a value of None to ignore intrinsic nodata
	 *            settings on the source datasets.
	 */
	public void setInputNoDataValues(List<Integer> inputNoDataValues) {
		this.inputNoDataValues = inputNoDataValues;
	}

	/**
	 * @param noDataValue
	 * 
	 *            (starting with GDAL 1.7.0) Set nodata values for input bands
	 *            (different values can be supplied for each band). If more than
	 *            one value is supplied all values should be quoted to keep them
	 *            together as a single operating system argument. If the option
	 *            is not specified, the instrinsic nodata settings on the source
	 *            datasets will be used (if they exist). The value set by this
	 *            option is written in the NODATA element of each ComplexSource
	 *            element. Use a value of None to ignore intrinsic nodata
	 *            settings on the source datasets.
	 */
	public void addInputNoDataValue(Integer noDataValue) {
		if (this.inputNoDataValues == null)
			this.inputNoDataValues = new ArrayList<Integer>();
		this.inputNoDataValues.add(noDataValue);
	}

	public List<Integer> getOutputNoDataValues() {
		return outputNoDataValues;
	}

	/**
	 * @param outputNoDataValues
	 * 
	 *            (starting with GDAL 1.7.0) Set nodata values at the VRT band
	 *            level (different values can be supplied for each band). If
	 *            more than one value is supplied all values should be quoted to
	 *            keep them together as a single operating system argument. If
	 *            the option is not specified, instrinsic nodata settings on the
	 *            first dataset will be used (if they exist). The value set by
	 *            this option is written in the NoDataValue element of each
	 *            VRTRasterBand element. Use a value of None to ignore intrinsic
	 *            nodata settings on the source datasets.
	 */
	public void setOutputNoDataValues(List<Integer> outputNoDataValues) {
		this.outputNoDataValues = outputNoDataValues;
	}

	/**
	 * @param noDataValue
	 * 
	 *            (starting with GDAL 1.7.0) Set nodata values at the VRT band
	 *            level (different values can be supplied for each band). If
	 *            more than one value is supplied all values should be quoted to
	 *            keep them together as a single operating system argument. If
	 *            the option is not specified, instrinsic nodata settings on the
	 *            first dataset will be used (if they exist). The value set by
	 *            this option is written in the NoDataValue element of each
	 *            VRTRasterBand element. Use a value of None to ignore intrinsic
	 *            nodata settings on the source datasets.
	 */
	public void addOutputNoDataValue(Integer noDataValue) {
		if (this.outputNoDataValues == null)
			this.outputNoDataValues = new ArrayList<Integer>();
		this.outputNoDataValues.add(noDataValue);
	}

	public boolean separate() {
		return separate;
	}

	/**
	 * @param separate
	 * 
	 *            (starting with GDAL 1.7.0) Place each input file into a
	 *            separate stacked band. In that case, only the first band of
	 *            each dataset will be placed into a new band. Contrary to the
	 *            default mode, it is not required that all bands have the same
	 *            datatype.
	 */
	public void separate(boolean separate) {
		this.separate = separate;
	}

	public boolean allowProjectionDifference() {
		return allowProjectionDifference;
	}

	/**
	 * @param allowProjectionDifference
	 * 
	 *            (starting with GDAL 1.7.0) When this option is specified, the
	 *            utility will accept to make a VRT even if the input datasets
	 *            have not the same projection. Note: this does not mean that
	 *            they will be reprojected. Their projection will just be
	 *            ignored.
	 */
	public void allowProjectionDifference(boolean allowProjectionDifference) {
		this.allowProjectionDifference = allowProjectionDifference;
	}

	public String getInputFileList() {
		return inputFileList;
	}

	/**
	 * @param inputFileList
	 * 
	 *            To specify a text file with an input filename on each line
	 */
	public void setInputFileList(String inputFileList) {
		this.inputFileList = inputFileList;
	}

	public boolean quiet() {
		return quiet;
	}

	/**
	 * @param quiet
	 * 
	 *            (starting with GDAL 1.7.0) To disable the progress bar on the
	 *            console
	 */
	public void quiet(boolean quiet) {
		this.quiet = quiet;
	}

	public boolean overwrite() {
		return overwrite;
	}

	/**
	 * @param overwrite
	 * 
	 *            Overwrite the VRT if it already exists.
	 */
	public void overwrite(boolean overwrite) {
		this.overwrite = overwrite;
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

	@Override
	public String convert() throws Exception {

		// generic help
		if (this.getScript() != null && !this.getScript().isEmpty()) {
			return this.getScript();
		} else if (this.showHelp()) {
			this.getSB().append("gdalinfo --help");
			return this.getSB().toString();
		}

		this.getSB().append("gdalbuildvrt ");
		if (this.getTileIndex() != null && !this.getTileIndex().isEmpty())
			this.getSB().append("-tileindex ").append(this.getTileIndex()).append(" ");
		if (this.getResolution() != null)
			this.getSB().append("-resolution ").append(this.getResolution().name()).append(" ");
		if (this.getTargetResolution() != null)
			this.getSB().append("-tr ").append(this.getTargetResolution()).append(" ");
		if (this.targetAlignedPoints())
			this.getSB().append("-tap ");
		if (this.separate())
			this.getSB().append("-separate ");
		if (this.allowProjectionDifference())
			this.getSB().append("-allow_projection_difference ");
		if (this.quiet())
			this.getSB().append("-q ");
		if (this.getGeoreferencedExtents() != null)
			this.getSB().append("-te ").append(this.getGeoreferencedExtents()).append(" ");
		if (this.addAlpha())
			this.getSB().append("-addalpha ");
		if (this.hideNoData())
			this.getSB().append("-hidenodata ");
		if (this.getInputNoDataValues() != null && !this.getInputNoDataValues().isEmpty()) {
			this.getSB().append("-srcnodata \"");
			for (Integer i : this.getInputNoDataValues())
				this.getSB().append(i).append(" ");
			this.getSB().append("\" ");
		}
		if (this.getOutputNoDataValues() != null && !this.getOutputNoDataValues().isEmpty()) {
			this.getSB().append("-vrtnodata \"");
			for (Integer i : this.getOutputNoDataValues())
				this.getSB().append(i).append(" ");
			this.getSB().append("\" ");
		}
		if (this.getInputFileList() != null && !this.getInputFileList().isEmpty())
			this.getSB().append("-input_file_list ").append(this.getInputFileList()).append(" ");
		if (this.overwrite())
			this.getSB().append("-overwrite ");
		if (this.getOutputFilepath() != null && !this.getOutputFilepath().isEmpty()) {
			this.getSB().append(this.getOutputFilepath()).append(" ");
		} else {
			throw new Exception("Output file has not been specified.");
		}
		if (this.getInputFilepath() != null && !this.getInputFilepath().isEmpty()) { 
			this.getSB().append(this.getInputFilepath()).append(" ");
		} else if (this.getInputFilepaths() != null && !this.getInputFilepaths().isEmpty()) {
			for (String i : this.getInputFilepaths())
				this.getSB().append(i).append(" ");
		} else {
			throw new Exception("Input files have not been specified.");
		}
		
		// configuration options
		if (this.getConfig() != null && !this.getConfig().isEmpty())
			for (CONFIG key : this.getConfig().keySet())
				this.getSB().append("--config ").append(key.name()).append(" ").append(this.getConfig().get(key)).append(" ");

		return this.getSB().toString();
	}

}