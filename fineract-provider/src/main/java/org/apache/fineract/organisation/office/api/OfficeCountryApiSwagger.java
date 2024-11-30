package org.apache.fineract.organisation.office.api;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;
import java.util.Collection;

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

    @Schema(description = "GetOfficesCountryTemplateResponse")
    public static final class GetOfficesCountryTemplateResponse{
        private  GetOfficesCountryTemplateResponse() {

        }

        public Collection<GetOfficesCountryResponse> countryResponses;


    }
    public static final class GetOfficesCountryResponse{

        @Schema(example = "1")
        public long id;
        @Schema(example = "Myanmar")
        public String name;
        @Schema(example = "For Myanmar Office Country")
        public String description;
        @Schema(example = "1")
        public long position;
        @Schema(example = "true")
        public boolean isActive;
        @Schema(example = "en")
        public String locale;

        public Collection<GetOfficesCountryResponse> countryResponses;
    }

    @Schema(description = "PutOfficeCountryRequest")
    public static final class PutOfficeCountryRequest{

        private   PutOfficeCountryRequest(){}
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


    public static final class PutOfficeCountryResponse{
        private  PutOfficeCountryResponse() {}

        @Schema(example = "1")
        public Long resourceId;
    }


}
