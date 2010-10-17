package org.opensourcebank.transaction.iso8583;

/**
 * <p>Issuer response to a point-of-sale terminal for authorization for a cardholder purchase.</p>
 *
 * @author anatoly.polinsky
 */
public class IssuerResponseTransaction  extends AbstractIso8583Transaction {

    private static final long serialVersionUID = -595562864790822371L;

    // some relevant properties.. and accessors

    @Override
    public String toString() {
        return "| ID: " + getId() + " \t| " +
                this.getClass().getSimpleName() + "\t\t| external ID: " + getExternalId() + " | " +
                getStatus() + "\t| ";
    }

}