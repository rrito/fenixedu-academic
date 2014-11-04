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
/*
 * Created on Dec 16, 2003 by jpvl
 *  
 */
package org.fenixedu.academic.ui.struts.action.teacher.professorship;

import java.util.Arrays;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.action.DynaActionForm;
import org.fenixedu.academic.domain.ExecutionYear;
import org.fenixedu.academic.domain.Person;
import org.fenixedu.academic.service.services.exceptions.NotAuthorizedException;
import org.fenixedu.academic.service.services.teacher.UpdateProfessorshipWithPerson;
import org.fenixedu.academic.service.services.teacher.professorship.ResponsibleForValidator;
import org.fenixedu.academic.ui.struts.action.departmentAdmOffice.TeacherSearchForExecutionCourseAssociation;
import org.fenixedu.academic.ui.struts.action.teacher.professorship.exception.handler.MaxResponsibleForExceedHandler;
import org.fenixedu.bennu.struts.annotations.ExceptionHandling;
import org.fenixedu.bennu.struts.annotations.Exceptions;
import org.fenixedu.bennu.struts.annotations.Forward;
import org.fenixedu.bennu.struts.annotations.Forwards;
import org.fenixedu.bennu.struts.annotations.Mapping;

import pt.ist.fenixframework.FenixFramework;

/**
 * @author jpvl
 */
@Mapping(module = "departmentAdmOffice", path = "/updateTeacherExecutionYearExecutionCourseResponsabilities",
        formBean = "teacherExecutionCourseResponsabilities", functionality = TeacherSearchForExecutionCourseAssociation.class)
@Forwards({ @Forward(name = "successfull-update", path = "/departmentAdmOffice/showTeacherProfessorshipsForManagement.do") })
@Exceptions(value = {
        @ExceptionHandling(type = ResponsibleForValidator.InvalidCategory.class, key = "message.professorship.invalidCategory",
                handler = org.apache.struts.action.ExceptionHandler.class, path = "/showTeacherProfessorshipsForManagement.do",
                scope = "request"),
        @ExceptionHandling(type = ResponsibleForValidator.MaxResponsibleForExceed.class,
                key = "message.professorship.numberOfResponsibleForExceeded", handler = MaxResponsibleForExceedHandler.class,
                path = "/showTeacherProfessorshipsForManagement.do", scope = "request") })
public class UpdateTeacherExecutionCourseResponsabilitiesAction extends Action {

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws Exception {

        DynaActionForm teacherExecutionYearResponsabilitiesForm = (DynaActionForm) form;
        String[] executionCourseResponsabilities =
                (String[]) teacherExecutionYearResponsabilitiesForm.get("executionCourseResponsability");

        String teacherId = (String) teacherExecutionYearResponsabilitiesForm.get("teacherName");
        String executionYearId = (String) teacherExecutionYearResponsabilitiesForm.get("executionYearId");
        Person person = Person.readPersonByUsername(teacherId);
        ExecutionYear executionYear = FenixFramework.getDomainObject(executionYearId);
        try {
            UpdateProfessorshipWithPerson.run(person, executionYear, Arrays.asList(executionCourseResponsabilities));
        } catch (NotAuthorizedException e) {
            ActionMessages messages = new ActionMessages();
            ActionMessage message = new ActionMessage("error.teacher.not.belong.department");
            messages.add("error.teacher.not.belong.department", message);
            addMessages(request, messages);
        }

        return mapping.findForward("successfull-update");
    }
}
