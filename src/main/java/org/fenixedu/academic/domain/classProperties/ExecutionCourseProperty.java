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
package org.fenixedu.academic.domain.classProperties;

import org.fenixedu.bennu.core.domain.Bennu;

/**
 * @author David Santos in Apr 7, 2004
 */

public class ExecutionCourseProperty extends ExecutionCourseProperty_Base {

    public ExecutionCourseProperty() {
        super();
        setRootDomainObject(Bennu.getInstance());
    }

    public void delete() {
        setExecutionCourse(null);
        setRootDomainObject(null);
        super.deleteDomainObject();
    }

}
