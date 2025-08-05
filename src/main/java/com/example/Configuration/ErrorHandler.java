package com.example.Configuration;

import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;

public class ErrorHandler {

  public static void handle(RoutingContext ctx, Throwable throwable) {
    int statusCode = 500;
    String message = "Internal Server Error";

    if (throwable != null) {
      message = throwable.getMessage();
    }

    ctx.response()
      .setStatusCode(statusCode)
      .putHeader("Content-Type", "application/json")
      .end("{\"error\":\"" + message + "\"}");
  }
  public static void respondWithError(RoutingContext ctx, int statusCode, String message) {
    ctx.response()
      .setStatusCode(statusCode)
      .putHeader("Content-Type", "application/json")
      .end(new JsonObject().put("error", message).encodePrettily());
  }

    public static void logError(String context, Throwable throwable) {
      System.err.println("[ERROR] [" + context + "] " + throwable.getMessage());
      throwable.printStackTrace();
    }
}
