package com.priceline.chutes;

import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

/* This POJO class is untested because the code is all Lombok-generated */

@Data
@RequiredArgsConstructor
public class Player {
    @NonNull
    private String name;
    private int position = 0;
}
