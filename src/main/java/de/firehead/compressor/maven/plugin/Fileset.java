/* compressor-maven-plugin
 *
 * Copyright (C) 2023
 *
 * This software may be modified and distributed under the terms
 * of the MIT license.  See the LICENSE file for details.
 */

package de.firehead.compressor.maven.plugin;

import java.io.File;
import java.util.Arrays;

/**
 * Defines a set of include and exclude patterns to select files and
 * subdirectories within a directory.
 */
public class Fileset {

	/**
	 * The directory to start from.
	 */
	private File directory;

	/**
	 * The include patterns. Not-included files are not part of the fileset.
	 * However, included files that are also excluded due to an exclude pattern are
	 * also not part of the fileset.
	 */
	private String[] includes;

	/**
	 * The exclude patterns. Excludes have precedence over includes: if a candidate
	 * file is included, but also excluded, it effectively is not part of the
	 * fileset.
	 */
	private String[] excludes;

	/**
	 * Whether to follow symbolic links.
	 */
	private boolean followSymlinks;

	/**
	 * Whether to use the default excludes (from
	 * {@link org.codehaus.plexus.util.AbstractScanner.DEFAULTEXCLUDES})
	 */
	private boolean useDefaultExcludes;

	/**
	 * @return {@link #directory}
	 */
	public File getDirectory() {
		return directory;
	}

	/**
	 * @return {@link #includes}
	 */
	public String[] getIncludes() {
		return (includes != null) ? includes : new String[0];
	}

	/**
	 * @return {@link #excludes}
	 */
	public String[] getExcludes() {
		return (excludes != null) ? excludes : new String[0];
	}

	/**
	 * @return {@link #followSymlinks}
	 */
	public boolean isFollowSymlinks() {
		return followSymlinks;
	}

	/**
	 * @return {@link #useDefaultExcludes}
	 */
	public boolean isUseDefaultExcludes() {
		return useDefaultExcludes;
	}

	/**
	 * Retrieves a string representation of the included and excluded files from
	 * this fileset's directory.
	 *
	 * @return a string representation of the included and excluded files from this
	 *         fileset's directory
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return "File Set: " + getDirectory() + " (included: " + Arrays.asList(getIncludes()) + ", excluded: "
				+ Arrays.asList(getExcludes()) + ")";
	}
}
