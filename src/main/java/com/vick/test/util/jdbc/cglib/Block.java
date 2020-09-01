package com.vick.test.util.jdbc.cglib;

import com.vick.test.util.jdbc.asm.Label;

/**
 * @author Vick Zhang
 * @create 2020/8/31
 */
public class Block {
    private CodeEmitter e;
    private Label start;
    private Label end;

    public Block(CodeEmitter e) {
        this.e = e;
        this.start = e.mark();
    }

    public CodeEmitter getCodeEmitter() {
        return this.e;
    }

    public void end() {
        if (this.end != null) {
            throw new IllegalStateException("end of label already set");
        } else {
            this.end = this.e.mark();
        }
    }

    public Label getStart() {
        return this.start;
    }

    public Label getEnd() {
        return this.end;
    }
}
