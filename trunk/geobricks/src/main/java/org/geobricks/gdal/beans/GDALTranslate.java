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
package org.geobricks.gdal.beans;

/**
 * 
 * @author <a href="mailto:guido.barbaglia@gmail.com">Guido Barbaglia</a>
 * 
 */
public class GDALTranslate extends GDAL {
	
	private String outputType = "";
	
	private boolean strict = true;
	
	private String outputFormat = "";
	
	private String band = "";
	
	private String expand = "rgba";
	
	private String outputWidth = "";
	
	private String outputHeight = "";
	
	private String scaleInputMin = "";
	
	private String scaleInputMax = "";
	
	private String scaleOutputMin = "";
	
	private String scaleOutputMax = "";
	
	private boolean unscale = false;
	
	private String subwindowXOff = "";
	
	private String subwindowYOff = "";
	
	private String subwindowXSize = "";
	
	private String subwindowYSize = "";
	
	private String subwindowXStart = "";
	
	private String subwindowXEnd = "";
	
	private String subwindowYStart = "";
	
	private String subwindowYEnd = "";
	
	private String outputProjection = "";
	
	private String outputBoundULX = "";
	
	private String outputBoundULY = "";
	
	private String outputBoundLRX = "";
	
	private String outputBoundLRY = "";
	
	private String noDataValue = "";
	
	private String metadataKey = "";
	
	private String metadataValue = "";
	
	private String creationOption = "";
	
	private String groundControlPointPixel = "";
	
	private String groundControlPointLine = "";
	
	private String groundControlPointEasting = "";
	
	private String groundControlPointNorthing = "";
	
	private String groundControlPointElevation = "";
	
	private boolean progressMonitor = false;
	
	private boolean subDatasets2IndividualOutputs = false;

	public GDALTranslate() {
		super();
	}
	
}