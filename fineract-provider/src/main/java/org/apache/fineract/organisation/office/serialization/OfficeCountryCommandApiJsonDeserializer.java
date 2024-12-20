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
 */package org.apache.fineract.organisation.office.serialization;

import com.google.common.reflect.TypeToken;
import com.google.gson.JsonElement;
import org.apache.commons.lang3.StringUtils;
import org.apache.fineract.infrastructure.core.data.ApiParameterError;
import org.apache.fineract.infrastructure.core.data.DataValidatorBuilder;
import org.apache.fineract.infrastructure.core.exception.InvalidJsonException;
import org.apache.fineract.infrastructure.core.exception.PlatformApiDataValidationException;
import org.apache.fineract.infrastructure.core.serialization.FromJsonHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.Type;
import java.util.*;

@Component
public class OfficeCountryCommandApiJsonDeserializer {

    public static final String NAME = "name";
    public static final String DESCRIPTION  = "description";
    public static final String POSITION  = "position";
    public static final String IS_ACTIVE = "isActive";
    public static final String LOCALE ="locale";

    private  static final Set<String> SUPPORTED_PARAMTERS = new HashSet<>(
            Arrays.asList(NAME, DESCRIPTION, POSITION, IS_ACTIVE, LOCALE)
    );

    private final FromJsonHelper fromApiJsonHelper;

    @Autowired
    public OfficeCountryCommandApiJsonDeserializer(final FromJsonHelper fromApiJsonHelper) {
        this.fromApiJsonHelper = fromApiJsonHelper;
    }

    public void validateForCreate(final String json){
        if(StringUtils.isBlank(json)){
            throw new InvalidJsonException();
        }
        final Type typeofMapping = new TypeToken<Map<String,Object>>() {}.getType();
        this.fromApiJsonHelper.checkForUnsupportedParameters(typeofMapping,json,SUPPORTED_PARAMTERS);

        final List<ApiParameterError> errors = new ArrayList<>();

        final DataValidatorBuilder baseDataValidator = new DataValidatorBuilder(errors).resource("officeCountry");
        final JsonElement element = this.fromApiJsonHelper.parse(json);

        final String name =this.fromApiJsonHelper.extractStringNamed(NAME, element);
        baseDataValidator.reset().parameter(NAME).value(name).notBlank();

        final String description =this.fromApiJsonHelper.extractStringNamed(DESCRIPTION, element);
        baseDataValidator.parameter(DESCRIPTION).value(description).notExceedingLengthOf(400);

        final String position =this.fromApiJsonHelper.extractStringNamed(POSITION, element);
        baseDataValidator.parameter(POSITION).value(position).notBlank();

        final Boolean isActive =this.fromApiJsonHelper.extractBooleanNamed(IS_ACTIVE, element);
          baseDataValidator.reset().parameter(IS_ACTIVE).value(isActive);

        throwExceptionIfValidationWarningsExist(errors);

    }

    public void validateForUpdate(final String json){
        if(StringUtils.isBlank(json)){
            throw new InvalidJsonException();
        }
        final Type typeofMapping = new TypeToken<Map<String,Object>>() {}.getType();
        this.fromApiJsonHelper.checkForUnsupportedParameters(typeofMapping,json,SUPPORTED_PARAMTERS);

        final List<ApiParameterError> errors = new ArrayList<>();

        final DataValidatorBuilder baseDataValidator = new DataValidatorBuilder(errors).resource("officeCountry");
        final JsonElement element = this.fromApiJsonHelper.parse(json);


        if(this.fromApiJsonHelper.parameterExists(NAME,element)){
            final String name =this.fromApiJsonHelper.extractStringNamed(NAME, element);
            baseDataValidator.reset().parameter(NAME).value(name).notBlank();
        }

        if(this.fromApiJsonHelper.parameterExists(DESCRIPTION,element)){
            final String description =this.fromApiJsonHelper.extractStringNamed(DESCRIPTION, element);
            baseDataValidator.parameter(DESCRIPTION).value(description).notExceedingLengthOf(400);
        }

        if(this.fromApiJsonHelper.parameterExists(POSITION,element)){
            final String position =this.fromApiJsonHelper.extractStringNamed(POSITION, element);
            baseDataValidator.parameter(POSITION).value(position).notBlank();
        }

        if(this.fromApiJsonHelper.parameterExists(NAME,element)){
            final Boolean isActive =this.fromApiJsonHelper.extractBooleanNamed(IS_ACTIVE, element);
            baseDataValidator.reset().parameter(IS_ACTIVE).value(isActive);
        }

        throwExceptionIfValidationWarningsExist(errors);


    }

    private void throwExceptionIfValidationWarningsExist(final List<ApiParameterError> dataValidationErrors) {
        if (!dataValidationErrors.isEmpty()) {
            throw new PlatformApiDataValidationException("validation.msg.validation.errors.exist", "Validation errors exist.",
                    dataValidationErrors);
        }

    }




}
