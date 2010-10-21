package org.opensourcebank.transaction.repository;

import org.opensourcebank.transaction.iso8583.Iso8583Transaction;

/**
 * <p>ISO 8583 transaction persistence strategy interface</p>
 *
 * @author anatoly.polinsky
 */
public interface Iso8583TransactionRepository {
    
    public void create( Iso8583Transaction tx );    // C
    public Iso8583Transaction findById( Long id );  // R
    public void update( Iso8583Transaction tx );    // U
    public void delete( Iso8583Transaction tx );    // D

    public Long size();
}
