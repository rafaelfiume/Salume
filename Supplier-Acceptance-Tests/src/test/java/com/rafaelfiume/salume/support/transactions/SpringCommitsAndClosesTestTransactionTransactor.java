package com.rafaelfiume.salume.support.transactions;

import org.springframework.test.context.transaction.TestTransaction;

public class SpringCommitsAndClosesTestTransactionTransactor {

    public void perform(UnitOfWork unitOfWork) throws Exception {
        // Spring starts transaction...

        unitOfWork.work();  // Do the job

        // Now commit stuff
        TestTransaction.flagForCommit();
        TestTransaction.end();
    }

}
