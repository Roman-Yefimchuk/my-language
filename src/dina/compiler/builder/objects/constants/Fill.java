package dina.compiler.builder.objects.constants;

public class Fill<O> {

    private O object;
    private int position;

    public Fill(O constant, int position) {
        this.object = constant;
        this.position = position;
    }

    public O getObject() {
        return object;
    }

    public int getPosition() {
        return position;
    }
}
