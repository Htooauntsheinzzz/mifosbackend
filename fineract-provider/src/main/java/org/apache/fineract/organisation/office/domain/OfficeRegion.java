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
package org.apache.fineract.organisation.office.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.apache.fineract.infrastructure.core.api.JsonCommand;
import org.apache.fineract.infrastructure.core.domain.AbstractPersistableCustom;
import org.apache.fineract.useradministration.domain.AppUser;

import java.time.LocalDateTime;

@Entity
@Table(name = "m_office_region",uniqueConstraints = {@UniqueConstraint(columnNames = {"region_name"},name = "region_name"),
            @UniqueConstraint(columnNames = {"position"},name ="position")})
@Getter
@Setter
@NoArgsConstructor
@Accessors(chain = true)
public class OfficeRegion extends AbstractPersistableCustom {

    @Column(name = "region_name",nullable = false,length = 300)
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "office_country_id")
    private OfficeCountry officeCountry;

    @Column(name = "description",nullable = true ,length = 500)
    private String description;

    @Column(name ="position" ,nullable = false,unique = true)
    private int position;

    @Column(name = "is_active" ,nullable = false)
    private Boolean isActive;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "createdby_id")
    private AppUser created_userId;

    @Column(name ="created_date")
    private LocalDateTime created_date;


    public static OfficeRegion fromjson(final AppUser user, final OfficeCountry country, final JsonCommand command){
        final String regionParamName = "name";
        final String name = command.stringValueOfParameterNamed(regionParamName);
        final String descriptionParamName = "description";
        final String description = command.stringValueOfParameterNamed(descriptionParamName);
        Integer position = command.integerValueOfParameterNamed("position");
        Boolean isActive = command.booleanObjectValueOfParameterNamed("isActive");
        final LocalDateTime createdDate = LocalDateTime.now();

        return new OfficeRegion(user, country, name, description, position, isActive, createdDate);
    }

    private  OfficeRegion (final AppUser currentUser,final OfficeCountry country,final String name,final String description,final int position,final Boolean isActive,final LocalDateTime created_date) {
        this.name = name;
        this.officeCountry = country;
        this.description = description;
        this.position = position;
        this.isActive = isActive;
        this.created_userId = currentUser;
        this.created_date = created_date;
    }



}
