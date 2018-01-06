package bank;

import bank.requests.*;
import utilities.Util;
import io.atomix.catalyst.concurrent.SingleThreadContext;
import pt.haslab.ekit.Clique;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class RemoteBank implements Bank {

    private final SingleThreadContext tc;
    private final Clique clique;
    private int id;

    public RemoteBank(SingleThreadContext tc, Clique clique) {
        this.tc = tc;
        this.clique = clique;

        id = 1; //TODO
    }

    @Override
    public Account search(String iban) {
        BankSearchRep r = null;
        CompletableFuture<BankSearchRep> rep = new CompletableFuture<>();
        clique.sendAndReceive(1, new BankSearchReq(iban, id))
                .thenAccept(s -> rep.complete((BankSearchRep)s));
        try {
            r = rep.get();
        } catch (InterruptedException|ExecutionException e) {
            e.printStackTrace();
        }

        return (Account) Util.makeRemote(tc, r.ref, clique);
    }
}
