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
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.UriInfo;
import lombok.RequiredArgsConstructor;
import org.apache.fineract.commands.domain.CommandWrapper;
import org.apache.fineract.commands.service.CommandWrapperBuilder;
import org.apache.fineract.commands.service.PortfolioCommandSourceWritePlatformService;
import org.apache.fineract.infrastructure.core.api.ApiRequestParameterHelper;
import org.apache.fineract.infrastructure.core.data.CommandProcessingResult;
import org.apache.fineract.infrastructure.core.serialization.ApiRequestJsonSerializationSettings;
import org.apache.fineract.infrastructure.core.serialization.DefaultToApiJsonSerializer;
import org.apache.fineract.infrastructure.core.service.SearchParameters;
import org.apache.fineract.infrastructure.security.service.PlatformSecurityContext;
import org.apache.fineract.infrastructure.security.service.SqlValidator;
import org.apache.fineract.organisation.office.data.OfficeCountryData;
import org.apache.fineract.organisation.office.service.OfficeCountryReadPlatformService;
import org.springframework.stereotype.Component;

import java.util.Collection;
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
    private final OfficeCountryReadPlatformService officeCountryReadPlatformService;
    private final PlatformSecurityContext context;
    private final DefaultToApiJsonSerializer<OfficeCountryData> toApiJsonSerializer;
    private final SqlValidator sqlValidator;
    private final ApiRequestParameterHelper apiRequestParameterHelper;



    @GET
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
    @Operation(summary = "Retrieve All Office Country Location Template",description = "Example Request:\n" + "\n" + "offices/template")
    @ApiResponses({
            @ApiResponse(responseCode = "200",description = "OK",content = @Content(schema =@Schema(implementation = OfficeCountryApiSwagger.GetOfficesCountryTemplateResponse.class)))})
    public String retrieveAllCountriesTemplate(@Context final UriInfo uriInfo,
                                               @QueryParam("orderBy") @Parameter(description = "orderBy") final String orderBy,
                                               @QueryParam("sortOrder") @Parameter(description = "sortOrder") final String sortOrder) {

        context.authenticatedUser().validateHasReadPermission(RESOURCE_NAME_FOR_PERMISSIONS);
        sqlValidator.validate(orderBy);
        sqlValidator.validate(sortOrder);
        final SearchParameters searchParameters = SearchParameters.builder().orphansOnly(false).isSelfUser(false).orderBy(orderBy).build();
        final Collection<OfficeCountryData> officeCountries = officeCountryReadPlatformService.retrieveAllCountries(searchParameters);
        final ApiRequestJsonSerializationSettings settings = apiRequestParameterHelper.process(uriInfo.getQueryParameters());
        return toApiJsonSerializer.serialize(settings,officeCountries,RESPONSE_DATA_PARAMETERS);

    }


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
