package com.sbothale.webserver;

import com.sbothale.webserver.bucket.CreateBucketHandler;
import com.sbothale.webserver.bucket.GetBucketHandler;
import com.sbothale.webserver.database.H2DatabaseServer;
import com.sbothale.webserver.object.CreateObjectHandler;
import com.sbothale.webserver.object.GetObjectHandler;
import com.sbothale.webserver.repository.BucketRepository;
import com.sbothale.webserver.repository.JdbcBucketRepository;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServer;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.BodyHandler;
import org.apache.log4j.Logger;

import java.sql.SQLException;

public class WebServer {
    private static final Logger logger = Logger.getLogger(WebServer.class);

    public static void main(String[] args) throws SQLException {
        H2DatabaseServer dbServer = new H2DatabaseServer();
        dbServer.start();
        Vertx vertx = Vertx.vertx();
        HttpServer server = vertx.createHttpServer();
        Router router = Router.router(vertx);

        BucketRepository bucketRepository = new JdbcBucketRepository(dbServer.getJdbcUrl(), dbServer.getDbUser(), dbServer.getDbPassword());

        // bucket routes and handlers
        router.post("/buckets").handler(BodyHandler.create()).handler(new CreateBucketHandler(bucketRepository));
        router.get("/buckets/:bucketId").handler(new GetBucketHandler(bucketRepository));

        //object routes and handlers
       router.post("/buckets/:bucketId/objects").handler(BodyHandler.create()).handler(new CreateObjectHandler(bucketRepository));
       router.get("/buckets/:bucketId/objects/:objectId").handler(new GetObjectHandler(bucketRepository));

        server.requestHandler(router).listen(8090, res -> {
            if (res.succeeded()) {
                logger.info("Server started on port 8090");
            } else {
                logger.error("Failed to start server", res.cause());
            }
        });
    }
}
