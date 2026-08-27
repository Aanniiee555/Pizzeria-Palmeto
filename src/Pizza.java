import java.util.Arrays;
import java.util.Objects;

public class Pizza {

    public static final Pizza REGULAR = new Pizza("Regular", PizzaType.REGULAR.name(), new String[7]);

    private String name;
    private String type;
    private String[] ingredients;
    private int ingredientsCount;

    public Pizza(String name, String type, String[] ingredients) {
        this.name = name;
        this.type = type;
        this.ingredients = ingredients;
        for (String ingredient : ingredients) {
            if (ingredient != null) {
                ingredientsCount++;
            }
        }
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public String[] getIngredients() {
        return ingredients;
    }

    public void addIngredient(String ingredient) {
        ingredients[ingredientsCount] = ingredient;
        ingredientsCount++;
    }

    public int getIngredientsCount() {
        return ingredientsCount;
    }

    @Override
    public String toString() {
        String[] filled = Arrays.stream(ingredients)
                .filter(Objects::nonNull)
                .toArray(String[]::new);
        return "Pizza{" +
                "name='" + name + '\'' +
                ", type='" + type + '\'' +
                ", ingredients=" + String.join(", ", filled) +
                ", ingredientsCount=" + ingredientsCount +
                '}';
    }
}
