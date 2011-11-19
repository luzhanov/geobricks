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
package org.geobricks.gdal.dem.aspect;

import org.geobricks.gdal.constant.CONFIG;
import org.geobricks.gdal.dem.GDALDEM;

/**
 * 
 * @author <a href="mailto:guido.barbaglia@gmail.com">Guido Barbaglia</a>
 * 
 *         This command outputs a 32-bit float raster with values between 0° and
 *         360° representing the azimuth that slopes are facing. The definition
 *         of the azimuth is such that : 0° means that the slope is facing the
 *         North, 90° it's facing the East, 180° it's facing the South and 270°
 *         it's facing the West (provided that the top of your input raster is
 *         north oriented). The aspect value -9999 is used as the nodata value
 *         to indicate undefined aspect in flat areas with slope=0.
 * 
 */
public class GDALDEMAspect extends GDALDEM {

	/**
	 * return trigonometric angle instead of azimuth. Thus 0° means East, 90°
	 * North, 180° West, 270° South
	 */
	private boolean trigonometric = false;

	/**
	 * return 0 for flat areas with slope=0, instead of -9999
	 */
	private boolean zeroForFlat = false;

	public GDALDEMAspect(String inputFilepath, String outputFilepath) {
		super(inputFilepath, outputFilepath);
	}

	public boolean trigonometric() {
		return trigonometric;
	}

	/**
	 * return trigonometric angle instead of azimuth. Thus 0° means East, 90°
	 * North, 180° West, 270° South
	 */
	public void trigonometric(boolean trigonometric) {
		this.trigonometric = trigonometric;
	}

	public boolean zeroForFlat() {
		return zeroForFlat;
	}

	/**
	 * return 0 for flat areas with slope=0, instead of -9999
	 */
	public void zeroForFlat(boolean zeroForFlat) {
		this.zeroForFlat = zeroForFlat;
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

		// GDALDEMAspect specific
		if (this.trigonometric())
			this.getSB().append("-trigonometric ");
		if (this.zeroForFlat())
			this.getSB().append("-zero_for_flat ");

		// this section is common to all GDALDEM classes
		this.getSB().append("gdaldem aspect ");
		if (this.getInputFilepath() != null && !this.getInputFilepath().isEmpty()) {
			this.getSB().append(this.getInputFilepath()).append(" ");
		} else {
			throw new Exception("Input raster has not been defined.");
		}
		if (this.getOutputFilepath() != null && !this.getOutputFilepath().isEmpty()) {
			this.getSB().append(this.getOutputFilepath()).append(" ");
		} else {
			throw new Exception("Output raster has not been defined.");
		}
		if (this.getAlgorithm() != null && !this.getAlgorithm().isEmpty())
			this.getSB().append("-alg ").append(this.getAlgorithm()).append(" ");
		if (this.computeEdges())
			this.getSB().append("-compute_edges ");
		if (this.getBand() != null)
			this.getSB().append("-b ").append(this.getBand()).append(" ");
		if (this.getOutputFormat() != null)
			this.getSB().append("-of ").append(this.getOutputFormat().name()).append(" ");
		if (this.getCreationOption() != null && !this.getCreationOption().isEmpty())
			for (String key : this.getCreationOption().keySet())
				this.getSB().append("-co \"").append(key).append("=").append(this.getCreationOption().get(key)).append("\" ");
		if (this.suppressProgressMonitor())
			this.getSB().append("-q ");

		// configuration options
		if (this.getConfig() != null && !this.getConfig().isEmpty())
			for (CONFIG key : this.getConfig().keySet())
				this.getSB().append("--config ").append(key.name()).append(" ").append(this.getConfig().get(key)).append(" ");

		return this.getSB().toString();
	}

}