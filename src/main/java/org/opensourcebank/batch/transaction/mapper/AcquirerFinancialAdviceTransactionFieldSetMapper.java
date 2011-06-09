/*
 * Copyright 2011 Anatoly Polinsky
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

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
