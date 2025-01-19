package org.poo.errors;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class Log {

    private String command, error, description, success;
    private int timestamp, detailsTimestamp;

    public static class Builder {
        private String command, error = null, description = null, success = null;
        private int timestamp, detailsTimestamp = 0;
        public Builder(final String command, final int timestamp) {
            this.command = command;
            this.timestamp = timestamp;
        }

        /**
         * sets the optional field to the given value
         * @param timestampInt the timestamp used in the "output" section of the command output
         * @return a Builder with the parameter set
         */
        public Builder setDetailsTimestamp(final int timestampInt) {
            this.detailsTimestamp = timestampInt;
            return this;
        }
        /**
         * sets the optional field to the given value
         * @param descriptionText the "description" used in the "output" section of the
         *                        command output
         * @return a Builder with the parameter set
         */
        public Builder setDescription(final String descriptionText) {
            this.description = descriptionText;
            return this;
        }
        /**
         * sets the optional field to the given value
         * @param errorText the error used in the "output" section of the command output
         * @return a Builder with the parameter set
         */
        public Builder setError(final String errorText) {
            this.error = errorText;
            return this;
        }
        /**
         * sets the optional field to the given value
         * @param successText the success text used in the "output" section of the command output
         * @return a Builder with the parameter set
         */
        public Builder setSuccess(final String successText) {
            this.success = successText;
            return this;
        }

        /**
         * will call the constructor of the main class to generate a new instance of it
         * @return a Log after setting the parameters
         */
        public Log build() {
            return new Log(this);
        }

    }

    public Log(final Builder builder) {
        this.command = builder.command;
        this.error = builder.error;
        this.timestamp = builder.timestamp;
        this.detailsTimestamp = builder.detailsTimestamp;
        this.description = builder.description;
        this.success = builder.success;
    }

    /**
     * adds the log to the output node
     * checks which parameters were set
     * @param mapper mapper used to create the ObjectNode
     * @return ObjectNode with the text of the error
     */
    public ObjectNode print(final ObjectMapper mapper) {
        ObjectNode errorNode = mapper.createObjectNode();
        errorNode.put("command", command);
        ObjectNode details = mapper.createObjectNode();
        if (error != null) {
            details.put("error", error);
        } else if (description != null) {
            details.put("description", description);
        } else {
            details.put("success", success);
        }
        if (detailsTimestamp != 0) {
            details.put("timestamp", detailsTimestamp);
        }
        errorNode.put("output", details);
        errorNode.put("timestamp", timestamp);
        return errorNode;
    }

}
