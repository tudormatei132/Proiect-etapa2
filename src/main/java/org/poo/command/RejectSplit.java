package org.poo.command;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.account.User;
import org.poo.errors.Log;
import org.poo.system.SplitCustom;

public class RejectSplit implements Command {

    private User user;
    private int timestamp;
    private String type;
    private ArrayNode output;
    private ObjectMapper mapper;

    public RejectSplit(User user, int timestamp, String type, ArrayNode output,
                       ObjectMapper mapper) {
        this.user = user;
        this.timestamp = timestamp;
        this.type = type;
        this.output = output;
        this.mapper = mapper;
    }

    @Override
    public void execute() {
        if (user == null) {
            Log error = new Log.Builder("rejectSplitPayment", timestamp).setDetailsTimestamp(timestamp)
                    .setDescription("User not found").build();
            output.add(error.print(mapper));
            return;
        }
        for (SplitCustom request : user.getRequests()) {
            if (request.getType().equals(type)) {
                request.reject();
                return;
            }
        }
    }
}

