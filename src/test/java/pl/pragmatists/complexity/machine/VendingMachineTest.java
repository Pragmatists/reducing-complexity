package pl.pragmatists.complexity.machine;

import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InOrder;
import pl.pragmatists.complexity.machine.common.MachineDisplay;
// Choose the package with your VendingMachine implementation!
import pl.pragmatists.complexity.machine.v1.VendingMachine;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class VendingMachineTest {
    private static final int CHOCO_BAR_PRICE = 5;
    private static final int JUICE_BOX_PRICE = 7;
    private static final int SELL_CHOCO_BAR = 1;
    private static final int SELL_JUICE_BOX = 2;
    private static final int RETURN_COINS = 0;

    @Test
    public void shouldDisplayInfoWhenUnavailableOptionsChosen() {
        MachineDisplay display = mock(MachineDisplay.class);
        VendingMachine vendingMachine = new VendingMachine(display);

        vendingMachine.choose(57);

        ArgumentCaptor<String> argCaptor = ArgumentCaptor.forClass(String.class);
        verify(display).display(argCaptor.capture());
        String argValue = argCaptor.getValue();
        assertTrue(String.format("Was expecting different display message than '%s'", argValue),
                argValue.equals("Choice unavailable") || argValue.equals("Choice 57 not available"));
    }

    @Test
    public void shouldSellChocoBarWhenEnoughCoins() {
        MachineDisplay display = mock(MachineDisplay.class);
        VendingMachine vendingMachine = new VendingMachine(display);
        int initialStock = vendingMachine.getChocoBarStock();

        vendingMachine.insertCoins(CHOCO_BAR_PRICE);
        vendingMachine.choose(SELL_CHOCO_BAR);

        assertEquals(initialStock - 1, vendingMachine.getChocoBarStock());
        verifyDisplay(display,
                "Inserted 5 coin(s), current balance: 5",
                "Sold choco bar, current balance: 0");
    }

    @Test
    public void shouldNotSellChocoBarWhenEnoughCoins() {
        MachineDisplay display = mock(MachineDisplay.class);
        VendingMachine vendingMachine = new VendingMachine(display);
        int initialStock = vendingMachine.getChocoBarStock();

        vendingMachine.insertCoins(CHOCO_BAR_PRICE - 1);
        vendingMachine.choose(SELL_CHOCO_BAR);

        assertEquals(initialStock, vendingMachine.getChocoBarStock());
        verifyDisplay(display,
                "Inserted 4 coin(s), current balance: 4",
                "Can't sell choco bar (price: 5), current balance: 4");
    }

    @Test
    public void shouldSellJuiceBoxWhenEnoughCoins() {
        MachineDisplay display = mock(MachineDisplay.class);
        VendingMachine vendingMachine = new VendingMachine(display);
        int initialStock = vendingMachine.getJuiceBoxStock();

        vendingMachine.insertCoins(JUICE_BOX_PRICE);
        vendingMachine.choose(SELL_JUICE_BOX);

        assertEquals(initialStock - 1, vendingMachine.getJuiceBoxStock());
        verifyDisplay(display,
                "Inserted 7 coin(s), current balance: 7",
                "Sold juice box, current balance: 0");
    }

    @Test
    public void shouldNotSellJuiceBoxWhenNotEnoughCoins() {
        MachineDisplay display = mock(MachineDisplay.class);
        VendingMachine vendingMachine = new VendingMachine(display);
        int initialStock = vendingMachine.getJuiceBoxStock();

        vendingMachine.insertCoins(JUICE_BOX_PRICE - 1);
        vendingMachine.choose(SELL_JUICE_BOX);

        assertEquals(initialStock, vendingMachine.getJuiceBoxStock());
        verifyDisplay(display,
                "Inserted 6 coin(s), current balance: 6",
                "Can't sell juice box (price: 7), current balance: 6");
    }

    @Test
    public void shouldReturnAllInsertedCoins() {
        MachineDisplay display = mock(MachineDisplay.class);
        VendingMachine vendingMachine = new VendingMachine(display);

        vendingMachine.insertCoins(2);
        vendingMachine.insertCoins(3);
        vendingMachine.insertCoins(4);
        vendingMachine.choose(RETURN_COINS);

        assertEquals(0, vendingMachine.getCoinBalance());
        verifyDisplay(display,
                "Inserted 2 coin(s), current balance: 2",
                "Inserted 3 coin(s), current balance: 5",
                "Inserted 4 coin(s), current balance: 9",
                "Returned 9 coin(s)"
        );
    }

    @Test
    public void shouldKeepCorrectAmountOfCoinsAfterSellingStuff() {
        VendingMachine vendingMachine = new VendingMachine(new MachineDisplay());
        int initialChocoBarStock = vendingMachine.getChocoBarStock();
        int initialJuiceBoxStock = vendingMachine.getJuiceBoxStock();

        vendingMachine.insertCoins(CHOCO_BAR_PRICE - 1);
        vendingMachine.choose(SELL_CHOCO_BAR);
        vendingMachine.insertCoins(CHOCO_BAR_PRICE);
        vendingMachine.choose(SELL_CHOCO_BAR);
        vendingMachine.insertCoins((JUICE_BOX_PRICE * 2) + 3);
        vendingMachine.choose(SELL_JUICE_BOX);
        vendingMachine.choose(SELL_JUICE_BOX);

        assertEquals(CHOCO_BAR_PRICE + 2, vendingMachine.getCoinBalance());
        assertEquals(initialChocoBarStock - 1, vendingMachine.getChocoBarStock());
        assertEquals(initialJuiceBoxStock - 2, vendingMachine.getJuiceBoxStock());
    }

    @Test
    public void shouldRunOutOfStockWhenSellingAllChocoBars() {
        VendingMachine vendingMachine = new VendingMachine(new MachineDisplay());
        int initialChocoBarStock = vendingMachine.getChocoBarStock();

        vendingMachine.insertCoins((CHOCO_BAR_PRICE * initialChocoBarStock) + (CHOCO_BAR_PRICE - 1));
        for (int i = 0; i < initialChocoBarStock; i++) {
            vendingMachine.choose(SELL_CHOCO_BAR);
        }
        // sold all choco bars, shouldn't be able to sell this one
        vendingMachine.choose(SELL_CHOCO_BAR);

        assertEquals(CHOCO_BAR_PRICE - 1, vendingMachine.getCoinBalance());
        assertEquals(0, vendingMachine.getChocoBarStock());
    }

    @Test
    public void shouldRunOutOfStockWhenSellingAllJuiceBoxes() {
        VendingMachine vendingMachine = new VendingMachine(new MachineDisplay());
        int initialChocoBarStock = vendingMachine.getChocoBarStock();

        vendingMachine.insertCoins((JUICE_BOX_PRICE * initialChocoBarStock) + (JUICE_BOX_PRICE - 1));
        for (int i = 0; i < initialChocoBarStock; i++) {
            vendingMachine.choose(SELL_JUICE_BOX);
        }
        // sold all juice boxes, shouldn't be able to sell this one
        vendingMachine.choose(SELL_JUICE_BOX);

        assertEquals(JUICE_BOX_PRICE - 1, vendingMachine.getCoinBalance());
        assertEquals(0, vendingMachine.getJuiceBoxStock());
    }

    private void verifyDisplay(MachineDisplay display, String... messages) {
        InOrder inOrder = inOrder(display);
        for (String message : messages) {
            inOrder.verify(display).display(message);
        }
        inOrder.verifyNoMoreInteractions();
    }
}
