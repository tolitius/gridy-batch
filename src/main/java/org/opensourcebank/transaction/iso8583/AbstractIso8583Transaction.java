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

package org.opensourcebank.transaction.iso8583;

/**
 * <p>An abstraction that ensures that all the ISO 8583 transactions have ID and STATUS</p>
 *
 * @author anatoly.polinsky
 */
public class AbstractIso8583Transaction implements Iso8583Transaction {

    // Primary Key
    private Long id;

    // ID that belongs/was assigned by an external system
    private Long externalId;

    private TransactionStatus status = TransactionStatus.UNKNOWN;

    // ... other properties ... //


    public String toString() {
        return "| ID: " + getId() + " \t| " +
                this.getClass().getSimpleName() + "\t| external ID: " + getExternalId() + " | " +
                getStatus() + "\t| ";
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public TransactionStatus getStatus() {
        return status;
    }

    public void setStatus(TransactionStatus status) {
        this.status = status;
    }

    public Long getExternalId() {
        return externalId;
    }

    public void setExternalId(Long externalId) {
        this.externalId = externalId;
    }
}