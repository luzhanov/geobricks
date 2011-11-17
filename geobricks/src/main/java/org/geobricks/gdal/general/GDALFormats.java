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

import org.geobricks.gdal.GDAL;
import org.geobricks.gdal.constant.CONFIG;

/**
 * 
 * @author <a href="mailto:guido.barbaglia@gmail.com">Guido Barbaglia</a>
 * 
 *         List all raster formats supported by this GDAL build (read-only and
 *         read-write) and exit. The format support is indicated as follows:
 *         'ro' is read-only driver; 'rw' is read or write (ie. supports
 *         CreateCopy); 'rw+' is read, write and update (ie. supports Create). A
 *         'v' is appended for formats supporting virtual IO (/vsimem, /vsigzip,
 *         /vsizip, etc). Note: The valid formats for the output of gdalwarp are
 *         formats that support the Create() method (marked as rw+), not just
 *         the CreateCopy() method.
 * 
 */
public class GDALFormats extends GDAL {

	public GDALFormats() {
		super();
	}

	public GDALFormats(String script) {
		super(script);
	}

	@Override
	public String convert() {
		
		this.getSB().append("gdalinfo --formats");
		
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