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

import com.github.luben.zstd.Zstd;
import com.github.luben.zstd.ZstdOutputStream;

public class ZstdJniFileCompressor extends AbstractAdaptableFileCompressor {

	@Override
	protected void compressFileBlock(File aSourceFile, File aTargetFile, Integer aCompressionLevel) throws IOException {
		byte[] inputBuffer = readFileContents(aSourceFile);
		byte[] outputBuffer = new byte[inputBuffer.length + getBlockCompressOutputBufferOversize(inputBuffer.length)];

		final int compressedBytes = (int) Zstd.compress(outputBuffer, inputBuffer,
				filterCompressionLevel(aCompressionLevel));

		writeFileContents(aTargetFile, outputBuffer, 0, compressedBytes);
	}

	@Override
	protected void compressFileStreaming(File aSourceFile, File aTargetFile, Integer aCompressionLevel)
			throws IOException {
		compressStreaming(aSourceFile, aTargetFile, out -> {
			try {
				return new ZstdOutputStream(out, filterCompressionLevel(aCompressionLevel));
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		});
	}

	protected int filterCompressionLevel(Integer aCompressionLevel) {
		return aCompressionLevel != null ? aCompressionLevel : 3;
	}

}
