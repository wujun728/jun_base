package com.geek.common.core.storage.domain;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SysFilePartETag implements Serializable {
    private static final long serialVersionUID = 2471854027355307627L;
    private Integer partNumber;
    @JsonProperty("ETag")
    private String eTag;
    private Long partSize;
    private Long partCRC;
    private Long fileSize;
    private String filePath;
    private String taskId;

    public void seteTag(String eTag) {
        this.eTag = eTag;
    }

    public int hashCode() {
        int result = 1;
        result = 31 * result + (this.eTag == null ? 0 : this.eTag.hashCode());
        result = 31 * result + this.partNumber;
        return result;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        } else if (!(obj instanceof SysFilePartETag)) {
            return false;
        } else {
            SysFilePartETag other = (SysFilePartETag) obj;
            if (this.partNumber != other.partNumber) {
                return false;
            } else {
                return this.eTag == null ? other.eTag == null : this.eTag.equals(other.eTag);
            }
        }
    }
}
