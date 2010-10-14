package org.opensourcebank.transaction.iso8583;

/**
 * <p>Request from a point-of-sale terminal for authorization for a cardholder purchase.</p>
 *
 * @author anatoly.polinsky
 */
public class AuthorizationRequestTransaction implements ISO8583Transaction {

    private static final long serialVersionUID = -7545924740119368008L;

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
