package dataaccess;

import model.AuthData;
import model.UserData;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.*;

import static java.sql.Types.NULL;

public class SQLAuthDAO implements AuthDAO {

    public SQLAuthDAO() throws DataAccessException {
        configureDatabase();
    }

    @Override
    public AuthData createAuth(String username) throws SQLException, DataAccessException {
        String authToken = java.util.UUID.randomUUID().toString();
        var statement = "INSERT INTO auths (authToken, username) VALUES (?, ?)";
        executeUpdate(statement,authToken, username);
        return new AuthData(authToken,username);
    }

    @Override
    public AuthData getAuth(String authToken) throws DataAccessException {
        try (Connection conn = DatabaseManager.getConnection()){
            var statement = "SELECT authToken, username FROM auths WHERE authToken=?";
            try(PreparedStatement ps = conn.prepareStatement(statement)){
                ps.setString(1,authToken);
                try(ResultSet rs = ps.executeQuery()){
                    if(rs.next()){
                        return new AuthData(rs.getString("authToken"),rs.getString("username"));
                    }
                }
            }
        } catch (SQLException e){
            throw new DataAccessException(String.format("Unable to read data: %s", e.getMessage()));
        }
        return null;
    }

    @Override
    public void deleteAuth(String authToken) throws SQLException, DataAccessException {
        var statement = "DELETE FROM auths WHERE authToken=?";
        executeUpdate(statement,authToken);
    }

    @Override
    public void clear() throws SQLException, DataAccessException {
        var statement = "TRUNCATE TABLE auths";
        executeUpdate(statement);
    }



    private void configureDatabase() throws DataAccessException{
        try {
            DatabaseManager.createDatabase();
            try (var conn = DatabaseManager.getConnection()) {
                var createAuths = """
                        CREATE TABLE IF NOT EXISTS auths (
                            authToken VARCHAR(255) NOT NULL,
                            username VARCHAR(255) NOT NULL,
                            PRIMARY KEY (authToken)
                        )""";
                conn.prepareStatement(createAuths).executeUpdate();
            }
        } catch (Exception e) {
            throw new DataAccessException(String.format("Unable to configure database: %s", e.getMessage()));
        }
    }


    private int executeUpdate(String statement, Object... params) throws DataAccessException, SQLException {
        try (Connection conn = DatabaseManager.getConnection()) {
            try (PreparedStatement ps = conn.prepareStatement(statement, Statement.RETURN_GENERATED_KEYS)){
                for (int i = 0; i< params.length; i++) {
                    Object param = params[i];
                    if (param instanceof String p) ps.setString(i + 1, p);
                    else if (param instanceof Integer p) ps.setInt(i + 1, p);
                    else if (param == null) ps.setNull(i + 1, NULL);
                }
                ps.executeUpdate();
                ResultSet rs = ps.getGeneratedKeys();
                if (rs.next()) {
                    return rs.getInt(1);
                }
                return 0;
            } catch (SQLException e) {
                throw new DataAccessException(String.format("unable to update database: %s, %s", statement, e.getMessage()));
            }
        }
    }
}

