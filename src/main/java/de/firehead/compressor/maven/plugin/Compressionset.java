/* compressor-maven-plugin
 *
 * Copyright (C) 2023
 *
 * This software may be modified and distributed under the terms
 * of the MIT license.  See the LICENSE file for details.
 */

package de.firehead.compressor.maven.plugin;

/**
 * Defines a {@link Fileset} and corresponding compression parameters to apply
 * to the matching files in the fileset.
 */
public class Compressionset {

	/**
	 * The compression algorithm to use.
	 * <p>
	 * Possible values are:
	 * <ul>
	 * <li>ZSTD</li>
	 * <li>BZIP2</li>
	 * <li>LZ4</li>
	 * <li>LZO</li>
	 * <li>SNAPPY</li>
	 * <li>ZSTD_NOJNI</li>
	 * </ul>
	 */
	private CompressionAlgorithm algorithm;

	/**
	 * The compression level. If not provided, an algorithm-specific default is
	 * used. The meaning of the levels and the level ranges depend on the algorithm
	 * implementation; specifying a level may even be entirely unsupported by some
	 * algorithms.
	 */
	private Integer compressionLevel;

	/**
	 * The fileset to compress.
	 */
	private Fileset[] filesets;

	/**
	 * Whether to add a (compression-algorithm-specific) suffix like .bz2 oder .zstd
	 * to compressed files, or to overwrite the original files instead. Defaults to
	 * false, which means that the original files are replaced with compressed
	 * versions of the files under the same name.
	 */
	private boolean addSuffix;

	public CompressionAlgorithm getAlgorithm() {
		return algorithm;
	}

	public Fileset[] getFilesets() {
		return filesets;
	}

	public boolean isAddSuffix() {
		return addSuffix;
	}

	public Integer getCompressionLevel() {
		return compressionLevel;
	}

}
