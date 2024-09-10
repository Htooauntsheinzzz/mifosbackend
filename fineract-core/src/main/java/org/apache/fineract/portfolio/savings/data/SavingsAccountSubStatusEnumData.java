/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements. See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership. The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.apache.fineract.portfolio.savings.data;

import java.io.Serializable;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Immutable data object represent savings account sub-status enumerations.
 */
@Getter
@RequiredArgsConstructor
public class SavingsAccountSubStatusEnumData implements Serializable {

    private final Long id;
    private final String code;
    private final String value;
    private final boolean none;
    private final boolean inactive;
    private final boolean dormant;
    private final boolean escheat;
    private final boolean block;
    private final boolean blockCredit;
    private final boolean blockDebit;

}
