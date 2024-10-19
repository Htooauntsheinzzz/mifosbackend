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
package org.apache.fineract.organisation.office.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import lombok.RequiredArgsConstructor;
import org.apache.fineract.commands.domain.CommandWrapper;
import org.apache.fineract.commands.service.CommandWrapperBuilder;
import org.apache.fineract.commands.service.PortfolioCommandSourceWritePlatformService;
import org.apache.fineract.infrastructure.core.api.ApiRequestParameterHelper;
import org.apache.fineract.infrastructure.core.data.CommandProcessingResult;
import org.apache.fineract.infrastructure.core.serialization.DefaultToApiJsonSerializer;
import org.apache.fineract.infrastructure.security.service.PlatformSecurityContext;
import org.apache.fineract.infrastructure.security.service.SqlValidator;
import org.apache.fineract.organisation.office.data.OfficeCountryData;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Path("/v1/officeCountry")
@Component
@Tag(name="OfficeCountry",description = "Office Country Location for while office create ")
@RequiredArgsConstructor
public class OfficeCountryApiResource {

    private static final Set<String> RESPONSE_DATA_PARAMETERS = new HashSet<>(List.of("id","name","description","position","isActive"));

    private static final String RESOURCE_NAME_FOR_PERMISSIONS = "OFFICE_COUNTRY";

    private final PortfolioCommandSourceWritePlatformService commandsSourceWritePlatformService;
    private final PlatformSecurityContext context;
    private final DefaultToApiJsonSerializer<OfficeCountryData> toApiJsonSerializer;
    private final SqlValidator sqlValidator;
    private final ApiRequestParameterHelper apiRequestParameterHelper;

    @POST
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
    @Operation(summary = "create office country location",description = "Mandatory Fields\n" + "name, position,isActive")
    @RequestBody(required = true, content = @Content(schema = @Schema(implementation =OfficeCountryApiSwagger.PostOfficeCountryRequest.class)))
    @ApiResponses({
            @ApiResponse(responseCode = "200",description = "OK",content = @Content(schema = @Schema(implementation = OfficeCountryApiSwagger.PostOfficeCountryResponse.class)))
    })
    public String createOfficeCountry(@Parameter(hidden = true)final String apiRequestBodyAsJson){
        final CommandWrapper commandRequest = new CommandWrapperBuilder()
                .createofficeCountry().withJson(apiRequestBodyAsJson).build();
        final CommandProcessingResult result = commandsSourceWritePlatformService.logCommandSource(commandRequest);
        return toApiJsonSerializer.serialize(result);

    }
}
