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
package org.geobricks.gdal.dem.rasterize;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.geobricks.gdal.GDAL;
import org.geobricks.gdal.constant.CONFIG;
import org.geobricks.gdal.constant.FORMAT;
import org.geobricks.gdal.general.FileResolution;
import org.geobricks.gdal.general.FileSize;
import org.geobricks.gdal.general.GeoreferencedExtents;

/**
 * 
 * @author <a href="mailto:guido.barbaglia@gmail.com">Guido Barbaglia</a>
 * 
 *         This program burns vector geometries (points, lines and polygons)
 *         into the raster band(s) of a raster image. Vectors are read from OGR
 *         supported vector formats. Note that the vector data must in the same
 *         coordinate system as the raster data; on the fly reprojection is not
 *         provided. Since GDAL 1.8.0, the target GDAL file can be created by
 *         gdal_rasterize. One of -tr or -ts option must be used in that case.
 * 
 */
public class GDALRasterize extends GDAL {

	/**
	 * The band(s) to burn values into. Multiple -b arguments may be used to
	 * burn into a list of bands. The default is to burn into band 1.
	 */
	private List<Integer> bands;

	/**
	 * Invert rasterization. Burn the fixed burn value, or the burn value
	 * associated with the first feature into all parts of the image not inside
	 * the provided a polygon.
	 */
	private boolean invertRasterizaion = false;

	/**
	 * Enables the ALL_TOUCHED rasterization option so that all pixels touched
	 * by lines or polygons will be updated not just those one the line render
	 * path, or whose center point is within the polygon. Defaults to disabled
	 * for normal rendering rules.
	 */
	private boolean enableAllTouchedRasterization = false;

	/**
	 * A fixed value to burn into a band for all objects. A list of -burn
	 * options can be supplied, one per band being written to.
	 */
	private List<String> burnValues;

	/**
	 * Identifies an attribute field on the features to be used for a burn in
	 * value. The value will be burned into all output bands.
	 */
	private String attributeName;

	/**
	 * Indicates that a burn value should be extracted from the "Z" values of
	 * the feature. These values are adjusted by the burn value given by
	 * "-burn value" or "-a attribute_name" if provided. As of now, only points
	 * and lines are drawn in 3D.
	 */
	private boolean force3D = false;

	/**
	 * Indicates the layer(s) from the datasource that will be used for input
	 * features. May be specified multiple times, but at least one layer name or
	 * a -sql option must be specified.
	 */
	private List<String> layerNames;

	/**
	 * An optional SQL WHERE style query expression to be applied to select
	 * features to burn in from the input layer(s).
	 */
	private String whereExpression;

	/**
	 * An SQL statement to be evaluated against the datasource to produce a
	 * virtual layer of features to be burned in.
	 */
	private String sqlExpression;

	/**
	 * (GDAL >= 1.8.0) Select the output format. The default is GeoTIFF (GTiff).
	 * Use the short format name.
	 */
	private FORMAT outputFormat;

	/**
	 * (GDAL >= 1.8.0) Assign a specified nodata value to output bands.
	 */
	private String noData;

	/**
	 * (GDAL >= 1.8.0) Pre-initialize the output image bands with these values.
	 * However, it is not marked as the nodata value in the output file. If only
	 * one value is given, the same value is used in all the bands.
	 */
	private List<String> initValues;

	/**
	 * (GDAL >= 1.8.0) Override the projection for the output file. If not
	 * specified, the projection of the input vector file will be used if
	 * available. If incompatible projections between input and output files, no
	 * attempt will be made to reproject features. The srs_def may be any of the
	 * usual GDAL/OGR forms, complete WKT, PROJ.4, EPSG:n or a file containing
	 * the WKT.
	 */
	private String outputProjection;

	/**
	 * (GDAL >= 1.8.0) Passes a creation option to the output format driver.
	 * Multiple -co options may be listed. See format specific documentation for
	 * legal creation options for each format.
	 */
	private Map<String, String> creationOptions;

	/**
	 * (GDAL >= 1.8.0) set georeferenced extents. The values must be expressed
	 * in georeferenced units. If not specified, the extent of the output file
	 * will be the extent of the vector layers.
	 */
	private GeoreferencedExtents georeferencedExtents;

	/**
	 * (GDAL >= 1.8.0) set target resolution. The values must be expressed in
	 * georeferenced units. Both must be positive values.
	 */
	private FileResolution outputResolution;

	/**
	 * (GDAL >= 1.8.0) (target aligned pixels) align the coordinates of the
	 * extent of the output file to the values of the -tr, such that the aligned
	 * extent includes the minimum extent.
	 */
	private boolean targetAlignedPixels = false;

	/**
	 * (GDAL >= 1.8.0) set output file size in pixels and lines. Note that -ts
	 * cannot be used with -tr
	 */
	private FileSize outputFileSize;

	/**
	 * (GDAL >= 1.8.0) For the output bands to be of the indicated data type.
	 * Defaults to Float64
	 */
	private String outputBandsType;

	/**
	 * (GDAL >= 1.8.0) Suppress progress monitor and other non-error output.
	 */
	private boolean quiet = false;

	public GDALRasterize(String inputFilepath, String outputFilepath) {
		this.setInputFilepath(inputFilepath);
		this.setOutputFilepath(outputFilepath);
	}

	public List<Integer> getBands() {
		return bands;
	}

	/**
	 * @param bands
	 * 
	 *            The band(s) to burn values into. Multiple -b arguments may be
	 *            used to burn into a list of bands. The default is to burn into
	 *            band 1.
	 */
	public void setBands(List<Integer> bands) {
		this.bands = bands;
	}

	/**
	 * @param band
	 * 
	 *            The band(s) to burn values into. Multiple -b arguments may be
	 *            used to burn into a list of bands. The default is to burn into
	 *            band 1.
	 */
	public void addBand(Integer band) {
		if (this.bands == null)
			this.bands = new ArrayList<Integer>();
		this.bands.add(band);
	}

	public boolean invertRasterizaion() {
		return invertRasterizaion;
	}

	/**
	 * @param invert
	 * 
	 *            Invert rasterization. Burn the fixed burn value, or the burn
	 *            value associated with the first feature into all parts of the
	 *            image not inside the provided a polygon.
	 */
	public void invertRasterizaion(boolean invert) {
		this.invertRasterizaion = invert;
	}

	public boolean enableAllTouchedRasterization() {
		return enableAllTouchedRasterization;
	}

	/**
	 * @param enable
	 * 
	 *            Enables the ALL_TOUCHED rasterization option so that all
	 *            pixels touched by lines or polygons will be updated not just
	 *            those one the line render path, or whose center point is
	 *            within the polygon. Defaults to disabled for normal rendering
	 *            rules.
	 */
	public void enableAllTouchedRasterization(boolean enable) {
		this.enableAllTouchedRasterization = enable;
	}

	public List<String> getBurnValues() {
		return burnValues;
	}

	/**
	 * @param burnValues
	 * 
	 *            A fixed value to burn into a band for all objects. A list of
	 *            -burn options can be supplied, one per band being written to.
	 */
	public void setBurnValues(List<String> burnValues) {
		this.burnValues = burnValues;
	}

	/**
	 * @param burnValue
	 * 
	 *            A fixed value to burn into a band for all objects. A list of
	 *            -burn options can be supplied, one per band being written to.
	 */
	public void addBurnValue(String burnValue) {
		if (this.burnValues == null)
			this.burnValues = new ArrayList<String>();
		this.burnValues.add(burnValue);
	}

	public String getAttributeName() {
		return attributeName;
	}

	/**
	 * @param attributeName
	 * 
	 *            Identifies an attribute field on the features to be used for a
	 *            burn in value. The value will be burned into all output bands.
	 */
	public void setAttributeName(String attributeName) {
		this.attributeName = attributeName;
	}

	public boolean force3D() {
		return force3D;
	}

	/**
	 * @param force
	 * 
	 *            Indicates that a burn value should be extracted from the "Z"
	 *            values of the feature. These values are adjusted by the burn
	 *            value given by "-burn value" or "-a attribute_name" if
	 *            provided. As of now, only points and lines are drawn in 3D.
	 */
	public void force3D(boolean force) {
		force3D = force;
	}

	public List<String> getLayerNames() {
		return layerNames;
	}

	/**
	 * @param layerName
	 * 
	 *            Indicates the layer(s) from the datasource that will be used
	 *            for input features. May be specified multiple times, but at
	 *            least one layer name or a -sql option must be specified.
	 */
	public void setLayerNames(List<String> layerNames) {
		this.layerNames = layerNames;
	}
	
	/**
	 * @param layerName
	 * 
	 *            Indicates the layer(s) from the datasource that will be used
	 *            for input features. May be specified multiple times, but at
	 *            least one layer name or a -sql option must be specified.
	 */
	public void addLayerName(String layerName) {
		if (this.layerNames == null)
			this.layerNames = new ArrayList<String>();
		this.layerNames.add(layerName);
	}

	public String getWhereExpression() {
		return whereExpression;
	}

	/**
	 * @param whereExpression
	 * 
	 *            An optional SQL WHERE style query expression to be applied to
	 *            select features to burn in from the input layer(s).
	 */
	public void setWhereExpression(String whereExpression) {
		this.whereExpression = whereExpression;
	}

	public String getSqlExpression() {
		return sqlExpression;
	}

	/**
	 * @param sqlExpression
	 * 
	 *            An SQL statement to be evaluated against the datasource to
	 *            produce a virtual layer of features to be burned in.
	 */
	public void setSqlExpression(String sqlExpression) {
		this.sqlExpression = sqlExpression;
	}

	public FORMAT getOutputFormat() {
		return outputFormat;
	}

	/**
	 * @param outputFormat
	 * 
	 *            (GDAL >= 1.8.0) Select the output format. The default is
	 *            GeoTIFF (GTiff). Use the short format name.
	 */
	public void setOutputFormat(FORMAT outputFormat) {
		this.outputFormat = outputFormat;
	}

	public String getNoData() {
		return noData;
	}

	/**
	 * @param noData
	 * 
	 *            (GDAL >= 1.8.0) Assign a specified nodata value to output
	 *            bands.
	 */
	public void setNoData(String noData) {
		this.noData = noData;
	}

	public List<String> getInitValues() {
		return initValues;
	}

	/**
	 * @param initValues
	 * 
	 *            (GDAL >= 1.8.0) Pre-initialize the output image bands with
	 *            these values. However, it is not marked as the nodata value in
	 *            the output file. If only one value is given, the same value is
	 *            used in all the bands.
	 */
	public void setInitValues(List<String> initValues) {
		this.initValues = initValues;
	}

	/**
	 * @param burnValue
	 * 
	 *            (GDAL >= 1.8.0) Pre-initialize the output image bands with
	 *            these values. However, it is not marked as the nodata value in
	 *            the output file. If only one value is given, the same value is
	 *            used in all the bands.
	 */
	public void addInitValue(String burnValue) {
		if (this.initValues == null)
			this.initValues = new ArrayList<String>();
		this.initValues.add(burnValue);
	}

	public String getOutputProjection() {
		return outputProjection;
	}

	/**
	 * @param outputProjection
	 * 
	 *            (GDAL >= 1.8.0) Override the projection for the output file.
	 *            If not specified, the projection of the input vector file will
	 *            be used if available. If incompatible projections between
	 *            input and output files, no attempt will be made to reproject
	 *            features. The srs_def may be any of the usual GDAL/OGR forms,
	 *            complete WKT, PROJ.4, EPSG:n or a file containing the WKT.
	 */
	public void setOutputProjection(String outputProjection) {
		this.outputProjection = outputProjection;
	}

	public Map<String, String> getCreationOptions() {
		return creationOptions;
	}

	/**
	 * @param creationOptions
	 * 
	 *            (GDAL >= 1.8.0) Passes a creation option to the output format
	 *            driver. Multiple -co options may be listed. See format
	 *            specific documentation for legal creation options for each
	 *            format.
	 */
	public void setCreationOptions(Map<String, String> creationOptions) {
		this.creationOptions = creationOptions;
	}
	
	/**
	 * @param creationOptions
	 * 
	 *            (GDAL >= 1.8.0) Passes a creation option to the output format
	 *            driver. Multiple -co options may be listed. See format
	 *            specific documentation for legal creation options for each
	 *            format.
	 */
	public void addCreationOption(String option, String value) {
		if (this.creationOptions == null)
			this.creationOptions = new HashMap<String, String>();
		this.creationOptions.put(option, value);
	}

	public GeoreferencedExtents getGeoreferencedExtents() {
		return georeferencedExtents;
	}

	/**
	 * @param georeferencedExtents
	 * 
	 *            (GDAL >= 1.8.0) set georeferenced extents. The values must be
	 *            expressed in georeferenced units. If not specified, the extent
	 *            of the output file will be the extent of the vector layers.
	 */
	public void setGeoreferencedExtents(GeoreferencedExtents georeferencedExtents) {
		this.georeferencedExtents = georeferencedExtents;
	}

	public FileResolution getOutputResolution() {
		return outputResolution;
	}

	/**
	 * @param outputResolution
	 * 
	 *            (GDAL >= 1.8.0) set target resolution. The values must be
	 *            expressed in georeferenced units. Both must be positive
	 *            values.
	 */
	public void setOutputResolution(FileResolution outputResolution) {
		this.outputResolution = outputResolution;
	}

	public boolean targetAlignedPixels() {
		return targetAlignedPixels;
	}

	/**
	 * @param targetAlignedPixels
	 * 
	 *            (GDAL >= 1.8.0) (target aligned pixels) align the coordinates
	 *            of the extent of the output file to the values of the -tr,
	 *            such that the aligned extent includes the minimum extent.
	 */
	public void targetAlignedPixels(boolean targetAlignedPixels) {
		this.targetAlignedPixels = targetAlignedPixels;
	}

	public FileSize getOutputFileSize() {
		return outputFileSize;
	}

	/**
	 * @param outputFileSize
	 * 
	 *            (GDAL >= 1.8.0) set output file size in pixels and lines. Note
	 *            that -ts cannot be used with -tr
	 */
	public void setOutputFileSize(FileSize outputFileSize) {
		this.outputFileSize = outputFileSize;
	}

	public String getOutputBandsType() {
		return outputBandsType;
	}

	/**
	 * @param outputBandsType
	 * 
	 *            (GDAL >= 1.8.0) For the output bands to be of the indicated
	 *            data type. Defaults to Float64
	 */
	public void setOutputBandsType(String outputBandsType) {
		this.outputBandsType = outputBandsType;
	}

	public boolean quiet() {
		return quiet;
	}

	/**
	 * @param quiet
	 * 
	 *            (GDAL >= 1.8.0) Suppress progress monitor and other non-error
	 *            output.
	 */
	public void quiet(boolean quiet) {
		this.quiet = quiet;
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

		this.getSB().append("gdal_rasterize ");
		if (this.getBands() != null && !this.getBands().isEmpty())
			for (Integer b : this.getBands())
				this.getSB().append("-b ").append(b).append(" ");
		if (this.invertRasterizaion())
			this.getSB().append("-i ");
		if (this.enableAllTouchedRasterization())
			this.getSB().append("-at ");
		if (this.getBurnValues() != null && !this.getBurnValues().isEmpty())
			for (String b : this.getBurnValues())
				this.getSB().append("-burn ").append(b).append(" ");
		if (this.getAttributeName() != null && !this.getAttributeName().isEmpty())
			this.getSB().append("-a ").append(this.getAttributeName()).append(" ");
		if (this.force3D())
			this.getSB().append("-3d ");
		if (this.getLayerNames() != null && !this.getLayerNames().isEmpty())
			for (String b : this.getLayerNames())
				this.getSB().append("-l ").append(b).append(" ");
		if (this.getWhereExpression() != null && !this.getWhereExpression().isEmpty())
			this.getSB().append("-where ").append(this.getWhereExpression()).append(" ");
		if (this.getSqlExpression() != null && !this.getSqlExpression().isEmpty())
			this.getSB().append("-sql ").append(this.getSqlExpression()).append(" ");
		if (this.getOutputFormat() != null)
			this.getSB().append("-of ").append(this.getOutputFormat().name()).append(" ");
		if (this.getOutputProjection() != null && !this.getOutputProjection().isEmpty())
			this.getSB().append("-a_srs ").append(this.getOutputProjection()).append(" ");
		if (this.getCreationOptions() != null && !this.getCreationOptions().isEmpty())
			for (String key : this.getCreationOptions().keySet())
				this.getSB().append("-co \"").append(key).append("=").append(this.getCreationOptions().get(key)).append("\" ");
		if (this.getNoData() != null && !this.getNoData().isEmpty())
			this.getSB().append("-a_nodata ").append(this.getNoData()).append(" ");
		if (this.getInitValues() != null && !this.getInitValues().isEmpty())
			for (String b : this.getInitValues())
				this.getSB().append("-init ").append(b).append(" ");
		if (this.getGeoreferencedExtents() != null)
			this.getSB().append("-te ").append(this.getGeoreferencedExtents()).append(" ");
		if (this.getOutputResolution() != null)
			this.getSB().append("-tr ").append(this.getOutputResolution()).append(" ");
		if (this.targetAlignedPixels())
			this.getSB().append("-tap ");
		if (this.getOutputFileSize() != null)
			this.getSB().append("-ts ").append(this.getOutputFileSize()).append(" ");
		if (this.getOutputBandsType() != null && !this.getOutputBandsType().isEmpty())
			this.getSB().append("-ot ").append(this.getOutputBandsType()).append(" ");
		if (this.quiet())
			this.getSB().append("-q ");
		if (this.getInputFilepath() != null && !this.getInputFilepath().isEmpty()) {
			this.getSB().append(this.getInputFilepath()).append(" ");
		} else {
			throw new Exception("Input file has not been defined.");
		}
		if (this.getOutputFilepath() != null && !this.getOutputFilepath().isEmpty()) {
			this.getSB().append(this.getOutputFilepath()).append(" ");
		} else {
			throw new Exception("Output file has not been defined.");
		}
		
		// configuration options
		if (this.getConfig() != null && !this.getConfig().isEmpty())
			for (CONFIG key : this.getConfig().keySet())
				this.getSB().append("--config ").append(key.name()).append(" ").append(this.getConfig().get(key)).append(" ");

		return this.getSB().toString();
	}

}