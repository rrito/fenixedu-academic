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
package org.fenixedu.academic.domain.accounting.events.serviceRequests;

import org.fenixedu.academic.domain.Person;
import org.fenixedu.academic.domain.accounting.EntryType;
import org.fenixedu.academic.domain.accounting.EventType;
import org.fenixedu.academic.domain.administrativeOffice.AdministrativeOffice;
import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.academic.domain.serviceRequests.ExtraExamRequest;
import org.fenixedu.academic.util.Money;

import pt.utl.ist.fenix.tools.resources.LabelFormatter;

public class PastExtraExamRequestEvent extends PastExtraExamRequestEvent_Base implements IPastRequestEvent {

    protected PastExtraExamRequestEvent() {
        super();
    }

    public PastExtraExamRequestEvent(final AdministrativeOffice administrativeOffice, final Person person,
            final ExtraExamRequest request) {
        this();
        super.init(administrativeOffice, EventType.PAST_EXTRA_EXAM_REQUEST, person, request);
    }

    @Override
    public void setPastAmount(Money amount) {
        throw new DomainException("error.accounting.events.cannot.modify.pastAmount");
    }

    @Override
    public LabelFormatter getDescriptionForEntryType(EntryType entryType) {
        final LabelFormatter labelFormatter = new LabelFormatter();

        labelFormatter.appendLabel(entryType.name(), LabelFormatter.ENUMERATION_RESOURCES);
        if (getAcademicServiceRequest().getExecutionYear() != null) {
            labelFormatter.appendLabel(" - " + getExecutionYear().getYear());
        }
        return labelFormatter;
    }

}
