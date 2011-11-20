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
package org.geobricks.gdal.gdal2tiles;

import org.geobricks.gdal.GDAL;
import org.geobricks.gdal.constant.CONFIG;
import org.geobricks.gdal.constant.PROFILE;
import org.geobricks.gdal.constant.RESAMPLING;
import org.geobricks.gdal.constant.WEBVIEWER;

/**
 * 
 * @author <a href="mailto:guido.barbaglia@gmail.com">Guido Barbaglia</a>
 * 
 *         This utility generates a directory with small tiles and metadata,
 *         following OSGeo Tile Map Service Specification. Simple web pages with
 *         viewers based on Google Maps and OpenLayers are generated as well -
 *         so anybody can comfortably explore your maps on-line and you do not
 *         need to install or configure any special software (like mapserver)
 *         and the map displays very fast in the webbrowser. You only need to
 *         upload generated directory into a web server. GDAL2Tiles creates also
 *         necessary metadata for Google Earth (KML SuperOverlay), in case the
 *         supplied map uses EPSG:4326 projection. World files and embedded
 *         georeference is used during tile generation, but you can publish a
 *         picture without proper georeference too.
 * 
 */
public class GDAL2Tiles extends GDAL {

	/**
	 * Tile cutting profile (mercator,geodetic,raster) - default 'mercator'
	 * (Google Maps compatible).
	 */
	private PROFILE profile;

	/**
	 * Resampling method
	 * (average,near,bilinear,cubic,cubicspline,lanczos,antialias) - default
	 * 'average'.
	 */
	private RESAMPLING resampling;

	/**
	 * The spatial reference system used for the source input data.
	 */
	private String spatialReferenceSystem;

	/**
	 * Zoom levels to render (format:'2-5' or '10').
	 */
	private String zoom;

	/**
	 * Resume mode. Generate only missing files.
	 */
	private boolean resume = false;

	/**
	 * NODATA transparency value to assign to the input data.
	 */
	private String noData;

	/**
	 * Generate verbose output of tile generation.
	 */
	private boolean verbose = false;

	/**
	 * Show help message and exit.
	 */
	private boolean help = false;

	/**
	 * Show program's version number and exit.
	 */
	private boolean version = false;

	/**
	 * Generate KML for Google Earth - default for 'geodetic' profile and
	 * 'raster' in EPSG:4326. For a dataset with different projection use with
	 * caution!
	 */
	private boolean forceKML = false;

	/**
	 * Avoid automatic generation of KML files for EPSG:4326.
	 */
	private boolean skipKML = false;

	/**
	 * URL address where the generated tiles are going to be published.
	 */
	private String publishURL;

	/**
	 * Web viewer to generate (all,google,openlayers,none) - default 'all'.
	 */
	private WEBVIEWER webViewer;

	/**
	 * Title of the map.
	 */
	private String title;

	/**
	 * Copyright for the map.
	 */
	private String copyright;

	/**
	 * Google Maps API key from http://code.google.com/apis/maps/signup.html.
	 */
	private String googleKey;

	/**
	 * Yahoo Application ID from http://developer.yahoo.com/wsregapp/.
	 */
	private String yahooKey;

	/**
	 * Output directory.
	 */
	private String outputDirectory;
	
	private boolean skipGoogleMaps = false;
	
	private boolean skipOpenLayers = false;
	
	public GDAL2Tiles(String inputFilepath, String outputDirectory) {
		this.setInputFilepath(inputFilepath);
		this.setOutputDirectory(outputDirectory);
	}
	
	public GDAL2Tiles(String inputFilepath) {
		this.setInputFilepath(inputFilepath);
	}

	public PROFILE getProfile() {
		return profile;
	}

	public void setProfile(PROFILE profile) {
		this.profile = profile;
	}

	public RESAMPLING getResampling() {
		return resampling;
	}

	public void setResampling(RESAMPLING resampling) {
		this.resampling = resampling;
	}

	public String getSpatialReferenceSystem() {
		return spatialReferenceSystem;
	}

	public void setSpatialReferenceSystem(String spatialReferenceSystem) {
		this.spatialReferenceSystem = spatialReferenceSystem;
	}

	public String getZoom() {
		return zoom;
	}

	public void setZoom(String zoom) {
		this.zoom = zoom;
	}

	public boolean resume() {
		return resume;
	}

	public void resume(boolean resume) {
		this.resume = resume;
	}

	public String getNoData() {
		return noData;
	}

	public void setNoData(String noData) {
		this.noData = noData;
	}

	public boolean verbose() {
		return verbose;
	}

	public void verbose(boolean verbose) {
		this.verbose = verbose;
	}

	public boolean help() {
		return help;
	}

	public void help(boolean help) {
		this.help = help;
	}

	public boolean version() {
		return version;
	}

	public void version(boolean version) {
		this.version = version;
	}

	public boolean forceKML() {
		return forceKML;
	}

	public void forceKML(boolean forceKML) {
		this.forceKML = forceKML;
	}

	public boolean skipKML() {
		return skipKML;
	}

	public void skipKML(boolean skipKML) {
		this.skipKML = skipKML;
	}

	public String getPublishURL() {
		return publishURL;
	}

	public void setPublishURL(String publishURL) {
		this.publishURL = publishURL;
	}

	public WEBVIEWER getWebViewer() {
		return webViewer;
	}

	public void setWebViewer(WEBVIEWER webViewer) {
		this.webViewer = webViewer;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getCopyright() {
		return copyright;
	}

	public void setCopyright(String copyright) {
		this.copyright = copyright;
	}

	public String getGoogleKey() {
		return googleKey;
	}

	public void setGoogleKey(String googleKey) {
		this.googleKey = googleKey;
	}

	public String getYahooKey() {
		return yahooKey;
	}

	public void setYahooKey(String yahooKey) {
		this.yahooKey = yahooKey;
	}

	public String getOutputDirectory() {
		return outputDirectory;
	}

	public void setOutputDirectory(String outputDirectory) {
		this.outputDirectory = outputDirectory;
	}
	
	public boolean skipGoogleMaps() {
		return skipGoogleMaps;
	}

	public void skipGoogleMaps(boolean skipGoogleMaps) {
		this.skipGoogleMaps = skipGoogleMaps;
	}

	public boolean skipOpenLayers() {
		return skipOpenLayers;
	}

	public void skipOpenLayers(boolean skipOpenLayers) {
		this.skipOpenLayers = skipOpenLayers;
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

		// GDALDEMSlope specific
		this.getSB().append("gdal2tiles.py ");
		if (this.getTitle() != null && this.getTitle().isEmpty())
			this.getSB().append("-title ").append(this.getTitle()).append(" ");
		if (this.getPublishURL() != null && this.getPublishURL().isEmpty())
			this.getSB().append("-publishurl ").append(this.getPublishURL()).append(" ");
		if (this.skipGoogleMaps())
			this.getSB().append("-nogooglemaps ");
		if (this.skipOpenLayers())
			this.getSB().append("-noopenlayers ");
		if (this.skipKML())
			this.getSB().append("-nokml ");
		if (this.getGoogleKey() != null && this.getGoogleKey().isEmpty())
			this.getSB().append("-googlemapskey ").append(this.getGoogleKey()).append(" ");
		if (this.forceKML())
			this.getSB().append("-forcekml ");
		if (this.verbose())
			this.getSB().append("-v ");
		if (this.getProfile() != null)
			this.getSB().append("-p ").append(this.getProfile().name()).append(" ");
		if (this.getResampling() != null)
			this.getSB().append("-r ").append(this.getResampling().name()).append(" ");
		if (this.getSpatialReferenceSystem() != null && this.getSpatialReferenceSystem().isEmpty())
			this.getSB().append("-s ").append(this.getSpatialReferenceSystem()).append(" ");
		if (this.getZoom() != null && this.getZoom().isEmpty())
			this.getSB().append("-z ").append(this.getZoom()).append(" ");
		if (this.resume())
			this.getSB().append("-e ");
		if (this.help())
			this.getSB().append("-h ");
		if (this.version())
			this.getSB().append("--version ");
		if (this.getWebViewer() != null)
			this.getSB().append("-w ").append(this.getWebViewer().name()).append(" ");
		if (this.getCopyright() != null && this.getCopyright().isEmpty())
			this.getSB().append("-c ").append(this.getCopyright()).append(" ");
		
		if (this.getInputFilepath() != null && this.getInputFilepath().isEmpty()) {
			this.getSB().append(this.getInputFilepath()).append(" ");
		} else {
			throw new Exception("No inout file has been defined.");
		}
		if (this.getOutputDirectory() != null && this.getOutputDirectory().isEmpty()) 
			this.getSB().append(this.getOutputDirectory()).append(" ");		

		// configuration options
		if (this.getConfig() != null && !this.getConfig().isEmpty())
			for (CONFIG key : this.getConfig().keySet())
				this.getSB().append("--config ").append(key.name()).append(" ").append(this.getConfig().get(key)).append(" ");

		return this.getSB().toString();
	}

}