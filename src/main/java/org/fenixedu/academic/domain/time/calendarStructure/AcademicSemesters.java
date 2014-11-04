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
package org.fenixedu.academic.domain.time.calendarStructure;

import org.fenixedu.academic.domain.time.AcademicPeriodType;
import org.fenixedu.academic.domain.time.chronologies.durationFields.AcademicSemestersDurationFieldType;
import org.joda.time.DurationFieldType;
import org.joda.time.PeriodType;

public class AcademicSemesters extends AcademicPeriod {

    protected AcademicSemesters(int period, String name) {
        super(period, name);
    }

    @Override
    public DurationFieldType getFieldType() {
        return AcademicSemestersDurationFieldType.academicSemesters();
    }

    @Override
    public PeriodType getPeriodType() {
        return AcademicPeriodType.academicSemesters();
    }

    @Override
    public float getWeight() {
        return getValue() / 2f;
    }

    @Override
    public AcademicPeriod getPossibleChild() {
        return getValue() > 1 ? AcademicPeriod.SEMESTER : AcademicPeriod.TRIMESTER;
    }

}
