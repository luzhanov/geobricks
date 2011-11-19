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
package org.geobricks.gdal.dem.tpi;

import org.geobricks.gdal.constant.CONFIG;
import org.geobricks.gdal.dem.GDALDEM;

/**
 * 
 * @author <a href="mailto:guido.barbaglia@gmail.com">Guido Barbaglia</a>
 * 
 *         This command outputs a single-band raster with values computed from
 *         the elevation. TPI stands for Topographic Position Index, which is
 *         defined as the difference between a central pixel and the mean of its
 *         surrounding cells (see Wilson et al 2007, Marine Geodesy 30:3-35).
 *         The value -9999 is used as the output nodata value. There are no
 *         specific options.
 * 
 */
public class GDALDEMTPI extends GDALDEM {

	public GDALDEMTPI(String inputFilepath, String outputFilepath) {
		super(inputFilepath, outputFilepath);
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
		this.getSB().append("gdaldem TPI ");

		// this section is common to all GDALDEM classes
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