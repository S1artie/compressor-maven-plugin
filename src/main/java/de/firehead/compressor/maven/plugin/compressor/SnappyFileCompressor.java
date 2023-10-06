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

import io.airlift.compress.snappy.SnappyCompressor;
import io.airlift.compress.snappy.SnappyHadoopStreams;

public class SnappyFileCompressor extends AbstractAdaptableFileCompressor {

	@Override
	protected void compressFileBlock(File aSourceFile, File aTargetFile, Integer aCompressionLevel) throws IOException {
		compressBlock(aSourceFile, aTargetFile, new SnappyCompressor());
	}

	@Override
	protected void compressFileStreaming(File aSourceFile, File aTargetFile, Integer aCompressionLevel)
			throws IOException {
		compressStreaming(aSourceFile, aTargetFile, out -> new SnappyHadoopStreams().createOutputStream(out));
	}

	@Override
	protected int getBlockCompressOutputBufferOversize(int anInputBufferLength) {
		// Snappy really needs large overhead buffer spaces (this calc was taken from
		// Snappy impl)
		return 40 + anInputBufferLength / 6;
	}

}
