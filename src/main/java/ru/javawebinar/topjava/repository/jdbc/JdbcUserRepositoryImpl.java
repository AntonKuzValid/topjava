package ru.javawebinar.topjava.repository.jdbc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.repository.UserRepository;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;

@Transactional(readOnly = true)
@Repository
public class JdbcUserRepositoryImpl implements UserRepository {

    private static final BeanPropertyRowMapper<User> ROW_MAPPER = BeanPropertyRowMapper.newInstance(User.class);
    private static final JDBCUserMapper userMapper = new JDBCUserMapper();

    private final JdbcTemplate jdbcTemplate;

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    private final SimpleJdbcInsert insertUser;

    @Autowired
    public JdbcUserRepositoryImpl(DataSource dataSource, JdbcTemplate jdbcTemplate, NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.insertUser = new SimpleJdbcInsert(dataSource)
                .withTableName("users")
                .usingGeneratedKeyColumns("id");

        this.jdbcTemplate = jdbcTemplate;
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    @Override
    @Transactional
    public User save(User user) {
        BeanPropertySqlParameterSource parameterSource = new BeanPropertySqlParameterSource(user);
//        UserSqlParameterSource parameterSource= new UserSqlParameterSource(user);
//        parameterSource.addValue("roles", user.getRoles());

        if (user.isNew()) {
            Number newKey = insertUser.executeAndReturnKey(parameterSource);
            user.setId(newKey.intValue());
//            user.getRoles().forEach(r->jdbcTemplate.update("INSERT  INTO user_roles (user_id, role) VALUES (?,?)",newKey.intValue(), r.toString()));

            List <Object[]> batch=new ArrayList<>();
            user.getRoles().forEach(r->batch.add(new Object[]{newKey.intValue(),r.toString()}));
            jdbcTemplate.batchUpdate("INSERT  INTO user_roles (user_id, role) VALUES (?,?)",batch);
        } else if (namedParameterJdbcTemplate.update(
                "UPDATE users SET name=:name, email=:email, password=:password, " +
                        "registered=:registered, enabled=:enabled, calories_per_day=:caloriesPerDay WHERE id=:id", parameterSource) == 0) {
            return null;
        }
        return user;
    }

    @Override
    @Transactional
    public boolean delete(int id) {
        return jdbcTemplate.update("DELETE FROM users WHERE id=?", id) != 0;
    }

    @Override
    public User get(int id) {
        List<User> users = jdbcTemplate.query("SELECT id, email, NAME, ENABLED, USER_ROLES.ROLE, CALORIES_PER_DAY, PASSWORD, REGISTERED FROM users LEFT OUTER JOIN user_roles ON user_roles.user_id=users.id WHERE id=?", userMapper, id);
        return DataAccessUtils.singleResult(users);
    }

    @Override
    public User getByEmail(String email) {
//        return jdbcTemplate.queryForObject("SELECT * FROM users WHERE email=?", ROW_MAPPER, email);
        List<User> users = jdbcTemplate.query("SELECT id, email, NAME, ENABLED, USER_ROLES.ROLE, CALORIES_PER_DAY, PASSWORD, REGISTERED FROM users LEFT OUTER JOIN user_roles ON user_roles.user_id=users.id WHERE email=?", userMapper, email);
        return DataAccessUtils.singleResult(users);
    }

    @Override
    public List<User> getAll() {
        return jdbcTemplate.query("SELECT id, email, NAME, ENABLED, USER_ROLES.ROLE, CALORIES_PER_DAY, PASSWORD, REGISTERED FROM users LEFT OUTER JOIN user_roles ON user_roles.user_id=users.id ORDER BY name, email",userMapper.getUserResultSetExtractor());

    }
}
