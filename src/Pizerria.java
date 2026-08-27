public class Pizerria {

    public static void main(String[] args) {

        Customer customer = new Customer(1111, "Vardan");
        Order order = new Order(customer, "regular", "margarita", 2);


        // think how to print prices as well or calculate total price of the order
        order.addIngredient("Tomato paste");
        order.addIngredient("Garlic");
        order.addIngredient("Peper");
        order.addIngredient("Bacon");

        PizzaType[] values = PizzaType.values();
        for (PizzaType value : values) {
            value.ordinal();
            value.name();
            System.out.println(value);
        }
    }
}