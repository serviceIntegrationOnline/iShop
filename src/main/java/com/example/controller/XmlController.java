package com.example.controller;

import com.example.service.ClientService;
import com.example.service.XmlService;
import com.example.Configuration.ErrorHandler;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;
import org.json.JSONObject;
import org.json.XML;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;

public class XmlController {

  private static final Logger logger = LoggerFactory.getLogger(XmlController.class);

  private final XmlService xmlService;
  private final ClientService clientService;

  public XmlController(XmlService xmlService, ClientService clientService) {
    this.xmlService = xmlService;
    this.clientService = clientService;
  }

  public Handler<RoutingContext> convertXmlAndSendHandler() {
    return ctx -> {
      String xmlData = ctx.getBodyAsString();

      if (xmlData == null || xmlData.isBlank()) {
        ErrorHandler.respondWithError(ctx, 400, "XML input is required.");
        return;
      }

      try {
        JsonObject json = xmlService.convertXmlToJson(xmlData);

        clientService.sendToVehicleApi(json)
          .onFailure(err -> logger.error("Failed to send JSON to Project B: {}", err.getMessage()));

        ctx.response()
          .setStatusCode(202)
          .putHeader("Content-Type", "application/json")
          .end(new JsonObject()
            .put("status", "accepted")
            .put("message", "Request received. Processing in background.")
            .put("timestamp", LocalDateTime.now().toString())
            .encodePrettily());

      } catch (Exception e) {
        logger.error("XML to JSON conversion failed", e);
        ErrorHandler.respondWithError(ctx, 500, "Error converting XML to JSON: " + e.getMessage());
      }
    };
  }

  public void jsonViewer(RoutingContext ctx) {
    String xml = ctx.getBodyAsString();

    if (xml == null || xml.isBlank()) {
      ErrorHandler.respondWithError(ctx, 400, "XML input is required.");
      return;
    }

    try {
      JSONObject jsonObject = XML.toJSONObject(xml);
      JsonObject vertxJson = new JsonObject(jsonObject.toString());

      ctx.response()
        .setStatusCode(200)
        .putHeader("Content-Type", "application/json")
        .end(vertxJson.encodePrettily());

    } catch (Exception e) {
      logger.error("Failed to convert XML to JSON", e);
      ErrorHandler.respondWithError(ctx, 400, "Invalid XML format.");
    }
  }
}
