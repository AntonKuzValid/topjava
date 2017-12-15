package ru.javawebinar.topjava.repository.jdbc;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.lang.Nullable;
import ru.javawebinar.topjava.model.Role;
import ru.javawebinar.topjava.model.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

public class JDBCUserMapper implements RowMapper<User> {

    private  JDBCResultSetextractor userResultSetExtractor=new JDBCResultSetextractor();

    public JDBCResultSetextractor getUserResultSetExtractor() {
        return userResultSetExtractor;
    }

    @Nullable
    @Override
    public User mapRow(ResultSet rs, int rowNum) throws SQLException {
        User user = new User();

        user.setId(rs.getInt("id"));
        user.setName(rs.getString("name"));
        user.setEmail(rs.getString("email"));
        user.setPassword(rs.getString("password"));
        user.setEnabled(rs.getBoolean("enabled"));
        user.setRegistered(rs.getDate("registered"));
        user.setCaloriesPerDay(rs.getInt("calories_per_day"));
        user.setRoles(Collections.singleton(Role.valueOf(rs.getString("role"))));
        return user;
    }

    private class JDBCResultSetextractor implements ResultSetExtractor<List<User>> {
        @Nullable
        @Override
        public List<User> extractData(ResultSet rs) throws SQLException, DataAccessException {
            Map<Integer, User> users = new HashMap<>();
            while (rs.next()) {
                User user = new User();
                user.setId(rs.getInt("id"));
                user.setName(rs.getString("name"));
                user.setEmail(rs.getString("email"));
                user.setPassword(rs.getString("password"));
                user.setEnabled(rs.getBoolean("enabled"));
                user.setRegistered(rs.getDate("registered"));
                user.setCaloriesPerDay(rs.getInt("calories_per_day"));
                if (rs.getString("role")!=null) {
                    Set<Role> roleSet = new HashSet<>();
                    roleSet.add(Role.valueOf(rs.getString("role")));
                    user.setRoles(roleSet);
                }
                users.merge(rs.getInt("id"), user, (user1, user2) -> {
                    user1.getRoles().addAll(user2.getRoles());
                    return user1;
                });
            }
            return new ArrayList<>(users.values()).stream().sorted(Comparator.comparing(User::getName).thenComparing(User::getEmail)).collect(Collectors.toList());
        }
    }
}
