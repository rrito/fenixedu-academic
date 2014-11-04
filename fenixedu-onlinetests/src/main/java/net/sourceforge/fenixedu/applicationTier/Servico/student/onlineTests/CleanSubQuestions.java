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
 * Created on 28/Ago/2003
 *
 */
package org.fenixedu.academic.service.services.student.onlineTests;

import static org.fenixedu.academic.predicate.AccessControl.check;

import java.util.Set;

import org.fenixedu.academic.service.services.exceptions.FenixServiceException;
import org.fenixedu.academic.domain.onlineTests.DistributedTest;
import org.fenixedu.academic.domain.onlineTests.StudentTestLog;
import org.fenixedu.academic.domain.onlineTests.StudentTestQuestion;
import org.fenixedu.academic.domain.onlineTests.SubQuestion;
import org.fenixedu.academic.domain.onlineTests.utils.ParseSubQuestion;
import org.fenixedu.academic.domain.student.Registration;
import org.fenixedu.academic.predicate.RolePredicates;
import pt.ist.fenixframework.Atomic;

/**
 * @author Susana Fernandes
 */
public class CleanSubQuestions {

    @Atomic
    public static void run(Registration registration, DistributedTest distributedTest, String exerciseCode, Integer itemCode)
            throws FenixServiceException {
        check(RolePredicates.STUDENT_PREDICATE);
        if (distributedTest == null) {
            throw new FenixServiceException();
        }
        Set<StudentTestQuestion> studentTestQuestionList =
                StudentTestQuestion.findStudentTestQuestions(registration, distributedTest);
        for (StudentTestQuestion studentTestQuestion : studentTestQuestionList) {
            if (studentTestQuestion.getQuestion().getExternalId().equals(exerciseCode)) {
                ParseSubQuestion parse = new ParseSubQuestion();
                try {
                    parse.parseStudentTestQuestion(studentTestQuestion);
                } catch (Exception e) {
                    throw new FenixServiceException(e);
                }
                SubQuestion subQuestion = studentTestQuestion.getStudentSubQuestions().iterator().next();
                if (subQuestion.getItemId().equals(studentTestQuestion.getItemId())) {
                    // e a 1ª
                    studentTestQuestion.setResponse(null);
                } else {
                    studentTestQuestion.delete();
                }
            }
        }
        new StudentTestLog(distributedTest, registration, "Apagou resposta da pergunta/alínea: " + itemCode);
        return;
    }

}