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
package org.geobricks.gdal.merge;

/**
 * 
 * @author <a href="mailto:guido.barbaglia@gmail.com">Guido Barbaglia</a> 
 *
 */
public class PixelSize {

	private String xPixelSize;
	
	private String yPixelSize;
	
	public PixelSize(String xPixelSize, String yPixelSize) {
		super();
		this.setxPixelSize(xPixelSize);
		this.setyPixelSize(yPixelSize);
	}
	
	public PixelSize(String xPixelSize) {
		super();
		this.setxPixelSize(xPixelSize);
		this.setyPixelSize(xPixelSize);
	}

	public String getxPixelSize() {
		return xPixelSize;
	}

	public void setxPixelSize(String xPixelSize) {
		this.xPixelSize = xPixelSize;
	}

	public String getyPixelSize() {
		return yPixelSize;
	}

	public void setyPixelSize(String yPixelSize) {
		this.yPixelSize = yPixelSize;
	}
	
	@Override
	public String toString() {
		return this.getxPixelSize() + " " + this.getyPixelSize();
	}
	
}