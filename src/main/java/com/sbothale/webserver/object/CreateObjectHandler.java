package com.sbothale.webserver.object;

import com.sbothale.webserver.bucket.Bucket;
import com.sbothale.webserver.repository.BucketRepository;
import io.vertx.core.http.HttpHeaders;
import io.vertx.core.json.Json;
import io.vertx.ext.web.RoutingContext;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import java.util.UUID;

public class CreateObjectHandler implements io.vertx.core.Handler<RoutingContext> {

    private static final Logger logger = Logger.getLogger(CreateObjectHandler.class);
    private final BucketRepository bucketRepository;

    public CreateObjectHandler(BucketRepository bucketRepository) {
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

        try {
            Bucket bucket = bucketRepository.getBucketById(bucketId).orElse(null);
            if (bucket == null) {
                routingContext.response()
                        .putHeader(HttpHeaders.CONTENT_TYPE, "application/json")
                        .setStatusCode(404)
                        .end(Json.encodePrettily(new ErrorResponse("Bucket not found")));
                return;
            }

            ObjectRequest objectRequest = Json.decodeValue(routingContext.getBodyAsString(), ObjectRequest.class);
            Object object = new Object(UUID.randomUUID().toString(), objectRequest.getName(), objectRequest.getContent());
            bucket.addObject(object);
            bucketRepository.updateBucket(bucket);
            routingContext.response()
                    .putHeader(HttpHeaders.CONTENT_TYPE, "application/json")
                    .setStatusCode(201)
                    .end(Json.encodePrettily(object));
        } catch (Exception e) {
            logger.error("Failed to create object", e);
            routingContext.response()
                    .putHeader(HttpHeaders.CONTENT_TYPE, "application/json")
                    .setStatusCode(500)
                    .end(Json.encodePrettily(new ErrorResponse("Failed to create object")));
        }
    }
}