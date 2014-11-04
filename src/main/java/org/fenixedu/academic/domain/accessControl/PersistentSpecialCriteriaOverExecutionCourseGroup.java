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
package org.fenixedu.academic.domain.accessControl;

import java.util.Optional;
import java.util.function.Supplier;

import org.fenixedu.academic.domain.ExecutionCourse;

public abstract class PersistentSpecialCriteriaOverExecutionCourseGroup extends
        PersistentSpecialCriteriaOverExecutionCourseGroup_Base {
    protected PersistentSpecialCriteriaOverExecutionCourseGroup() {
        super();
    }

    protected void init(ExecutionCourse executionCourse) {
        setExecutionCourse(executionCourse);
    }

    @Override
    protected void gc() {
        setExecutionCourse(null);
        super.gc();
    }

    protected static <T extends PersistentSpecialCriteriaOverExecutionCourseGroup> T singleton(Class<T> type,
            ExecutionCourse executionCourse, Supplier<T> creator) {
        return singleton(
                () -> (Optional<T>) executionCourse.getSpecialCriteriaOverExecutionCourseGroupSet().stream()
                        .filter(group -> group.getClass() == type).findAny(), creator);
    }
}
