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

import org.geobricks.gdal.addoverviews.GDALAddOverviews;
import org.geobricks.gdal.constant.CONFIG;
import org.geobricks.gdal.constant.FORMAT;
import org.geobricks.gdal.constant.RESAMPLING;
import org.geobricks.gdal.general.GDALFormat;
import org.geobricks.gdal.general.GDALFormats;
import org.geobricks.gdal.info.GDALInfo;
import org.geobricks.gdal.translate.GDALTranslate;
import org.geobricks.gdal.warp.GDALWarp;
import org.geobricks.test.GeoBricksTest;

/**
 * 
 * @author <a href="mailto:guido.barbaglia@gmail.com">Guido Barbaglia</a>
 * 
 */
public class GDALConnectorTest extends GeoBricksTest {

	private GDALConnector c = new GDALConnector();

	public void _testGDALInfo() {
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

	public void _testGDALFormats() {
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

	public void _testGDALFormat() {
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

	public void _testGDALTranslate1() {
		try {
			GDALTranslate g = new GDALTranslate(getFilePath("long_beach-e.dem"), "/home/kalimaha/Desktop/long_beach-e.tif");
			g.setCreationOption("TILED", "YES");
			List<String> l = c.invoke(g);
			print(l);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Starting with GDAL 1.8.0, to create a JPEG-compressed TIFF with internal
	 * mask from a RGBA dataset
	 */
	public void _testGDALTranslate2() {
		try {
			GDALTranslate g = new GDALTranslate(getFilePath("marbles.tif"), "/home/kalimaha/Desktop/withmask.tif");
			g.addBand(1);
			g.addBand(2);
			g.addBand(3);
			g.setMask(4);
			g.setCreationOption("COMPRESS", "JPEG");
			g.setCreationOption("PHOTOMETRIC", "YCBCR");
			g.setConfig(CONFIG.GDAL_TIFF_INTERNAL_MASK, "YES");
			List<String> l = c.invoke(g);
			print(l);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Starting with GDAL 1.8.0, to create a RGBA dataset from a RGB dataset
	 * with a mask
	 */
	public void _testGDALTranslate3() {
		try {
			GDALTranslate g = new GDALTranslate(getFilePath("marbles.tif"), "/home/kalimaha/Desktop/withmask.tif");
			g.addBand(1);
			g.addBand(2);
			g.addBand(3);
			g.setMask(4);
			List<String> l = c.invoke(g);
			print(l);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create overviews, embedded in the supplied TIFF file:
	 */
	public void _testGDALAddOverviews1() {
		try {
			GDALAddOverviews g = new GDALAddOverviews(getFilePath("marbles.tif"), RESAMPLING.average);
			g.buildLevel(2);
			g.buildLevel(4);
			g.buildLevel(8);
			g.buildLevel(16);
			List<String> l = c.invoke(g);
			print(l);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create an external JPEG-compressed GeoTIFF overview file from a 3-band
	 * RGB dataset (if the dataset is a writable GeoTIFF, you also need to add
	 * the -ro option to force the generation of external overview):
	 */
	public void _testGDALAddOverviews3() {
		try {
			GDALAddOverviews g = new GDALAddOverviews(getFilePath("marbles.tif"), RESAMPLING.nearest);
			g.buildLevel(2);
			g.buildLevel(4);
			g.buildLevel(8);
			g.buildLevel(16);
			g.setConfig(CONFIG.COMPRESS_OVERVIEW, "JPEG");
			g.setConfig(CONFIG.INTERLEAVE_OVERVIEW, "PIXEL");
			g.setConfig(CONFIG.PHOTOMETRIC_OVERVIEW, "YCBCR");
			List<String> l = c.invoke(g);
			print(l);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create an Erdas Imagine format overviews for the indicated JPEG file:
	 */
	public void _testGDALAddOverviews4() {
		try {
			GDALAddOverviews g = new GDALAddOverviews(getFilePath("marbles.tif"), RESAMPLING.nearest);
			g.buildLevel(3);
			g.buildLevel(9);
			g.buildLevel(27);
			g.buildLevel(81);
			g.setConfig(CONFIG.USE_RRD, "YES");
			List<String> l = c.invoke(g);
			print(l);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * For instance, an eight bit spot scene stored in GeoTIFF with control
	 * points mapping the corners to lat/long could be warped to a UTM
	 * projection with a command like this:
	 */
	public void testGDALWarp1() {
		try {
			GDALWarp g = new GDALWarp(getFilePath("rapallo.tif"), "/home/kalimaha/Desktop/utm11.tif");
			g.setOutputSpatialReference("+proj=utm +zone=11 +datum=WGS84");
			List<String> l = c.invoke(g);
			print(l);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}