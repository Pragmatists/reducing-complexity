package pl.pragmatists.complexity.machine.v5b;

import java.util.Arrays;

public enum VendingMachineAction {
    SELL_CHOCO_BAR(1),
    SELL_JUICE_BOX(2),
    RETURN_COINS(0),
    UNAVAILABLE(Integer.MIN_VALUE);

    private final int choiceNumber;

    VendingMachineAction(int choiceNumber) {
        this.choiceNumber = choiceNumber;
    }

    public static VendingMachineAction of(int code) {
        return Arrays.stream(values())
                .filter(v -> code == v.choiceNumber)
                .findAny()
                .orElse(UNAVAILABLE);
    }
}
