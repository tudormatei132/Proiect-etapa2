package org.poo.command;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class CommerciantSpendings implements Comparable<CommerciantSpendings> {

    private String commerciant;
    private double total;

    public CommerciantSpendings(final String commerciant, final double total) {
        this.commerciant = commerciant;
        this.total = total;
    }

    /**
     * overridden in order to sort the payments
     * @param o the object to be compared.
     * @return the result of the comparison
     */
    @Override
    public int compareTo(final CommerciantSpendings o) {
        return commerciant.compareTo(o.getCommerciant());
    }

    /**
     * used to add the print the details of payments to different commerciants
     * @param mapper mapper to create the Object Node
     * @return an objectNode that will be added to the output node
     */
    public ObjectNode print(final ObjectMapper mapper) {
        ObjectNode result = mapper.createObjectNode();
        result.put("commerciant", commerciant);
        result.put("total", total);
        return result;
    }

}
