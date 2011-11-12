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
package org.geobricks.gdal;

import java.io.IOException;
import java.util.List;

import org.geobricks.gdal.info.GDALInfo;
import org.geobricks.test.GeoBricksTest;

/**
 * 
 * @author <a href="mailto:guido.barbaglia@gmail.com">Guido Barbaglia</a> 
 *
 */
public class GDALConnectorTest extends GeoBricksTest {

	public void testInvokeGDALInfo() {
		try {
			GDALInfo g = new GDALInfo();
			g.setInputFilepath(getFilePath("long_beach-e.dem"));
			g.metadata(false);
			GDALConnector c = new GDALConnector();
			List<String> l = c.invoke(g);
			print(l);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}