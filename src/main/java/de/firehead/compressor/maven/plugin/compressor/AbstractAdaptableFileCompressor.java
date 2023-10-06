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

public abstract class AbstractAdaptableFileCompressor extends AbstractFileCompressor {

	/**
	 * Maximum file size to be processed as a block. Larger files are processed in a
	 * streaming fashion.
	 * <p>
	 * Programmatic default can be overridden via
	 * de.firehead.compressor.maven.plugin.maxSizeForBlockProcessing.
	 */
	private static final long DEFAULT_MAX_FILE_SIZE_FOR_BLOCK_PROCESSING = Long.parseLong(System.getProperty(
			"de.firehead.compressor.maven.plugin.maxSizeForBlockProcessing", Integer.toString(1024 * 1024 * 1024)));

	@Override
	public void compressFileInternally(File aSourceFile, File aTargetFile, Integer aCompressionLevel)
			throws IOException {
		if (aSourceFile.length() <= getMaxFileSizeForBlockProcessing()) {
			compressFileBlock(aSourceFile, aTargetFile, aCompressionLevel);
		} else {
			compressFileStreaming(aSourceFile, aTargetFile, aCompressionLevel);
		}
	}

	protected long getMaxFileSizeForBlockProcessing() {
		return DEFAULT_MAX_FILE_SIZE_FOR_BLOCK_PROCESSING;
	}

	protected abstract void compressFileBlock(File aSourceFile, File aTargetFile, Integer aCompressionLevel)
			throws IOException;

	protected abstract void compressFileStreaming(File aSourceFile, File aTargetFile, Integer aCompressionLevel)
			throws IOException;

}
