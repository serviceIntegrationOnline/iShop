package com.example.Configuration;


import com.example.controller.XmlController;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpMethod;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.ext.web.handler.CorsHandler;

import java.util.HashSet;
import java.util.Set;

public class AppRouter {
  private final XmlController xmlController;

  public AppRouter(XmlController controller) {
    this.xmlController = controller;
  }

  public Router createRouter(Vertx vertx) {
    Router router = Router.router(vertx);

    Set<HttpMethod> allowedMethods = new HashSet<>();
    allowedMethods.add(HttpMethod.GET);
    allowedMethods.add(HttpMethod.POST);
    allowedMethods.add(HttpMethod.OPTIONS);

    router.route().handler(CorsHandler.create("*")
      .allowedMethods(allowedMethods)
      .allowedHeader("Content-Type")
      .allowedHeader("Access-Control-Allow-Origin")
    );

    router.route().handler(BodyHandler.create());
    router.post("/convert").handler(xmlController.convertXmlAndSendHandler());
    router.post("/view").handler(xmlController::jsonViewer);
    router.get("/health").handler(ctx -> ctx.response().end("OK"));

    return router;
  }
}
