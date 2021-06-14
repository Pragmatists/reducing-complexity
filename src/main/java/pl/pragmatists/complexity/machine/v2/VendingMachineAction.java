package pl.pragmatists.complexity.machine.v2;

public enum VendingMachineAction {
    SELL_CHOCO_BAR,
    SELL_JUICE_BOX,
    RETURN_COINS,
    UNAVAILABLE;

    public static VendingMachineAction of(int code) {
        if (code == 1) {
            return SELL_CHOCO_BAR;
        }

        if (code == 2) {
            return SELL_JUICE_BOX;
        }

        if (code == 0) {
            return RETURN_COINS;
        }

        return UNAVAILABLE;
    }
}
