import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Main1 {
    public static void main(String[] args) {
        Bank bank = new Bank(500, 500);

        Thread thread1 = new TransferThread(bank);
        Thread thread2 = new TransferThread(bank);

        thread1.start();
        thread2.start();
    }

    public static class Bank {
        private int account1, account2;
        private final Lock lock = new ReentrantLock();

        public Bank(int acc1, int acc2) {
            account1 = acc1;
            account2 = acc2;
        }

        public void transfer() throws InterruptedException {
            lock.lock();

            try {
                int amount = (int)(Math.random()*500);

                if (account1 > amount) {
                    account1 = account1 - amount;
                    Thread.sleep(2000);
                    account2 = account2 + amount;
                } else {
                    account1 = account1 + amount;
                    Thread.sleep(2000);
                    account2 = account2 - amount;
                }

                System.out.println("Account #1: " + account1 + "\t" +
                        "Account #2: " + account2 + "\t" +
                        "Total balance: " + totalBalance() + "\t");
            } finally {
                lock.unlock();
            }
        }

        protected int totalBalance() {
            return account1 + account2;
        }
    }

    public static class TransferThread extends Thread {
        private Bank bank;

        public TransferThread(Bank bank) {
            this.bank = bank;
        }

        public void run() {
            for (int i = 0; i < 100; i++) {
                try {
                    bank.transfer();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}