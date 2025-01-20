package org.poo.command;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.account.User;

import java.util.ArrayList;

public class PrintUsers implements Command {

    private ArrayList<User> users;
    private ObjectMapper mapper;
    private int timestamp;
    private ArrayNode outputNode;


    public PrintUsers(final ArrayList<User> users, final ObjectMapper mapper,
                      final int timestamp, final ArrayNode outputNode) {
        this.users = users;
        this.mapper = mapper;
        this.timestamp = timestamp;
        this.outputNode = outputNode;
    }

    /**
     * will iterate through the user ArrayList and call the printUser command from the User class
     * which will add to the output the details for every user
     */
    public void execute() {
        ObjectNode output = mapper.createObjectNode();
        output.put("command", "printUsers");
        output.put("timestamp", timestamp);

        ArrayNode usersArray = mapper.createArrayNode();
        for (User user : users) {
            usersArray.add(user.printUser(mapper));
        }
        output.put("output", usersArray);
        outputNode.add(output);
    }

}
