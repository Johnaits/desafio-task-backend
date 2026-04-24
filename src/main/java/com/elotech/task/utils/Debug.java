package com.elotech.task.utils;

import java.util.Collection;

public class Debug {

    public static synchronized void info(Object... objetos){
        System.out.println("=== Log (Tamanho: " + objetos.length + ") ===");

        for (Object obj : objetos) {

            //Collection
            if(obj instanceof Collection<?> lista){
                System.out.println("  [Lista com " + lista.size() + "]:");
                lista.forEach(item -> System.out.println("    -> " + item));

                // Array fixo
            }else if(obj instanceof Object[] array){
                System.out.println("  [Array Fixo com " + array.length + " itens]:");
                for (Object item : array){
                    System.out.println("    -> " + item);
                }

                // Qualquer outra coisa
            } else{
                System.out.println("  -> " + obj);
            }
        }
        DebugStackTrace stackTrace = obterStackTrace();

        System.out.println("=== Fim do Log === " + "\tat " + stackTrace.classe() + "." + stackTrace.metodo() + "(" + stackTrace.arquivo() + ":" + stackTrace.linha() + ")");
        System.out.println();
    }

    private static DebugStackTrace obterStackTrace(){
        StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();

        StackTraceElement caller = stackTrace[3];

        DebugStackTrace dbgStackTrace = new DebugStackTrace(
                caller.getClassName(),
                caller.getFileName(),
                caller.getMethodName(),
                caller.getLineNumber()
        );
        return dbgStackTrace;

    }

}
