package pl.pragmatists.complexity.machine;

// switch the package here to use different implementations
import pl.pragmatists.complexity.machine.v7.VendingMachine;

public class Demo {
    /*
        Vending machine actions:
            1 -- sell choco bar
            2 -- sell juice box
            0 -- return coins

            100 -- report issue to service
     */

    public static void main(String[] args) {
        var vendingMachine = new VendingMachine();
        vendingMachine.insertCoins(10);
        vendingMachine.choose(1);
        vendingMachine.choose(2);
        vendingMachine.returnCoins();
        vendingMachine.choose(100);
    }
}
