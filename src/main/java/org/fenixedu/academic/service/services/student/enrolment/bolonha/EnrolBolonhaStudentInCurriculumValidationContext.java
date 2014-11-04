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
package org.fenixedu.academic.service.services.student.enrolment.bolonha;

import java.util.HashSet;
import java.util.List;

import org.fenixedu.academic.domain.Enrolment;
import org.fenixedu.academic.domain.ExecutionSemester;
import org.fenixedu.academic.domain.StudentCurricularPlan;
import org.fenixedu.academic.domain.curricularRules.executors.RuleResult;
import org.fenixedu.academic.domain.curricularRules.executors.ruleExecutors.CurricularRuleLevel;
import org.fenixedu.academic.domain.enrolment.IDegreeModuleToEvaluate;
import org.fenixedu.academic.domain.serviceRequests.documentRequests.CourseLoadRequest;
import org.fenixedu.academic.domain.serviceRequests.documentRequests.ProgramCertificateRequest;
import org.fenixedu.academic.domain.studentCurriculum.CurriculumModule;

import pt.ist.fenixframework.Atomic;

public class EnrolBolonhaStudentInCurriculumValidationContext {

    @Atomic
    public static RuleResult run(final StudentCurricularPlan studentCurricularPlan, final ExecutionSemester executionSemester,
            final List<IDegreeModuleToEvaluate> degreeModulesToEnrol, final List<CurriculumModule> curriculumModulesToRemove,
            final CurricularRuleLevel curricularRuleLevel) {

        for (CurriculumModule module : curriculumModulesToRemove) {
            if (!module.isEnrolment()) {
                continue;
            }

            Enrolment enrolment = (Enrolment) module;

            enrolment.getCourseLoadRequestsSet().retainAll(new HashSet<CourseLoadRequest>());
            enrolment.getProgramCertificateRequestsSet().retainAll(new HashSet<ProgramCertificateRequest>());
        }

        return EnrolBolonhaStudent.run(studentCurricularPlan, executionSemester, degreeModulesToEnrol, curriculumModulesToRemove,
                curricularRuleLevel);
    }
}
