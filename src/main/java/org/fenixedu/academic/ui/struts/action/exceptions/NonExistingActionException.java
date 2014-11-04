/**
 * Copyright © 2002 Instituto Superior Técnico
 *
 * This file is part of FenixEdu Core.
 *
 * FenixEdu Core is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FenixEdu Core is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with FenixEdu Core.  If not, see <http://www.gnu.org/licenses/>.
 */
/*
 * ExistingActionException.java
 *
 * Febuary 28th, 2003, Sometime in the morning
 */

package org.fenixedu.academic.ui.struts.action.exceptions;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionForward;

/**
 * 
 * @author Luis Cruz & Nuno Nunes & João Mota
 */

public class NonExistingActionException extends FenixActionException {

    public static String key = "error.exception.nonExisting";

    public NonExistingActionException(String value) {
        super(key, value);
    }

    public NonExistingActionException(String key, String value) {
        super(key, value);
    }

    public NonExistingActionException(Throwable cause) {

        super(key, cause);
    }

    public NonExistingActionException(Object value, Throwable cause) {
        super(key, value, cause);
    }

    public NonExistingActionException(String key, Object value, Throwable cause) {
        super(key, value, cause);
    }

    public NonExistingActionException(Object[] values, Throwable cause) {
        super(key, values, cause);
    }

    public NonExistingActionException(String text, ActionForward actionForward) {
        super(actionForward);
        error = new ActionError(text);
    }

    /**
     * @return String
     */
    public static String getKey() {
        return key;
    }

    /**
     * Sets the key.
     * 
     * @param key
     *            The key to set
     */
    public static void setKey(String key) {
        NonExistingActionException.key = key;
    }

    @Override
    public String toString() {
        String result = "[NonExistingActionException\n";
        result += "property" + this.getProperty() + "\n";
        result += "error" + this.getError() + "\n";
        result += "cause" + this.getCause() + "\n";
        result += "]";
        return result;
    }
    // TODO find a way of internationalizing the message passed as argument to
    // the exception error message of the resource bundle
}