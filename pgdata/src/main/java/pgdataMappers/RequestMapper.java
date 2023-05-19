package pgdataMappers;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.lang.Nullable;

import model.Request;

public class RequestMapper implements RowMapper<Request>  {

    @Override
    @Nullable
    public Request mapRow(ResultSet rs, int rowNum) throws SQLException {
        int id = rs.getInt("request_id");
        int userFromId = rs.getInt("client_from_id");
        int userToId = rs.getInt("client_to_id");
        String message = rs.getString("message");
        boolean satisfied = rs.getBoolean("satisfied");
        Date timeChanged = rs.getDate("time_changed");

        return new Request(id, userFromId, userToId, message, satisfied, timeChanged);
    }
    
}
