package bookstore;

import bank.Txn;
import bank.requests.TxnReq;
import io.atomix.catalyst.concurrent.SingleThreadContext;
import io.atomix.catalyst.transport.Address;
import io.atomix.catalyst.transport.Connection;
import io.atomix.catalyst.transport.Transport;
import io.atomix.catalyst.transport.netty.NettyTransport;
import bookstore.requests.CartAddRep;
import bookstore.requests.CartAddReq;

import java.util.concurrent.ExecutionException;

import static bank.Queues.send_req;

public class RemoteCart implements Cart {
    private final SingleThreadContext tc;
    private Connection c;
    private Address address;
    private int id;

    public RemoteCart(SingleThreadContext tc, Address address, int id) {
        this.tc = tc;
        this.address = address;
        Transport t = new NettyTransport();
        try {
            c = tc.execute(() ->
                    t.client().connect(address)
            ).join().get();
        } catch (InterruptedException|ExecutionException e) {
            e.printStackTrace();
            c = null;
        }
        this.id = id;
    }

    public boolean buy() {
        Txn x = new Txn(1, "PT12345", 1);
        Address adr = new Address("localhost", 10001);
        System.out.println("buy req sent");
        send_req(new NettyTransport(), adr, x);
        return false;
    }

    public void add(Book b) {
        RemoteBook book = (RemoteBook) b;
        try {
            CartAddRep r = (CartAddRep) tc.execute(() ->
                    c.sendAndReceive(new CartAddReq(book.id, id))
            ).join().get();
        } catch (InterruptedException|ExecutionException e) {
            e.printStackTrace();
        }
    }
}