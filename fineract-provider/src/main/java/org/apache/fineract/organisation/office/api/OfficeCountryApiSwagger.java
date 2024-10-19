package org.apache.fineract.organisation.office.api;

import io.swagger.v3.oas.annotations.media.Schema;

final class OfficeCountryApiSwagger {

    private OfficeCountryApiSwagger() {}


    @Schema(description = "PostOfficeCountryRequest")
    public static final class PostOfficeCountryRequest {

    private PostOfficeCountryRequest() {

    }

    @Schema(example = "Myanmar")
    public String name;
    @Schema(example = "For Myanmar Country")
    public String description;
    @Schema(example = "1")
    public Long position;
    @Schema(example = "en")
    public int locale;
    @Schema(example = "true")
    public boolean isActive;
    }
    @Schema(description = "PostOfficeCountryResponse")
    public static final class PostOfficeCountryResponse {

        private PostOfficeCountryResponse() {

        }

        @Schema(example = "1")
        public Long id;
        @Schema(example = "3")
        public Long resourceId;
    }

}
