package com.example;

import io.vertx.core.Vertx;

public class LauncherApp {
  public static void main(String[] args) {
    Vertx vertx = Vertx.vertx();
    vertx.deployVerticle(new MainVerticle());
  }
}
