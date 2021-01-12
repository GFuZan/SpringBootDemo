package org.gfuzan.modules.entity;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;

public abstract class BaseData implements Serializable {

    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = 5740823114523904178L;

    @JsonIgnore
    private Map<String, Object> baseDataOtherRawData;

    @JsonAnyGetter
    public Map<String, Object> getBaseDataOtherDataKey() {
        return baseDataOtherRawData;
    }

    @JsonAnySetter
    public void setBaseDataOtherDataKey(String key, Object value) {
        if (Objects.isNull(baseDataOtherRawData)) {
            baseDataOtherRawData = new HashMap<>();
        }
        baseDataOtherRawData.put(key, value);
    }

}
