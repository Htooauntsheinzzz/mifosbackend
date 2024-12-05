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
import org.apache.fineract.organisation.office.domain.*;
import org.apache.fineract.organisation.office.serialization.OfficeRegionCommandFromApiJsonDeserializer;
import org.apache.fineract.useradministration.domain.AppUser;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.orm.jpa.JpaSystemException;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class OfficeRegionWritePlatformServiceImpl implements  OfficeRegionWritePlatformService {

    private final PlatformSecurityContext context;
    private final OfficeRegionCommandFromApiJsonDeserializer fromApiJsonDeserializer;
    private final OfficeRegionRepository officeRegionRepository;
    private final OfficeRegionRepositoryWrapper officeRegionRepositoryWrapper;
    private final OfficeCountryRepositoryWrapper officeCountryRepositoryWrapper;

    @Transactional
    @Override
    public CommandProcessingResult createOfficeRegion(final JsonCommand command) {

        try{
            final AppUser currentUser = this.context.authenticatedUser();

            this.fromApiJsonDeserializer.validateForCreate(command.json());

            final Long countryId = command.longValueOfParameterNamed("countryId");

            final OfficeCountry country =this.officeCountryRepositoryWrapper.findOneWithNotFoundDetection(countryId);

            final OfficeRegion officeRegion = OfficeRegion.fromjson(currentUser,country,command);
            this.officeRegionRepositoryWrapper.saveAndFlash(officeRegion);
            this.officeRegionRepository.save(officeRegion);

            return new CommandProcessingResultBuilder().withCommandId(command.commandId())
                    .withEntityId(officeRegion.getId())
                    .withOfficeRegionId(officeRegion.getId())
                    .withMessage("Create Region Successful with ID " + officeRegion.getId())
                    .build();
        }catch(final JpaSystemException | DataIntegrityViolationException dve){
            handleOfficeCountryDataIntegrityIssues(command,dve.getMostSpecificCause(),dve);
            return CommandProcessingResult.empty();
        }catch (final PersistenceException dve){
        Throwable throwable = ExceptionUtils.getRootCause(dve.getCause());
        handleOfficeCountryDataIntegrityIssues(command,throwable,dve);
        return CommandProcessingResult.empty();
    }
    }
    private void handleOfficeCountryDataIntegrityIssues(final JsonCommand jsonCommand,final Throwable realCause,final Exception dve){
        if(realCause.getMessage().contains("name")){
            final String r_name = jsonCommand.stringValueOfParameterNamed("name");
            throw new PlatformDataIntegrityException("error.msg.office_country.duplicate.Region Name",
                    "Office Region with name `" + r_name + "` already exists", "name", r_name);
        }else if(realCause.getMessage().contains("position")){
            final Integer r_position = jsonCommand.integerValueOfParameterNamed("position");
            throw new PlatformDataIntegrityException("error.msg.officeRegion.duplicate.position",
                    "Office Region with position `" + r_position + "` already exists", "position", r_position);

        }
        log.error("Error occured." ,dve);
        throw ErrorHandler.getMappable(dve, "error.msg.officeRegion.unknown.data.integrity.issue", "Unknown data integrity issue with resource.");
    }

}
