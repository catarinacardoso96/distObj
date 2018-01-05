package twopc.requests;

import io.atomix.catalyst.buffer.BufferInput;
import io.atomix.catalyst.buffer.BufferOutput;
import io.atomix.catalyst.serializer.CatalystSerializable;
import io.atomix.catalyst.serializer.Serializer;

public class Begin implements CatalystSerializable {

    private TransactInfo transactInfo;

    public Begin() {}

    @Override
    public void writeObject(BufferOutput<?> bufferOutput, Serializer serializer) {
        serializer.writeObject(transactInfo, bufferOutput);
    }

    @Override
    public void readObject(BufferInput<?> bufferInput, Serializer serializer) {
        transactInfo = serializer.readObject(bufferInput);
    }

    public TransactInfo getTransactInfo() {
        return  transactInfo;
    }
}
