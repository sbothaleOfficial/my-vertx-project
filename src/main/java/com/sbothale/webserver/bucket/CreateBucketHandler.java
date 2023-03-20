package com.sbothale.webserver.bucket;

import com.sbothale.webserver.repository.BucketRepository;
import io.vertx.core.Handler;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;
import org.apache.log4j.Logger;

public class CreateBucketHandler implements Handler<RoutingContext> {
    private final BucketRepository repository;
    private static final Logger logger = Logger.getLogger(CreateBucketHandler.class);

    public CreateBucketHandler(BucketRepository repository) {
        this.repository = repository;
    }

    @Override
    public void handle(RoutingContext routingContext) {
        HttpServerResponse response = routingContext.response();
        response.putHeader("content-type", "application/json");

        JsonObject requestBody = routingContext.getBodyAsJson();

        String bucketName = requestBody.getString("name");
        String location = requestBody.getString("location");

        if (bucketName == null || location == null) {
            response.setStatusCode(400).end("Bucket name and location are required.");
            logger.error("Bucket creation failed: missing required parameters");
            return;
        }

        Bucket newBucket = new Bucket(null, bucketName, location);
        repository.createBucket(newBucket);

        JsonObject responseBody = new JsonObject()
                .put("id", newBucket.getId())
                .put("name", bucketName)
                .put("location", location);

        response.setStatusCode(201).end(responseBody.encodePrettily());
        logger.info("Bucket created successfully: " + responseBody.encodePrettily());
    }
}
