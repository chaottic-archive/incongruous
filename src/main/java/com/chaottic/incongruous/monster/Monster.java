package com.chaottic.incongruous.monster;

import com.chaottic.incongruous.persistent.Readable;
import com.chaottic.incongruous.persistent.Writable;
import lombok.Getter;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public final class Monster implements Readable, Writable {
    private final Species species;

    @Getter
    private int hp;
    @Getter
    private int atk;
    @Getter
    private int def;
    @Getter
    private int exp;

    public Monster(Species species) {
        this.species = species;
    }

    @Override
    public void read(DataInput dataInput) throws IOException {
        hp = dataInput.readInt();
        atk = dataInput.readInt();
        def = dataInput.readInt();
        exp = dataInput.readInt();
    }

    @Override
    public void write(DataOutput dataOutput) throws IOException {
        dataOutput.writeInt(species.ordinal());
        dataOutput.writeInt(hp);
        dataOutput.writeInt(atk);
        dataOutput.writeInt(def);
        dataOutput.writeInt(exp);
    }

    public int getLevel() {
        return 1;
    }

    public static Monster createFrom(DataInput dataInput) throws IOException {
        var species = dataInput.readInt();

        var values = Species.values();

        for (int i = 0; i < values.length; i++) {

            if (species == i) {
                var monster = new Monster(values[i]);
                monster.read(dataInput);

                return monster;
            }
        }

        throw new RuntimeException("Unknown species %d".formatted(species));
    }
}
