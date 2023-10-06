/* compressor-maven-plugin
 *
 * Copyright (C) 2023
 *
 * This software may be modified and distributed under the terms
 * of the MIT license.  See the LICENSE file for details.
 */

package de.firehead.compressor.maven.plugin;

import de.firehead.compressor.maven.plugin.compressor.Bzip2FileCompressor;
import de.firehead.compressor.maven.plugin.compressor.FileCompressor;
import de.firehead.compressor.maven.plugin.compressor.Lz4FileCompressor;
import de.firehead.compressor.maven.plugin.compressor.LzoFileCompressor;
import de.firehead.compressor.maven.plugin.compressor.SnappyFileCompressor;
import de.firehead.compressor.maven.plugin.compressor.ZstdFileCompressor;
import de.firehead.compressor.maven.plugin.compressor.ZstdJniFileCompressor;

public enum CompressionAlgorithm {

	BZIP2(new Bzip2FileCompressor(), ".bz2"),

	LZ4(new Lz4FileCompressor(), ".lz4"),

	LZO(new LzoFileCompressor(), ".lzo"),

	SNAPPY(new SnappyFileCompressor(), ".snappy"),

	ZSTD(new ZstdJniFileCompressor(), ".zstd"),

	ZSTD_NOJNI(new ZstdFileCompressor(), ".zstd");

	private FileCompressor fileCompressor;

	private String suffix;

	private CompressionAlgorithm(FileCompressor aFileCompressor, String aSuffix) {
		fileCompressor = aFileCompressor;
		suffix = aSuffix;
	}

	public FileCompressor getFileCompressor() {
		return fileCompressor;
	}

	public String getSuffix() {
		return suffix;
	}

}
