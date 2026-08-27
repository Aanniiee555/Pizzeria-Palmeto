package am.pizzeria.palmetto;

/**
 * The 7 toppings the Pizzeria currently offers, with their price.
 * This did not exist in the original exercise files - it is needed
 * so that addIngredient() calls (which only store the ingredient's
 * name as a String on Pizza/Order) can still be priced when we build
 * the receipt.
 */
public enum Ingredient {

    TOMATO_PASTE("Tomato paste", 1.0),
    CHEESE("Cheese", 1.0),
    SALAMI("Salami", 1.5),
    BACON("Bacon", 1.2),
    GARLIC("Garlic", 0.3),
    CORN("Corn", 0.7),
    PEPPERONI("Pepperoni", 0.6),
    OLIVES("Olives", 0.5);

    private final String label;
    private final double price;

    Ingredient(String label, double price) {
        this.label = label;
        this.price = price;
    }

    public String getLabel() {
        return label;
    }

    public double getPrice() {
        return price;
    }

    /**
     * Looks an ingredient up by the text the customer typed at the console
     * (case-insensitive, ignores surrounding whitespace).
     */
    public static Ingredient fromLabel(String label) {
        if (label == null) {
            return null;
        }
        String trimmed = label.trim();
        for (Ingredient ingredient : values()) {
            if (ingredient.label.equalsIgnoreCase(trimmed)) {
                return ingredient;
            }
        }
        return null;
    }

    public static void printMenu() {
        System.out.println("Available ingredients:");
        for (Ingredient ingredient : values()) {
            System.out.printf("  %-15s %.2f $%n", ingredient.label, ingredient.price);
        }
    }
}
