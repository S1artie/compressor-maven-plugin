/* compressor-maven-plugin
 *
 * Copyright (C) 2023
 *
 * This software may be modified and distributed under the terms
 * of the MIT license.  See the LICENSE file for details.
 */

package de.firehead.compressor.maven.plugin.compressor;

import java.io.File;
import java.io.IOException;

public interface FileCompressor {

	/**
	 * Compresses the given file.
	 * 
	 * @param aFile             the file to compress
	 * @param aSuffix           the suffix to append to the file name for the
	 *                          compressed version (if null, replace the original
	 *                          file with the compressed version)
	 * @param aCompressionLevel the compression level (null = use default)
	 * @return the target file name
	 * @throws IOException
	 */
	public File compressFile(File aFile, String aSuffix, Integer aCompressionLevel) throws IOException;

}
