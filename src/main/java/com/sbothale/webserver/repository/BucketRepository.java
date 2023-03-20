package com.sbothale.webserver.repository;

import com.sbothale.webserver.bucket.Bucket;

import java.util.List;
import java.util.Optional;

public interface BucketRepository {
    List<Bucket> getAllBuckets();
    Optional<Bucket> getBucketById(String bucketId);
    void createBucket(Bucket bucket);
    void deleteBucket(String bucketId);
    void updateBucket(Bucket bucket);
}