package com.medici.app.resource;

import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.google.api.gax.paging.Page;
import com.google.cloud.storage.Blob;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Bucket;
import com.google.cloud.storage.BucketInfo;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;

/**
 * 
 * @author a73s
 *
 */
@RestController
public class StorageController {

	protected Logger logger = Logger.getLogger(StorageController.class.getName());

	@Value("${PROJECT_ID}")
	private String projectId;

	@RequestMapping(value = "/create/{bucketName}", method = RequestMethod.GET)
	public ResponseEntity<?> create(@PathVariable String bucketName) {

		try {
			Storage storage = StorageOptions.getDefaultInstance().getService();
			Bucket bucket = storage.create(BucketInfo.of(bucketName));
			return new ResponseEntity(bucket, new HttpHeaders(), HttpStatus.OK);

		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}

	}

	@RequestMapping(value = "/metadata/{bucketName}", method = RequestMethod.GET)
	public ResponseEntity<?> metadata(@PathVariable String bucketName) {

		try {
			Storage storage = StorageOptions.getDefaultInstance().getService();
			Bucket bucket = storage.create(BucketInfo.of(bucketName));
			return new ResponseEntity(bucket.getLabels(), new HttpHeaders(), HttpStatus.OK);

		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}

	}

	@RequestMapping(value = "/listObjects/{bucketName}", method = RequestMethod.GET)
	public ResponseEntity<?> listObjects(@PathVariable String bucketName) {

		try {
			Storage storage = StorageOptions.newBuilder().setProjectId(projectId).build().getService();
			Bucket bucket = storage.get(bucketName, Storage.BucketGetOption.fields(Storage.BucketField.values()));
			Page<Blob> blobs = bucket.list();
			return new ResponseEntity(blobs, new HttpHeaders(), HttpStatus.OK);

		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}

	}

	@RequestMapping(value = "/listBuckets/{bucketName}", method = RequestMethod.GET)
	public ResponseEntity<?> listBuckets(@PathVariable String bucketName) {

		try {
			Storage storage = StorageOptions.newBuilder().setProjectId(projectId).build().getService();
			Bucket bucket = storage.get(bucketName, Storage.BucketGetOption.fields(Storage.BucketField.values()));
			return new ResponseEntity(bucket, new HttpHeaders(), HttpStatus.OK);

		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}

	}

	@RequestMapping(value = "/delete/{bucketName}/{objectName}", method = RequestMethod.GET)
	public ResponseEntity<?> delete(@PathVariable String bucketName, @PathVariable String objectName) {

		try {
			Storage storage = StorageOptions.newBuilder().setProjectId(projectId).build().getService();
			boolean deleted = storage.delete(bucketName, objectName);

			return new ResponseEntity(deleted, new HttpHeaders(), HttpStatus.OK);

		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}

	}

	@RequestMapping(value = "/upload/{bucketName}", method = RequestMethod.POST)
	public ResponseEntity<?> upload(@RequestBody MultipartFile file, @PathVariable String bucketName) {

		try {
			Storage storage = StorageOptions.newBuilder().setProjectId(projectId).build().getService();
			BlobId blobId = BlobId.of(bucketName, file.getOriginalFilename());
			BlobInfo blobInfo = BlobInfo.newBuilder(blobId).build();
			Blob blob = storage.create(blobInfo, file.getBytes());

			return new ResponseEntity(blob.getMetadata(), new HttpHeaders(), HttpStatus.OK);

		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}

	} 
}
