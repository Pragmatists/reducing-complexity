package pl.pragmatists.complexity.machine.v1;

public class VendingMachine {
    private static final int CHOCO_BAR_PRICE = 5;
    private static final int JUICE_BOX_PRICE = 7;

    private int chocoBarStock = 5;
    private int juiceBoxStock = 5;
    private int coinBalance = 0;

    public void choose(int selectedNumber) {
        if (stockAvailable()) {
            var selectedAction = VendingMachineAction.of(selectedNumber);
            if (available(selectedAction)) {
                switch (selectedAction) {
                    case SELL_CHOCO_BAR:
                        if (coinBalance >= CHOCO_BAR_PRICE) {
                            if (isChocoBarAvailable()) {
                                chocoBarStock--;
                                coinBalance -= CHOCO_BAR_PRICE;
                                display(String.format("Sold choco bar, current balance: %d", coinBalance));
                            } else {
                                display("Choco bar unavailable");
                            }
                        } else {
                            display(String.format("Can't sell choco bar (price: %d), current balance: %d", CHOCO_BAR_PRICE, coinBalance));
                        }
                        break;
                    case SELL_JUICE_BOX:
                        if (coinBalance >= JUICE_BOX_PRICE) {
                            if (isJuiceBoxAvailable()) {
                                juiceBoxStock--;
                                coinBalance -= JUICE_BOX_PRICE;
                                display(String.format("Sold juice box, current balance: %d", coinBalance));
                            } else {
                                display("Juice box unavailable");
                            }
                        } else {
                            display(String.format("Can't sell juice box (price: %d), current balance: %d", JUICE_BOX_PRICE, coinBalance));
                        }
                        break;
                    case RETURN_COINS:
                        returnCoins();
                        break;
                    default:
                        break;
                }
            } else {
                display(String.format("Choice %d not available", selectedNumber));
            }
        } else {
            display("No items left for sale");
            returnCoins();
        }
    }

    public void insertCoins(int amount) {
        coinBalance += amount;
        display(String.format("Inserted %d coin(s), current balance: %d", amount, coinBalance));
    }

    public void returnCoins() {
        display(String.format("Returned %d coin(s)", coinBalance));
    }

    public boolean isChocoBarAvailable() {
        return chocoBarStock > 0;
    }

    public boolean isJuiceBoxAvailable() {
        return juiceBoxStock > 0;
    }

    private void display(String message) {
        System.out.println(message);
    }

    private boolean available(VendingMachineAction vendingMachineAction) {
        return vendingMachineAction != VendingMachineAction.UNAVAILABLE;
    }

    private boolean stockAvailable() {
        return isChocoBarAvailable() || isJuiceBoxAvailable();
    }

    public int getChocoBarStock() {
        return chocoBarStock;
    }

    public int getJuiceBoxStock() {
        return juiceBoxStock;
    }

    public int getCoinBalance() {
        return coinBalance;
    }
}
