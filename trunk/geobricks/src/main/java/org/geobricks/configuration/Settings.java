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
package org.geobricks.configuration;

public class Settings {

	private String geoserverIP = "";
	
	private String geoserverPORT = "";
	
	private String layersFolder = "";
	
	public Settings() {
		
	}

	public Settings(String geoserverIP, String geoserverPORT, String layersFolder) {
		super();
		this.setGeoserverIP(geoserverIP);
		this.setGeoserverPORT(geoserverPORT);
		this.setLayersFolder(layersFolder);
	}

	public String getGeoserverIP() {
		return geoserverIP;
	}

	public void setGeoserverIP(String geoserverIP) {
		this.geoserverIP = geoserverIP;
	}

	public String getGeoserverPORT() {
		return geoserverPORT;
	}

	public void setGeoserverPORT(String geoserverPORT) {
		this.geoserverPORT = geoserverPORT;
	}

	public String getLayersFolder() {
		return layersFolder;
	}

	public void setLayersFolder(String layersFolder) {
		this.layersFolder = layersFolder;
	}
	
}