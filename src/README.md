# Pizzeria Palmetto — I/O update

## What's new

**Task 1 — Interactive console ordering** (`PizzeriaConsoleApp.java`)
Order details (customer number/name, pizza type, pizza name, quantity,
ingredients) are typed at the console. Input is read with a
`BufferedReader` wrapped around an `InputStreamReader` on `System.in`,
as required.

**Task 2 — Receipt file** (`PizzeriaConsoleApp.writeReceipt`)
Instead of printing the check, the app writes it to `receipt.txt` with a
`PrintWriter`/`FileWriter` opened inside a **try-with-resources**
statement, so the stream is always closed, even on error.

Two small supporting pieces were added because pricing a receipt needs
them and the original classes didn't have them yet:

- `Ingredient.java` — the 7 toppings and their prices (from the exercise
  brief), plus a lookup by name and a `printMenu()` helper.
- `PriceCalculator.java` — computes a pizza's base price (via
  `PizzaType`) + ingredient prices, and the order's total.

Minor additions to existing classes (everything else is untouched):
- `Pizza.getType()` — getter was missing, needed to price the base.
- `Order.getOrderNumber()`, `getCustomerNumber()`, `getQuantity()` —
  getters were missing, needed to print the receipt.

The original `Pizerria.java` (hard-coded `main`) is left as-is;
`PizzeriaConsoleApp.java` is the new entry point for Part 4.

## Build & run

```bash
mkdir out
javac -d out src/am/pizzeria/palmetto/*.java
java -cp out am.pizzeria.palmetto.PizzeriaConsoleApp
```

Follow the prompts for each pizza (type an ingredient name to add it,
`done` when finished, then `y`/`n` to add another pizza). When you're
done, check `receipt.txt` in the directory you ran the command from.

## Example session

```
Customer number: 7717
Customer name: Vardan
Pizza type [CALZONE, REGULAR]: regular
Pizza name (4-20 Latin letters, or it will be auto-named): Margarita
Quantity: 2
> Tomato paste
> Pepperoni
> Garlic
> Bacon
> done
Add another pizza to the order? (y/n): n
```

produces a `receipt.txt` like:

```
********************************
Order: 10000
Client: 7717
Name: Margarita
--------------------------------
Pizza Base (REGULAR) 1.00 $
Tomato paste 1.00 $
Pepperoni 0.60 $
Garlic 0.30 $
Bacon 1.20 $
--------------------------------
Amount: 8.20 $
Quantity: 2
--------------------------------
Total amount: 8.20 $
********************************
```
