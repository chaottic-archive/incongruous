package com.chaottic.incongruous.persistent;

import java.io.DataOutput;
import java.io.IOException;

public interface Writable {

    void write(DataOutput dataOutput) throws IOException;
}
