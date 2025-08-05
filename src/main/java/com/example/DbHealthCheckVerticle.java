package com.example;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.jdbc.JDBCClient;
import io.vertx.ext.sql.SQLConnection;

public class DbHealthCheckVerticle extends AbstractVerticle {

  @Override
  public void start(Promise<Void> startPromise) {
    JsonObject dbConfig = config().getJsonObject("db");
    JDBCClient jdbcClient = JDBCClient.createShared(vertx, dbConfig);

    // Run blocking connection check off the event loop
    vertx.executeBlocking(promise -> {
      jdbcClient.getConnection(ar -> {
        if (ar.succeeded()) {
          SQLConnection connection = ar.result();
          System.out.println("Connected to MySQL DB successfully!");
          connection.close();
          promise.complete();
        } else {
          System.err.println(" Failed to connect to MySQL: " + ar.cause().getMessage());
          promise.fail(ar.cause());
        }
      });
    }, res -> {
      if (res.succeeded()) {
        startPromise.complete();
      } else {
        startPromise.fail(res.cause());
      }
    });
  }
}
