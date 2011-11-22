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
package org.geobricks.gdal.dem.transform;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.geobricks.gdal.GDAL;
import org.geobricks.gdal.constant.CONFIG;
import org.geobricks.gdal.general.GroundControlPoint;

/**
 * 
 * @author <a href="mailto:guido.barbaglia@gmail.com">Guido Barbaglia</a>
 * 
 */
public class GDALTransform extends GDAL {

	/**
	 * source spatial reference set. The coordinate systems that can be passed
	 * are anything supported by the OGRSpatialReference.SetFromUserInput()
	 * call, which includes EPSG PCS and GCSes (ie. EPSG:4296), PROJ.4
	 * declarations (as above), or the name of a .prf file containing well known
	 * text.
	 */
	private String inputSpatialReferenceSet;

	/**
	 * target spatial reference set. The coordinate systems that can be passed
	 * are anything supported by the OGRSpatialReference.SetFromUserInput()
	 * call, which includes EPSG PCS and GCSes (ie. EPSG:4296), PROJ.4
	 * declarations (as above), or the name of a .prf file containing well known
	 * text.
	 */
	private String outputSpatialReferenceSet;

	/**
	 * set a transformer option suitable to pass to
	 * GDALCreateGenImgProjTransformer2().
	 */
	private Map<String, String> transformerOptions;

	/**
	 * order of polynomial used for warping (1 to 3). The default is to select a
	 * polynomial order based on the number of GCPs.
	 */
	private Integer order;

	/**
	 * Force use of thin plate spline transformer based on available GCPs.
	 */
	private boolean forceThinSplineTransformer = false;

	/**
	 * Force use of RPCs.
	 */
	private boolean forceRPCs = false;

	/**
	 * Force use of Geolocation Arrays.
	 */
	private boolean forceGeolocationArrays = false;

	/**
	 * Inverse transformation: from destination to source.
	 */
	private boolean inverse = false;

	/**
	 * Provide a GCP to be used for transformation (generally three or more are
	 * required)
	 */
	private List<GroundControlPoint> groundControlPoints;

	public String getInputSpatialReferenceSet() {
		return inputSpatialReferenceSet;
	}

	/**
	 * @param inputSpatialReferenceSet
	 * 
	 *            source spatial reference set. The coordinate systems that can
	 *            be passed are anything supported by the
	 *            OGRSpatialReference.SetFromUserInput() call, which includes
	 *            EPSG PCS and GCSes (ie. EPSG:4296), PROJ.4 declarations (as
	 *            above), or the name of a .prf file containing well known text.
	 */
	public void setInputSpatialReferenceSet(String inputSpatialReferenceSet) {
		this.inputSpatialReferenceSet = inputSpatialReferenceSet;
	}

	public String getOutputSpatialReferenceSet() {
		return outputSpatialReferenceSet;
	}

	/**
	 * @param outputSpatialReferenceSet
	 * 
	 *            target spatial reference set. The coordinate systems that can
	 *            be passed are anything supported by the
	 *            OGRSpatialReference.SetFromUserInput() call, which includes
	 *            EPSG PCS and GCSes (ie. EPSG:4296), PROJ.4 declarations (as
	 *            above), or the name of a .prf file containing well known text.
	 */
	public void setOutputSpatialReferenceSet(String outputSpatialReferenceSet) {
		this.outputSpatialReferenceSet = outputSpatialReferenceSet;
	}

	public Map<String, String> getTransformerOptions() {
		return transformerOptions;
	}

	/**
	 * @param transformerOptions
	 * 
	 *            set a transformer option suitable to pass to
	 *            GDALCreateGenImgProjTransformer2().
	 */
	public void setTransformerOptions(Map<String, String> transformerOptions) {
		this.transformerOptions = transformerOptions;
	}

	public void addTransformerOption(String option, String value) {
		if (this.transformerOptions == null)
			this.transformerOptions = new HashMap<String, String>();
		this.transformerOptions.put(option, value);
	}

	public Integer getOrder() {
		return order;
	}

	/**
	 * @param order
	 * 
	 *            order of polynomial used for warping (1 to 3). The default is
	 *            to select a polynomial order based on the number of GCPs.
	 */
	public void setOrder(Integer order) {
		this.order = order;
	}

	public boolean forceThinSplineTransformer() {
		return forceThinSplineTransformer;
	}

	/**
	 * @param force
	 * 
	 *            Force use of thin plate spline transformer based on available
	 *            GCPs.
	 */
	public void forceThinSplineTransformer(boolean force) {
		this.forceThinSplineTransformer = force;
	}

	public boolean forceRPCs() {
		return forceRPCs;
	}

	/**
	 * @param force
	 * 
	 *            Force use of RPCs.
	 */
	public void forceRPCs(boolean force) {
		this.forceRPCs = force;
	}

	public boolean forceGeolocationArrays() {
		return forceGeolocationArrays;
	}

	/**
	 * @param force
	 * 
	 *            Force use of Geolocation Arrays.
	 */
	public void forceGeolocationArrays(boolean force) {
		this.forceGeolocationArrays = force;
	}

	public boolean inverse() {
		return inverse;
	}

	/**
	 * @param inverse
	 * 
	 *            Inverse transformation: from destination to source.
	 */
	public void inverse(boolean inverse) {
		this.inverse = inverse;
	}

	public List<GroundControlPoint> getGroundControlPoints() {
		return groundControlPoints;
	}

	/**
	 * @param groundControlPoint
	 * 
	 *            Provide a GCP to be used for transformation (generally three
	 *            or more are required)
	 */
	public void setGroundControlPoint(List<GroundControlPoint> groundControlPoints) {
		this.groundControlPoints = groundControlPoints;
	}
	
	/**
	 * @param groundControlPoint
	 * 
	 *            Provide a GCP to be used for transformation (generally three
	 *            or more are required)
	 */
	public void addGroundControlPoint(GroundControlPoint p) {
		if (this.groundControlPoints == null)
			this.groundControlPoints = new ArrayList<GroundControlPoint>();
		this.groundControlPoints.add(p);
	}
	
	@Override
	public String convert() throws Exception {

		// generic help
		if (this.getScript() != null && !this.getScript().isEmpty()) {
			return this.getScript();
		} else if (this.showHelp()) {
			this.getSB().append("gdalinfo --help");
			return this.getSB().toString();
		}

		// GDALDEMSlope specific
		this.getSB().append("gdaltransform ");
		if (this.inverse())
			this.getSB().append("-i ");
		if (this.getInputSpatialReferenceSet() != null && !this.getInputSpatialReferenceSet().isEmpty())
			this.getSB().append("-s_srs ").append(this.getInputSpatialReferenceSet()).append(" ");
		if (this.getOutputSpatialReferenceSet() != null && !this.getOutputSpatialReferenceSet().isEmpty())
			this.getSB().append("-t_srs ").append(this.getOutputSpatialReferenceSet()).append(" ");
		if (this.getTransformerOptions() != null && !this.getTransformerOptions().isEmpty()) 
			for (String key : this.getTransformerOptions().keySet())
				this.getSB().append("-to \"").append(key).append("=").append(this.getTransformerOptions().get(key)).append("\" ");
		if (this.getOrder() != null) {
			if (this.getOrder() > 0 && this.getOrder() < 4) {
				this.getSB().append("-order ").append(this.getOrder()).append(" ");
			} else {
				throw new Exception("Order must be between 1 and 3.");
			}
		}
		if (this.forceThinSplineTransformer())
			this.getSB().append("-tps ");
		if (this.forceRPCs())
			this.getSB().append("-rpc ");
		if (this.forceGeolocationArrays())
			this.getSB().append("-geoloc ");
		if (this.getGroundControlPoints() != null && !this.getGroundControlPoints().isEmpty())
			for (GroundControlPoint p : this.getGroundControlPoints())
				this.getSB().append("-gcp ").append(p).append(" ");
		if (this.getInputFilepath() != null && !this.getInputFilepath().isEmpty()) 
			this.getSB().append(this.getInputFilepath()).append(" ");
		if (this.getOutputFilepath() != null && !this.getOutputFilepath().isEmpty()) 
			this.getSB().append(this.getOutputFilepath()).append(" ");
		
		// configuration options
		if (this.getConfig() != null && !this.getConfig().isEmpty())
			for (CONFIG key : this.getConfig().keySet())
				this.getSB().append("--config ").append(key.name()).append(" ").append(this.getConfig().get(key)).append(" ");

		return this.getSB().toString();
	}

}