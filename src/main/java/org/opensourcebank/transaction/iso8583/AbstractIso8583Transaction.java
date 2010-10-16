package org.opensourcebank.transaction.iso8583;

/**
 * <p>TODO: Add Description</p>
 *
 * @author anatoly.polinsky
 */
public class AbstractIso8583Transaction implements Iso8583Transaction {

    private Long id;
    private TransactionStatus status = TransactionStatus.UNKNOWN;
    // ... other properties ... //

    public String toString() {
        return "[ ID: " + id + " ] <<< " + this.getClass().getSimpleName() + " | " + status + " | ";
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
}