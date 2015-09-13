package dina.disassembler;

public class OpcodeLabel {

    private int address;
    private String label;

    public OpcodeLabel(int address, String label) {
        this.address = address;
        this.label = label;
    }

    public int getAddress() {
        return address;
    }

    @Override
    public String toString() {
        return label;
    }
}
