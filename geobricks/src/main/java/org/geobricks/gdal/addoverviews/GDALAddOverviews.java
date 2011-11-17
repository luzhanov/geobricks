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
package org.geobricks.gdal.addoverviews;

import java.util.ArrayList;
import java.util.List;

import org.geobricks.gdal.GDAL;
import org.geobricks.gdal.constant.CONFIG;
import org.geobricks.gdal.constant.RESAMPLING;

/**
 * 
 * @author <a href="mailto:guido.barbaglia@gmail.com">Guido Barbaglia</a>
 * 
 *         The GDALAddOverviews utility can be used to build or rebuild overview
 *         images for most supported file formats with one over several
 *         downsampling algorithms.
 * 
 */
public class GDALAddOverviews extends GDAL {

	/**
	 * Select a resampling algorithm.
	 */
	private RESAMPLING resampling = RESAMPLING.nearest;

	/**
	 * (available from GDAL 1.6.0) open the dataset in read-only mode, in order
	 * to generate external overview (for GeoTIFF especially).
	 */
	private boolean readOnly = false;

	/**
	 * (available from GDAL 1.7.0) remove all overviews.
	 */
	private boolean clean = false;

	/**
	 * A list of integral overview levels to build. Ignored with -clean option.
	 */
	private List<Integer> levels;

	public GDALAddOverviews(String inputFilepath, RESAMPLING resampling) {
		this.setInputFilepath(inputFilepath);
		this.resampling(resampling);
	}

	public GDALAddOverviews(String script) {
		super(script);
	}

	public RESAMPLING getResampling() {
		return resampling;
	}

	/**
	 * @param resampling
	 * 
	 *            Select a resampling algorithm.
	 */
	public void resampling(RESAMPLING resampling) {
		this.resampling = resampling;
	}

	public boolean isReadOnly() {
		return readOnly;
	}

	/**
	 * @param readOnly
	 * 
	 *            (available from GDAL 1.6.0) open the dataset in read-only
	 *            mode, in order to generate external overview (for GeoTIFF
	 *            especially).
	 */
	public void readOnly(boolean readOnly) {
		this.readOnly = readOnly;
	}

	public boolean clean() {
		return clean;
	}

	/**
	 * @param clean
	 * 
	 *            (available from GDAL 1.7.0) remove all overviews.
	 */
	public void clean(boolean clean) {
		this.clean = clean;
	}

	public List<Integer> getLevels() {
		return levels;
	}

	/**
	 * @param levels
	 * 
	 *            A list of integral overview levels to build. Ignored with
	 *            -clean option.
	 */
	public void setLevels(List<Integer> levels) {
		this.levels = levels;
	}

	/**
	 * @param level
	 * 
	 *            A list of integral overview levels to build. Ignored with
	 *            -clean option.
	 */
	public void buildLevel(Integer level) {
		if (this.levels == null)
			this.levels = new ArrayList<Integer>();
		this.levels.add(level);
	}

	@Override
	public String convert() throws Exception {
		this.getSB().append("gdaladdo ");
		if (this.getResampling() != null) {
			this.getSB().append("-r ").append(this.getResampling().name()).append(" ");
		} else {
			throw new Exception("Resampling algorithm is null or empty.");
		}
		if (this.clean())
			this.getSB().append("-clean ");
		if (this.isReadOnly())
			this.getSB().append("-ro ");
		if (this.getConfig() != null && !this.getConfig().isEmpty())
			for (CONFIG key : this.getConfig().keySet())
				this.getSB().append("--config ").append(key.name()).append(" ").append(this.getConfig().get(key)).append(" ");
		if (this.getInputFilepath() != null) {
			this.getSB().append(this.getInputFilepath()).append(" ");
		} else {
			throw new Exception("Input filepath is null or empty.");
		}
		if (!this.getLevels().isEmpty()) 
			for (Integer i : this.getLevels())
				this.getSB().append(i).append(" ");
		if (this.getScript() != null && !this.getScript().isEmpty()) {
			return this.getScript();
		} else if (this.showHelp()) {
			this.getSB().append("gdalinfo --help");
			return this.getSB().toString();
		}
		return this.getSB().toString();
	}

}