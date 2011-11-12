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
package org.geobricks.gdal.general;

import org.geobricks.gdal.FORMAT;
import org.geobricks.gdal.GDAL;

/**
 * 
 * @author <a href="mailto:guido.barbaglia@gmail.com">Guido Barbaglia</a> 
 *
 */
public class GDALFormat extends GDAL {

	private FORMAT format;
	
	public GDALFormat() {
		super();
	}
	
	public GDALFormat(String script) {
		super(script);
	}
	
	public GDALFormat(FORMAT format) {
		super();
		this.setFormat(format);
	}
	
	@Override
	public String convert() {
		return "gdalinfo --format " + this.getFormat().name();
	}

	public FORMAT getFormat() {
		return format;
	}

	public void setFormat(FORMAT format) {
		this.format = format;
	}
	
}