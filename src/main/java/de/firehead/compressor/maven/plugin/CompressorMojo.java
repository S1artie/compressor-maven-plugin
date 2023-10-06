/* compressor-maven-plugin
 *
 * Copyright (C) 2023
 *
 * This software may be modified and distributed under the terms
 * of the MIT license.  See the LICENSE file for details.
 */

package de.firehead.compressor.maven.plugin;

import java.io.File;
import java.io.IOException;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.codehaus.plexus.util.DirectoryScanner;

import de.firehead.compressor.maven.plugin.compressor.FileCompressor;

/**
 * Goal which compresses a bunch of files.
 */
@Mojo(name = "compress", defaultPhase = LifecyclePhase.PREPARE_PACKAGE)
public class CompressorMojo extends AbstractMojo {

	/**
	 * The compression sets define which files to compress and which algorithm to
	 * use.
	 */
	@Parameter
	private Compressionset[] compressionsets;

	/**
	 * Disables the plugin execution.
	 */
	@Parameter(defaultValue = "false")
	private boolean skip;

	public void execute() throws MojoExecutionException {
		if (skip) {
			getLog().info("Compressor is skipped.");
			return;
		}

		try {
			int numFiles = 0;
			for (Compressionset set : compressionsets) {
				numFiles += processCompressionSet(set);
			}
			getLog().info("Compressed " + numFiles + " files in total");
		} catch (IOException e) {
			throw new MojoExecutionException("Error processing compression", e);
		}
	}

	protected int processCompressionSet(Compressionset aSet) throws IOException {
		final CompressionAlgorithm algorithm = aSet.getAlgorithm();
		final FileCompressor compressor = algorithm.getFileCompressor();

		int numFilesTotal = 0;

		for (Fileset fileset : aSet.getFilesets()) {
			final DirectoryScanner scanner = new DirectoryScanner();
			scanner.setBasedir(fileset.getDirectory());
			scanner.setExcludes(fileset.getExcludes());
			scanner.setIncludes(fileset.getIncludes());
			scanner.setFollowSymlinks(fileset.isFollowSymlinks());

			scanner.scan();

			long totalSourceSize = 0;
			long totalTargetSize = 0;
			for (String includedFile : scanner.getIncludedFiles()) {
				final File sourceFile = new File(fileset.getDirectory(), includedFile);
				final long sourceFileSize = sourceFile.length();

				final File targetFile = compressor.compressFile(sourceFile,
						aSet.isAddSuffix() ? algorithm.getSuffix() : null, aSet.getCompressionLevel());
				final long targetFileSize = targetFile.length();

				totalSourceSize += sourceFileSize;
				totalTargetSize += targetFileSize;

				if (getLog().isDebugEnabled()) {
					getLog().debug("Compressed '" + sourceFile + "' to '" + targetFile + "' using " + algorithm + ": "
							+ sourceFileSize + " bytes -> " + targetFileSize + " bytes ("
							+ formatCompressionEfficiency(sourceFileSize, targetFileSize) + "%)");
				}
			}

			int numFiles = scanner.getIncludedFiles().length;
			getLog().info("Compressed " + numFiles + " files using " + algorithm + " at "
					+ formatCompressionEfficiency(totalSourceSize, totalTargetSize) + "%");
			numFilesTotal += numFiles;
		}

		return numFilesTotal;
	}

	protected String formatCompressionEfficiency(long aSourceSize, long aTargetSize) {
		return "" + (Math.round(((double) aTargetSize / (double) aSourceSize) * 10000.0) / 100.0);
	}
}
