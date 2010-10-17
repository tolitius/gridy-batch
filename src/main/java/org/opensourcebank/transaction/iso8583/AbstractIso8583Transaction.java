package org.opensourcebank.transaction.iso8583;

/**
 * <p>TODO: Add Description</p>
 *
 * @author anatoly.polinsky
 */
public class AbstractIso8583Transaction implements Iso8583Transaction {

    // Primary Key
    private Long id;

    // ID that belongs/was assigned by an external system
    private Long externalId;

    private TransactionStatus status = TransactionStatus.UNKNOWN;

    // ... other properties ... //


    public String toString() {
        return "| ID: " + getId() + " \t| " +
                this.getClass().getSimpleName() + "\t| external ID: " + getExternalId() + " | " +
                getStatus() + "\t| ";
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public TransactionStatus getStatus() {
        return status;
    }

    public void setStatus(TransactionStatus status) {
        this.status = status;
    }

    public Long getExternalId() {
        return externalId;
    }

    public void setExternalId(Long externalId) {
        this.externalId = externalId;
    }
}