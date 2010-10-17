package org.opensourcebank.batch.transaction.mapper;

import org.opensourcebank.transaction.iso8583.AcquirerFinancialAdviceTransaction;
import org.opensourcebank.transaction.iso8583.IssuerResponseTransaction;
import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.FieldSet;

/**
 * <p>>Maps a {@link org.springframework.batch.item.file.transform.FieldSet} into
*      {@link org.opensourcebank.transaction.iso8583.IssuerResponseTransaction}</p>
 *
 * @author anatoly.polinsky
 */
public class IssuerResponseTransactionFieldSetMapper implements FieldSetMapper<IssuerResponseTransaction> {

    public IssuerResponseTransaction mapFieldSet( FieldSet fieldSet ) {

        IssuerResponseTransaction transaction = new IssuerResponseTransaction();

        transaction.setExternalId( fieldSet.readLong( "TX_EXTERNAL_ID" ) );

        return transaction;
    }
}