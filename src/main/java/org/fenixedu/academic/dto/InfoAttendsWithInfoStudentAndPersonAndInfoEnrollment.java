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
 * Created on Dec 9, 2004
 *
 */
package org.fenixedu.academic.dto;

import org.fenixedu.academic.domain.Attends;

/**
 * @author André Fernandes / João Brito
 */
public class InfoAttendsWithInfoStudentAndPersonAndInfoEnrollment extends InfoFrequentaWithInfoStudentAndPerson {
    @Override
    public void copyFromDomain(Attends frequenta) {
        super.copyFromDomain(frequenta);
        if (frequenta != null) {
            setInfoEnrolment(InfoEnrolment.newInfoFromDomain(frequenta.getEnrolment()));
        }
    }

    public static InfoFrequenta newInfoFromDomain(Attends attend) {
        InfoAttendsWithInfoStudentAndPersonAndInfoEnrollment infoAttend = null;
        if (attend != null) {
            infoAttend = new InfoAttendsWithInfoStudentAndPersonAndInfoEnrollment();
            infoAttend.copyFromDomain(attend);
        }

        return infoAttend;
    }

}
