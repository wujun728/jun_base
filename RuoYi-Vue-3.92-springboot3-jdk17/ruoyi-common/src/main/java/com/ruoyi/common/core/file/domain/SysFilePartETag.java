package com.ruoyi.common.core.file.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SysFilePartETag {
    private Integer partNumber;
    private String eTag;
    private Long partSize;
    private Long partCRC;

    public SysFilePartETag(int partNumber, String eTag) {
        this.partNumber = partNumber;
        this.eTag = eTag;
    }
}
