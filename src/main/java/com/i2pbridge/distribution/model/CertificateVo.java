package com.i2pbridge.distribution.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.persistence.Id;
import java.sql.Timestamp;
import java.util.ArrayList;

@Data
public class CertificateVo {
    @JsonProperty("certificate")
    Certificate certificate;

    @JsonProperty("bridgeId")
    private Long bridgeId;

    @JsonProperty("vote")
    private Boolean vote;

}
