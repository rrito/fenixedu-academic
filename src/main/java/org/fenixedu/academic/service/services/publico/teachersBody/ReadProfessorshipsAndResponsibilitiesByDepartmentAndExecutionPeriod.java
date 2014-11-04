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
package org.fenixedu.academic.service.services.publico.teachersBody;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;
import org.fenixedu.academic.domain.CurricularCourse;
import org.fenixedu.academic.domain.Department;
import org.fenixedu.academic.domain.ExecutionCourse;
import org.fenixedu.academic.domain.ExecutionSemester;
import org.fenixedu.academic.domain.ExecutionYear;
import org.fenixedu.academic.domain.Professorship;
import org.fenixedu.academic.domain.Teacher;
import org.fenixedu.academic.dto.InfoCurricularCourse;
import org.fenixedu.academic.dto.InfoProfessorship;
import org.fenixedu.academic.dto.teacher.professorship.DetailedProfessorship;
import org.fenixedu.academic.service.services.exceptions.FenixServiceException;

import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;

public class ReadProfessorshipsAndResponsibilitiesByDepartmentAndExecutionPeriod {

    @Atomic
    public static List run(String departmentId, String executionYearID, Integer semester, Integer teacherType)
            throws FenixServiceException {

        ExecutionYear executionYear = null;
        if (executionYearID != null) {
            executionYear = FenixFramework.getDomainObject(executionYearID);
        }

        final ExecutionSemester executionSemester = executionYear.getExecutionSemesterFor(semester);
        if (semester.intValue() != 0 && executionSemester == null) {
            throw new FenixServiceException("error.noExecutionPeriod");
        }

        final Department department = FenixFramework.getDomainObject(departmentId);
        if (department == null) {
            throw new FenixServiceException("error.noDepartment");
        }

        final List<Teacher> teachers = department.getAllCurrentTeachers();

        Iterator iter = teachers.iterator();

        List professorships = new ArrayList();
        List responsibleFors = new ArrayList();
        while (iter.hasNext()) {
            Teacher teacher = (Teacher) iter.next();
            Collection teacherProfessorships = null;
            if (executionYear == null) {
                teacherProfessorships = teacher.getProfessorships();
            } else {
                if (semester.intValue() == 0) {
                    teacherProfessorships = teacher.getProfessorships(executionYear);
                } else {
                    teacherProfessorships = teacher.getProfessorships(executionSemester);
                }
            }
            if (teacherProfessorships != null) {
                professorships.addAll(teacherProfessorships);
            }

            List teacherResponsibleFors;
            List<Professorship> teacherResponsibleForsAux = null;

            if (executionYear == null) {
                teacherResponsibleFors = teacher.responsibleFors();
            } else {
                teacherResponsibleForsAux = teacher.responsibleFors();
                teacherResponsibleFors = new ArrayList<Professorship>();
                for (Professorship professorship : teacherResponsibleForsAux) {
                    if (professorship.getExecutionCourse().getExecutionPeriod().getExecutionYear().equals(executionYear)) {
                        teacherResponsibleFors.add(professorship);
                    }
                }
            }
            if (teacherResponsibleFors != null) {
                responsibleFors.addAll(teacherResponsibleFors);
            }
        }

        List detailedProfessorships = getDetailedProfessorships(professorships, responsibleFors, teacherType);

        // Cleaning out possible null elements inside the list
        Iterator itera = detailedProfessorships.iterator();
        while (itera.hasNext()) {
            Object dp = itera.next();
            if (dp == null) {
                itera.remove();
            }
        }

        Collections.sort(detailedProfessorships, new Comparator() {

            @Override
            public int compare(Object o1, Object o2) {

                DetailedProfessorship detailedProfessorship1 = (DetailedProfessorship) o1;
                DetailedProfessorship detailedProfessorship2 = (DetailedProfessorship) o2;
                int result =
                        detailedProfessorship1
                                .getInfoProfessorship()
                                .getInfoExecutionCourse()
                                .getExternalId()
                                .compareTo(detailedProfessorship2.getInfoProfessorship().getInfoExecutionCourse().getExternalId());
                if (result == 0
                        && (detailedProfessorship1.getResponsibleFor().booleanValue() || detailedProfessorship2
                                .getResponsibleFor().booleanValue())) {
                    if (detailedProfessorship1.getResponsibleFor().booleanValue()) {
                        return -1;
                    }
                    if (detailedProfessorship2.getResponsibleFor().booleanValue()) {
                        return 1;
                    }
                }

                return result;
            }

        });

        List result = new ArrayList();
        iter = detailedProfessorships.iterator();
        List temp = new ArrayList();
        while (iter.hasNext()) {
            DetailedProfessorship detailedProfessorship = (DetailedProfessorship) iter.next();
            if (temp.isEmpty()
                    || ((DetailedProfessorship) temp.get(temp.size() - 1)).getInfoProfessorship().getInfoExecutionCourse()
                            .equals(detailedProfessorship.getInfoProfessorship().getInfoExecutionCourse())) {
                temp.add(detailedProfessorship);
            } else {
                result.add(temp);
                temp = new ArrayList();
                temp.add(detailedProfessorship);
            }
        }
        if (!temp.isEmpty()) {
            result.add(temp);
        }
        return result;
    }

    protected static List getDetailedProfessorships(List professorships, final List responsibleFors, final Integer teacherType) {

        List detailedProfessorshipList = (List) CollectionUtils.collect(professorships, new Transformer() {

            @Override
            public Object transform(Object input) {

                Professorship professorship = (Professorship) input;

                InfoProfessorship infoProfessorShip = InfoProfessorship.newInfoFromDomain(professorship);

                List executionCourseCurricularCoursesList = getInfoCurricularCourses(professorship.getExecutionCourse());

                DetailedProfessorship detailedProfessorship = new DetailedProfessorship();

                Boolean isResponsible = Boolean.valueOf(professorship.getResponsibleFor());

                if ((teacherType.intValue() == 1) && (!isResponsible.booleanValue())) {
                    return null;
                }

                detailedProfessorship.setResponsibleFor(isResponsible);

                detailedProfessorship.setInfoProfessorship(infoProfessorShip);
                detailedProfessorship.setExecutionCourseCurricularCoursesList(executionCourseCurricularCoursesList);

                return detailedProfessorship;
            }

            private List getInfoCurricularCourses(ExecutionCourse executionCourse) {

                List infoCurricularCourses =
                        (List) CollectionUtils.collect(executionCourse.getAssociatedCurricularCoursesSet(), new Transformer() {

                            @Override
                            public Object transform(Object input) {

                                CurricularCourse curricularCourse = (CurricularCourse) input;

                                InfoCurricularCourse infoCurricularCourse =
                                        InfoCurricularCourse.newInfoFromDomain(curricularCourse);
                                return infoCurricularCourse;
                            }
                        });
                return infoCurricularCourses;
            }
        });

        return detailedProfessorshipList;
    }

}