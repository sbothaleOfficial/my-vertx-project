package com.sbothale.webserver.object;

import com.sbothale.webserver.bucket.Bucket;
import com.sbothale.webserver.repository.BucketRepository;
import io.vertx.core.Handler;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.ext.web.RoutingContext;
import org.apache.commons.lang3.StringUtils;

import java.util.Optional;

public class GetObjectHandler implements Handler<RoutingContext> {
    private final BucketRepository bucketRepository;

    public GetObjectHandler(BucketRepository bucketRepository) {
        this.bucketRepository = bucketRepository;
    }

    @Override
    public void handle(RoutingContext routingContext) {
        String bucketId = routingContext.pathParam("bucketId");
        String objectId = routingContext.pathParam("objectId");

        if (StringUtils.isBlank(bucketId) || StringUtils.isBlank(objectId)) {
            routingContext.response().setStatusCode(400).end();
            return;
        }

        Optional<Bucket> optionalBucket = bucketRepository.getBucketById(bucketId);
        if (optionalBucket.isEmpty()) {
            routingContext.response().setStatusCode(404).end();
            return;
        }

        Optional<com.sbothale.webserver.object.Object> optionalObject = optionalBucket.get().getObjects().stream()
                .filter(obj -> objectId.equals(obj.getId()))
                .findFirst();

        HttpServerResponse response = routingContext.response();
        if (optionalObject.isPresent()) {
            response.putHeader("content-type", "application/json").end(optionalObject.get().toString());
        } else {
            response.setStatusCode(404).end();
        }
    }
}
