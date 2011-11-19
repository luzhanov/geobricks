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
package org.geobricks.gdal.constant;

/**
 * 
 * @author <a href="mailto:guido.barbaglia@gmail.com">Guido Barbaglia</a>
 * 
 *         Warp control options for use with GDALWarp
 * 
 */
public enum WARPOPTION {

	INIT_DEST, NO_DATA, WRITE_FLUSH, YES, NO, 
	SKIP_NOSOURCE, UNIFIED_SRC_NODATA, SAMPLE_GRID, SAMPLE_STEPS, SOURCE_EXTRA, 
	CUTLINE, CUTLINE_BLEND_DIST, CUTLINE_ALL_TOUCHED, TRUE, FALSE, OPTIMIZE_SIZE;

}