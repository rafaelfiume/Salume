package com.rafaelfiume.salume.support.transactions;

public interface UnitOfWork {

    void work() throws Exception;
}
