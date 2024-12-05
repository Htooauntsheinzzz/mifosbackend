package org.apache.fineract.organisation.office.api;

import io.swagger.v3.oas.annotations.media.Schema;

public class OfficeRegionApiSwagger {

    private OfficeRegionApiSwagger() {}

    @Schema(description = "PostOfficeRegionRequest")
    public static final class PostOfficeRegionRequest{

        private PostOfficeRegionRequest() {}

        @Schema(example = "Myanmar")
        public String name;
        @Schema(example = "1")
        public Long countryId;
        @Schema(example = "For Myanmar Country")
        public String description;
        @Schema(example = "1")
        public Long position;
        @Schema(example = "en")
        public int locale;
        @Schema(example = "true")
        public boolean isActive;

    }
    @Schema(description = "PostOfficeRegionResponse")
    public static final class PostOfficeRegionResponse{

        private PostOfficeRegionResponse() {}
    }

        @Schema(example = "1")
        public Long id;
        @Schema(example = "3")
        public Long resourceId;

}
