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

/**
 * 
 * @author <a href="mailto:guido.barbaglia@gmail.com">Guido Barbaglia</a> 
 *
 */
public class GeoreferencedExtents {

	private String xMin;
	
	private String yMin;
	
	private String xMax;
	
	private String yMax;

	public GeoreferencedExtents(String xMin, String yMin, String xMax, String yMax) {
		super();
		this.setxMin(xMin);
		this.setyMin(yMin);
		this.setxMax(xMax);
		this.setyMax(yMax);
	}

	public String getxMin() {
		return xMin;
	}

	public void setxMin(String xMin) {
		this.xMin = xMin;
	}

	public String getyMin() {
		return yMin;
	}

	public void setyMin(String yMin) {
		this.yMin = yMin;
	}

	public String getxMax() {
		return xMax;
	}

	public void setxMax(String xMax) {
		this.xMax = xMax;
	}

	public String getyMax() {
		return yMax;
	}

	public void setyMax(String yMax) {
		this.yMax = yMax;
	}

}