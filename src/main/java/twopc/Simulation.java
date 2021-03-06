package twopc;

import io.atomix.catalyst.concurrent.SingleThreadContext;
import io.atomix.catalyst.concurrent.ThreadContext;
import io.atomix.catalyst.serializer.Serializer;
import io.atomix.catalyst.transport.Address;
import io.atomix.catalyst.transport.Transport;
import io.atomix.catalyst.transport.netty.NettyTransport;
import pt.haslab.ekit.Clique;
import twopc.requests.*;

public class Simulation {
    private static int prepared = 0;
    public static final Integer GLOBAL_COMMIT = 1;
    public static final Integer ABORT = -1;
    public static final Integer START = 0;



    public static void main(String[] args) {
        Address[] addresses = new Address[] {
                new Address("localhost:12345"),
                new Address("localhost:12346"),
                new Address("localhost:12347")//coordinator
        };
        int id = Integer.parseInt(args[0]);
        Transport t = new NettyTransport();
        ThreadContext tc = new SingleThreadContext("proto-%d",
                                                    new Serializer());
        tc.serializer().register(Prepare.class);
        tc.serializer().register(Commit.class);
        tc.serializer().register(Rollback.class);
        tc.serializer().register(Begin.class);
        tc.serializer().register(StartCommit.class);
        tc.serializer().register(TransactInfo.class);
        tc.serializer().register(Vote.class);
        tc.serializer().register(BeginRep.class);
        tc.serializer().register(NewParticipant.class);

        Clique c = new Clique(t, Clique.Mode.ANY, id, addresses);
        if(id == 2) {
            (new Coordinator(c, id, tc)).listen(new Address(":12348"));
        } else {
            if(id == 1)
                (new Participant(c, id, tc, 2)).listen(new Address(":12349"));
            else
                (new Participant(c, id, tc, 2)).listen(new Address(":12341"));
        }
    }
}
