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
package org.geobricks.gdal.retile;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.geobricks.gdal.GDAL;
import org.geobricks.gdal.constant.BANDSTYPE;
import org.geobricks.gdal.constant.CONFIG;
import org.geobricks.gdal.constant.FORMAT;
import org.geobricks.gdal.constant.RESAMPLING;
import org.geobricks.gdal.general.PixelSize;

/**
 * 
 * @author <a href="mailto:guido.barbaglia@gmail.com">Guido Barbaglia</a>
 * 
 *         This utility will retile a set of input tile(s). All the input
 *         tile(s) must be georeferenced in the same coordinate system and have
 *         a matching number of bands. Optionally pyramid levels are generated.
 *         It is possible to generate shape file(s) for the tiled output. If
 *         your number of input tiles exhausts the command line buffer, use the
 *         general --optfile option
 * 
 */
public class GDALRetile extends GDAL {

	/**
	 * The directory where the tile result is created. Pyramids are stored in
	 * subdirs numbered from 1. Created tile names have a numbering schema and
	 * contain the name of the source tiles(s)
	 */
	private String targetDirectory;

	/**
	 * Output format, defaults to GeoTIFF (GTiff).
	 */
	private FORMAT outputFormat;

	/**
	 * Passes a creation option to the output format driver. Multiple -co
	 * options may be listed. See format specific documentation for legal
	 * creation options for each format.
	 */
	private Map<String, String> creationOption;

	/**
	 * Force the output image bands to have a specific type. Use type names (ie.
	 * Byte, Int16,...)
	 */
	private BANDSTYPE outputBandsType;

	/**
	 * Pixel size to be used for the output file. If not specified, 256 x 256 is
	 * the default
	 */
	private PixelSize pixelSize;

	/**
	 * Number of pyramids levels to build.
	 */
	private Integer levels;

	/**
	 * Generate verbose output of tile operations as they are done.
	 */
	private boolean verbose = false;

	/**
	 * No retiling, build only the pyramids
	 */
	private boolean pyramidsOnly = false;

	/**
	 * Resampling algorithm, default is near
	 */
	private RESAMPLING resamplingAlgorithm;

	/**
	 * Source spatial reference to use. The coordinate systems that can be
	 * passed are anything supported by the
	 * OGRSpatialReference.SetFro‐mUserInput() call, which includes EPSG PCS and
	 * GCSes (ie.EPSG:4296), PROJ.4 declarations (as above), or the name of a
	 * .prf file containing well known text. If no srs_def is given, the srs_def
	 * of the source tiles is used (if there is any). The srs_def will be
	 * propageted to created tiles (if possible) and to the optional shape
	 * file(s)
	 */
	private String inputSpatialReference;

	/**
	 * The name of shape file containing the result tile(s) index
	 */
	private String tileIndexName;

	/**
	 * The name of the attribute containing the tile name
	 */
	private String tileIndexField;

	/**
	 * The name of the csv file containing the tile(s) georeferencing
	 * information. The file contains 5 columns: tilename,minx,maxx,miny,maxy
	 */
	private String csvFilename;

	/**
	 * The column delimter used in the csv file, default value is a semicolon
	 * ";"
	 */
	private String csvDelimiter;

	/**
	 * Normally the tiles of the base image are stored as described in
	 * -targetDir. For large images, some file systems have performance problems
	 * if the number of files in a directory is to big, causing gdal_retile not
	 * to finish in reasonable time. Using this parameter creates a different
	 * output structure. The tiles of the base image are stored in a
	 * subdirectory called 0, the pyramids in subdirectories numbered 1,2,....
	 * Within each of these directories another level of subdirectories is
	 * created, numbered from 0...n, depending of how many tile rows are needed
	 * for each level. Finally, a directory contains only the the tiles for one
	 * row for a specific level. For large images a performance improvement of a
	 * factor N could be achieved.
	 */
	private boolean useDirectoryForEachRow = false;

	private List<String> inputFilepaths;

	public GDALRetile(String inputFilepath, String targetDirectory) {
		this.setInputFilepath(inputFilepath);
		this.setTargetDirectory(targetDirectory);
	}

	public GDALRetile(List<String> inputFilepaths, String targetDirectory) {
		this.setInputFilepaths(inputFilepaths);
		this.setTargetDirectory(targetDirectory);
	}

	public String getTargetDirectory() {
		return targetDirectory;
	}

	/**
	 * @param targetDirectory
	 * 
	 *            The directory where the tile result is created. Pyramids are
	 *            stored in subdirs numbered from 1. Created tile names have a
	 *            numbering schema and contain the name of the source tiles(s)
	 */
	public void setTargetDirectory(String targetDirectory) {
		this.targetDirectory = targetDirectory;
	}

	public FORMAT getOutputFormat() {
		return outputFormat;
	}

	/**
	 * @param outputFormat
	 * 
	 *            Output format, defaults to GeoTIFF (GTiff).
	 */
	public void setOutputFormat(FORMAT outputFormat) {
		this.outputFormat = outputFormat;
	}

	public Map<String, String> getCreationOption() {
		return creationOption;
	}

	/**
	 * @param creationOption
	 * 
	 *            Creation option for output file. Multiple options can be
	 *            specified.
	 */
	public void setCreationOption(Map<String, String> creationOption) {
		this.creationOption = creationOption;
	}

	/**
	 * @param option
	 * @param value
	 * 
	 *            Creation option for output file. Multiple options can be
	 *            specified.
	 */
	public void addOption(String option, String value) {
		if (this.creationOption == null)
			this.creationOption = new HashMap<String, String>();
		this.creationOption.put(option, value);
	}

	public BANDSTYPE getOutputBandsType() {
		return outputBandsType;
	}

	/**
	 * @param outputBandsType
	 * 
	 *            Force the output image bands to have a specific type. Use type
	 *            names (ie. Byte, Int16,...)
	 */
	public void setOutputBandsType(BANDSTYPE outputBandsType) {
		this.outputBandsType = outputBandsType;
	}

	public PixelSize getPixelSize() {
		return pixelSize;
	}

	/**
	 * @param pixelSize
	 * 
	 *            Pixel size to be used for the output file. If not specified,
	 *            256 x 256 is the default
	 */
	public void setPixelSize(PixelSize pixelSize) {
		this.pixelSize = pixelSize;
	}

	public Integer getLevels() {
		return levels;
	}

	/**
	 * @param levels
	 * 
	 *            Number of pyramids levels to build.
	 */
	public void setLevels(Integer levels) {
		this.levels = levels;
	}

	public boolean verbose() {
		return verbose;
	}

	/**
	 * @param verbose
	 * 
	 *            Generate verbose output of tile operations as they are done.
	 */
	public void verbose(boolean verbose) {
		this.verbose = verbose;
	}

	public boolean pyramidsOnly() {
		return pyramidsOnly;
	}

	/**
	 * @param pyramidsOnly
	 * 
	 *            No retiling, build only the pyramids
	 */
	public void pyramidsOnly(boolean pyramidsOnly) {
		this.pyramidsOnly = pyramidsOnly;
	}

	public RESAMPLING getResamplingAlgorithm() {
		return resamplingAlgorithm;
	}

	/**
	 * @param resamplingAlgorithm
	 * 
	 *            Resampling algorithm, default is near
	 */
	public void setResamplingAlgorithm(RESAMPLING resamplingAlgorithm) {
		this.resamplingAlgorithm = resamplingAlgorithm;
	}

	public String getInputSpatialReference() {
		return inputSpatialReference;
	}

	/**
	 * @param inputSpatialReference
	 * 
	 *            Source spatial reference to use. The coordinate systems that
	 *            can be passed are anything supported by the
	 *            OGRSpatialReference.SetFro‐mUserInput() call, which includes
	 *            EPSG PCS and GCSes (ie.EPSG:4296), PROJ.4 declarations (as
	 *            above), or the name of a .prf file containing well known text.
	 *            If no srs_def is given, the srs_def of the source tiles is
	 *            used (if there is any). The srs_def will be propageted to
	 *            created tiles (if possible) and to the optional shape file(s)
	 */
	public void setInputSpatialReference(String inputSpatialReference) {
		this.inputSpatialReference = inputSpatialReference;
	}

	public String getTileIndexName() {
		return tileIndexName;
	}

	/**
	 * @param tileIndexName
	 * 
	 *            The name of shape file containing the result tile(s) index
	 */
	public void setTileIndexName(String tileIndexName) {
		this.tileIndexName = tileIndexName;
	}

	public String getTileIndexField() {
		return tileIndexField;
	}

	/**
	 * @param tileIndexField
	 * 
	 *            The name of the attribute containing the tile name
	 */
	public void setTileIndexField(String tileIndexField) {
		this.tileIndexField = tileIndexField;
	}

	public String getCsvFilename() {
		return csvFilename;
	}

	/**
	 * @param csvFilename
	 * 
	 *            The name of the csv file containing the tile(s) georeferencing
	 *            information. The file contains 5 columns:
	 *            tilename,minx,maxx,miny,maxy
	 */
	public void setCsvFilename(String csvFilename) {
		this.csvFilename = csvFilename;
	}

	public String getCsvDelimiter() {
		return csvDelimiter;
	}

	/**
	 * @param csvDelimiter
	 * 
	 *            The column delimter used in the csv file, default value is a
	 *            semicolon ";"
	 */
	public void setCsvDelimiter(String csvDelimiter) {
		this.csvDelimiter = csvDelimiter;
	}

	public boolean useDirectoryForEachRow() {
		return useDirectoryForEachRow;
	}

	public void useDirectoryForEachRow(boolean useDirectoryForEachRow) {
		this.useDirectoryForEachRow = useDirectoryForEachRow;
	}

	public List<String> getInputFilepaths() {
		return inputFilepaths;
	}

	/**
	 * @param inputFilepaths
	 * 
	 *            Normally the tiles of the base image are stored as described
	 *            in -targetDir. For large images, some file systems have
	 *            performance problems if the number of files in a directory is
	 *            to big, causing gdal_retile not to finish in reasonable time.
	 *            Using this parameter creates a different output structure. The
	 *            tiles of the base image are stored in a subdirectory called 0,
	 *            the pyramids in subdirectories numbered 1,2,.... Within each
	 *            of these directories another level of subdirectories is
	 *            created, numbered from 0...n, depending of how many tile rows
	 *            are needed for each level. Finally, a directory contains only
	 *            the the tiles for one row for a specific level. For large
	 *            images a performance improvement of a factor N could be
	 *            achieved.
	 */
	public void setInputFilepaths(List<String> inputFilepaths) {
		this.inputFilepaths = inputFilepaths;
	}

	/**
	 * @param inputFilepath
	 * 
	 *            Normally the tiles of the base image are stored as described
	 *            in -targetDir. For large images, some file systems have
	 *            performance problems if the number of files in a directory is
	 *            to big, causing gdal_retile not to finish in reasonable time.
	 *            Using this parameter creates a different output structure. The
	 *            tiles of the base image are stored in a subdirectory called 0,
	 *            the pyramids in subdirectories numbered 1,2,.... Within each
	 *            of these directories another level of subdirectories is
	 *            created, numbered from 0...n, depending of how many tile rows
	 *            are needed for each level. Finally, a directory contains only
	 *            the the tiles for one row for a specific level. For large
	 *            images a performance improvement of a factor N could be
	 *            achieved.
	 */
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

		this.getSB().append("gdal_retile.py ");
		if (this.verbose())
			this.getSB().append("-v ");
		if (this.getCreationOption() != null && !this.getCreationOption().isEmpty())
			for (String key : this.getCreationOption().keySet())
				this.getSB().append("-co \"").append(key).append("=").append(this.getCreationOption().get(key)).append("\" ");
		if (this.getOutputFormat() != null)
			this.getSB().append("-of ").append(this.getOutputFormat().name()).append(" ");
		if (this.getPixelSize() != null)
			this.getSB().append("-ps ").append(this.getPixelSize()).append(" ");
		if (this.getOutputBandsType() != null)
			this.getSB().append("-ot ").append(this.getOutputBandsType().name()).append(" ");
		if (this.getTileIndexName() != null && !this.getTileIndexName().isEmpty())
			this.getSB().append("-tileIndex ").append(this.getTileIndexName()).append(" ");
		if (this.getTileIndexField() != null && !this.getTileIndexField().isEmpty())
			this.getSB().append("-tileIndexField ").append(this.getTileIndexField()).append(" ");
		if (this.getCsvFilename() != null && !this.getCsvFilename().isEmpty())
			this.getSB().append("-csv ").append(this.getCsvFilename()).append(" ");
		if (this.getCsvDelimiter() != null && !this.getCsvDelimiter().isEmpty())
			this.getSB().append("-csvDelim ").append(this.getCsvDelimiter()).append(" ");
		if (this.getInputSpatialReference() != null && !this.getInputSpatialReference().isEmpty())
			this.getSB().append("-s_srs ").append(this.getInputSpatialReference()).append(" ");
		if (this.pyramidsOnly())
			this.getSB().append("-pyramidOnly ");
		if (this.getResamplingAlgorithm() != null)
			this.getSB().append("-r ").append(this.getResamplingAlgorithm().name()).append(" ");
		if (this.getLevels() != null)
			this.getSB().append("-levels ").append(this.getLevels()).append(" ");
		if (this.useDirectoryForEachRow())
			this.getSB().append("-useDirForEachRow ");
		if (this.getTargetDirectory() != null && !this.getTargetDirectory().isEmpty()) {
			this.getSB().append(this.getTargetDirectory()).append(" ");
		} else {
			throw new Exception("Target directory has not been defined.");
		}
		if (this.getInputFilepath() != null && !this.getInputFilepath().isEmpty()) {
			this.getSB().append(this.getInputFilepath()).append(" ");
		} else if (this.getInputFilepaths() != null && !this.getInputFilepaths().isEmpty()) {
			for (String i : this.getInputFilepaths())
				this.getSB().append(i).append(" ");
		} else {
			throw new Exception("Input files have not been defined.");
		}
		
		// configuration options
		if (this.getConfig() != null && !this.getConfig().isEmpty())
			for (CONFIG key : this.getConfig().keySet())
				this.getSB().append("--config ").append(key.name()).append(" ").append(this.getConfig().get(key)).append(" ");

		return this.getSB().toString();
	}

}