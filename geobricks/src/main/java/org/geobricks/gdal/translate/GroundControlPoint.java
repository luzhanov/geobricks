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
package org.geobricks.gdal.translate;

/**
 * 
 * @author <a href="mailto:guido.barbaglia@gmail.com">Guido Barbaglia</a> 
 *
 */
public class GroundControlPoint {

	private String pixel;
	
	private String line;
	
	private String easting;
	
	private String northing;

	public GroundControlPoint(String pixel, String line, String easting, String northing) {
		super();
		this.setPixel(pixel);
		this.setLine(line);
		this.setEasting(easting);
		this.setNorthing(northing);
	}

	public String getPixel() {
		return pixel;
	}

	public void setPixel(String pixel) {
		this.pixel = pixel;
	}

	public String getLine() {
		return line;
	}

	public void setLine(String line) {
		this.line = line;
	}

	public String getEasting() {
		return easting;
	}

	public void setEasting(String easting) {
		this.easting = easting;
	}

	public String getNorthing() {
		return northing;
	}

	public void setNorthing(String northing) {
		this.northing = northing;
	}
	
}