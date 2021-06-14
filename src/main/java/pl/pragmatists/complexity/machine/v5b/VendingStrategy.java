package pl.pragmatists.complexity.machine.v5b;

@FunctionalInterface
public interface VendingStrategy {
    void performOn(VendingMachine vendingMachine);
}
