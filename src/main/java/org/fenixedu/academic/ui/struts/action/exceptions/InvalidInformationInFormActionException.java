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
package org.fenixedu.academic.ui.struts.action.exceptions;

/**
 * 
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt) Joana Mota (jccm@rnl.ist.utl.pt)
 * 
 */
public class InvalidInformationInFormActionException extends FenixActionException {

    public static String key = "error.exception.invalidInformationInForm";

    public InvalidInformationInFormActionException(String key) {
        super(key);
    }

    public InvalidInformationInFormActionException(Throwable cause) {
        super(key, cause);
    }

    public InvalidInformationInFormActionException(Object value, Throwable cause) {
        super(key, value, cause);
    }

    public InvalidInformationInFormActionException(Object[] values, Throwable cause) {
        super(key, values, cause);
    }

    public static String getKey() {
        return key;
    }

    public static void setKey(String key) {
        InvalidInformationInFormActionException.key = key;
    }

}