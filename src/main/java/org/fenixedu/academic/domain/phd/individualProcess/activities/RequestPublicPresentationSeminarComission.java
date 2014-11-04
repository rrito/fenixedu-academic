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

import org.fenixedu.academic.domain.caseHandling.PreConditionNotValidException;
import org.fenixedu.academic.domain.caseHandling.Process;
import org.fenixedu.academic.domain.phd.PhdIndividualProgramProcess;
import org.fenixedu.academic.domain.phd.PhdIndividualProgramProcessState;
import org.fenixedu.academic.domain.phd.alert.AlertService;
import org.fenixedu.academic.domain.phd.seminar.PublicPresentationSeminarProcess;
import org.fenixedu.academic.domain.phd.seminar.PublicPresentationSeminarProcessBean;
import org.fenixedu.bennu.core.domain.User;

public class RequestPublicPresentationSeminarComission extends PhdIndividualProgramProcessActivity {

    @Override
    protected void activityPreConditions(PhdIndividualProgramProcess process, User userView) {
        if (process.getSeminarProcess() != null || process.getActiveState() != PhdIndividualProgramProcessState.WORK_DEVELOPMENT) {
            throw new PreConditionNotValidException();
        }

        if (!process.isAllowedToManageProcess(userView) && !process.isGuider(userView.getPerson())) {
            throw new PreConditionNotValidException();
        }
    }

    @Override
    protected PhdIndividualProgramProcess executeActivity(PhdIndividualProgramProcess individualProcess, User userView,
            Object object) {

        PublicPresentationSeminarProcessBean bean = (PublicPresentationSeminarProcessBean) object;
        bean.setPhdIndividualProgramProcess(individualProcess);

        final PublicPresentationSeminarProcess publicPresentationSeminarProcess =
                Process.createNewProcess(userView, PublicPresentationSeminarProcess.class, object);

        if (((PublicPresentationSeminarProcessBean) object).getGenerateAlert()) {
            AlertService.alertCoordinators(individualProcess,
                    "message.phd.alert.public.presentation.seminar.comission.definition.subject",
                    "message.phd.alert.public.presentation.seminar.comission.definition.body");

        }

        return individualProcess;
    }
}