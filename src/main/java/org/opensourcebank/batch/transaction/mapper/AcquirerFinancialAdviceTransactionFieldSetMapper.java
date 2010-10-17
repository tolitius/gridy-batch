package org.opensourcebank.batch.transaction.mapper;

import org.opensourcebank.transaction.iso8583.AcquirerFinancialAdviceTransaction;
import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.validation.BindException;

/**
 * <p>Maps a {@link org.springframework.batch.item.file.transform.FieldSet} into
 *    {@link org.opensourcebank.transaction.iso8583.AcquirerFinancialAdviceTransaction} </p>
 *
 * @author anatoly.polinsky
 */
public class AcquirerFinancialAdviceTransactionFieldSetMapper implements FieldSetMapper<AcquirerFinancialAdviceTransaction> {

    public AcquirerFinancialAdviceTransaction mapFieldSet( FieldSet fieldSet ) {

        AcquirerFinancialAdviceTransaction transaction = new AcquirerFinancialAdviceTransaction();

        transaction.setExternalId( fieldSet.readLong( "TX_EXTERNAL_ID" ) );

        return transaction;
    }
}
