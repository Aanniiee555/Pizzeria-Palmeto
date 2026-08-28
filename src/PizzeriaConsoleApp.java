import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


public class PizzeriaConsoleApp {

    private static final String RECEIPT_FILE = "receipt.txt";

    public static void main(String[] args) {
        BufferedReader console = new BufferedReader(new InputStreamReader(System.in, StandardCharsets.UTF_8));
        List<Order> orders = new ArrayList<>();

        System.out.println("*** Welcome to Pizzeria Palmetto ***");

        try {
            boolean addAnother = true;
            while (addAnother) {
                orders.add(takeOrder(console));
                addAnother = readYesNo(console, "Add another pizza to the order? (y/n): ");
            }
        } catch (EndOfInputException e) {
            System.out.println();
            System.out.println("Input ended unexpectedly; finishing up with what we have so far.");
        } catch (IOException e) {
            System.out.println("Could not read console input: " + e.getMessage());
            return;
        }

        if (orders.isEmpty()) {
            System.out.println("No orders were taken.");
            return;
        }

        System.out.println();
        System.out.println("Order summary:");
        for (Order order : orders) {
            System.out.println(order.getOrderDescription());
        }

        writeReceipt(orders);
    }

    /**
     * Signals that the console input stream ended (readLine() returned null)
     * while we were still expecting more input.
     */
    private static class EndOfInputException extends IOException {
        EndOfInputException() {
            super("End of input reached");
        }
    }

 
    private static Order takeOrder(BufferedReader console) throws IOException {
        System.out.println();
        int customerNumber = readInt(console, "Customer number: ");
        String customerName = readNonEmpty(console, "Customer name: ");
        Customer customer = new Customer(customerNumber, customerName);

        String type = readPizzaType(console);
        String pizzaName = readNonEmpty(console, "Pizza name (4-20 Latin letters, or it will be auto-named): ");
        int quantity = readInt(console, "Quantity: ");

        Order order = new Order(customer, type, pizzaName, quantity);

        Ingredient.printMenu();
        System.out.println("Type an ingredient name to add it, or \"done\" to finish this pizza (max 7).");
        while (order.getPizza().getIngredientsCount() < 7) {
            System.out.print("> ");
            String line = console.readLine();
            if (line == null) {
                throw new EndOfInputException();
            }
            if (line.equalsIgnoreCase("done")) {
                break;
            }
            Ingredient ingredient = Ingredient.fromLabel(line.trim());
            if (ingredient == null) {
                System.out.println("Unknown ingredient, please pick one from the menu above.");
                continue;
            }
            // Store the canonical label from the enum, not the user's raw
            // (possibly differently-cased) input, so later lookups in
            // writeReceipt() always succeed.
            order.addIngredient(ingredient.getLabel());
        }

        return order;
    }

    private static String readPizzaType(BufferedReader console) throws IOException {
        String[] types = PizzaTypeInterface.getTypes();
        while (true) {
            System.out.print("Pizza type " + java.util.Arrays.toString(types) + ": ");
            String line = console.readLine();
            if (line == null) {
                throw new EndOfInputException();
            }
            for (String type : types) {
                if (type.equalsIgnoreCase(line.trim())) {
                    return type;
                }
            }
            System.out.println("Please enter one of " + java.util.Arrays.toString(types) + ".");
        }
    }

    private static int readInt(BufferedReader console, String prompt) throws IOException {
        while (true) {
            System.out.print(prompt);
            String line = console.readLine();
            if (line == null) {
                throw new EndOfInputException();
            }
            try {
                return Integer.parseInt(line.trim());
            } catch (NumberFormatException e) {
                System.out.println("Please enter a whole number.");
            }
        }
    }

    private static String readNonEmpty(BufferedReader console, String prompt) throws IOException {
        while (true) {
            System.out.print(prompt);
            String line = console.readLine();
            if (line == null) {
                throw new EndOfInputException();
            }
            if (!line.trim().isEmpty()) {
                return line.trim();
            }
            System.out.println("This can't be empty.");
        }
    }

    private static boolean readYesNo(BufferedReader console, String prompt) throws IOException {
        while (true) {
            System.out.print(prompt);
            String line = console.readLine();
            if (line == null) {
                throw new EndOfInputException();
            }
            line = line.trim().toLowerCase(Locale.ROOT);
            if (line.equals("y") || line.equals("yes")) {
                return true;
            }
            if (line.equals("n") || line.equals("no")) {
                return false;
            }
            System.out.println("Please answer y or n.");
        }
    }

    private static void writeReceipt(List<Order> orders) {
        double total = 0.0;

        try (PrintWriter out = new PrintWriter(new FileWriter(RECEIPT_FILE, StandardCharsets.UTF_8))) {
            out.println("********************************");
            for (Order order : orders) {
                Pizza pizza = order.getPizza();
                out.println("Order: " + order.getOrderNumber());
                out.println("Client: " + order.getCustomerNumber());
                out.println("Name: " + pizza.getName());
                out.println("--------------------------------");
                out.printf(Locale.US, "Pizza Base (%s) %.2f $%n", pizza.getType(), PriceCalculator.basePrice(pizza.getType()));

                String[] ingredients = pizza.getIngredients();
                for (int i = 0; i < pizza.getIngredientsCount(); i++) {
                    Ingredient ingredient = Ingredient.fromLabel(ingredients[i]);
                    if (ingredient != null) {
                        out.printf(Locale.US, "%s %.2f $%n", ingredient.getLabel(), ingredient.getPrice());
                    }
                }

                double amount = PriceCalculator.orderAmount(order);
                total += amount;

                out.println("--------------------------------");
                out.printf(Locale.US, "Amount: %.2f $%n", amount);
                out.println("Quantity: " + order.getQuantity());
                out.println("--------------------------------");
            }
            out.printf(Locale.US, "Total amount: %.2f $%n", total);
            out.println("********************************");

            System.out.println();
            System.out.println("Receipt written to " + RECEIPT_FILE);
        } catch (IOException e) {
            System.out.println("Could not write receipt file: " + e.getMessage());
        }
    }
}
