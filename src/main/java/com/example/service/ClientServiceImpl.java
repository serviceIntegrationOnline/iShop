package com.example.service;

import io.vertx.core.Future;
import io.vertx.ext.web.client.WebClient;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.client.WebClientOptions;
import java.time.Instant;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class ClientServiceImpl implements ClientService {

  private final WebClient webClient;
  private final String host;
  private final int port;
  private final String path;
  private final String connectionId;


  public ClientServiceImpl(Vertx vertx, String host, int port, String path) {
    this.connectionId = UUID.randomUUID().toString().substring(0, 8); // for logging

    WebClientOptions options = new WebClientOptions()
      .setKeepAlive(true)
      .setIdleTimeout(120)
      .setIdleTimeoutUnit(TimeUnit.SECONDS)
      .setMaxPoolSize(10);
//    this.webClient = WebClient.create(vertx);
    this.host = host;
    this.port = port;
    this.path = path;
    this.webClient = WebClient.create(vertx, options);
  }

  @Override
  public Future<Void> sendToVehicleApi(JsonObject json) {
    return webClient
      .post(port, host, path)
      .putHeader("Content-Type", "application/json")
      .sendJsonObject(json)
      .map(response -> {
        System.out.println("[" + connectionId + "] Sending request at " + Instant.now()
          + " on thread: " + Thread.currentThread().getName());
        return (Void) null;
      })
      .onFailure(err -> {
        System.err.println("Failed to send Json " + err.getMessage());
      });
  }
}
