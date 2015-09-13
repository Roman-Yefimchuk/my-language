package core.libs;

public interface AbstractLibrary {

    public void constructor();

    public Object invoke(int functionId, Object[] args);

    public String getName();

    public void destructor();
}
