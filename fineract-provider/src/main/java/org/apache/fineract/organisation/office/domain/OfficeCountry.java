/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements. See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership. The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICEN SE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.apache.fineract.organisation.office.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.apache.fineract.infrastructure.core.api.JsonCommand;
import org.apache.fineract.infrastructure.core.domain.AbstractPersistableCustom;
import org.apache.fineract.useradministration.domain.AppUser;

import java.io.Serializable;
import java.math.BigInteger;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "m_office_country", uniqueConstraints = { @UniqueConstraint(columnNames = { "country_name" }, name = "uni_country_name"),
        @UniqueConstraint(columnNames = { "position" }, name = "uni_position") })
@Getter
@Setter
@NoArgsConstructor
@Accessors(chain = true)
public class OfficeCountry extends AbstractPersistableCustom implements Serializable {

        @Column(name = "country_name",nullable = false,length = 300)
        private String name;

        @Column(name = "description",nullable = true ,length = 500)
        private String description;

        @Column(name ="position" ,nullable = false,unique = true)
        private int position;

        @Column(name = "active" ,nullable = false)
        private Boolean isActive;

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "createdby_id")
        private AppUser created_userId;

        @Column(name ="created_date")
        private LocalDateTime created_date;

        public static OfficeCountry fromJson(final AppUser current_user,final JsonCommand command){
                final String name = command.stringValueOfParameterNamed("name");
                final String description = command.stringValueOfParameterNamed("description");
                Integer position =command.integerValueOfParameterNamed("position");
                Boolean isActiveApiValue = command.booleanObjectValueOfParameterNamed("isActive");
            final LocalDateTime createdDate = LocalDateTime.now();

                return  new OfficeCountry(current_user,name,description,position,isActiveApiValue,createdDate);
        }
        private OfficeCountry(final AppUser currentUser,final String countryName, final String description, final int position, final boolean isActive, final LocalDateTime created_date) {
                this.name = countryName;
                this.description = description;
                this.position = position;
                this.isActive = isActive;
                this.created_userId = currentUser;
                this.created_date = created_date;

        }


}
