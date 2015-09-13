package core.libs;

public class EventsLibrary implements AbstractLibrary {

    public static final int GET_X = -964090703;//"Events.getX()I"
    public static final int GET_Y = -964060912;//"Events.getY()I"
    public static final int GET_MOUSE_ACTION = 383382990;//"Events.getMouseAction()I"
    public static final int KEY_TO_ACTION = 183597941;//"Events.keyToAction(I)B"

    public void constructor() {
    }

    public Object invoke(int functionId, Object[] args) {
        switch (functionId) {
            case GET_X: {
            }
            case GET_Y: {
            }
            case GET_MOUSE_ACTION: {
            }
            case KEY_TO_ACTION: {
            }
        }
        throw new RuntimeException(getName() + "[" + functionId + "]");
    }

    public String getName() {
        return "Events";
    }

    public void destructor() {
    }
}
