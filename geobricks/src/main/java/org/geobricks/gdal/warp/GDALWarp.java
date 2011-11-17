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

import java.util.List;
import java.util.Map;

import org.geobricks.gdal.GDAL;
import org.geobricks.gdal.constant.CONFIG;

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

	private List<String> inputFilepath;
	
	private String inputSpatialReference;
	
	private String outputSpatialReference;
	
	private Map<String, String> transformerOption;
	
	private Integer order;
	
	private boolean forceThinPlateSplineTransformer = false;
	
	private boolean forceRPCs = false;
	
	private boolean forceGeolocationArrays = false;
	
	private Double errorTreshold = 0.125;
	
	private Double toleranceMinimumGCPs;
	
	private GeoreferencedExtents georeferencedExtents;
	
	private FileResolution outputFileResolution;
	
	private boolean targetAlignedPixels = false;
	
	private FileSize outputFileSize;

	@Override
	public String convert() throws Exception {

		// generic section
		if (this.getScript() != null && !this.getScript().isEmpty()) {
			return this.getScript();
		} else if (this.showHelp()) {
			this.getSB().append("gdalinfo --help");
			return this.getSB().toString();
		}
		if (this.getConfig() != null && !this.getConfig().isEmpty())
			for (CONFIG key : this.getConfig().keySet())
				this.getSB().append("--config ").append(key.name()).append(" ").append(this.getConfig().get(key)).append(" ");

		return this.getSB().toString();
	}

}