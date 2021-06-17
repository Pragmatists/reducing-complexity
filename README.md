# Reducing complexity
This project demonstrates techniques of reducing code complexity on the example of a series of implementations of a
simple Vending Machine. Each implementation introduces improvements concerning code complexity.

Each version is in a distinct package named: `pl.pragmatists.complexity.machine.[v1|v2|v3|v4|v5a|v5b|v6|v7]`.
See the **Steps** section below for explanation of how complexity has been improved between different versions.
A convenient way to work with the versions is to diff files to see exact modifications.

You're invited to follow the `Steps` described below or try your own approach. There are also exercises below.
To start coding just copy the two classes from `v1` to some new package, point the tests to the `VendingMachine` there
(see **Tests**), and you're good to go!

## Vending Machine overview
We'll be working on a simplistic implementation of a Vending Machine. It's just an example, so don't worry if this
doesn't seem to be industry-grade, bulletproof code.

Three things can be done with the machine:
- you can insert any amount of coins (we assume that there is only one type of coins, worth `1`).
- you can input a number corresponding to some action the machine will execute, the actions are:
    - code `1`: sell a choco bar for 5 coins. The machine checks if there is enough choco bars left in it and if
      there are enough coins.
    - code `2`: sell a juice box for 7 coins. Analogous conditions hold as for choco bars.
    - code `0`: return all remaining coins.
    - code `100`: call Vening Machine service help (implemented in `v7`).

In response, the machine will display appropriate messages.

Have a look at the class `pl.pragmatists.complexity.machine.Demo` to play around with the Vending Machine (don't forget
to choose the right package for `VendingMachine`!).

## Steps

Here are explanations and comments about the steps between consecutive version of the code, from most to least complex.
It may be helpful to diff the code to see what are the exact changes discussed.

### From 1 to 2: Return fast!
Here we placed a guard condition just at the beginning of the `choose` method, checking if there's any stock available.
It doesn't strictly reduce the number of possible execution paths of the code, but it reduces the cognitive load when 
reading further lines.

It may not always be a good choice to have multiple returns, but quickly dealing with the edge cases and moving to the 
_real_ stuff is worth it.  

### From 2 to 3: Extract smaller methods
The actual code for selling choco bars and juice boxes has been extracted from `choose`. This immediately left us with 
several methods having less complexity. The next part of this step was extracting common part of these methods, so we 
ended up with only one complex method (which will be dealt with further ahead).

Notice that the new method `sellItem(int, BooleanSupplier, String, IntSupplier, IntConsumer)` has a rather complex
signature. It looks like in the end we traded one type of complexity for another. However, further refactoring is
possible to deal with that.  

### From 3 to 4: Hide complexity behind abstractions
Now there were changes done concerning the `VendingMachineAction` enum. First, each enum value got a code associated 
with it (in Java enums are capable of really useful stuff!). Then, we filtered a stream of all the values to find the
one matching the argument the `of` method. The logic of several sloppy `if`s is now hidden under the series of 
descriptive API calls.

Using a `Stream` is only one example, you'll encounter pretty many chances to abstract the complexity away. 

### From 4 to 5a: Use strategies! With enums!
In this part we added a `Consumer<VendingMachine>` field to the `VendingMachineAction` enum. This field represents what 
should happen with the `VendingMachine` when the enum value is chosen. Such a dynamically chosen object used to pass the
action to be taken is called a _Strategy_.  
And it frees us from using the switch. In the `VendingMachine` we are left with simple:
```
    var selectedAction = VendingMachineAction.of(selectedNumber);
    selectedAction.performOn(this);
```
Note, however, that to do this, we made several initially `private` methods `package-private`. There's a trade-off to
everything (but there's an exercise to fix this below).

Also, the message for unavailable choice has been changed, as it is no longer convenient (albeit possible) to pass the
choice number to be displayed.

### From 4 to 5b: Provide strategies
This version is an alternative to `5a`. Sometimes it's not reasonable to push more stuff to the enum. Sometimes the 
choice of the appropriate strategy may not be as simple as mapping `int` to an enum value. Here we used a `Map`. But
the point is that creating the appropriate strategies may take place in many ways.

Further on the road you maye combine the _Factory_ pattern with _Strategy_ to choose strategies based on complex inputs,
and to be able to have each strategy use a different set of dependencies such as repos, message queues, http clients etc.

### From 5a to 6: Return fast (again)!
Here we have a quick attempt to civilize `sellItem(..)`'s nested `if`s and `elses`. The cognitive complexity is reduced,
but by some other metrics we might have increased the complexity. When in doubt, choose what seems reasonable.

### From 6 to 7: Implement new functionality -- fast!
We introduced a feature to call a vending machine service by choosing `100`, which required only adding a new enum value
and pointing it the `ReportIssueStrategy` instance which holds all the necessary code. In real life it could send an SMS 
notification with details to the repairman, here it only prints the machine's id to console. But the important thing is 
that the choice logic has been extremely simplified and the business logic of reporting a malfunction is held in one,
dedicated place. Which feels like a huge win!

## Tests
I prepared tests so that you can check if the Vending Machine is still working after each refactoring cycle. In order 
to do so, in `VendingMachineTest` change the imported `VendingMachines` package to the one you're currently working on 
and run the tests using JUnit.

NOTE: the VendingMachine has many `void` methods and often what is tested is the args with which 
`MachineDisplay#display(String)` is called. Such tests may prove brittle if you diverge quite a lot from the original
implementation. Don't worry in such a case. Just carefully adjust the tests and keep on the good work!

## Exercises
These are fairly open-ended exercises. You can (and probably will need to) add new methods, classes, enums etc. to
accomplish some of them.

1. **Implement selling another type of product**
When the choice is `3` have the Vending Machine sell an apple. Do this first using code in `v1` then in version `v6`/`v7`.
2. **Test your apple-selling functionality**
Try writing tests for the changes you made in task 1. You'll see that for the later version it boils down to 
testing the strategy and making sure a correct strategy is chosen. 
3. **Simplify the `sellItem(..)` signature**
As explained in _Step from 2 to 3_, the method `sellItem(..)` is fairly advanced for what it does. There are plenty ways
of simplifying this, however. Think of a couple and implement one of them.
(Hint: Instead of passing a getter and setter to access and modify the stock of specific stuff, you could have a mapping 
of item type to the current stock. Then the method would get the stock type (a new enum?) as a parameter instead of the 
method references.) 
4. **Get rid of `sellItem(..)` `if`s**
In `sellItem(..)` there are still two `if`s in `v7`. It's unlikely that we can get rid of all the flow control, but 
maybe the checks of stock availability could be done somewhere else? Could you implement the strategies for choices `1`
and `2` to first check if there's enough stock (or coins, or both) and only then call `sellItem(..)`?
5. **De-hermetization trade-off**
In _Step from 4 to 5a_ there's a trade-off mentioned. It consists in making `private` fields `package-private` to be 
able to access them in the enum. Can you think of a way of using strategies (and implement it) without sacrificing
encapsulation?
