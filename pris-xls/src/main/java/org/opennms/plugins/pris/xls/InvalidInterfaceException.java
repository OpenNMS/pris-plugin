package org.opennms.plugins.pris.xls;

public class InvalidInterfaceException extends Exception {

    public InvalidInterfaceException(String string, IllegalArgumentException ex) {
        super(string, ex);
    }
}
