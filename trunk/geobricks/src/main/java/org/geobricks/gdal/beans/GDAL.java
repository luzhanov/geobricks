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
public abstract class GDAL {

	private String filepath = "";
	
	private boolean help = false;

	public String getFilepath() {
		return filepath;
	}

	public void setFilepath(String filepath) {
		this.filepath = filepath;
	}

	public boolean showHelp() {
		return help;
	}

	public void help(boolean show) {
		this.help = show;
	}	
	
}