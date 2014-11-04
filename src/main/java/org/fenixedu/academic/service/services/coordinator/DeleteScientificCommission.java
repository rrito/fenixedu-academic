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
package org.fenixedu.academic.service.services.coordinator;

import org.fenixedu.academic.domain.ExecutionDegree;
import org.fenixedu.academic.domain.ScientificCommission;
import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.academic.service.filter.ResponsibleDegreeCoordinatorAuthorizationFilter;
import org.fenixedu.academic.service.services.exceptions.NotAuthorizedException;

import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;

public class DeleteScientificCommission {

    protected void run(String executionDegreeId, ScientificCommission commission) {
        ExecutionDegree executionDegree = FenixFramework.getDomainObject(executionDegreeId);

        if (!executionDegree.getScientificCommissionMembersSet().contains(commission)) {
            throw new DomainException("scientificCommission.delete.incorrectExecutionDegree");
        }

        commission.delete();
    }

    // Service Invokers migrated from Berserk

    private static final DeleteScientificCommission serviceInstance = new DeleteScientificCommission();

    @Atomic
    public static void runDeleteScientificCommission(String executionDegreeId, ScientificCommission commission)
            throws NotAuthorizedException {
        ResponsibleDegreeCoordinatorAuthorizationFilter.instance.execute(executionDegreeId);
        serviceInstance.run(executionDegreeId, commission);
    }

}