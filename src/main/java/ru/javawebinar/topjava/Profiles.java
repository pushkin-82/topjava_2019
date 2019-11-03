package ru.javawebinar.topjava;

public class Profiles {
    public static final String
            JDBC = "jdbc",
            JPA = "jpa",
            DATAJPA = "datajpa";

    public static final String REPOSITORY_IMPLEMENTATION = DATAJPA;

    public static final String
            POSTGRES_DB = "postgres",
            HSQL_DB = "hsqldb";

    public static final String
            JDBC_HSQLDB = "jdbchsqldb",
            JDBC_POSTGRES = "jdbcpostgres";

    //  Get DB profile depending of DB driver in classpath
    public static String getActiveDbProfile() {
        try {
            Class.forName("org.postgresql.Driver");
            return POSTGRES_DB;
        } catch (ClassNotFoundException ex) {
            try {
                Class.forName("org.hsqldb.jdbcDriver");
                return Profiles.HSQL_DB;
            } catch (ClassNotFoundException e) {
                throw new IllegalStateException("Could not find DB driver");
            }
        }
    }

    public static String getActiveRepositoryProfile() {
        return DATAJPA;
    }

    public static String getActiveJdbcDbProfile() {
        String dbProfile = getActiveDbProfile();
        return POSTGRES_DB.equals(dbProfile) ? JDBC_POSTGRES : HSQL_DB.equals(dbProfile) ? JDBC_HSQLDB : null;
    }
}
