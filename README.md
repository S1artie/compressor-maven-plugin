# compressor-maven-plugin

This Maven plugin allows to perform file compression tasks as part of a Maven build sequence. Note that "file compression" refers only to the pure size reduction part in this case, not the aspect of grouping several files into some sort of archive, which is often associated with "file compression" as well.

This plugin supports the following compression algorithms:
- Zstandard (with customizable compression level)
- BZip2
- LZ4
- LZO
- Snappy

It leverages two other Maven libraries in order to do the actual compression:
- [aircompressor](https://central.sonatype.com/artifact/io.airlift/aircompressor) for pure-java implementations of all algorithms except Zstd (its Zstd impl does not support all compression levels)
- [zstd-jni](https://central.sonatype.com/artifact/com.github.luben/zstd-jni) for the Zstd implementation

## Usage

### Integration Example

```
<plugin>
    <groupId>de.firehead</groupId>
    <artifactId>compressor-maven-plugin</artifactId>
    <version>1.0.0</version>
    <executions>
        <execution>
            <id>compress-resources</id>
            <phase>process-resources</phase>
            <goals>
                <goal>compress</goal>
            </goals>
            <configuration>
                <compressionsets>
                    <!-- A compression set is a compression algorithm configuration plus one or more filesets defining the files to be compressed. -->
                    <compressionset>
                        <!-- Replace original files with compressed files (false) or place compressed files with a suffix next to original files (true) -->
                        <addSuffix>false</addSuffix>
                        <!-- Use the Zstandard algorithm -->
                        <algorithm>ZSTD</algorithm>
                        <!-- Compress using level 18, which is fairly high (22 is max, but extremely slow) -->
                        <compressionLevel>18</compressionLevel>
                        <!-- Compress all files matching the following fileset. The syntax for file selection is identical to the well-known syntax of other Maven plugins, such as the maven-clean-plugin for example. -->
                        <filesets>
                            <fileset>
                                <directory>${project.basedir}/resources/</directory>
                                <includes>
                                    <include>**/*</include>
                                </includes>
                            </fileset>
                        </filesets>
                    </compressionset>
                </compressionsets>
            </configuration>
        </execution>
    </executions>  				
</plugin>
```

