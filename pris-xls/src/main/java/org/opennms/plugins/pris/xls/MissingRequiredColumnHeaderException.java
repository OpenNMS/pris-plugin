package org.opennms.plugins.pris.xls;

public class MissingRequiredColumnHeaderException extends Exception {

    public MissingRequiredColumnHeaderException(String columnName) {
        this(columnName, null);
    }

    public MissingRequiredColumnHeaderException(String columnName, Throwable cause) {
        super("Required Column-Header is missing: " + columnName, cause);
    }
}