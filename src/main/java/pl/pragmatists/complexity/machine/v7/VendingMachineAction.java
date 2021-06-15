package pl.pragmatists.complexity.machine.v7;

import java.util.Arrays;
import java.util.function.Consumer;

public enum VendingMachineAction {
    SELL_CHOCO_BAR(1, VendingMachine::sellChocoBar),
    SELL_JUICE_BOX(2, VendingMachine::sellJuiceBox),
    RETURN_COINS(0, VendingMachine::returnCoins),
    CALL_SERVICE(100, new ReportIssueStrategy()),
    UNAVAILABLE(Integer.MIN_VALUE, vm -> vm.display("Choice unavailable."));

    private final int choiceNumber;
    private final Consumer<VendingMachine> action;

    VendingMachineAction(int choiceNumber, Consumer<VendingMachine> action) {
        this.choiceNumber = choiceNumber;
        this.action = action;
    }

    public void performOn(VendingMachine vendingMachine) {
        action.accept(vendingMachine);
    }

    public static VendingMachineAction of(int code) {
        return Arrays.stream(values())
                .filter(v -> code == v.choiceNumber)
                .findAny()
                .orElse(UNAVAILABLE);
    }
}
