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

import io.airlift.compress.bzip2.BZip2HadoopStreams;

public class Bzip2FileCompressor extends AbstractFileCompressor {

	@Override
	public void compressFileInternally(File aSourceFile, File aTargetFile, Integer aCompressionLevel)
			throws IOException {
		compressStreaming(aSourceFile, aTargetFile, out -> new BZip2HadoopStreams().createOutputStream(out));
	}

}
