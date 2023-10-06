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

import io.airlift.compress.lz4.Lz4Compressor;
import io.airlift.compress.lz4.Lz4HadoopStreams;

public class Lz4FileCompressor extends AbstractAdaptableFileCompressor {

	@Override
	protected void compressFileBlock(File aSourceFile, File aTargetFile, Integer aCompressionLevel) throws IOException {
		compressBlock(aSourceFile, aTargetFile, new Lz4Compressor());
	}

	@Override
	protected void compressFileStreaming(File aSourceFile, File aTargetFile, Integer aCompressionLevel)
			throws IOException {
		compressStreaming(aSourceFile, aTargetFile, out -> new Lz4HadoopStreams().createOutputStream(out));
	}

}
