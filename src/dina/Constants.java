package dina;

public interface Constants {

    byte BEGIN_FUNCTION = 127;//Начало функции
    byte END_FUNCTION = 126;//Конец функции
    //Constants
    //Loads
    //Stores
    //Stack
    //Math
    //Conversions
    //Comparisons
    //Control
    //References
    byte ICONST = 1;//Загрузка целочисленной константы в стек
    byte FCONST = 2;//Загрузка дробной константы в стек
    byte SCONST = 3;//Загрузка строковой константи в стек
    byte BCONST = 4;//Положить в стек один байт
    byte ZCONST = 5;//Положить в стек логическую константу
    byte CCONST = 6;//Положить в стек символьную константу
    byte SICONST = 7;////Загрузка короткого целого в стек
    byte NEW = 8;//
    byte LLOAD = 9;//Загрузка значения из локальной переменной
    byte GLOAD = 10;//Загрузка значения из глобальной переменной
    byte LSTORE = 11;//Сохранение значения в локальной переменной
    byte GSTORE = 12;//Сохранение значения в глобальной переменной
    byte IALOAD = 13;//Загрузить целое число из массива
    byte FALOAD = 14;//Загрузить дробное число из массива
    byte SALOAD = 15;//Загрузить строку число из массива
    byte RALOAD = 16;//
    byte OALOAD = 17;//
    byte IASTORE_0 = 18;//Сохранить целое число в массива
    byte FASTORE_0 = 19;//Сохранить дробное число в массива
    byte SASTORE_0 = 20;//Сохранить строку в массива
    byte RASTORE_0 = 21;//
    byte OASTORE_0 = 22;//
    byte IASTORE_1 = 23;//Инициализация массива целых чисел
    byte FASTORE_1 = 24;//Инициализация массива дробный чисел
    byte SASTORE_1 = 25;//Инициализация массива строк
    byte RASTORE_1 = 26;//
    byte OASTORE_1 = 27;//
    byte POP = 28;//
    byte DUP = 29;
    byte IADD = 30;//Сложение целых
    byte FADD = 31;//Сложение дробных
    byte IDIV = 32;//Деление целых
    byte FDIV = 33;//Деление дробных
    byte ISUB = 34;//Вычитание целых
    byte FSUB = 35;//Вычитание дробных
    byte IMUL = 36;//Умножение целых
    byte FMUL = 37;//Умножение дробных
    byte IMOD = 38;//Остаток от деления целых
    byte FMOD = 39;//Остаток от деления дробных
    byte INEG = 40;//Отрицание целого числа
    byte FNEG = 41;//Отрицание дробного число
    byte I2F = 42;//Целое число в дробное
    byte F2S = 43;//Дробное число в строку
    byte S2F = 44;//Строку в дробное число
    byte I2S = 45;//Целое число в строку
    byte S2I = 46;//Строку в целое число
    byte C2S = 47;//Символ в строку
    byte I2C = 48;//Число в символ
    byte IFEQ = 49;//(true == true){1}
    byte IFNE = 50;//(true != true){0}
    byte OR = 51;//Логическое сумирование
    byte AND = 52;//Логическое умножение
    byte NOT = 53;//Инверсия
    byte IF_ICMPEQ = 54;//"=="
    byte IF_ICMPNE = 55;//"!="
    byte IF_ICMPLT = 56;//"<"
    byte IF_ICMPLE = 57;//"<="
    byte IF_ICMPGT = 58;//">"
    byte IF_ICMPGE = 59;//">="
    byte IF_FCMPEQ = 60;//"=="
    byte IF_FCMPNE = 61;//"!="
    byte IF_FCMPLT = 62;//"<"
    byte IF_FCMPLE = 63;//"<="
    byte IF_FCMPGT = 64;//">"
    byte IF_FCMPGE = 65;//">="
    byte IF_SCMPEQ = 66;//Равенство двох строк
    byte IF_SCMPNE = 67;//Неравенство двох строк
    byte IF_ACMPEQ = 68;//Сравнение двох массивов
    byte IF_ACMPNE = 69;//Сравнение двох массивов
    byte GOTO = 70;//Безусловный переход
    byte TABLESWITCH = 71;
    byte RETURN = 72;//Выход из метода(передача управления родительскому методу)
    byte PUTFIELD = 73;//
    byte GETFIELD = 74;//
    byte INVOKENATIVE = 75;//Выполнить метод виртуальной машины
    byte INVOKE = 76;//Выполнить метод
    byte SCONCAT = 77;//Соединение двох строк
    byte ACONCAT = 78;//Соеденить два массива
    byte NEWARRAY = 79;
    byte ARRAYLENGTH = 80;
    byte STRINGLENGTH = 81;
    byte TRY = 82;//Вход в участок обработки исключения
    byte BREAK_TRY = 83;//Выход из участка обработки исключения
    byte THROW = 84;//Генерация исключения
    byte QUIT = 85;//
    byte TYPEOF = 86;
    byte CHECKCAST = 87;
    //
    int BYTE = -1;
    int SHORT = -2;
    int INTEGER = -4;
}
