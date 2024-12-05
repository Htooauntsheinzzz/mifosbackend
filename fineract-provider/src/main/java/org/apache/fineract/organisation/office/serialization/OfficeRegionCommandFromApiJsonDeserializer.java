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
package org.apache.fineract.organisation.office.serialization;

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
import org.springframework.web.servlet.View;

import java.lang.reflect.Type;
import java.util.*;

@Component
public class OfficeRegionCommandFromApiJsonDeserializer {

    public static final String NAME = "name";
    public static final String Country_ID = "countryId";
    public static final String DESCRIPTION  = "description";
    public static final String POSITION  = "position";
    public static final String IS_ACTIVE = "isActive";
    public static final String LOCALE ="locale";

    private final FromJsonHelper fromJsonHelper;

    private  static final Set<String> SUPPORTED_PARAMETERS=new HashSet<>(Arrays.asList(NAME,Country_ID,DESCRIPTION,POSITION,IS_ACTIVE,LOCALE));
    private final View error;

    @Autowired
    private OfficeRegionCommandFromApiJsonDeserializer(final FromJsonHelper fromJsonHelper, View error) {
        this.fromJsonHelper = fromJsonHelper;
        this.error = error;
    }

    public void validateForCreate(final String json){
        if (StringUtils.isBlank(json)) {
            throw new InvalidJsonException();
        }
        final Type typeofMap = new TypeToken<Map<String,Object>>(){}.getType();

        this.fromJsonHelper.checkForUnsupportedParameters(typeofMap,json,SUPPORTED_PARAMETERS);

        final List<ApiParameterError> dataValidationErrors = new ArrayList<>();
        final DataValidatorBuilder baseDataValidator = new DataValidatorBuilder(dataValidationErrors).resource("office_region");

        final JsonElement element = this.fromJsonHelper.parse(json);

        final String name = this.fromJsonHelper.extractStringNamed(NAME,element);
        baseDataValidator.reset().parameter(NAME).value(name).notBlank().notExceedingLengthOf(150);

        final Long countryId = this.fromJsonHelper.extractLongNamed(Country_ID,element);
        baseDataValidator.reset().parameter(Country_ID).value(countryId).notNull().integerGreaterThanZero();

        final String description = this.fromJsonHelper.extractStringNamed(DESCRIPTION,element);
        baseDataValidator.reset().parameter(DESCRIPTION).value(description).notBlank().notExceedingLengthOf(300);

        final Long position = this.fromJsonHelper.extractLongNamed(POSITION,element);
        baseDataValidator.reset().parameter(POSITION).value(position).notNull();

        final Boolean isActive = this.fromJsonHelper.extractBooleanNamed(IS_ACTIVE,element);
        baseDataValidator.reset().parameter(IS_ACTIVE).value(isActive);

        throwExceptionIfValidationWarningsExist(dataValidationErrors);

    }

    private void throwExceptionIfValidationWarningsExist(final List<ApiParameterError> dataValidationErrors) {
        if (!dataValidationErrors.isEmpty()) {
            throw new PlatformApiDataValidationException("validation.msg.validation.errors.exist", "Validation errors exist.",
                    dataValidationErrors);
        }
    }
}
