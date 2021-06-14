package pl.pragmatists.complexity.machine.v5b;

import java.util.Map;
import java.util.function.BooleanSupplier;
import java.util.function.IntConsumer;
import java.util.function.IntSupplier;

public class VendingMachine {
    private static final int CHOCO_BAR_PRICE = 5;
    private static final int JUICE_BOX_PRICE = 7;

    private static final Map<VendingMachineAction, VendingStrategy> choiceToStrategy = Map.of(
        VendingMachineAction.SELL_CHOCO_BAR, VendingMachine::sellChocoBar,
        VendingMachineAction.SELL_JUICE_BOX, VendingMachine::sellJuiceBox,
        VendingMachineAction.RETURN_COINS, VendingMachine::returnCoins,
        VendingMachineAction.UNAVAILABLE, vm -> vm.display("Choice unavailable.")
    );

    private int chocoBarStock = 5;
    private int juiceBoxStock = 5;
    private int coinBalance = 0;

    public void choose(int selectedNumber) {
        if (!stockAvailable()) {
            display("No items left for sale");
            returnCoins();
            return;
        }

        var selectedAction = VendingMachineAction.of(selectedNumber);
        choiceToStrategy.get(selectedAction).performOn(this);
    }

    private void sellChocoBar() {
        sellItem(CHOCO_BAR_PRICE, this::isChocoBarAvailable, "choco bar", this::getChocoBarStock, this::setChocoBarStock);
    }

    private void sellJuiceBox() {
        sellItem(JUICE_BOX_PRICE, this::isJuiceBoxAvailable, "juice box", this::getJuiceBoxStock, this::setJuiceBoxStock);
    }

    private void sellItem(int price,
                          BooleanSupplier checkStockAvailable,
                          String itemName,
                          IntSupplier stockGetter,
                          IntConsumer stockSetter) {
        if (coinBalance >= price) {
            if (checkStockAvailable.getAsBoolean()) {
                stockSetter.accept(stockGetter.getAsInt() - 1);
                setCoinBalance(getCoinBalance() - price);
                display(String.format("Sold %s, current balance: %d", itemName, coinBalance));
            } else {
                display(String.format("%s unavailable", itemName));
            }
        } else {
            display(String.format("Can't sell %s (price: %d), current balance: %d", itemName, price, coinBalance));
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

    private boolean stockAvailable() {
        return isChocoBarAvailable() || isJuiceBoxAvailable();
    }

    public int getChocoBarStock() {
        return chocoBarStock;
    }

    private void setChocoBarStock(int value) {
        this.chocoBarStock = value;
    }

    public int getJuiceBoxStock() {
        return juiceBoxStock;
    }

    private void setJuiceBoxStock(int value) {
        this.juiceBoxStock = value;
    }

    public int getCoinBalance() {
        return coinBalance;
    }

    private void setCoinBalance(int value) {
        this.coinBalance = value;
    }
}
