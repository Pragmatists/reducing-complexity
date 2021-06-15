package pl.pragmatists.complexity.machine.v7;

import java.util.function.Consumer;

public class ReportIssueStrategy implements Consumer<VendingMachine> {
    private final MachineService service = new MachineService();

    @Override
    public void accept(VendingMachine vendingMachine) {
        service.reportIssue(vendingMachine.getMachineSerialId());
    }
}
