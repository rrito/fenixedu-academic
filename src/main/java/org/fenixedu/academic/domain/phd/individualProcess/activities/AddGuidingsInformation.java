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
package org.fenixedu.academic.domain.phd.individualProcess.activities;

import java.util.List;

import org.fenixedu.academic.domain.phd.PhdIndividualProgramProcess;
import org.fenixedu.academic.domain.phd.PhdParticipant;
import org.fenixedu.academic.domain.phd.PhdParticipantBean;
import org.fenixedu.academic.domain.phd.PhdProgramDocumentUploadBean;
import org.fenixedu.academic.domain.phd.candidacy.PhdGuiderAcceptanceLetter;
import org.fenixedu.bennu.core.domain.User;

public class AddGuidingsInformation extends PhdIndividualProgramProcessActivity {

    @Override
    protected void activityPreConditions(PhdIndividualProgramProcess arg0, User arg1) {
        // no precondition to check
    }

    @SuppressWarnings("unchecked")
    @Override
    protected PhdIndividualProgramProcess executeActivity(PhdIndividualProgramProcess process, User userView, Object object) {
        for (final PhdParticipantBean bean : (List<PhdParticipantBean>) object) {
            PhdParticipant guiding = process.addGuiding(bean);
            if (bean.getGuidingAcceptanceLetter() != null && bean.getGuidingAcceptanceLetter().getFileContent() != null) {
                PhdProgramDocumentUploadBean acceptanceLetter = bean.getGuidingAcceptanceLetter();
                new PhdGuiderAcceptanceLetter(guiding, acceptanceLetter.getType(), "", bean.getGuidingAcceptanceLetter()
                        .getFileContent(), bean.getGuidingAcceptanceLetter().getFilename(),
                        userView != null ? userView.getPerson() : process.getPerson());
            }
        }
        return process;
    }
}