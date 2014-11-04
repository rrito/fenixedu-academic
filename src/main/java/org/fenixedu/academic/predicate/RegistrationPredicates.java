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
package org.fenixedu.academic.predicate;

import org.fenixedu.academic.domain.accessControl.academicAdministration.AcademicAccessRule;
import org.fenixedu.academic.domain.accessControl.academicAdministration.AcademicOperationType;
import org.fenixedu.academic.domain.person.RoleType;
import org.fenixedu.academic.domain.student.Registration;
import org.fenixedu.bennu.core.security.Authenticate;

public class RegistrationPredicates {

    public static final AccessControlPredicate<Registration> TRANSIT_TO_BOLONHA = new AccessControlPredicate<Registration>() {
        @Override
        public boolean evaluate(final Registration registration) {
            return AccessControl.getPerson().hasRole(RoleType.MANAGER);
        };
    };

    public static final AccessControlPredicate<Registration> MANAGE_CONCLUSION_PROCESS =
            new AccessControlPredicate<Registration>() {

                @Override
                public boolean evaluate(final Registration registration) {
                    if (AccessControl.getPerson().hasRole(RoleType.MANAGER)) {
                        return true;
                    }

                    return AcademicAccessRule.isProgramAccessibleToFunction(AcademicOperationType.MANAGE_CONCLUSION,
                            registration.getDegree(), Authenticate.getUser());
                }
            };

}
