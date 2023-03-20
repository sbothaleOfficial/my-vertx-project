package com.sbothale.webserver.bucket;

import com.sbothale.webserver.object.ErrorResponse;
import com.sbothale.webserver.repository.BucketRepository;
import io.vertx.core.http.HttpHeaders;
import io.vertx.core.json.Json;
import io.vertx.ext.web.RoutingContext;
import org.apache.commons.lang3.StringUtils;

import java.util.Optional;

public class GetBucketHandler implements io.vertx.core.Handler<RoutingContext> {

    private final BucketRepository bucketRepository;

    public GetBucketHandler(BucketRepository bucketRepository) {
        this.bucketRepository = bucketRepository;
    }

    @Override
    public void handle(RoutingContext routingContext) {
        String bucketId = routingContext.pathParam("bucketId");

        if (StringUtils.isBlank(bucketId)) {
            routingContext.response()
                    .putHeader(HttpHeaders.CONTENT_TYPE, "application/json")
                    .setStatusCode(400)
                    .end(Json.encodePrettily(new ErrorResponse("Bucket ID is required")));
            return;
        }

        Optional<Bucket> optionalBucket = bucketRepository.getBucketById(bucketId);
        if (optionalBucket.isPresent()) {
            Bucket bucket = optionalBucket.get();
            routingContext.response()
                    .putHeader(HttpHeaders.CONTENT_TYPE, "application/json")
                    .setStatusCode(200)
                    .end(Json.encodePrettily(bucket.getObjects()));
        } else {
            routingContext.response()
                    .putHeader(HttpHeaders.CONTENT_TYPE, "application/json")
                    .setStatusCode(404)
                    .end(Json.encodePrettily(new ErrorResponse("Bucket not found")));
        }
    }
}
