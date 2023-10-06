/* compressor-maven-plugin
 *
 * Copyright (C) 2023
 *
 * This software may be modified and distributed under the terms
 * of the MIT license.  See the LICENSE file for details.
 */

package de.firehead.compressor.maven.plugin;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.File;

import org.apache.maven.plugin.logging.SystemStreamLog;
import org.apache.maven.plugin.testing.MojoRule;
import org.apache.maven.plugin.testing.WithoutMojo;
import org.junit.Rule;
import org.junit.Test;

public class CompressorTests {
	@Rule
	public MojoRule rule = new MojoRule() {
		@Override
		protected void before() throws Throwable {
		}

		@Override
		protected void after() {
		}
	};

	protected void runTest(String aTestProjectName, String aFileSuffix) throws Exception {
		final File pomRoot = getPom(aTestProjectName);
		final CompressorMojo myMojo = createMojo(pomRoot);

		myMojo.execute();

		File inputFile = new File(pomRoot, "test-data/dummyFile.txt");
		assertTrue(inputFile.exists());
		File outputFile = new File(pomRoot, "test-data/dummyFile.txt" + aFileSuffix);
		assertTrue(outputFile.exists());

		// Compressed file should be smaller than original file
		assertTrue(outputFile.length() < inputFile.length());
	}

	@Test
	public void testZstdCompression() throws Exception {
		runTest("zstd-test", ".zstd");
	}

	@Test
	public void testZstdLargeBlockCompression() throws Exception {
		runTest("zstd-medium-test", ".zstd");
	}

	@Test
	public void testZstdStreamCompression() throws Exception {
		runTest("zstd-large-test", ".zstd");
	}

	@Test
	public void testBzip2Compression() throws Exception {
		runTest("bzip2-test", ".bz2");
	}

	@Test
	public void testBzip2LargeBlockCompression() throws Exception {
		runTest("bzip2-medium-test", ".bz2");
	}

	@Test
	public void testBzip2StreamCompression() throws Exception {
		runTest("bzip2-large-test", ".bz2");
	}

	@Test
	public void testLz4Compression() throws Exception {
		runTest("lz4-test", ".lz4");
	}

	@Test
	public void testLz4LargeBlockCompression() throws Exception {
		runTest("lz4-medium-test", ".lz4");
	}

	@Test
	public void testLz4StreamCompression() throws Exception {
		runTest("lz4-large-test", ".lz4");
	}

	@Test
	public void testLzoCompression() throws Exception {
		runTest("lzo-test", ".lzo");
	}

	@Test
	public void testLzoLargeBlockCompression() throws Exception {
		runTest("lzo-medium-test", ".lzo");
	}

	@Test
	public void testLzoStreamCompression() throws Exception {
		runTest("lzo-large-test", ".lzo");
	}

	@Test
	public void testSnappyCompression() throws Exception {
		runTest("snappy-test", ".snappy");
	}

	@Test
	public void testSnappyLargeBlockCompression() throws Exception {
		runTest("snappy-medium-test", ".snappy");
	}

	@Test
	public void testSnappyStreamCompression() throws Exception {
		runTest("snappy-large-test", ".snappy");
	}

	protected File getPom(String aProjectNameToTest) {
		File pom = new File("target/test-classes/projects/compressor-tests", aProjectNameToTest);
		assertNotNull(pom);
		assertTrue(pom.exists());

		return pom;
	}

	protected CompressorMojo createMojo(File aPomRoot) throws Exception {
		CompressorMojo myMojo = (CompressorMojo) rule.lookupConfiguredMojo(aPomRoot, "compress");
		assertNotNull(myMojo);

		myMojo.setLog(new SystemStreamLog() {

			@Override
			public boolean isDebugEnabled() {
				return true;
			}

		});

		return myMojo;
	}

	/** Do not need the MojoRule. */
	@WithoutMojo
	@Test
	public void testSomethingWhichDoesNotNeedTheMojoAndProbablyShouldBeExtractedIntoANewClassOfItsOwn() {
		assertTrue(true);
	}

}
