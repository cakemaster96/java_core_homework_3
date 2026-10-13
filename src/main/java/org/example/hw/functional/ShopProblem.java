package org.example.hw.functional;
import java.util.Comparator;
import java.util.List;

public class ShopProblem {

    public CoffeeShop findCoffeeNearMe(Location you, List<CoffeeShop> shops) {
       return shops.stream().min(Comparator.comparingLong(shop -> you.distanceBetween(shop.location()))).
               orElseThrow(() -> new IllegalArgumentException("Пустой список кофеен"));
    }

    interface Location {
        long distanceBetween(Location other);
    }

    interface CoffeeShop {
        Location location();
    }

}
