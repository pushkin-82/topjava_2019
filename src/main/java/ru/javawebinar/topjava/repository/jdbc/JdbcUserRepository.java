package ru.javawebinar.topjava.repository.jdbc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.javawebinar.topjava.model.Role;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.repository.UserRepository;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.*;

@Repository
@Transactional(readOnly = true)
public class JdbcUserRepository implements UserRepository {

    private final JdbcTemplate jdbcTemplate;

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    private final SimpleJdbcInsert insertUser;

    @Autowired
    public JdbcUserRepository(JdbcTemplate jdbcTemplate, NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.insertUser = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("users")
                .usingGeneratedKeyColumns("id");

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
        } else if (namedParameterJdbcTemplate.update(
                "UPDATE users SET name = :name, email = :email, password = :password, " +
                        "registered = :registered, enabled = :enabled, calories_per_day = :caloriesPerDay " +
                        "WHERE id = :id", parameterSource) == 0) {
            return null;
        } else {
            jdbcTemplate.update("DELETE FROM user_roles WHERE user_id=?", user.getId());
        }
        batchInsert(user.getId(), new ArrayList<>(user.getRoles()));
        return user;
    }

    @Override
    @Transactional
    public boolean delete(int id) {
        String deleteQuery = "DELETE FROM users WHERE id=?";
        return jdbcTemplate.update(deleteQuery, id) != 0;
    }

    @Override
    public User get(int id) {
        String getQuery = "SELECT * FROM users u LEFT OUTER JOIN user_roles ur ON u.id = ur.user_id WHERE id=?";
        List<User> users = jdbcTemplate.query(getQuery, getUsersWithRolesResultSetExtractor(), id);
        return DataAccessUtils.singleResult(users);
    }

    @Override
    public User getByEmail(String email) {
        String getByEmailQuery = "SELECT * FROM users u LEFT OUTER JOIN user_roles ur ON u.id = ur.user_id WHERE email=?";
        List<User> users = jdbcTemplate.query(getByEmailQuery, getUsersWithRolesResultSetExtractor(), email);
        return DataAccessUtils.singleResult(users);
    }

    @Override
    public List<User> getAll() {
        String getAllQuery = "SELECT * FROM users u LEFT OUTER JOIN user_roles ur on u.id = ur.user_id ORDER BY name, email";
        return jdbcTemplate.query(getAllQuery, getUsersWithRolesResultSetExtractor());
    }

    private ResultSetExtractor<List<User>> getUsersWithRolesResultSetExtractor() {
        return rs -> {
            Map<Integer, User> usersMap = new HashMap<>();
            Map<Integer, Set<Role>> usersRoles = new HashMap<>();

            while (rs.next()) {

                int userId = rs.getInt("id");
                Role userRole = Role.valueOf(rs.getString("role"));

                usersRoles.computeIfAbsent(userId, k -> EnumSet.noneOf(Role.class));
                usersRoles.get(userId).add(userRole);

                User user = new User();
                user.setId(userId);
                user.setName(rs.getString("name"));
                user.setEmail(rs.getString("email"));
                user.setPassword(rs.getString("password"));
                user.setRegistered(rs.getDate("registered"));
                user.setEnabled(rs.getBoolean("enabled"));
                user.setCaloriesPerDay(rs.getInt("calories_per_day"));
                usersMap.computeIfAbsent(userId, k -> user);
            }
            List<User> userList = new ArrayList<>(usersMap.values());
            userList.forEach(user -> user.setRoles(usersRoles.get(user.getId())));

            return userList;
        };
    }

    private int[] batchInsert(int userId, List<Role> roles) {

        return jdbcTemplate.batchUpdate(
                "INSERT INTO user_roles VALUES (?,?)",
                new BatchPreparedStatementSetter() {

                    public void setValues(PreparedStatement ps, int i) throws SQLException {
                        ps.setInt(1, userId);
                        ps.setString(2, roles.get(i).name());
                    }

                    public int getBatchSize() {
                        return roles.size();
                    }
                });
    }

}
