package dina.runtime.unit;

import dina.runtime.unit.function.*;
import dina.runtime.variables.*;

public class Unit {

    private String name;//имя модуля
    public Function constructor;//конструктор
    public Function destructor;//деструктор
    public Function[] functions;//функции модуля
    public int functionsAmount;//количество функций
    public Variable[] global;//глобальные переменные
    public Variable[] prototypes;//прототипы записей
    public static String[] names;//имена записей
    public float[][] floatConstants;//дробные константы
    public String[][] stringConstants;//строковые константы

    public Unit(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
