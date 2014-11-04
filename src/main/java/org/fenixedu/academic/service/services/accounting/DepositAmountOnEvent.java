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
package org.fenixedu.academic.service.services.accounting;

import org.fenixedu.academic.domain.accounting.AccountingTransaction;
import org.fenixedu.academic.domain.accounting.PaymentMode;
import org.fenixedu.academic.dto.accounting.AccountingTransactionDetailDTO;
import org.fenixedu.academic.dto.accounting.DepositAmountBean;

import pt.ist.fenixframework.Atomic;

public class DepositAmountOnEvent {

    @Atomic
    public static AccountingTransaction run(final DepositAmountBean depositAmountBean) {
        return depositAmountBean.getEvent().depositAmount(
                null,
                depositAmountBean.getAmount(),
                depositAmountBean.getEntryType(),
                new AccountingTransactionDetailDTO(depositAmountBean.getWhenRegistered(), PaymentMode.CASH, depositAmountBean
                        .getReason()));

    }

}