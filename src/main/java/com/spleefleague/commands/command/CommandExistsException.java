/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.spleefleague.commands.command;

/**
 *
 * @author notrodash
 */
public class CommandExistsException extends Exception {

    public CommandExistsException(String command) {
        super("Command \"" + command + "\" already exists!");
    }
}
