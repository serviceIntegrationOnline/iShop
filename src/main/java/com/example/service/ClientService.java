package com.example.service;

import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;

public interface ClientService {
  Future<Void> sendToVehicleApi(JsonObject json);
}
