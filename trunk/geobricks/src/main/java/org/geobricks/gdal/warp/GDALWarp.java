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
package org.geobricks.gdal.warp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.geobricks.gdal.GDAL;
import org.geobricks.gdal.constant.CONFIG;
import org.geobricks.gdal.constant.FORMAT;
import org.geobricks.gdal.constant.RESAMPLING;
import org.geobricks.gdal.constant.WARPOPTION;

/**
 * 
 * @author <a href="mailto:guido.barbaglia@gmail.com">Guido Barbaglia</a>
 * 
 *         The GDALWarp utility is an image mosaicing, reprojection and warping
 *         utility. The program can reproject to any supported projection, and
 *         can also apply GCPs stored with the image if the image is "raw" with
 *         control information.
 * 
 */
public class GDALWarp extends GDAL {

	/**
	 * The source file name(s).
	 */
	private List<String> inputFilepaths;

	/**
	 * source spatial reference set. The coordinate systems that can be passed
	 * are anything supported by the OGRSpatialReference.SetFromUserInput()
	 * call, which includes EPSG PCS and GCSes (ie. EPSG:4296), PROJ.4
	 * declarations (as above), or the name of a .prf file containing well known
	 * text.
	 */
	private String inputSpatialReference;

	/**
	 * target spatial reference set. The coordinate systems that can be passed
	 * are anything supported by the OGRSpatialReference.SetFromUserInput()
	 * call, which includes EPSG PCS and GCSes (ie. EPSG:4296), PROJ.4
	 * declarations (as above), or the name of a .prf file containing well known
	 * text.
	 */
	private String outputSpatialReference;

	/**
	 * set a transformer option suitable to pass to
	 * GDALCreateGenImgProjTransformer2().
	 */
	private Map<String, String> transformerOptions;

	/**
	 * order of polynomial used for warping (1 to 3). The default is to select a
	 * polynomial order based on the number of GCPs.
	 */
	private Integer order;

	/**
	 * Force use of thin plate spline transformer based on available GCPs.
	 */
	private boolean forceThinPlateSplineTransformer = false;

	/**
	 * Force use of RPCs.
	 */
	private boolean forceRPCs = false;

	/**
	 * Force use of Geolocation Arrays.
	 */
	private boolean forceGeolocationArrays = false;

	/**
	 * error threshold for transformation approximation (in pixel units -
	 * defaults to 0.125).
	 */
	private Double errorTreshold = 0.125;

	/**
	 * (GDAL >= 1.9.0) refines the GCPs by automatically eliminating outliers.
	 * Outliers will be eliminated until minimum_gcps are left or when no
	 * outliers can be detected. The tolerance is passed to adjust when a GCP
	 * will be eliminated. Not that GCP refinement only works with polynomial
	 * interpolation. The tolerance is in pixel units if no projection is
	 * available, otherwise it is in SRS units. If minimum_gcps is not provided,
	 * the minimum GCPs according to the polynomial model is used.
	 */
	private Double toleranceMinimumGCPs;

	/**
	 * set georeferenced extents of output file to be created (in target SRS).
	 */
	private GeoreferencedExtents georeferencedExtents;

	/**
	 * set output file resolution (in target georeferenced units)
	 */
	private FileResolution outputFileResolution;

	/**
	 * (GDAL >= 1.8.0) (target aligned pixels) align the coordinates of the
	 * extent of the output file to the values of the -tr, such that the aligned
	 * extent includes the minimum extent.
	 */
	private boolean targetAlignedPixels = false;

	/**
	 * set output file size in pixels and lines. If width or height is set to 0,
	 * the other dimension will be guessed from the computed resolution. Note
	 * that -ts cannot be used with -tr
	 */
	private FileSize outputFileSize;

	/**
	 * Set a warp options. The GDALWarpOptions::papszWarpOptions docs show all
	 * options. Multiple -wo options may be listed.
	 */
	private Map<WARPOPTION, String> warpOptions;

	/**
	 * For the output bands to be of the indicated data type.
	 */
	private String outputBandsType;

	/**
	 * Working pixel data type. The data type of pixels in the source image and
	 * destination image buffers.
	 */
	private String workingPixelDataType;

	/**
	 * Resampling method to use.
	 */
	private RESAMPLING resampling;

	/**
	 * Set nodata masking values for input bands (different values can be
	 * supplied for each band). If more than one value is supplied all values
	 * should be quoted to keep them together as a single operating system
	 * argument. Masked values will not be used in interpolation. Use a value of
	 * None to ignore intrinsic nodata settings on the source dataset.
	 */
	private Map<String, List<String>> inputNoData;

	/**
	 * Set nodata values for output bands (different values can be supplied for
	 * each band). If more than one value is supplied all values should be
	 * quoted to keep them together as a single operating system argument. New
	 * files will be initialized to this value and if possible the nodata value
	 * will be recorded in the output file.
	 */
	private Map<String, List<String>> outputNoData;

	/**
	 * Create an output alpha band to identify nodata (unset/transparent)
	 * pixels.
	 */
	private boolean outputAlphaBand = false;

	/**
	 * Set the amount of memory (in megabytes) that the warp API is allowed to
	 * use for caching.
	 */
	private Integer cacheMemory;

	/**
	 * Use multithreaded warping implementation. Multiple threads will be used
	 * to process chunks of image and perform input/output operation
	 * simultaneously.
	 */
	private boolean multithread = false;

	/**
	 * Be quiet.
	 */
	private boolean quiet = false;

	/**
	 * Select the output format. The default is GeoTIFF (GTiff). Use the short
	 * format name.
	 */
	private FORMAT outputFormat = FORMAT.GTiff;

	/**
	 * passes a creation option to the output format driver. Multiple -co
	 * options may be listed. See format specific documentation for legal
	 * creation options for each format.
	 */
	private Map<String, String> creationOption;

	/**
	 * Enable use of a blend cutline from the name OGR support datasource.
	 */
	private String cutlineDatasource;

	/**
	 * Select the named layer from the cutline datasource.
	 */
	private String cutlineLayer;

	/**
	 * Restrict desired cutline features based on attribute query.
	 */
	private String cutlineWhere;

	/**
	 * Select cutline features using an SQL query instead of from a layer with
	 * -cl.
	 */
	private String cutlineSQL;

	/**
	 * Set a blend distance to use to blend over cutlines (in pixels).
	 */
	private String cutlineBlendDistance;

	/**
	 * (GDAL >= 1.8.0) Crop the extent of the target dataset to the extent of
	 * the cutline.
	 */
	private boolean cropToCutline = false;

	/**
	 * (GDAL >= 1.8.0) Overwrite the target dataset if it already exists.
	 */
	private boolean overwrite = false;

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

	public String getInputSpatialReference() {
		return inputSpatialReference;
	}

	public void setInputSpatialReference(String inputSpatialReference) {
		this.inputSpatialReference = inputSpatialReference;
	}

	public String getOutputSpatialReference() {
		return outputSpatialReference;
	}

	public void setOutputSpatialReference(String outputSpatialReference) {
		this.outputSpatialReference = outputSpatialReference;
	}

	public Map<String, String> getTransformerOptions() {
		return transformerOptions;
	}

	public void setTransformerOptions(Map<String, String> transformerOptions) {
		this.transformerOptions = transformerOptions;
	}
	
	public void addTransformerOption(String option, String value) {
		if (this.transformerOptions == null)
			this.transformerOptions = new HashMap<String, String>();
		this.transformerOptions.put(option, value);
	}

	public Integer getOrder() {
		return order;
	}

	public void setOrder(Integer order) {
		this.order = order;
	}

	public boolean forceThinPlateSplineTransformer() {
		return forceThinPlateSplineTransformer;
	}

	public void forceThinPlateSplineTransformer(boolean force) {
		this.forceThinPlateSplineTransformer = force;
	}

	public boolean forceRPCs() {
		return forceRPCs;
	}

	public void forceRPCs(boolean force) {
		this.forceRPCs = force;
	}

	public boolean forceGeolocationArrays() {
		return forceGeolocationArrays;
	}

	public void forceGeolocationArrays(boolean force) {
		this.forceGeolocationArrays = force;
	}

	public Double getErrorTreshold() {
		return errorTreshold;
	}

	public void setErrorTreshold(Double errorTreshold) {
		this.errorTreshold = errorTreshold;
	}

	public Double getToleranceMinimumGCPs() {
		return toleranceMinimumGCPs;
	}

	public void setToleranceMinimumGCPs(Double toleranceMinimumGCPs) {
		this.toleranceMinimumGCPs = toleranceMinimumGCPs;
	}

	public GeoreferencedExtents getGeoreferencedExtents() {
		return georeferencedExtents;
	}

	public void setGeoreferencedExtents(GeoreferencedExtents georeferencedExtents) {
		this.georeferencedExtents = georeferencedExtents;
	}

	public FileResolution getOutputFileResolution() {
		return outputFileResolution;
	}

	public void setOutputFileResolution(FileResolution outputFileResolution) {
		this.outputFileResolution = outputFileResolution;
	}

	public boolean targetAlignedPixels() {
		return targetAlignedPixels;
	}

	public void targetAlignedPixels(boolean targetAlignedPixels) {
		this.targetAlignedPixels = targetAlignedPixels;
	}

	public FileSize getOutputFileSize() {
		return outputFileSize;
	}

	public void setOutputFileSize(FileSize outputFileSize) {
		this.outputFileSize = outputFileSize;
	}

	public Map<WARPOPTION, String> getWarpOptions() {
		return warpOptions;
	}

	public void setWarpOptions(Map<WARPOPTION, String> warpOptions) {
		this.warpOptions = warpOptions;
	}
	
	public void addWarpOption(WARPOPTION option, String value) {
		if (this.warpOptions == null)
			this.warpOptions = new HashMap<WARPOPTION, String>();
		this.warpOptions.put(option, value);
	}

	public String getOutputBandsType() {
		return outputBandsType;
	}

	public void setOutputBandsType(String outputBandsType) {
		this.outputBandsType = outputBandsType;
	}

	public String getWorkingPixelDataType() {
		return workingPixelDataType;
	}

	public void setWorkingPixelDataType(String workingPixelDataType) {
		this.workingPixelDataType = workingPixelDataType;
	}

	public RESAMPLING getResampling() {
		return resampling;
	}

	public void setResampling(RESAMPLING resampling) {
		this.resampling = resampling;
	}

	public Map<String, List<String>> getInputNoData() {
		return inputNoData;
	}

	public void setInputNoData(Map<String, List<String>> inputNoData) {
		this.inputNoData = inputNoData;
	}
	
	public void addInputNoData(String key, List<String> values) {
		if (this.inputNoData == null)
			this.inputNoData = new HashMap<String, List<String>>();
		this.inputNoData.put(key, values);
	}

	public Map<String, List<String>> getOutputNoData() {
		return outputNoData;
	}

	public void setOutputNoData(Map<String, List<String>> outputNoData) {
		this.outputNoData = outputNoData;
	}
	
	public void addOutputNoData(String key, List<String> values) {
		if (this.outputNoData == null)
			this.outputNoData = new HashMap<String, List<String>>();
		this.outputNoData.put(key, values);
	}

	public boolean outputAlphaBand() {
		return outputAlphaBand;
	}

	public void outputAlphaBand(boolean outputAlphaBand) {
		this.outputAlphaBand = outputAlphaBand;
	}

	public Integer getCacheMemory() {
		return cacheMemory;
	}

	public void setCacheMemory(Integer cacheMemory) {
		this.cacheMemory = cacheMemory;
	}

	public boolean multithread() {
		return multithread;
	}

	public void multithread(boolean multithread) {
		this.multithread = multithread;
	}

	public boolean quiet() {
		return quiet;
	}

	public void quiet(boolean quiet) {
		this.quiet = quiet;
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
	
	public void addCreationOption(String option, String value) {
		if (this.creationOption == null)
			this.creationOption = new HashMap<String, String>();
		this.creationOption.put(option, value);
	}

	public String getCutlineDatasource() {
		return cutlineDatasource;
	}

	public void setCutlineDatasource(String cutlineDatasource) {
		this.cutlineDatasource = cutlineDatasource;
	}

	public String getCutlineLayer() {
		return cutlineLayer;
	}

	public void setCutlineLayer(String cutlineLayer) {
		this.cutlineLayer = cutlineLayer;
	}

	public String getCutlineWhere() {
		return cutlineWhere;
	}

	public void setCutlineWhere(String cutlineWhere) {
		this.cutlineWhere = cutlineWhere;
	}

	public String getCutlineSQL() {
		return cutlineSQL;
	}

	public void setCutlineSQL(String cutlineSQL) {
		this.cutlineSQL = cutlineSQL;
	}

	public String getCutlineBlendDistance() {
		return cutlineBlendDistance;
	}

	public void setCutlineBlendDistance(String cutlineBlendDistance) {
		this.cutlineBlendDistance = cutlineBlendDistance;
	}

	public boolean cropToCutline() {
		return cropToCutline;
	}

	public void cropToCutline(boolean crop) {
		this.cropToCutline = crop;
	}

	public boolean overwrite() {
		return overwrite;
	}

	public void overwrite(boolean overwrite) {
		this.overwrite = overwrite;
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
		
		this.getSB().append("gdalwarp ");
		if (this.getInputSpatialReference() != null && !this.getInputSpatialReference().isEmpty())
			this.getSB().append("-s_srs ").append(this.getInputSpatialReference()).append(" ");
		if (this.getOutputSpatialReference() != null && !this.getOutputSpatialReference().isEmpty())
			this.getSB().append("-t_srs ").append(this.getOutputSpatialReference()).append(" ");
		if (this.getTransformerOptions() != null && !this.getTransformerOptions().isEmpty())
			for (String key : this.getTransformerOptions().keySet())
				this.getSB().append("-to \"").append(key).append("=").append(this.getTransformerOptions().get(key)).append("\" ");
		if (this.getOrder() != null) {
			if (this.getOrder() > 0 && this.getOrder() < 4) {
				this.getSB().append("-order ").append(this.getOrder()).append(" ");
			} else {
				throw new Exception("Order of polynomial used for warping must be between 1 and 3.");
			}
		}
		if (this.forceThinPlateSplineTransformer)
			this.getSB().append("-tps ");
		if (this.forceRPCs)
			this.getSB().append("-rpc ");
		if (this.forceGeolocationArrays)
			this.getSB().append("-geoloc ");
		if (this.getErrorTreshold() != null)
			this.getSB().append("-ec ").append(this.getErrorTreshold()).append(" ");
		if (this.getToleranceMinimumGCPs() != null)
			this.getSB().append("-refine_gcps ").append(this.getToleranceMinimumGCPs()).append(" ");
		if (this.getGeoreferencedExtents() != null)
			this.getSB().append("-te ").append(this.getGeoreferencedExtents()).append(" ");
		if (this.getOutputFileResolution() != null)
			this.getSB().append("-tr ").append(this.getOutputFileResolution()).append(" ");
		if (this.targetAlignedPixels)
			this.getSB().append("-tap ");
		if (this.getOutputFileSize() != null)
			this.getSB().append("-tr ").append(this.getOutputFileSize()).append(" ");
		if (this.getWarpOptions() != null && !this.getWarpOptions().isEmpty())
			for (WARPOPTION key : this.getWarpOptions().keySet())
				this.getSB().append("-wo \"").append(key.name()).append("=").append(this.getWarpOptions().get(key)).append("\" ");
		if (this.getOutputBandsType() != null && !this.getOutputBandsType().isEmpty())
			this.getSB().append("-ot ").append(this.getOutputBandsType()).append(" ");
		if (this.getWorkingPixelDataType() != null && !this.getWorkingPixelDataType().isEmpty())
			this.getSB().append("-wt ").append(this.getWorkingPixelDataType()).append(" ");
		if (this.getResampling() != null)
			this.getSB().append("-r ").append(this.getResampling().name()).append(" ");
		if (this.getInputNoData() != null && this.getInputNoData().isEmpty()) {
			for (String key : this.getInputNoData().keySet()) {
				this.getSB().append("-srcnodata ");
				for (String value : this.getInputNoData().get(key))
					this.getSB().append(value).append(" ");
			}
		}
		if (this.getOutputNoData() != null && this.getOutputNoData().isEmpty()) {
			for (String key : this.getOutputNoData().keySet()) {
				this.getSB().append("-dstnodata ");
				for (String value : this.getOutputNoData().get(key))
					this.getSB().append(value).append(" ");
			}
		}
		if (this.outputAlphaBand())
			this.getSB().append("-dstalpha ");
		if (this.getCacheMemory() != null)
			this.getSB().append("-wm ").append(this.getCacheMemory()).append(" ");
		if (this.multithread())
			this.getSB().append("-multi ");
		if (this.quiet())
			this.getSB().append("-q ");
		if (this.getOutputFormat() != null)
			this.getSB().append("-of ").append(this.getOutputFormat().name()).append(" ");
		if (this.getCreationOption() != null && !this.getCreationOption().isEmpty())
			for (String key : this.getCreationOption().keySet())
				this.getSB().append("-co \"").append(key).append("=").append(this.getTransformerOptions().get(key)).append("\" ");
		if (this.getCutlineDatasource() != null && !this.getCutlineDatasource().isEmpty())
			this.getSB().append("-cutline ").append(this.getCutlineDatasource()).append(" ");
		if (this.getCutlineLayer() != null && !this.getCutlineLayer().isEmpty())
			this.getSB().append("-cl ").append(this.getCutlineLayer()).append(" ");
		if (this.getCutlineWhere() != null && !this.getCutlineWhere().isEmpty())
			this.getSB().append("-cwhere ").append(this.getCutlineWhere()).append(" ");
		if (this.getCutlineSQL() != null && !this.getCutlineSQL().isEmpty())
			this.getSB().append("-csql ").append(this.getCutlineSQL()).append(" ");
		if (this.getCutlineBlendDistance() != null && !this.getCutlineBlendDistance().isEmpty())
			this.getSB().append("-cblend ").append(this.getCutlineBlendDistance()).append(" ");
		if (this.cropToCutline())
			this.getSB().append("-crop_to_cutline ");
		if (this.overwrite())
			this.getSB().append("-overwrite ");
		if (this.getInputFilepaths().isEmpty()) {
			throw new Exception("No input data sources have been defined.");
		} else {
			for (String i : this.getInputFilepaths())
				this.getSB().append(i).append(" ");
		}
		if (this.getOutputFilepath() != null && !this.getOutputFilepath().isEmpty()) {
			this.getSB().append(this.getOutputFilepath()).append(" ");
		} else {
			throw new Exception("No output file has been defined.");
		}
		
		// configuration options
		if (this.getConfig() != null && !this.getConfig().isEmpty())
			for (CONFIG key : this.getConfig().keySet())
				this.getSB().append("--config ").append(key.name()).append(" ").append(this.getConfig().get(key)).append(" ");

		return this.getSB().toString();
	}

}