package pias.backend.flyway;

public interface FlywayManaged {
    void migrate();

    void clean();
}
