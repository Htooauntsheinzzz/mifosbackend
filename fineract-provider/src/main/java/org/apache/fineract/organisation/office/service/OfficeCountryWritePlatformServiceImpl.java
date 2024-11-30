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

import jakarta.persistence.PersistenceException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.fineract.infrastructure.core.api.JsonCommand;
import org.apache.fineract.infrastructure.core.data.CommandProcessingResult;
import org.apache.fineract.infrastructure.core.data.CommandProcessingResultBuilder;
import org.apache.fineract.infrastructure.core.exception.ErrorHandler;
import org.apache.fineract.infrastructure.core.exception.PlatformDataIntegrityException;
import org.apache.fineract.infrastructure.security.service.PlatformSecurityContext;
import org.apache.fineract.organisation.office.domain.OfficeCountry;
import org.apache.fineract.organisation.office.domain.OfficeCountryRepository;
import org.apache.fineract.organisation.office.domain.OfficeCountryRepositoryWrapper;
import org.apache.fineract.organisation.office.serialization.OfficeCountryCommandApiJsonDeserializer;
import org.apache.fineract.useradministration.domain.AppUser;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.orm.jpa.JpaSystemException;
import org.springframework.stereotype.Service;

import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@Service
public class OfficeCountryWritePlatformServiceImpl implements OfficeCountryWritePlatformService {

    private final PlatformSecurityContext context;
    private final OfficeCountryCommandApiJsonDeserializer fromApiJsonDeserializer;
    private final OfficeCountryRepositoryWrapper officeCountryRepositoryWrapper;
    private final OfficeCountryRepository repository;


    @Transactional
    @Override
    public CommandProcessingResult createOfficeCountry(final JsonCommand jsonCommand) {
        try{
            final AppUser currentUsr = this.context.authenticatedUser();

            this.fromApiJsonDeserializer.validateForCreate(jsonCommand.json());

            final OfficeCountry officeCountry = OfficeCountry.fromJson(currentUsr,jsonCommand);

            this.officeCountryRepositoryWrapper.saveAndflash(officeCountry);

            this.officeCountryRepositoryWrapper.save(officeCountry);
            return new CommandProcessingResultBuilder().withCommandId(jsonCommand.commandId())
                    .withEntityId(officeCountry.getId())
                    .withOfficeCountryId(officeCountry.getId())
                    .build();
        }catch(final JpaSystemException | DataIntegrityViolationException dve ){
            handleOfficeCountryDataIntegrityIssues(jsonCommand,dve.getMostSpecificCause(),dve);
            return CommandProcessingResult.empty();
        }catch (final PersistenceException dve){
            Throwable throwable = ExceptionUtils.getRootCause(dve.getCause());
            handleOfficeCountryDataIntegrityIssues(jsonCommand,throwable,dve);
            return CommandProcessingResult.empty();
        }

    }

    @Override
    public CommandProcessingResult updateOfficeCountry(Long countryId, JsonCommand jsonCommand) {

        this.fromApiJsonDeserializer.validateForUpdate(jsonCommand.json());
        final OfficeCountry officeCountry = this.officeCountryRepositoryWrapper.findOneWithNotFoundDetection(countryId);
        final Map<String,Object> changes = officeCountry.update(jsonCommand);

        if(changes.isEmpty()){
            this.repository.save(officeCountry);
        }
        return new CommandProcessingResultBuilder().withCommandId(jsonCommand.commandId())
                .withEntityId(jsonCommand.entityId())
                .withMessage("Update Successful officeCountry Id - "+jsonCommand.entityId())
                .build();
    }

    private void handleOfficeCountryDataIntegrityIssues(final JsonCommand jsonCommand,final Throwable realCause,final Exception dve){
        if(realCause.getMessage().contains("uni_country_name")){
            final String c_name = jsonCommand.stringValueOfParameterNamed("name");
                        throw new PlatformDataIntegrityException("error.msg.office_country.duplicate.countryName",
                    "Office with externalId `" + c_name + "` already exists", "externalId", c_name);
        }else if(realCause.getMessage().contains("uni_position")){
            final Integer c_position = jsonCommand.integerValueOfParameterNamed("position");
            throw new PlatformDataIntegrityException("error.msg.office_country.duplicate.position",
                    "Office with externalId `" + c_position + "` already exists", "externalId", c_position);

        }
        log.error("Error occured." ,dve);
        throw ErrorHandler.getMappable(dve, "error.msg.office_country.unknown.data.integrity.issue", "Unknown data integrity issue with resource.");
    }


}
