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

public class FilesetTests {
	@Rule
	public MojoRule rule = new MojoRule() {
		@Override
		protected void before() throws Throwable {
		}

		@Override
		protected void after() {
		}
	};

	protected static final String FILE_SUFFIX = ".zstd";

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
	public void testMultipleFiles() throws Exception {
		final File pomRoot = getPom("multifile-test");
		final CompressorMojo myMojo = createMojo(pomRoot);

		myMojo.execute();

		File inputFile1 = new File(pomRoot, "test-data/dummyFile1.txt");
		File inputFile2 = new File(pomRoot, "test-data/dummyFile2.txt");
		assertTrue(inputFile1.exists());
		assertTrue(inputFile2.exists());
		File outputFile1 = new File(pomRoot, "test-data/dummyFile1.txt" + FILE_SUFFIX);
		File outputFile2 = new File(pomRoot, "test-data/dummyFile2.txt" + FILE_SUFFIX);
		assertTrue(outputFile1.exists());
		assertTrue(outputFile2.exists());

		// Compressed files should be smaller than original file
		assertTrue(outputFile1.length() < inputFile1.length());
		assertTrue(outputFile2.length() < inputFile2.length());
	}

	@Test
	public void testMultipleSets() throws Exception {
		final File pomRoot = getPom("multiset-test");
		final CompressorMojo myMojo = createMojo(pomRoot);

		myMojo.execute();

		File inputFile1 = new File(pomRoot, "test-data/dummyFile1.txt");
		File inputFile2 = new File(pomRoot, "test-data/dummyFile2.txt");
		assertTrue(inputFile1.exists());
		assertTrue(inputFile2.exists());
		File outputFile1 = new File(pomRoot, "test-data/dummyFile1.txt" + FILE_SUFFIX);
		File outputFile2 = new File(pomRoot, "test-data/dummyFile2.txt" + FILE_SUFFIX);
		assertTrue(outputFile1.exists());
		assertTrue(outputFile2.exists());

		// Compressed files should be smaller than original file
		assertTrue(outputFile1.length() < inputFile1.length());
		assertTrue(outputFile2.length() < inputFile2.length());
	}

	@Test
	public void testSubdirectoryNegative() throws Exception {
		final File pomRoot = getPom("subdirectory-negative-test");
		final CompressorMojo myMojo = createMojo(pomRoot);

		myMojo.execute();

		File inputFile1 = new File(pomRoot, "test-data/dummyFile1.txt");
		File inputFile2 = new File(pomRoot, "test-data/subdir/dummyFile2.txt");
		assertTrue(inputFile1.exists());
		assertTrue(inputFile2.exists());
		File outputFile1 = new File(pomRoot, "test-data/dummyFile1.txt" + FILE_SUFFIX);
		File outputFile2 = new File(pomRoot, "test-data/subdir/dummyFile2.txt" + FILE_SUFFIX);
		assertTrue(outputFile1.exists());
		assertTrue(!outputFile2.exists()); // Subdirs are not in fileset!

		// Compressed files should be smaller than original file
		assertTrue(outputFile1.length() < inputFile1.length());
	}

	@Test
	public void testSubdirectoryPositive() throws Exception {
		final File pomRoot = getPom("subdirectory-positive-test");
		final CompressorMojo myMojo = createMojo(pomRoot);

		myMojo.execute();

		File inputFile1 = new File(pomRoot, "test-data/dummyFile1.txt");
		File inputFile2 = new File(pomRoot, "test-data/subdir/dummyFile2.txt");
		assertTrue(inputFile1.exists());
		assertTrue(inputFile2.exists());
		File outputFile1 = new File(pomRoot, "test-data/dummyFile1.txt" + FILE_SUFFIX);
		File outputFile2 = new File(pomRoot, "test-data/subdir/dummyFile2.txt" + FILE_SUFFIX);
		assertTrue(outputFile1.exists());
		assertTrue(outputFile2.exists());

		// Compressed files should be smaller than original file
		assertTrue(outputFile1.length() < inputFile1.length());
	}

	protected File getPom(String aProjectNameToTest) {
		File pom = new File("target/test-classes/projects/fileset-tests", aProjectNameToTest);
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
