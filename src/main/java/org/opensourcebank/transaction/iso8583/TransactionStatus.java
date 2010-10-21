package org.opensourcebank.transaction.iso8583;

/**
 * <p>ISO 8583 transaction status that reflects a runtime / last known transaction state</p>
 *
 * @author anatoly.polinsky
 */
public enum TransactionStatus {

    STARTING,
    IN_PROGRESS,
    FAILED,
    UNKNOWN,
    COMPLETED;
}
