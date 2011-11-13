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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.geobricks.gdal.constant.FORMAT;
import org.geobricks.gdal.general.GDALFormat;
import org.geobricks.gdal.general.GDALFormats;
import org.geobricks.gdal.info.GDALInfo;
import org.geobricks.gdal.translate.GDALTranslate;
import org.geobricks.test.GeoBricksTest;

/**
 * 
 * @author <a href="mailto:guido.barbaglia@gmail.com">Guido Barbaglia</a> 
 *
 */
public class GDALConnectorTest extends GeoBricksTest {
	
	private GDALConnector c = new GDALConnector();

	public void testGDALInfo() {
		try {
			GDALInfo g = new GDALInfo();
			g.setInputFilepath(getFilePath("long_beach-e.dem"));
			g.metadata(false);
			List<String> l = c.invoke(g);
			print(l);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void testGDALFormats() {
		try {
			GDALFormats g = new GDALFormats();
			List<String> l = c.invoke(g);
			print(l);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void testGDALFormat() {
		try {
			GDALFormat g = new GDALFormat(FORMAT.GTiff);
			List<String> l = c.invoke(g);
			print(l);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void testGDALTranslate() {
		
		try {
			
			// translate layer
			GDALTranslate g = new GDALTranslate(getFilePath("long_beach-e.dem"), "/home/kalimaha/Desktop/long_beach-e.tif");
			Map<String, String> creationOutput = new HashMap<String, String>();
			creationOutput.put("TILED", "YES");
			g.setCreationOption(creationOutput);
			List<String> l = c.invoke(g);
			print(l);
			
			// ask information about the new layer
			GDALInfo g2 = new GDALInfo();
			g2.setInputFilepath("/home/kalimaha/Desktop/long_beach-e.tif");
			l = c.invoke(g2);
			print(l);
			
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
}