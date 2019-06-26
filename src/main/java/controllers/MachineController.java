package controllers;

import io.vertx.core.Vertx;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;
import services.MachineService;


public class MachineController {
    private Vertx vertx;
    private MachineService machineService;
    private Router router;
    private EventBus eventBus;

    public MachineController(Vertx vertx) {
        this.vertx = vertx;
        this.machineService = new MachineService(vertx);
        this.router = Router.router(vertx);
        this.eventBus = vertx.eventBus();
        paths(router);
    }

    public Router getRouter(){
        return this.router;
    }

    private void paths(Router router){
        router.get("/api/machine_status").handler(this::state);
        router.get("/api/beverages").handler(this::getAll);
        router.route("/api/beverages*").handler(BodyHandler.create());
        router.post("/api/beverages").handler(this::addOne);
        router.get("/api/beverages/:id").handler(this::getOne);
        router.put("/api/beverages/:id").handler(this::updateOne);
        router.delete("/api/beverages/:id").handler(this::deleteOne);
    }

    private void state(RoutingContext routingContext) {
        machineService.getMachineStatus("machine.state",routingContext);
    }

    public void getAll(RoutingContext routingContext) {
        machineService.getAllBeverages("beverages.all",routingContext);
    }

    public void addOne(RoutingContext routingContext) {
        machineService.addBeverage("create.insert",routingContext.getBodyAsJson(),routingContext);
    }

    public void getOne(RoutingContext routingContext) {
        final String type = routingContext.request().getParam("id");
        if (type == null) {
            routingContext.response().setStatusCode(400).end();
        } else {
            machineService.getBeverage("getBeverage",type,routingContext);
        }
    }

    public void updateOne(RoutingContext routingContext) {
        final String type = routingContext.request().getParam("id");
        JsonObject json = routingContext.getBodyAsJson();
        JsonArray jsonArray = new JsonArray();
        jsonArray.add(type);
        jsonArray.add(json);
        if (type == null || json == null) {
            routingContext.response().setStatusCode(400).end();
        } else {
            machineService.updateBeverage("updateBeverage",jsonArray, routingContext);
        }
    }

    public void deleteOne(RoutingContext routingContext) {
        String type = routingContext.request().getParam("id");
        if (type == null) {
            routingContext.response().setStatusCode(400).end();
        } else {
            machineService.deleteBeverage("deleteBeverage",type,routingContext);
        }

    }
}
