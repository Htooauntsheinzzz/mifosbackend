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

import org.apache.fineract.organisation.office.exception.OfficeCountryNotFoundException;
import org.apache.fineract.organisation.office.exception.OfficeRegionNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OfficeRegionRepositoryWrapper {

    private  final OfficeRegionRepository officeRegionRepository;

    @Autowired
    public OfficeRegionRepositoryWrapper(final OfficeRegionRepository officeRegionRepository) {
        this.officeRegionRepository = officeRegionRepository;
    }

    public OfficeRegion save(final OfficeRegion officeRegion) {
        return officeRegionRepository.save(officeRegion);
    }

    public OfficeRegion saveAndFlash(final OfficeRegion officeRegion) {
        return  officeRegionRepository.saveAndFlush(officeRegion);
    }

    public OfficeRegion findOneWithNotFoundDetection(final Long id){
        return  this.officeRegionRepository.findById(id).orElseThrow(() -> new OfficeRegionNotFoundException(id));
    }
}
