package dina.library;

public interface FunctionsID {

    //console
    int WRITE_BOOLEAN = 0;
    int WRITE_CHAR = 1;
    int WRITE_INTEGER = 2;
    int WRITE_FLOAT = 3;
    int WRITE_STRING = 4;
    int WRITELN_BOOLEAN = 5;
    int WRITELN_CHAR = 6;
    int WRITELN_INTEGER = 7;
    int WRITELN_FLOAT = 8;
    int WRITELN_STRING = 9;
    int WRITELN = 10;
    int CLS = 11;
    int NEXT_INT = 12;
    int NEXT_FLOAT = 13;
    int NEXT_STRING = 14;
    int GET_KEY = 15;
    int SET_STATE = 16;
    int GET_STATE = 17;
    int SET_TITLE = 18;
    int SET_FULL_SCREEN_MODE = 19;
    //system
    int SYSTEM = 0;
    int FREE = 1;
    int HALT = 2;
    int SLEEP = 3;
    int GET_ERROR_MESSAGE = 4;
    int YIELD = 5;
    int TRACE = 6;
    //math
    int ABS_INTEGER = 0;
    int ABS_FLOAT = 1;
    int COS = 2;
    int SIN = 3;
    int SQR_INTEGER = 4;
    int SQR_FLOAT = 5;
    int SQRT = 6;
    int TAN = 7;
    int ROUND = 8;
    int RANDOM = 9;
    int RANDOMIZE = 10;
    //graphics
    int DRAW_ARC = 0;
    int DRAW_IMAGE = 1;
    int DRAW_LINE = 2;
    int DRAW_RECT = 3;
    int DRAW_ROUND_RECT = 4;
    int DRAW_STRING = 5;
    int DRAW_SUBSTRING = 6;
    int FILL_ARC = 7;
    int FILL_RECT = 8;
    int FILL_ROUND_RECT = 9;
    int GET_WIDTH = 10;
    int GET_HEIGHT = 11;
    int GET_IMAGE_WIDTH = 12;
    int GET_IMAGE_HEIGHT = 13;
    int GET_STRING_WIDTH = 14;
    int GET_SUBSTRING_WIDTH = 15;
    int GET_FONT_HEIGHT = 16;
    int LOAD_IMAGE = 17;
    int PLOT = 18;
    int REPAINT = 19;
    int REPAINT_AREA = 20;
    int SET_CLIP = 21;
    int GET_CLIP_X = 22;
    int GET_CLIP_Y = 23;
    int GET_CLIP_WIDTH = 24;
    int GET_CLIP_HEIGHT = 25;
    int SET_COLOR = 26;
    int SET_DEFAULT_FONT = 27;
    int SET_FONT = 28;
    int DRAW_OVAL = 29;
    int FILL_OVAL = 30;
    int DRAW_POLYGON = 31;
    int DRAW_POLYLINE = 32;
    int FILL_POLYGON = 33;
    int GET_FONT = 34;
    int GET_FONT_HEIGHT_FROM_FONT = 35;
    //string
    int CHARAT = 0;
    int TO_CHAR_ARRAY = 1;
    int TO_LOWER_CASE = 2;
    int TO_UPPER_CASE = 3;
    int TRIM = 4;
    int LENGTH = 5;
    int SUBSTRING = 6;
    int INDEX_OF = 7;
    int TO_STRING = 8;
    //date
    int GET_YEAR = 0;
    int GET_MONTH = 1;
    int GET_DAY = 2;
    int GET_HOUR = 3;
    int GET_MINUTE = 4;
    int GET_SECOND = 5;
    int GET_MILLISECOND = 6;
    //events
    int GET_X = 0;
    int GET_Y = 1;
    int GET_MOUSE_ACTION = 2;
    int KEY_TO_ACTION = 3;
    //io
    int OPEN_FILE = 0;
    int RESET_FILE = 1;
    int REWRITE_FILE = 2;
    int CLOSE_FILE = 3;
    int WRITE = 4;
    int WRITE_CHAR_TO_FILE = 5;
    int WRITE_INTEGER_TO_FILE = 6;
    int WRITE_FLOAT_TO_FILE = 7;
    int READ = 8;
    int READ_CHAR_FROM_FILE = 9;
    int READ_INTEGER_FROM_FILE = 10;
    int READ_FLOAT_FROM_FILE = 11;
    int EOF = 12;
    int GET_ROOTS = 13;
}
