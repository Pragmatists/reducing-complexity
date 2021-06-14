package pl.pragmatists.complexity.machine;

import pl.pragmatists.complexity.machine.v5b.VendingMachine;

public class Demo {
    /*
        Vending machine actions:
            1 -- sell choco bar
            2 -- sell juice box
            0 -- return coins
     */

    public static void main(String[] args) {
        var vendingMachine = new VendingMachine();
        vendingMachine.insertCoins(10);
        vendingMachine.choose(1);
        vendingMachine.choose(2);
        vendingMachine.returnCoins();
    }
}
