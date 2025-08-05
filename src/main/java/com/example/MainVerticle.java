package com.example;

import com.example.Configuration.AppRouter;
import com.example.Configuration.DependencyProvider;
import com.example.controller.XmlController;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class MainVerticle extends AbstractVerticle {

  @Override
  public void start(Promise<Void> startPromise) {
    try {
      // Load config.json from classpath (inside JAR)
      InputStream is = getClass().getClassLoader().getResourceAsStream("config.json");

      if (is == null) {
        throw new RuntimeException("config.json not found in classpath!");
      }

      String configStr = new String(is.readAllBytes(), StandardCharsets.UTF_8);
      JsonObject config = new JsonObject(configStr);

      JsonObject httpConfig = config.getJsonObject("http");
      if (httpConfig == null || !httpConfig.containsKey("port")) {
        startPromise.fail("Missing 'http.port' in config.json");
        return;
      }

      int port = httpConfig.getInteger("port");

      JsonObject clientApiConfig = config.getJsonObject("clientApi");
      String apiHost = clientApiConfig.getString("host");
      int apiPort = clientApiConfig.getInteger("port");
      String apiPath = clientApiConfig.getString("path");

      DependencyProvider dependencies = new DependencyProvider(vertx, apiHost, apiPort, apiPath);
      XmlController xmlController = dependencies.getXmlController();

      // Set up router
      AppRouter appRouter = new AppRouter(xmlController);
      Router router = appRouter.createRouter(vertx);

      // Start HTTP server
      vertx.createHttpServer()
              .requestHandler(router)
              .listen(port)
              .onSuccess(server -> {
                System.out.println("✅ Server started on port " + port);
                startPromise.complete();
              })
              .onFailure(err -> {
                System.err.println("❌ Failed to start server: " + err.getMessage());
                startPromise.fail(err);
              });

    } catch (Exception e) {
      System.err.println("❌ Failed during startup: " + e.getMessage());
      startPromise.fail(e);
    }
  }
}
