package com.github.lit.rpc.protocol;

import lombok.Data;

/**
 * @author liulu
 * @version : v1.0
 * date : 2018-09-12 17:52
 */
@Data
public class LitResponse {

    private String returnType;

    private byte[] returnValue;



}
