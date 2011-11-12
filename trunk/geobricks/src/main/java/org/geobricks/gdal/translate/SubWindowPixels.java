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
public class SubWindowPixels {

	private String xOffset;
	
	private String yOffset;
	
	private String xSize;
	
	private String ySize;
	
	public SubWindowPixels(String xOffset, String yOffset, String xSize, String ySize) {
		super();
		this.setxOffset(xOffset);
		this.setyOffset(yOffset);
		this.setxSize(xSize);
		this.setySize(ySize);
	}

	public String getxOffset() {
		return xOffset;
	}

	public void setxOffset(String xOffset) {
		this.xOffset = xOffset;
	}

	public String getyOffset() {
		return yOffset;
	}

	public void setyOffset(String yOffset) {
		this.yOffset = yOffset;
	}

	public String getxSize() {
		return xSize;
	}

	public void setxSize(String xSize) {
		this.xSize = xSize;
	}

	public String getySize() {
		return ySize;
	}

	public void setySize(String ySize) {
		this.ySize = ySize;
	}
	
}