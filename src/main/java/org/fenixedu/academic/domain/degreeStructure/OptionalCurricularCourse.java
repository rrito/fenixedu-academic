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
package org.fenixedu.academic.domain.degreeStructure;

import java.util.List;

import org.fenixedu.academic.domain.ExecutionSemester;
import org.fenixedu.academic.domain.curricularPeriod.CurricularPeriod;
import org.fenixedu.academic.domain.curricularRules.AnyCurricularCourse;
import org.fenixedu.academic.domain.curricularRules.CreditsLimit;
import org.fenixedu.academic.domain.curricularRules.CurricularRuleType;
import org.fenixedu.academic.domain.curriculum.CurricularCourseType;

public class OptionalCurricularCourse extends OptionalCurricularCourse_Base {

    protected OptionalCurricularCourse() {
        super();
    }

    /**
     * - This constructor is used to create a 'special' curricular course that
     * will represent any curricular course accordding to a rule
     */
    public OptionalCurricularCourse(CourseGroup parentCourseGroup, String name, String nameEn, CurricularStage curricularStage,
            CurricularPeriod curricularPeriod, ExecutionSemester beginExecutionPeriod, ExecutionSemester endExecutionPeriod) {

        this();
        setName(name);
        setNameEn(nameEn);
        setCurricularStage(curricularStage);
        setType(CurricularCourseType.OPTIONAL_COURSE);
        new Context(parentCourseGroup, this, curricularPeriod, beginExecutionPeriod, endExecutionPeriod);
    }

    @Override
    public boolean isOptionalCurricularCourse() {
        return true;
    }

    @Override
    public Double getMaxEctsCredits(final ExecutionSemester executionSemester) {
        final CreditsLimit creditsLimitRule = getCreditsLimitRule(executionSemester);
        if (creditsLimitRule != null) {
            return creditsLimitRule.getMaximumCredits();
        }
        final AnyCurricularCourse anyCurricularCourseRule = getAnyCurricularCourseRule(executionSemester);
        if (anyCurricularCourseRule != null) {
            return anyCurricularCourseRule.hasCredits() ? anyCurricularCourseRule.getCredits() : 0;
        }
        return Double.valueOf(0d);
    }

    @Override
    public Double getMinEctsCredits(ExecutionSemester executionSemester) {
        final CreditsLimit creditsLimitRule = getCreditsLimitRule(executionSemester);
        if (creditsLimitRule != null) {
            return creditsLimitRule.getMinimumCredits();
        }
        final AnyCurricularCourse anyCurricularCourseRule = getAnyCurricularCourseRule(executionSemester);
        if (anyCurricularCourseRule != null) {
            return anyCurricularCourseRule.hasCredits() ? anyCurricularCourseRule.getCredits() : 0;
        }
        return Double.valueOf(0d);
    }

    private AnyCurricularCourse getAnyCurricularCourseRule(final ExecutionSemester executionSemester) {
        final List<AnyCurricularCourse> result =
                (List<AnyCurricularCourse>) getCurricularRules(CurricularRuleType.ANY_CURRICULAR_COURSE, executionSemester);
        // must have only one
        return result.isEmpty() ? null : (AnyCurricularCourse) result.iterator().next();
    }

}
