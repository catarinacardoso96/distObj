package bookstore.requests;

import io.atomix.catalyst.buffer.BufferInput;
import io.atomix.catalyst.buffer.BufferOutput;
import io.atomix.catalyst.serializer.CatalystSerializable;
import io.atomix.catalyst.serializer.Serializer;

public class StoreMakeCartReq implements CatalystSerializable {

	public int id;
    public int txid;

	public StoreMakeCartReq() {}
	public StoreMakeCartReq(int id, int txid) {
	    this.id = id;
	    this.txid = txid;
	}

    @Override
    public void writeObject(BufferOutput<?> bufferOutput, Serializer serializer) {
        bufferOutput.writeInt(id);
        bufferOutput.writeInt(txid);
    }

    @Override
    public void readObject(BufferInput<?> bufferInput, Serializer serializer) {
        id = bufferInput.readInt();
        txid = bufferInput.readInt();
	}
}