package services;

import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;
import model.Beverage;
import model.Machine;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;


public class MachineService {
    private Vertx vertx;
    private EventBus eventBus;


    public MachineService(Vertx vertx) {
        this.vertx = vertx;
        this.eventBus = vertx.eventBus();
    }

    public MachineService() {
    }

    public void getMachineStatus(String address, RoutingContext routingContext) {
        eventBus.send(address, "", res -> {
            if (res.succeeded()) {
                routingContext.response()
                        .setStatusCode(200)
                        .putHeader("content-type", "application/json; charset=utf-8")
                        .end(Json.encodePrettily(res.result().body()));
            }
        });
    }

    public void addBeverage(String address, JsonObject beverage, RoutingContext routingContext) {
        Future<JsonObject> dbFut = Future.future();
        eventBus.send("machine.state", "", res -> {
            {
                if (res.succeeded()) {
                    dbFut.complete((JsonObject) res.result().body());
                } else {
                    dbFut.fail(res.cause());
                }
            }
        });

        dbFut.setHandler(asyncResult -> {
            Beverage bev = beverage.mapTo(Beverage.class);
            Machine machine = null;
            if (asyncResult.result() == null){
                machine = new Machine();
            }
            else machine = asyncResult.result().mapTo(Machine.class);
            machine.getBeverages().add(bev);
            machine.setMoneyAmount(machine.getMoneyAmount() + bev.getPrice());
            JsonObject machineJson = JsonObject.mapFrom(machine);
            if (machine.getBeverages().size() > 1) {
                eventBus.send("updateMachine", machineJson, res -> {
                    if (res.succeeded()) {
                        System.out.println(res.result().body());
                        routingContext.response()
                                .setStatusCode(201)
                                .putHeader("content-type", "application/json; charset=utf-8")
                                .end(Json.encodePrettily(machineJson));
                    } else {
                        System.out.println("Communication not succeeded");
                    }
                });
                return;
            }
            eventBus.send(address, machineJson, res -> {
                if (res.succeeded()) {
                    System.out.println(res.result().body());
                    routingContext.response()
                            .setStatusCode(201)
                            .putHeader("content-type", "application/json; charset=utf-8")
                            .end(Json.encodePrettily(machineJson));
                } else {
                    System.out.println("Communication not succeeded");
                }
            });
        });

    }

    public void getBeverage(String address, String id, RoutingContext routingContext) {
        eventBus.send(address, Integer.valueOf(id), res -> {
            if (res.succeeded()) {
                routingContext.response()
                        .setStatusCode(200)
                        .putHeader("content-type", "application/json; charset=utf-8")
                        .end(Json.encodePrettily(res.result().body()));
            }
        });
    }

    public void getAllBeverages(String address, RoutingContext routingContext) {
        eventBus.send(address, "", res -> {
            if (res.succeeded()) {
                routingContext.response()
                        .setStatusCode(200)
                        .putHeader("content-type", "application/json; charset=utf-8")
                        .end(Json.encodePrettily(res.result().body()));
            } else res.cause().printStackTrace();
        });
    }

    public void updateBeverage(String address, JsonArray array, RoutingContext routingContext) {
        Future<JsonObject> dbFut = Future.future();
        eventBus.send("machine.state", "", res -> {
            {
                if (res.succeeded()) {
                    dbFut.complete((JsonObject) res.result().body());
                } else {
                    dbFut.fail(res.cause());
                }
            }
        });

        dbFut.setHandler(asyncResult -> {
            Machine machine = null;
            if (asyncResult.succeeded()) {
               machine = asyncResult.result().mapTo(Machine.class);
                JsonObject bev = array.getJsonObject(1);
                String id = array.getString(0);
                Beverage real = bev.mapTo(Beverage.class);
                machine.setBeverages(machine.getBeverages().stream().map(bev1 -> {
                    if (bev1.getId() == Integer.valueOf(id)){
                        bev1.setType(real.getType());
                        bev1.setPrice(real.getPrice());
                        return bev1;
                    }
                    return bev1;
                }).collect(Collectors.toList()));

                machine.setMoneyAmount(machine.getBeverages().stream().mapToDouble(Beverage::getPrice).sum());
            }

            JsonObject machineJson = JsonObject.mapFrom(machine);
            eventBus.send(address, machineJson, res -> {
                if (res.succeeded()) {
                    if (res.result().body().equals("Updated")) {
                        routingContext.response()
                                .setStatusCode(200)
                                .putHeader("content-type", "application/json; charset=utf-8")
                                .end(Json.encodePrettily(machineJson));
                    }
                }
            });

        });

    }

    public void deleteBeverage(String address, String id, RoutingContext routingContext) {
        Future<JsonObject> dbFut = Future.future();
        eventBus.send("machine.state", "", res -> {
            {
                if (res.succeeded()) {
                    dbFut.complete((JsonObject) res.result().body());
                } else {
                    dbFut.fail(res.cause());
                }
            }
        });

        dbFut.setHandler(asyncResult -> {
            Machine machine = null;
            if (asyncResult.succeeded()) {
                machine = asyncResult.result().mapTo(Machine.class);

                for (int i = 0; i < machine.getBeverages().size(); i++) {
                    if (machine.getBeverages().get(i).getId() == Integer.valueOf(id)) {
                        machine.getBeverages().remove(i);
                        i--;
                    }
                }
                if (machine.getBeverages().isEmpty()) {
                    eventBus.send("deleteMachine", "", res -> {
                        if (res.succeeded()) {
                            if (res.result().body().equals("Deleted")) {
                                routingContext.response()
                                        .setStatusCode(200)
                                        .putHeader("content-type", "application/json; charset=utf-8")
                                        .end(new JsonObject().put("Successfull", true).toString());
                            }
                        }
                    });
                    return;
                }
                JsonObject machineJson = JsonObject.mapFrom(machine);
                eventBus.send(address, machineJson, res -> {
                    if (res.succeeded()) {
                        if (res.result().body().equals("Deleted")) {
                            routingContext.response()
                                    .setStatusCode(200)
                                    .putHeader("content-type", "application/json; charset=utf-8")
                                    .end(new JsonObject().put("Successfull", true).toString());
                        }
                    }
                });
            }
        });

    }
}
