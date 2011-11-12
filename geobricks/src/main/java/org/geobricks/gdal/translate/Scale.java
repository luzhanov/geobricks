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
public class Scale {

	private String inputMin;
	
	private String inputMax;
	
	private String outputMin = "0";
	
	private String outputMax = "255";
	
	public Scale(String inputMin, String inputMax, String outputMin, String outputMax) {
		super();
		this.setInputMin(inputMin);
		this.setInputMax(inputMax);
		this.setOutputMin(outputMin);
		this.setOutputMax(outputMax);
	}
	
	public Scale(String outputMin, String outputMax) {
		super();
		this.setOutputMin(outputMin);
		this.setOutputMax(outputMax);
	}

	public String getInputMin() {
		return inputMin;
	}

	public void setInputMin(String inputMin) {
		this.inputMin = inputMin;
	}

	public String getInputMax() {
		return inputMax;
	}

	public void setInputMax(String inputMax) {
		this.inputMax = inputMax;
	}

	public String getOutputMin() {
		return outputMin;
	}

	public void setOutputMin(String outputMin) {
		this.outputMin = outputMin;
	}

	public String getOutputMax() {
		return outputMax;
	}

	public void setOutputMax(String outputMax) {
		this.outputMax = outputMax;
	}
	
}