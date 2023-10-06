/* compressor-maven-plugin
 *
 * Copyright (C) 2023
 *
 * This software may be modified and distributed under the terms
 * of the MIT license.  See the LICENSE file for details.
 */

package de.firehead.compressor.maven.plugin.compressor;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.function.Function;

import io.airlift.compress.Compressor;
import io.airlift.compress.hadoop.HadoopStreams;

/**
 * Base class for file compressors based on the "aircompressor" library and its
 * {@link Compressor} and {@link HadoopStreams} interfaces.
 */
public abstract class AbstractFileCompressor implements FileCompressor {

	/**
	 * The suffix used for temporary output files.
	 */
	protected static final String TEMP_FILE_SUFFIX = ".temp";

	/**
	 * The size of the streaming buffer for compression.
	 */
	protected static final int STREAM_COMPRESS_READ_BUFFER_SIZE = 65535;

	/**
	 * The additional number of bytes to add to the output buffer in order to ensure
	 * it's big enough, even if the input data actually grows during compression.
	 */
	protected static final int DEFAULT_BLOCK_COMPRESS_OUTPUT_BUFFER_OVERSIZE = 65535;

	protected int getBlockCompressOutputBufferOversize(int anInputBufferLength) {
		return DEFAULT_BLOCK_COMPRESS_OUTPUT_BUFFER_OVERSIZE;
	}

	protected void ensureFileExistsAndIsReadable(File aFile) throws IOException {
		if (!aFile.exists()) {
			throw new FileNotFoundException("File does not exist: " + aFile.getAbsolutePath());
		}
		if (!aFile.isFile()) {
			throw new IOException("File is not a file: " + aFile.getAbsolutePath());
		}
		if (!aFile.canRead()) {
			throw new IOException("File is not readable: " + aFile.getAbsolutePath());
		}
	}

	protected void ensureDirectoryContainingFileIsWritable(File aFile) throws IOException {
		final File parentDir = aFile.getParentFile();

		if (!parentDir.isDirectory()) {
			throw new IOException("Parent directory is not a directory: " + parentDir.getAbsolutePath());
		}
		if (!parentDir.canWrite()) {
			throw new IOException("Cannot write into directory: " + parentDir.getAbsolutePath());
		}
	}

	protected void performAccessChecks(File aFile) throws IOException {
		ensureFileExistsAndIsReadable(aFile);
		ensureDirectoryContainingFileIsWritable(aFile);
	}

	protected byte[] readFileContents(File aFile) throws IOException {
		byte[] inputBuffer = new byte[(int) aFile.length()];

		final FileInputStream in = new FileInputStream(aFile);
		try {
			int tempPosition = 0;
			while (tempPosition < inputBuffer.length) {
				final int bytesRead = in.read(inputBuffer, tempPosition, inputBuffer.length - tempPosition);
				tempPosition += bytesRead;
			}
		} finally {
			in.close();
		}

		return inputBuffer;
	}

	protected FileOutputStream openFileForWriting(File aFile) throws IOException {
		return new FileOutputStream(aFile);
	}

	protected void writeFileContents(File aFile, byte[] someContent) throws IOException {
		writeFileContents(aFile, someContent, 0, someContent.length);
	}

	protected void writeFileContents(File aFile, byte[] someContent, int anOffset, int aLength) throws IOException {
		final FileOutputStream out = openFileForWriting(aFile);

		try {
			out.write(someContent, anOffset, aLength);
		} finally {
			out.close();
		}
	}

	protected void renameFileForced(File aSourceFile, File aTargetFile) throws IOException {
		ensureFileExistsAndIsReadable(aSourceFile);
		ensureDirectoryContainingFileIsWritable(aTargetFile);

		if (aTargetFile.exists()) {
			aTargetFile.delete();
		}

		aSourceFile.renameTo(aTargetFile);
	}

	@Override
	public File compressFile(File aFile, String aSuffix, Integer aCompressionLevel) throws IOException {
		performAccessChecks(aFile);

		if (aSuffix != null && !aSuffix.isEmpty()) {
			final File targetFile = new File(aFile.getAbsolutePath() + aSuffix);
			compressFileInternally(aFile, targetFile, aCompressionLevel);
			return targetFile;
		} else {
			final File targetFile = new File(aFile.getAbsolutePath() + TEMP_FILE_SUFFIX);
			compressFileInternally(aFile, targetFile, aCompressionLevel);
			renameFileForced(targetFile, aFile);
			return aFile;
		}
	}

	protected abstract void compressFileInternally(File aSourceFile, File aTargetFile, Integer aCompressionLevel)
			throws IOException;

	protected void compressBlock(File aSourceFile, File aTargetFile, Compressor aCompressor) throws IOException {
		byte[] inputBuffer = readFileContents(aSourceFile);
		byte[] outputBuffer = new byte[inputBuffer.length + getBlockCompressOutputBufferOversize(inputBuffer.length)];

		final int compressedBytes = aCompressor.compress(inputBuffer, 0, inputBuffer.length, outputBuffer, 0,
				outputBuffer.length);

		writeFileContents(aTargetFile, outputBuffer, 0, compressedBytes);
	}

	protected void compressStreaming(InputStream anInputStream, OutputStream anOutputStream) throws IOException {
		byte[] buffer = new byte[STREAM_COMPRESS_READ_BUFFER_SIZE];

		do {
			final int bytesRead = anInputStream.read(buffer);
			if (bytesRead < 0) {
				return;
			}
			anOutputStream.write(buffer, 0, bytesRead);
		} while (true);
	}

	protected void compressStreaming(File aSourceFile, File aTargetFile,
			Function<OutputStream, OutputStream> aStreamSupplier) throws IOException {
		final FileInputStream in = new FileInputStream(aSourceFile);
		try {
			final FileOutputStream out = new FileOutputStream(aTargetFile);
			try {
				final OutputStream hadoopOut = aStreamSupplier.apply(out);
				try {
					compressStreaming(in, hadoopOut);
				} finally {
					hadoopOut.close();
				}
			} finally {
				out.close();
			}
		} finally {
			in.close();
		}
	}

}
