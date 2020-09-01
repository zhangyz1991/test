package com.vick.test.util.jdbc.asm;

/**
 * @author Vick Zhang
 * @create 2020/8/31
 */
final class CurrentFrame extends Frame {

    CurrentFrame(final Label owner) {
        super(owner);
    }

    /**
     * Sets this CurrentFrame to the input stack map frame of the next "current" instruction, i.e. the
     * instruction just after the given one. It is assumed that the value of this object when this
     * method is called is the stack map frame status just before the given instruction is executed.
     */
    @Override
    void execute(
            final int opcode, final int arg, final Symbol symbolArg, final SymbolTable symbolTable) {
        super.execute(opcode, arg, symbolArg, symbolTable);
        Frame successor = new Frame(null);
        merge(symbolTable, successor, 0);
        copyFrom(successor);
    }
}
