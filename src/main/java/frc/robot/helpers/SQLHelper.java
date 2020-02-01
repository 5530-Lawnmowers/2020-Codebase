/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.helpers;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;

import java.sql.*;
import java.util.*;

import edu.wpi.first.wpilibj.shuffleboard.SimpleWidget;

/**
 * A mySQL helper for interacting with the debug database
 * Note: ALWAYS CLOSE CONNECTIONS WHEN NOT USING THEM
 */
public class SQLHelper {
    private static final String DB_URL = "jdbc:mysql://10.55.30.69:3306";
    private static final String USER = "5530";
    private static final String PASS = "larry";

    private static Connection conn;
    private static ResultSet row = null;
    private static ArrayList<SimpleWidget> widgets = new ArrayList<>();
    private static String status;

    static {
        try {
            Class.forName("org.mariadb.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        try {
            conn = DriverManager.getConnection(DB_URL, USER, PASS);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Opens the mySQL connection to the debug server
     *
     * @throws SQLException
     */
    public static void openConnection() throws SQLException {
        if (isOpen()) conn.close();
        conn = DriverManager.getConnection(DB_URL, USER, PASS);
    }

    /**
     * Closes the mySQL connection to the debug server
     *
     * @throws SQLException
     */
    public static void closeConnection() throws SQLException {
        if (isOpen()) {
            conn.close();
            status = null;
        }
    }

    /**
     * Checks the mySQL connection to the debug server
     *
     * @return <b>true</b> if open, <b>false</b> if closed
     * @throws SQLException
     */
    public static boolean isOpen() throws SQLException {
        return conn != null && !conn.isClosed();
    }

    /**
     * Initializes a new <b>NETWORK_TABLES</b> under the <b>DEBUG_PLATFORM</b> database
     * <br>DELETES ALL EXISTING DATA. BACKUP DATA USING THE {@link #backupTable()} METHOD
     *
     * @throws SQLException
     */
    public static void initTable() throws SQLException {
        Statement stmnt = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
        DatabaseMetaData metaData = conn.getMetaData();
        ResultSet tables = metaData.getTables(null, null, "NETWORK_TABLES", null);
        stmnt.execute("USE DEBUG_PLATFORM");
        if (tables.next()) {
            stmnt.execute("DROP TABLE `NETWORK_TABLES`");
        }
        stmnt.execute("CREATE TABLE `NETWORK_TABLES`(`Time` int NOT NULL , PRIMARY KEY(`Time`))");
        if (row != null && !row.isClosed()) row.getStatement().close();
        stmnt.close();
        stmnt = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
        row = stmnt.executeQuery("SELECT * FROM NETWORK_TABLES");
        row.moveToInsertRow();
        updateValue("Time", 0);
        for (SimpleWidget widget : widgets) {
            String title = widget.getTitle();
            String tab = widget.getParent().getTitle();
            Object value = ShuffleboardHelpers.getWidgetValue(tab, title);
            addColumn(tab + "/" + title, value.getClass());
            updateValue(tab + "/" + title, value);
        }
        stmnt = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
        row = stmnt.executeQuery("SELECT * FROM `NETWORK_TABLES`");
        row.moveToInsertRow();
        status = "initialized";
    }

    /**
     * Adds a new column to the <b>NETWORK_TABLES</b> table
     *
     * @param title Title of the new column
     * @param type  Data type of the new column
     * @throws SQLException
     */
    public static void addColumn(String title, Class<?> type) throws SQLException {
        Statement stmnt = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
        String colType;
        if (type == String.class) {
            colType = "VARCHAR(255)";
        } else if (type == Integer.class) {
            colType = "INT";
        } else if (type == Double.class) {
            colType = "DOUBLE";
        } else if (type == Boolean.class) {
            colType = "BOOLEAN";
        } else {
            throw new ClassCastException();
        }
        stmnt.execute("USE DEBUG_PLATFORM");
        stmnt.execute("ALTER TABLE NETWORK_TABLES ADD COLUMN `" + title + "` " + colType);
        row.getStatement().close();
        row = stmnt.executeQuery("SELECT * FROM `NETWORK_TABLES`");
        row.moveToInsertRow();
    }

    /**
     * Deletes a column from the <b>NETWORK_TABLES</b> table
     *
     * @param title Title of the column to be deleted
     * @throws SQLException
     */
    public static void delColumn(String title) throws SQLException {
        Statement stmnt = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
        stmnt.execute("USE DEBUG_PLATFORM");
        stmnt.execute("ALTER NETWORK_TABLES DROP `" + title + "`");
        row.getStatement().close();
        row = stmnt.executeQuery("SELECT * FROM `NETWORK_TABLES`");
        row.moveToInsertRow();
    }

    /**
     * Gets the data type of a column from the <b>NETWORK_TABLES</b> table
     *
     * @param title Title of the column to get
     * @return The data type of the column
     * @throws SQLException
     */
    public static Class<?> getType(String title) throws SQLException {
        Statement stmnt = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
        stmnt.execute("USE DEBUG_PLATFORM");
        ResultSet result = stmnt.executeQuery("SELECT DATA_TYPE FROM INFORMATION_SCHEMA.COLUMNS WHERE table_name = 'NETWORK_TABLES' AND COLUMN_NAME = '" + title + "';");
        String type = result.getString(0);
        result.close();
        stmnt.close();
        switch (type) {
            case "varchar":
                return String.class;
            case "int":
                return Integer.class;
            case "double":
                return Double.class;
            case "tinyint":
                return Boolean.class;
        }
        throw new ClassCastException();
    }

    /**
     * Get the a column from the <b>NETWORK_TABLES</b> table
     *
     * @param title Title of the column to get
     * @return The result set of the column
     * @throws SQLException
     */
    public static ResultSet getColumn(String title) throws SQLException {
        Statement stmnt = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
        stmnt.execute("USE DEBUG_PLATFORM");
        return stmnt.executeQuery("SELECT `" + title + "` FROM `NETWORK_TABLES`");
    }

    /**
     * Get a generic object array of a column's values
     *
     * @param title Title of the column to get
     * @return A generic object array list of column values
     * @throws SQLException
     */
    public static ArrayList<Object> getGenericCol(String title) throws SQLException {
        ResultSet set = getColumn(title);
        ArrayList<Object> arr = new ArrayList<Object>();
        while (set.next()) {
            arr.add(set.getObject(1));
        }
        set.getStatement().close();
        return arr;
    }

    /**
     * Get a String array of a column's values
     *
     * @param title Title of the column to get
     * @return A String array list of column values
     * @throws SQLException
     * @throws ClassCastException if the data type is not of type <b>String</b>
     */
    public static ArrayList<String> getStringCol(String title) throws SQLException {
        ResultSet set = getColumn(title);
        Class<?> type = getType(title);
        if (type != String.class) {
            throw new ClassCastException();
        }
        ArrayList<String> arr = new ArrayList<>();
        while (set.next()) {
            arr.add(set.getString(1));
        }
        set.getStatement().close();
        return arr;
    }

    /**
     * Get an Integer array of a column's values
     *
     * @param title Title of the column to get
     * @return An Integer array list of column values
     * @throws SQLException
     * @throws ClassCastException if the data type is not of type <b>int</b>
     */
    public static ArrayList<Integer> getIntCol(String title) throws SQLException {
        ResultSet set = getColumn(title);
        Class<?> type = getType(title);
        if (type != Integer.class) {
            throw new ClassCastException();
        }
        ArrayList<Integer> arr = new ArrayList<>();
        while (set.next()) {
            arr.add(set.getInt(1));
        }
        set.getStatement().close();
        return arr;
    }

    /**
     * Get a Double array of a column's values
     *
     * @param title Title of the column to get
     * @return A Double array list of column values
     * @throws SQLException
     * @throws ClassCastException if the data type is not of type <b>double</b>
     */
    public static ArrayList<Double> getDoubleCol(String title) throws SQLException {
        ResultSet set = getColumn(title);
        Class<?> type = getType(title);
        if (type != Double.class) {
            throw new ClassCastException();
        }
        ArrayList<Double> arr = new ArrayList<>();
        while (set.next()) {
            arr.add(set.getDouble(1));
        }
        set.getStatement().close();
        return arr;
    }

    /**
     * Get a Boolean array of a column's values
     *
     * @param title Title of the column to get
     * @return A Boolean array list of column values
     * @throws SQLException
     * @throws ClassCastException if the data type is not of type <b>boolean</b>
     */
    public static ArrayList<Boolean> getBooleanCol(String title) throws SQLException {
        ResultSet set = getColumn(title);
        Class<?> type = getType(title);
        if (type != Boolean.class) {
            throw new ClassCastException();
        }
        ArrayList<Boolean> arr = new ArrayList<>();
        while (set.next()) {
            arr.add(set.getBoolean(1));
        }
        set.getStatement().close();
        return arr;
    }

    /**
     * Update a column's value in the insert row
     *
     * @param column Title of the column to get
     * @param value  The value to update to
     * @throws SQLException
     */
    public static void updateValue(String column, Object value) throws SQLException {
        row.updateObject(column, value);
    }

    /**
     * Update a column's value in the insert row
     *
     * @param widget The widget to update the table from
     * @throws SQLException
     */
    public static void updateValue(SimpleWidget widget) throws SQLException {
        String title = widget.getTitle();
        String tab = widget.getParent().getTitle();
        Object value = ShuffleboardHelpers.getWidgetValue(tab, title);
        updateValue(tab + "/" + title, value);
    }

    /**
     * Push the insert row to the table
     *
     * @throws SQLException
     */
    public static void pushRow() throws SQLException {
        row.insertRow();
    }

    /**
     * Backup the <b>NETWORK_TABLES</b> table on the debug server
     *
     * @return The ID of the new table
     * @throws SQLException
     */
    public static String backupTable() throws SQLException {
        if (status == null || status == "initialized") return "Zoinks, Scoob!";
        Statement stmnt = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
        String time = new java.util.Date().toString();
        stmnt.execute("USE DEBUG_PLATFORM");
        stmnt.execute("CREATE TABLE IF NOT EXISTS `" + time + "` LIKE `NETWORK_TABLES`;");
        stmnt.execute("INSERT `" + time + "` SELECT * FROM `NETWORK_TABLES`");
        stmnt.close();
        return time;
    }

    /**
     * Stage a Shuffleboard widget to the SQL debug server
     *
     * @param widget The widget to monitor
     */
    public static void stageWidget(SimpleWidget widget) {
        widgets.add(widget);
    }

    /**
     * Call this method once per auton/teleop period
     *
     * @param timestamp The timestamp of this call
     * @throws SQLException
     */
    public static void mySQLperiodic(int timestamp) throws SQLException {
        status = "running";
        if (!compareLast()) {
            row.moveToInsertRow();
            updateValue("Time", timestamp);
            for (SimpleWidget widget : widgets) {
                updateValue(widget);
            }
            pushRow();
        }
    }

    /**
     * Compares the last row of the <b>NETWORK_TABLES</b> table to the new insert row
     * <br>Used to determine if the new row should be pushed or not (to save storage space)
     *
     * @return <b>true</b> if they are equal, <b>false</b> if they are not
     * @throws SQLException
     */
    public static boolean compareLast() throws SQLException {
        if (!row.isClosed() && row != null) row.getStatement().close();
        Statement stmnt = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
        row = stmnt.executeQuery("SELECT * FROM `NETWORK_TABLES`");
        int cols = row.getMetaData().getColumnCount();
        ArrayList<Object> lastVals = new ArrayList<>();
        if (!row.last()) return false;
        row.last();
        for (int i = 2; i <= cols; i++) {
            lastVals.add(row.getObject(i));
        }
        row.moveToInsertRow();
        ArrayList<Object> newVals = new ArrayList<>();
        for (int i = 0; i < cols - 1; i++) {
            newVals.add(ShuffleboardHelpers.getWidgetValue(widgets.get(i).getParent().getTitle(), widgets.get(i).getTitle()));
        }
        for (int i = 0; i < cols - 1; i++) {
            Object val = newVals.get(i);
            Class<?> type = val.getClass();
            if (type == String.class) {
                if (!val.equals(lastVals.get(i))) return false;
            } else if (type == Double.class) {
                if (Math.abs((double)(val) - (double)lastVals.get(i)) > 0.00001) return false;
            } else if (type == Boolean.class) {
                if ((boolean)(val) != (boolean)lastVals.get(i)) return false;
            } else if (type == Integer.class) {
                if ((int)(val) != (int)lastVals.get(i)) return false;
            }
        }
        return true;
    }
}
