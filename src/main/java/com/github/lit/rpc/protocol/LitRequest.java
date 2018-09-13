package com.github.lit.rpc.protocol;

import lombok.Data;

import java.util.List;

/**
 * @author liulu
 * @version : v1.0
 * date : 2018-09-12 17:46
 */
@Data
public class LitRequest {

    private String className;

    private String methodName;

    private Class<?>[] paramTypes;

    private List<Object> paramValues;



}
