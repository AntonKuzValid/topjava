package ru.javawebinar.topjava.repository.jdbc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.javawebinar.topjava.model.Role;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.repository.UserRepository;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.*;
import java.util.stream.Collectors;

@Transactional(readOnly = true)
@Repository
public class JdbcUserRepositoryImpl implements UserRepository {

    private static final JDBCUserMapper userMapper = new JDBCUserMapper();

    private final JdbcTemplate jdbcTemplate;

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    private final SimpleJdbcInsert insertUser;

    private final SimpleJdbcInsert insertRoles;

    @Autowired
    public JdbcUserRepositoryImpl(DataSource dataSource, JdbcTemplate jdbcTemplate, NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.insertUser = new SimpleJdbcInsert(dataSource)
                .withTableName("users")
                .usingGeneratedKeyColumns("id");

        this.insertRoles = new SimpleJdbcInsert(dataSource)
                .withTableName("user_roles");

        this.jdbcTemplate = jdbcTemplate;
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    @Override
    @Transactional
    public User save(User user) {
        BeanPropertySqlParameterSource parameterSource = new BeanPropertySqlParameterSource(user);

        if (user.isNew()) {
            Number newKey = insertUser.executeAndReturnKey(parameterSource);
            user.setId(newKey.intValue());
//            jdbcTemplate.batchUpdate("INSERT  INTO user_roles (role,user_id) VALUES (?,?)", createBatch(user.getRoles(), newKey.intValue()));
            insertRoles.executeBatch(createMapBatch(user.getRoles(), newKey.intValue()).toArray(new Map[user.getRoles().size()]));
        } else if (namedParameterJdbcTemplate.update(
                "UPDATE users SET name=:name, email=:email, password=:password, " +
                        "registered=:registered, enabled=:enabled, calories_per_day=:caloriesPerDay WHERE id=:id", parameterSource) == 0) {
            return null;
        } else {
            List<Object[]> batch;
            if ((batch = createBatch(user.getRoles(), user.getId())).size() > 1) {
                jdbcTemplate.update("UPDATE user_roles SET role=? WHERE user_id=?", batch.get(0), new int[]{Types.VARCHAR, Types.INTEGER});
                jdbcTemplate.update("INSERT  INTO user_roles (role,user_id) VALUES (?,?)", batch.get(1), new int[]{Types.VARCHAR, Types.INTEGER});
            } else
                jdbcTemplate.update("UPDATE user_roles SET role=? WHERE user_id=?", batch.get(0), new int[]{Types.VARCHAR, Types.INTEGER});
        }
        return user;
    }

    private static List<Object[]> createBatch(Set<Role> roles, int id) {
        List<Object[]> batch = new ArrayList<>();
        roles.forEach(r -> batch.add(new Object[]{r.toString(), id}));
        return batch;
    }

    private static List<Map<String, Object>> createMapBatch(Set<Role> roles, int id) {
        List<Map<String, Object>> batch = new ArrayList<>();
        roles.forEach(r -> {
            Map<String, Object> map = new HashMap<>();
            map.put("user_id", id);
            map.put("role", r.toString());
            batch.add(map);
        });
        return batch;
    }

    @Override
    @Transactional
    public boolean delete(int id) {
        return jdbcTemplate.update("DELETE FROM users WHERE id=?", id) != 0;
    }

    @Override
    public User get(int id) {
        List<User> users = jdbcTemplate.query("SELECT id, email, NAME, ENABLED, USER_ROLES.ROLE, CALORIES_PER_DAY, PASSWORD, REGISTERED " +
                "FROM users LEFT OUTER JOIN user_roles ON user_roles.user_id=users.id WHERE id=?", userMapper, id);
        return DataAccessUtils.singleResult(users);
    }

    @Override
    public User getByEmail(String email) {
        List<User> users = jdbcTemplate.query("SELECT id, email, NAME, ENABLED, USER_ROLES.ROLE, CALORIES_PER_DAY, PASSWORD, REGISTERED " +
                "FROM users LEFT OUTER JOIN user_roles ON user_roles.user_id=users.id WHERE email=?", userMapper, email);
        return DataAccessUtils.singleResult(users);
    }

    @Override
    public List<User> getAll() {
        return jdbcTemplate.query("SELECT id, email, NAME, ENABLED, USER_ROLES.ROLE, CALORIES_PER_DAY, PASSWORD, REGISTERED " +
                "FROM users LEFT OUTER JOIN user_roles ON user_roles.user_id=users.id ORDER BY name, email", userMapper);

    }

    private static class JDBCUserMapper implements ResultSetExtractor<List<User>> {
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
                Set<Role> roleSet = new HashSet<>();
                roleSet.add(Role.valueOf(rs.getString("role")));
                user.setRoles(roleSet);

                users.merge(rs.getInt("id"), user, (user1, user2) -> {
                    user1.getRoles().addAll(user2.getRoles());
                    return user1;
                });
            }
            return users.size() > 1 ?
                    new ArrayList<>(users.values()).stream().sorted(Comparator.comparing(User::getName).thenComparing(User::getEmail)).collect(Collectors.toList()) :
                    new ArrayList<>(users.values());
        }
    }
}
