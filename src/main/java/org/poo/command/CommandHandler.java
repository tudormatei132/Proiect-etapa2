package org.poo.command;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import org.poo.commerciant.Commerciant;
import org.poo.fileio.CommandInput;
import org.poo.system.SystemManager;

public class CommandHandler {

    private final SystemManager system;
    private final ArrayNode output;
    private final ObjectMapper mapper;
    public CommandHandler(final SystemManager system, final ObjectMapper mapper,
                          final ArrayNode output) {
        this.system = system;
        this.mapper = mapper;
        this.output = output;
    }

    /**
     * will execute the command given by the Command Factory below
     * @param input the input used to determine which command to use
     */
    public void execute(final CommandInput input) {
        Command command;
        command = getCommand(input);
        if (command != null) {
            command.execute();
        }
    }


    /**
     * gets an instance of the correct command
     * @param command the input that will be used to determine which command to use
     * @return the instance of the correct command
     */
    private Command getCommand(final CommandInput command) {
        switch (command.getCommand()) {
            case "addAccount":
                return new AddAccount(command, system.getUserMap(), system.getAccountMap());


            case "deleteAccount":
                return new DeleteAccount(command, system.getAccountMap(), mapper, output);


            case "printUsers":
                return new PrintUsers(system.getUsers(), mapper, command.getTimestamp(), output);

            case "createCard":
                return new CreateCard(system.getAccountMap().get(command.getAccount()),
                        system.getCardMap(), command.getTimestamp(), command.getEmail());

            case "addFunds":
                return new AddFunds(system.getAccountMap().get(command.getAccount()),
                        command.getAmount(), command.getTimestamp());

            case "createOneTimeCard":
                return new CreateOneTimeCard(system.getAccountMap().get(command.getAccount()),
                        system.getCardMap(), command.getTimestamp());

            case "deleteCard":
                return new RemoveCard(command.getCardNumber(), system.getCardMap(),
                                      command.getTimestamp());


            case "payOnline":
                Commerciant commerciant = system.getCommerciantMap().get(command.getCommerciant());
                if (commerciant == null) {
                    commerciant = system.getCommerciantMap().get(command.getAccount());
                }
                return new PayOnline(system.getCardMap().get(command.getCardNumber()),
                        command.getAmount(), command.getCurrency(), command.getEmail(),
                        output, mapper, command.getTimestamp(), system.getCardMap(),
                        system.getConverter(), commerciant);


            case "sendMoney":
                return new SendMoney(command.getDescription(),
                        system.getUserMap().get(command.getEmail()), command.getAmount(),
                        system.getConverter(), command.getAccount(), command.getReceiver(),
                        command.getTimestamp(), system.getAccountMap());


            case "printTransactions":
                return new PrintTransactions(system.getUserMap().get(command.getEmail()),
                        output, mapper, command.getTimestamp());


            case "checkCardStatus":
                return new CheckCardStatus(system.getCardMap().get(command.getCardNumber()),
                        command.getTimestamp(), output, mapper);


            case "setMinBalance":
                return new SetMinimumBalance(command.getAmount(),
                        system.getAccountMap().get(command.getAccount()));


            case "setAlias":
                return new SetAlias(command.getAlias(),
                        system.getAccountMap().get(command.getAccount()),
                        system.getUserMap().get(command.getEmail()));


            case "splitPayment":
                return new SplitPayment(command.getAccounts(), command.getTimestamp(),
                        command.getCurrency(), command.getAmount(), system.getAccountMap(),
                        system.getConverter());


            case "report":
                return new PrintReport(system.getAccountMap().get(command.getAccount()),
                        command.getStartTimestamp(), command.getEndTimestamp(), output, mapper,
                        command.getTimestamp());


            case "spendingsReport":
                return new SpendingsReport(system.getAccountMap().get(command.getAccount()),
                        command.getStartTimestamp(), command.getEndTimestamp(), output, mapper,
                        command.getTimestamp());


            case "changeInterestRate":
                return new ChangeInterestRate(system.getAccountMap().get(command.getAccount()),
                        command.getInterestRate(), command.getTimestamp(), output, mapper);


            case "addInterest":
                return new GetInterest(system.getAccountMap().get(command.getAccount()),
                        command.getTimestamp(), output, mapper);


            case "withdrawSavings":
                return new WithdrawSavings(system.getAccountMap().get(command.getAccount()),
                        command.getAmount(),
                        command.getCurrency(), command.getTimestamp(), mapper, output);
            default:
                return null;

        }
    }

}
