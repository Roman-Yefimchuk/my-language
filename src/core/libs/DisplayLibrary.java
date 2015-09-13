package core.libs;

public class DisplayLibrary implements AbstractLibrary {

    public static final int SET_FULL_SCREEN_MODE = 1349104827;//"Display.setFullScreenMode(B)V"
    public static final int GET_WIDTH = -237563380;//"Display.getWidth()I"
    public static final int GET_HEIGHT = -336156681;//"Display.getHeight()I"

    public void constructor() {
    }

    public Object invoke(int functionId, Object[] args) {
        switch (functionId) {
            case SET_FULL_SCREEN_MODE: {
            }
            case GET_WIDTH: {
            }
            case GET_HEIGHT: {
            }
        }
        throw new RuntimeException(getName() + "[" + functionId + "]");
    }

    public String getName() {
        return "Display";
    }

    public void destructor() {
    }
}
