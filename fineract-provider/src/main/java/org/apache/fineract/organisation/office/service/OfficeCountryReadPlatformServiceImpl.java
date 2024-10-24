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
package org.apache.fineract.organisation.office.service;

import lombok.RequiredArgsConstructor;
import org.apache.fineract.infrastructure.core.service.SearchParameters;
import org.apache.fineract.infrastructure.core.service.database.DatabaseSpecificSQLGenerator;
import org.apache.fineract.infrastructure.security.service.PlatformSecurityContext;
import org.apache.fineract.infrastructure.security.utils.ColumnValidator;
import org.apache.fineract.organisation.office.data.OfficeCountryData;
import org.apache.fineract.organisation.office.domain.OfficeCountry;
import org.apache.fineract.organisation.office.domain.OfficeCountryRepository;
import org.apache.fineract.organisation.office.domain.OfficeCountryRepositoryWrapper;
import org.apache.fineract.organisation.office.mapper.CountryLocationMapper;
import org.apache.fineract.useradministration.domain.AppUser;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OfficeCountryReadPlatformServiceImpl implements OfficeCountryReadPlatformService{


    private final JdbcTemplate jdbcTemplate;
    private final DatabaseSpecificSQLGenerator databaseSpecificSQLGenerator;
    private final PlatformSecurityContext context;
    private final ColumnValidator columnValidator;

    private final OfficeCountryRepository officeCountryRepository;
    private final OfficeCountryRepositoryWrapper officeCountryWrapper;
    private final CountryLocationMapper countryLocationMapper;


    @Override
    public Collection<OfficeCountryData> retrieveAllCountries(SearchParameters searchParameters) {
        final AppUser currentUser = this.context.authenticatedUser();
        final OfficeCountryMapper rm = new OfficeCountryMapper();
        final StringBuilder sqlbuilder = new StringBuilder(400);
        sqlbuilder.append("select");
        sqlbuilder.append(rm.schema());
        if (searchParameters != null) {
        if (searchParameters.hasOrderBy()) {
            this.columnValidator.validateSqlInjection(sqlbuilder.toString(), searchParameters.getOrderBy());
            sqlbuilder.append(" order by ").append(searchParameters.getOrderBy());
            if (searchParameters.hasSortOrder()) {
                this.columnValidator.validateSqlInjection(sqlbuilder.toString(), searchParameters.getSortOrder());
                sqlbuilder.append(' ').append(searchParameters.getSortOrder());
            }
        } else {
            sqlbuilder.append(" order by moc.id");
        }
    } else {
        sqlbuilder.append(" order by moc.id");
    }
        return this.jdbcTemplate.query(sqlbuilder.toString(),rm);
    }

    private static final class OfficeCountryMapper implements RowMapper<OfficeCountryData> {

        public String schema(){
            return " moc.id as id, moc.country_name as countryName,"
                    + " moc.description as description, moc.position as position,"
                    + " moc.active as isActive from m_office_country moc";
        }

        @Override
        public OfficeCountryData mapRow(ResultSet rs, int rowNum) throws SQLException {

            final Long id = rs.getLong("id");
            final String countryName = rs.getString("countryName");
            final String description = rs.getString("description");
            final int position = rs.getInt("position");
            final boolean isactive = rs.getBoolean("isActive");
            return OfficeCountryData.instance(id,countryName,description,position,isactive);
        }
    }

    @Override
    public OfficeCountryData retrieveCountry(Long countryId) {

        this.context.authenticatedUser();
        final OfficeCountry country = this.officeCountryWrapper.findOneWithNotFoundDetection(countryId);

        return this.countryLocationMapper.map(country);
    }
}
