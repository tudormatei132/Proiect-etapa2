package org.poo.command;



public interface Command {
    /**
     * will execute different type of commands from the input
     * I used an interface with this method so I can use the Factory Pattern to get
     * an instance of a Class that implements this interface, then only call
     * command.execute(), using the Command Pattern
     */
    void execute();
}
