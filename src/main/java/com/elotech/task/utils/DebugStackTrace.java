package com.elotech.task.utils;

public record DebugStackTrace (
        String classe,
        String arquivo,
        String metodo,
        Integer linha
){

}
