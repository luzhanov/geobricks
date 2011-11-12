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
public class SubWindowCorners {

	private String upperLeftX;
	
	private String upperLeftY;
	
	private String lowerRightX;
	
	private String lowerRightY;
	
	public SubWindowCorners(String upperLeftX, String upperLeftY, String lowerRightX, String lowerRightY) {
		super();
		this.setUpperLeftX(upperLeftX);
		this.setUpperLeftY(upperLeftY);
		this.setLowerRightX(lowerRightX);
		this.setLowerRightY(lowerRightY);
	}

	public String getUpperLeftX() {
		return upperLeftX;
	}

	public void setUpperLeftX(String upperLeftX) {
		this.upperLeftX = upperLeftX;
	}

	public String getUpperLeftY() {
		return upperLeftY;
	}

	public void setUpperLeftY(String upperLeftY) {
		this.upperLeftY = upperLeftY;
	}

	public String getLowerRightX() {
		return lowerRightX;
	}

	public void setLowerRightX(String lowerRightX) {
		this.lowerRightX = lowerRightX;
	}

	public String getLowerRightY() {
		return lowerRightY;
	}

	public void setLowerRightY(String lowerRightY) {
		this.lowerRightY = lowerRightY;
	}
	
}