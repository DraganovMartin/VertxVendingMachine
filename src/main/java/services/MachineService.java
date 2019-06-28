package services;

import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;

public interface MachineService {

    public void getMachineStatus(String address, RoutingContext routingContext);

    public void addBeverage(String address, JsonObject beverage, RoutingContext routingContext);

    public void getBeverage(String address, String id, RoutingContext routingContext);

    public void getAllBeverages(String address, RoutingContext routingContext);

    public void updateBeverage(String address, JsonArray array, RoutingContext routingContext);

    public void deleteBeverage(String address, String id, RoutingContext routingContext);
}
