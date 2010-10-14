package org.opensourcebank.transaction.iso8583;

/**
 * <p>Checkout at a hotel. Used to complete transaction initiated with authorization request.</p>
 *
 * @author anatoly.polinsky
 */
public class AcquirerFinancialAdviceTransaction implements ISO8583Transaction {

    private static final long serialVersionUID = -6067227003111990498L;

    private Long id;
    // ... other properties ... //

    public String toString() {
        return this.getClass().getSimpleName() + " ID:" + id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }
}
