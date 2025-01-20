package org.poo.command;

import org.poo.account.User;
import org.poo.system.SplitCustom;

public class AcceptSplit implements Command{

    private User user;
    private int timestamp;
    private String type;
    public AcceptSplit(User user, int timestamp, String type) {
        this.user = user;
        this.timestamp = timestamp;
        this.type = type;
    }
    @Override
    public void execute() {
        if (user == null) {
            return;
        }
        for (SplitCustom request : user.getRequests()) {
            if (request.getType().equals(type)) {
                request.accept();
                return;
            }
        }
    }

}
