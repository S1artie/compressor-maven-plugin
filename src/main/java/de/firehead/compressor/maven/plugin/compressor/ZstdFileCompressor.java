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

import io.airlift.compress.zstd.ZstdCompressor;
import io.airlift.compress.zstd.ZstdHadoopStreams;

public class ZstdFileCompressor extends AbstractAdaptableFileCompressor {

	@Override
	protected void compressFileBlock(File aSourceFile, File aTargetFile, Integer aCompressionLevel) throws IOException {
		compressBlock(aSourceFile, aTargetFile, new ZstdCompressor());
	}

	@Override
	protected void compressFileStreaming(File aSourceFile, File aTargetFile, Integer aCompressionLevel)
			throws IOException {
		compressStreaming(aSourceFile, aTargetFile, out -> {
			try {
				return new ZstdHadoopStreams().createOutputStream(out);
			} catch (IOException e) {
				// cannot happen
				throw new RuntimeException();
			}
		});
	}

}
