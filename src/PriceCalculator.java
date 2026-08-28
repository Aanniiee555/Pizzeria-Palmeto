
public final class PriceCalculator {

    private PriceCalculator() {
    }

    public static double basePrice(String type) {
        try {
            return PizzaType.valueOf(type.toUpperCase()).getPrice();
        } catch (IllegalArgumentException e) {
            // unrecognised type -> fall back to the regular base price
            return PizzaType.REGULAR.getPrice();
        }
    }

    public static double ingredientsPrice(String[] ingredients, int count) {
        double total = 0.0;
        for (int i = 0; i < count; i++) {
            Ingredient ingredient = Ingredient.fromLabel(ingredients[i]);
            if (ingredient != null) {
                total += ingredient.getPrice();
            }
        }
        return total;
    }

  
    public static double pizzaUnitPrice(Order order) {
        Pizza pizza = order.getPizza();
        return basePrice(pizza.getType()) + ingredientsPrice(pizza.getIngredients(), pizza.getIngredientsCount());
    }

    
    public static double orderAmount(Order order) {
        return pizzaUnitPrice(order) * order.getQuantity();
    }
}
