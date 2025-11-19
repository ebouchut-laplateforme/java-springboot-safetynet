package com.ericbouchut.springboot.safetynet.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * This class models a fire station.
 * <p>
 * The <b>unicity</b> of a fire station is based upon a pair of fields:
 * {@link #address} and {@link #number}.
 * <p>
 * IMPORTANT: Several fire stations can share the same (station) number.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FireStation {
    private String address;

    /**
     * The number of a fire station.
     * <br/>
     * Several fire stations can share the same number.
     */
    @JsonProperty("station")
    private Integer number;
}
