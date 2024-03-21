package com.fintracker.backend.fintrackermonolith.core.util;

import lombok.experimental.UtilityClass;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

@UtilityClass
public class RequestParamUtils  {

    public <T> List<T> parseParamAsList(String param, String separator,  Function<String, T> fromStringToReturnType) {
        List<T> resultList = new ArrayList<>();

        if (param == null || separator == null) {
            throw new IllegalArgumentException("Input or separator cannot be null.");
        }

        if (param.isEmpty()) {
            return resultList;
        }

        return Arrays.stream(param.split(separator)).map(fromStringToReturnType).toList();
    }
}
