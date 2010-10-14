package org.opensourcebank.transaction.iso8583;

/**
 * <p>Confirmation of receipt of reversal advice.</p>
 *
 * @author anatoly.polinsky
 */
public class IssuerReversalResponseTransaction implements ISO8583Transaction {

    private static final long serialVersionUID = 1705852075002822994L;
    
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
