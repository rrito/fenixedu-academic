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
package org.fenixedu.academic.domain.phd.candidacy;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.fenixedu.academic.domain.Person;
import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.academic.domain.phd.PhdIndividualProgramProcess;
import org.fenixedu.academic.domain.phd.alert.PhdCandidacyRefereeAlert;
import org.fenixedu.academic.util.Bundle;
import org.fenixedu.bennu.core.i18n.BundleUtil;

import pt.ist.fenixframework.Atomic;

public class PhdCandidacyReferee extends PhdCandidacyReferee_Base {

    private PhdCandidacyReferee() {
        super();
    }

    public PhdCandidacyReferee(final PhdProgramCandidacyProcess process, final PhdCandidacyRefereeBean bean) {
        this();
        String[] args = {};

        if (process == null) {
            throw new DomainException("error.PhdCandidacyReferee.invalid.process", args);
        }
        String obj = bean.getName();
        String[] args1 = {};
        if (obj == null || obj.isEmpty()) {
            throw new DomainException("error.PhdCandidacyReferee.invalid.name", args1);
        }
        String obj1 = bean.getEmail();
        String[] args2 = {};
        if (obj1 == null || obj1.isEmpty()) {
            throw new DomainException("error.PhdCandidacyReferee.invalid.email", args2);
        }

        if (process.getCandidacyRefereeByEmail(bean.getEmail()) != null) {
            throw new DomainException("error.PhdCandidacyReferee.for.email.exists");
        }

        setPhdProgramCandidacyProcess(process);
        setName(bean.getName());
        setEmail(bean.getEmail());
        setInstitution(bean.getInstitution());
        setValue(UUID.randomUUID().toString());

        // new PhdCandidacyRefereeAlert(this);
        sendEmail();
    }

    @Override
    public boolean hasCandidacyProcess() {
        return getPhdProgramCandidacyProcess() != null;
    }

    public boolean isLetterAvailable() {
        return getLetter() != null;
    }

    public PhdIndividualProgramProcess getIndividualProgramProcess() {
        return getPhdProgramCandidacyProcess().getIndividualProgramProcess();
    }

    @Atomic
    public void sendEmail() {
        sendEmail(createSubject(), createBody());
    }

    private String createSubject() {
        return String.format(BundleUtil.getString(Bundle.PHD, "message.phd.email.subject.referee"), getCandidatePerson()
                .getName(), getCandidatePerson().getName());
    }

    public Person getCandidatePerson() {
        return getPhdProgramCandidacyProcess().getPerson();
    }

    private String createBody() {
        return getPhdProgramCandidacyProcess().getPublicPhdCandidacyPeriod().getEmailMessageBodyForRefereeForm(this);
    }

    public void delete() {
        disconnect();
        deleteDomainObject();
    }

    private void disconnect() {
        if (getLetter() != null) {
            throw new DomainException("error.PhdCandidacyReferee.has.letter");
        }

        setPhdProgramCandidacyProcess(null);
        setRootDomainObject(null);

        List<PhdCandidacyRefereeAlert> alerts = new ArrayList<PhdCandidacyRefereeAlert>();
        alerts.addAll(getAlertsSet());

        for (PhdCandidacyRefereeAlert phdCandidacyRefereeAlert : alerts) {
            removeAlerts(phdCandidacyRefereeAlert);
        }
    }

    public String getRefereeSubmissionFormLinkPt() {
        if (getPhdProgramCandidacyProcess().getPublicPhdCandidacyPeriod().isInstitutionCandidacyPeriod()) {
            InstitutionPhdCandidacyPeriod publicPhdCandidacyPeriod =
                    (InstitutionPhdCandidacyPeriod) getPhdProgramCandidacyProcess().getPublicPhdCandidacyPeriod();

            return publicPhdCandidacyPeriod.getRefereeSubmissionFormLinkPt(this);
        }

        return null;
    }

    public String getRefereeSubmissionFormLinkEn() {
        if (getPhdProgramCandidacyProcess().getPublicPhdCandidacyPeriod().isInstitutionCandidacyPeriod()) {
            InstitutionPhdCandidacyPeriod publicPhdCandidacyPeriod =
                    (InstitutionPhdCandidacyPeriod) getPhdProgramCandidacyProcess().getPublicPhdCandidacyPeriod();

            return publicPhdCandidacyPeriod.getRefereeSubmissionFormLinkEn(this);
        }

        return null;

    }

}
