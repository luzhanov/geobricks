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

import java.io.File;
import java.io.IOException;
import java.util.List;

import junit.framework.TestCase;

import org.geobricks.gdal.beans.GDALInfo;

/**
 * 
 * @author <a href="mailto:guido.barbaglia@gmail.com">Guido Barbaglia</a> 
 *
 */
public class GDALConnectorTest extends TestCase {

	public void testInvokeGDALInfo() {
		try {
			GDALInfo g = new GDALInfo();
			String p = getFilePath("long_beach-e.dem");
			g.setFilepath(p);
			g.metadata(false);
			GDALConnector c = new GDALConnector();
			List<String> l = c.invoke(g);
			for (String s : l)
				System.out.println(s);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public String getFilePath(String filename) {
		String p = "";
		File f = new File(filename);
		p = f.getAbsolutePath();
		int idx = p.lastIndexOf(File.separator);
		p = p.substring(0, idx);
		p = p + File.separator + "src" + File.separator + "test" + File.separator + "resources" + File.separator + "layers" + File.separator + "raster" + File.separator + filename;
		return p;
	}
	
}