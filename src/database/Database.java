package database;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import models.Order;
import models.Product;
import models.ProductType;
import models.User;
import models.UserType;

public class Database {
    List<User> importUsers(List<User> users) {
        if (users.isEmpty()) {
        User owner = new User("Dimitar Enev", "2525", "0875555555",  UserType.OWNER);
        User user1 = new User("Viktor Todorov", "0101", "08888888", UserType.MANAGER);
        User user2 = new User("Vladislav", "0000", "088999999", UserType.WAITRESS);
        User user3 = new User("Petko", "0202", "0887777777", UserType.WAITRESS);
        return new ArrayList<>(List.of(owner, user1, user2, user3));
        } else {
            return users;
        }
    }

    Map<Integer, List<Product>> getOrders(Map<Integer, List<Product>> orders) {
        for (int i = 1; i <= 20; i++) {
            if (orders.get(i) == null) {
                orders.put(i, new ArrayList<>());
            } else {
                orders.replace(i, orders.get(i));
            }
        }
        return orders;
    }


    Map<Integer, List<Order>> getHistory(Map<Integer, List<Order>> historyies) {
        for (int i = 1; i <= 20; i++) {
            if (historyies.get(i) == null) {
                historyies.put(i, new ArrayList<>());
            } else {
                historyies.replace(i, historyies.get(i));
            }
        }
        return historyies;
    }

    public List<Product> getProducts(List<Product> products) {
        if (products.isEmpty()) {
            Product blackRam = new Product(ProductType.ALCOHOLIC, "Уиски", "Black Ram", 0.05, 10, 2.8, true);
            Product redLabel = new Product(ProductType.ALCOHOLIC, "Уиски", "Jonny Walker Red Label", 0.05, 10, 3.2, true);
            Product blackLabel = new Product(ProductType.ALCOHOLIC, "Уиски", "Jonny Walker Black Label", 0.05, 5, 5.8, true);
            Product jackDaniels = new Product(ProductType.ALCOHOLIC, "Уиски", "Jack Daniel's", 0.05, 5, 4.8, true);
            Product jameson = new Product(ProductType.ALCOHOLIC, "Уиски", "Jameson", 0.05, 10, 4.3, true);
            Product tullamoreDew = new Product(ProductType.ALCOHOLIC, "Уиски", "Tullamore Dew", 0.05, 10, 4.0, true);
            Product chivasRegal = new Product(ProductType.ALCOHOLIC, "Уиски", "Chivas Regal", 0.05, 10, 5.5, true);
            Product bushmills = new Product(ProductType.ALCOHOLIC, "Уиски", "Bushmills", 0.05, 10, 4.8, true);
            Product flirt = new Product(ProductType.ALCOHOLIC, "Водка", "Flirt", 0.05, 10, 2.5, true);
            Product beluga = new Product(ProductType.ALCOHOLIC, "Водка", "Beluga", 0.05, 5, 3.7, true);
            Product absolut = new Product(ProductType.ALCOHOLIC, "Водка", "Absolut", 0.05, 5, 4.6, true);
            Product savoy = new Product(ProductType.ALCOHOLIC, "Водка", "Savoy", 0.05, 10, 2.8, true);
            Product smirnoff = new Product(ProductType.ALCOHOLIC, "Водка", "Smirnoff", 0.05, 5, 4.2, true);
            Product finlandia = new Product(ProductType.ALCOHOLIC, "Водка", "Finlandia", 0.05, 5, 3.5, true);
            Product bacardiRum = new Product(ProductType.ALCOHOLIC, "Ром - бял", "Bacardi", 0.05, 3, 4.2, true);
            Product savoyRum = new Product(ProductType.ALCOHOLIC, "Ром - бял", "Savoy", 0.05, 5, 3.2, true);
            Product captainMorgan = new Product(ProductType.ALCOHOLIC, "Ром - бял", "Captain Morgan", 0.05, 3, 4.6, true);
            Product atlantic = new Product(ProductType.ALCOHOLIC, "Ром - червен", "Atlantic", 0.05, 5, 3.6, true);
            Product captainFred = new Product(ProductType.ALCOHOLIC, "Ром - червен", "Captain Fred", 0.05, 3, 4.2, true);
            Product sailorJerry = new Product(ProductType.ALCOHOLIC, "Ром - червен", "Sailor Jerry", 0.05, 3, 4.4, true);
            Product ciociosan = new Product(ProductType.ALCOHOLIC, "Вермут", "Ciociosan", 0.05, 3, 4.0, true);
            Product martini = new Product(ProductType.ALCOHOLIC, "Вермут", "Martini", 0.05, 3, 4.5, true);
            Product noillyPrat = new Product(ProductType.ALCOHOLIC, "Вермут", "Noilly Prat", 0.05, 3, 5, true);
            Product yatrus = new Product(ProductType.ALCOHOLIC, "Вино - червено", "Yatrus", 0.2, 5, 4.2, true);
            Product maxima = new Product(ProductType.ALCOHOLIC, "Вино - червено", "Maxima", 0.2, 5, 4.7, true);
            Product chateuKarnobat = new Product(ProductType.ALCOHOLIC, "Вино - червено", "Chateu Karnobat", 0.2, 5, 3.8, true);
            Product mavrud = new Product(ProductType.ALCOHOLIC, "Вино - червено", "Mavrud", 0.2, 5, 4, true);
            Product todoroff = new Product(ProductType.ALCOHOLIC, "Вино - червено", "Todoroff boutique", 0.2, 5, 5.8, true);
            Product unique = new Product(ProductType.ALCOHOLIC, "Вино - червено", "Unique", 0.2, 5, 6, true);
            Product mezzek = new Product(ProductType.ALCOHOLIC, "Вино - червено", "Mezzek", 0.2, 5, 4.7, true);
            Product tiara = new Product(ProductType.ALCOHOLIC, "Вино - червено", "Tiara", 0.2, 5, 4.6, true);
            Product yambol = new Product(ProductType.ALCOHOLIC, "Вино - червено", "Villa Yambol", 0.2, 5, 4.3, true);

            Product cocaCola = new Product(ProductType.NONALCOHOLIC, "Газирани", "Coca-Cola", 0.25, 125, 2.0, true);

            Product peanuts = new Product(ProductType.FOOD, "Ядки", "Фъстъци", 0.20, 200, 2.0, false);

            Product sexOnTheBeach = new Product(ProductType.COCKTAIL, "Алкохолни", "Sex on the beach", 0.20, 200, 4.8, true);

            products.addAll(Arrays.asList(blackRam, redLabel, blackLabel, jackDaniels, jameson, tullamoreDew, chivasRegal, bushmills, flirt, beluga, absolut, savoy,
                    smirnoff, finlandia, bacardiRum, savoyRum, captainMorgan, atlantic, captainFred, sailorJerry,ciociosan, martini, noillyPrat, yatrus, maxima, chateuKarnobat,
                    mavrud, todoroff, unique, mezzek, tiara, yambol, cocaCola, peanuts, sexOnTheBeach));
            return products;
        } else {
            return products;
        }
    }
}
