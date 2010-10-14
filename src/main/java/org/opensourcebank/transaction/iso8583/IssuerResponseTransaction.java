package org.opensourcebank.transaction.iso8583;

/**
 * <p>Issuer response to a point-of-sale terminal for authorization for a cardholder purchase.</p>
 *
 * @author anatoly.polinsky
 */
public class IssuerResponseTransaction implements ISO8583Transaction {

    private static final long serialVersionUID = -595562864790822371L;
    
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
