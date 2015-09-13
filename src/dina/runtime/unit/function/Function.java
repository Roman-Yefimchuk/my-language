package dina.runtime.unit.function;

import dina.runtime.unit.*;

public class Function {

    private String name;//имя функции
    private String signature;//подпись функции
    public Unit parentUnit;//модуль, в котором находится данная функция
    public int[] instructions;//набор инструций
    public int variablesAmount;//количество локальных переменных
    public int argumentsAmount;//количество аргументов
    public int[] id;//типы агрументов
    public int[] dimension;//размерность массива

    public Function(String name) {
        this.name = name;
    }

    public String getName() {
        return parentUnit.getName() + "." + name + signature;
    }
}
