package net.sourceforge.fenixedu.dataTransferObject.accounting;

import java.io.Serializable;
import java.math.BigDecimal;

import net.sourceforge.fenixedu.domain.DomainReference;
import net.sourceforge.fenixedu.domain.accounting.EntryType;
import net.sourceforge.fenixedu.domain.accounting.Event;

public class EntryDTO implements Serializable {

    private boolean selected;

    private EntryType entryType;

    private DomainReference<Event> event;

    private BigDecimal totalAmount;

    private BigDecimal payedAmount;

    private BigDecimal amountToPay;

    private String description;

    public EntryDTO(EntryType entryType, Event event, BigDecimal totalAmount, BigDecimal payedAmount,
            BigDecimal amountToPay, String description) {
        setEntryType(entryType);
        setEvent(event);
        setTotalAmount(totalAmount);
        setPayedAmount(payedAmount);
        setAmountToPay(amountToPay);
        setDescription(description);
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public BigDecimal getPayedAmount() {
        return payedAmount;
    }

    public void setPayedAmount(BigDecimal payedAmount) {
        this.payedAmount = payedAmount;
    }

    public EntryType getEntryType() {
        return entryType;
    }

    public void setEntryType(EntryType entryType) {
        this.entryType = entryType;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public BigDecimal getAmountToPay() {
        return amountToPay;
    }

    public void setAmountToPay(BigDecimal amountToPay) {
        this.amountToPay = amountToPay;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Event getEvent() {
        return (this.event != null) ? this.event.getObject() : null;
    }

    public void setEvent(Event event) {
        this.event = (event != null) ? new DomainReference<Event>(event) : null;
    }

}
