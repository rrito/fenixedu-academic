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
package org.fenixedu.academic.ui.renderers.converters;

import org.fenixedu.academic.domain.time.calendarStructure.AcademicInterval;

import pt.ist.fenixWebFramework.renderers.components.converters.BiDirectionalConverter;

public class AcademicIntervalConverter extends BiDirectionalConverter {

    @Override
    public Object convert(Class type, Object value) {
        return AcademicInterval.getAcademicIntervalFromResumedString((String) value);
    }

    @Override
    public String deserialize(Object object) {
        AcademicInterval academicInterval = (AcademicInterval) object;
        return academicInterval.getResumedRepresentationInStringFormat();
    }
}
