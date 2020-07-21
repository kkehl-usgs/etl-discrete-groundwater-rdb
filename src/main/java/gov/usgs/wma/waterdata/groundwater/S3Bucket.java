package gov.usgs.wma.waterdata.groundwater;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;

/**
 * Manager class for S3 Bucket actions.
 * @author duselman
 */
public class S3Bucket implements AutoCloseable {

	protected String region;
	protected String bucket;
	protected String keyName;
	protected File file;
	protected FileWriter writer;
	protected boolean disposeFile = true;

	S3Bucket(String region, String bucket, String keyName, File file) {
		this.region = region;
		this.bucket = bucket;
		this.keyName = keyName;
		this.file = file;
	}

	protected AmazonS3 buildS3() {
		AmazonS3 s3 = AmazonS3ClientBuilder.standard().withRegion(region).build();
		return s3;
	}

	@Override
	public void close() throws Exception {
		try {
			writer.close();
			AmazonS3 s3 = buildS3();
			s3.putObject(bucket, keyName, file);
		} finally {
			if (disposeFile) {
				file.delete();
			}
		}
	}

	public void setDisposeFile(boolean disposeFile) {
		this.disposeFile = disposeFile;
	}

	public Writer getWriter() {
		try {
			writer = new FileWriter(file);
			return writer;
		} catch (IOException ioe) {
			throw new RuntimeException("Cannot open temp file from the current runtime env.");
		}
	}
}
