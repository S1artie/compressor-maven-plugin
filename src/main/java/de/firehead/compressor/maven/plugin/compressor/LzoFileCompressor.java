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

import io.airlift.compress.lzo.LzoCompressor;
import io.airlift.compress.lzo.LzoHadoopStreams;

public class LzoFileCompressor extends AbstractAdaptableFileCompressor {

	@Override
	protected void compressFileBlock(File aSourceFile, File aTargetFile, Integer aCompressionLevel) throws IOException {
		compressBlock(aSourceFile, aTargetFile, new LzoCompressor());
	}

	@Override
	protected void compressFileStreaming(File aSourceFile, File aTargetFile, Integer aCompressionLevel)
			throws IOException {
		compressStreaming(aSourceFile, aTargetFile, out -> new LzoHadoopStreams().createOutputStream(out));
	}

}
