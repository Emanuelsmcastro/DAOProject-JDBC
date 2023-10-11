package db;

public class DbIntegrityException extends RuntimeException {
    static final long serialVersionUID = 1L;

    public DbIntegrityException(String msg) {
        super(msg);
    }
}
