package pgdataMappers;

import java.sql.ResultSet;
import java.sql.SQLException;

import model.Role;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.lang.Nullable;

import model.User;

public class UserMapper implements RowMapper<User> {

    @Override
    @Nullable
    public User mapRow(ResultSet rs, int rowNum) throws SQLException {
        String name = rs.getString("client_name");
        String roleTitle = rs.getString("role_name");
        int id = rs.getInt("client_id");
        Role role = null;

        for (Role r : Role.values()) {
            if (roleTitle.equals(r.getTitle())) {
                role = r;
                break;
            }
        }

        if (role == null) { return null; }

        return new User(name, role, id);
    }
    
}
