package org.poo.transactions;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class Transaction {

    private int timestamp;
    private String description;

    public Transaction(final int timestamp, final String description) {
        this.timestamp = timestamp;
        this.description = description;
    }
    /**
     * will print the details of the transaction
     * it will also be used in other simple particular transactions
     * @param mapper used to create the ObjectNode
     * @return the node which will be added to the output node
     */
    public ObjectNode print(final ObjectMapper mapper) {
        ObjectNode result = mapper.createObjectNode();
        result.put("timestamp", timestamp);
        result.put("description", description);
        return result;
    }

}
