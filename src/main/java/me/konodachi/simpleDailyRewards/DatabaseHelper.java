package me.konodachi.simpleDailyRewards;

import org.bukkit.entity.Player;

import java.io.File;
import java.sql.*;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class DatabaseHelper {
    private static ConcurrentMap<UUID, LoginData> data;
    private static String path;

    private DatabaseHelper(){}

    public static void prepareDatabase(String path){
        DatabaseHelper.path = path;

        File file = new File(path);
        if (!file.getParentFile().exists()) file.getParentFile().mkdirs();

        try (Connection connection = DriverManager.getConnection("jdbc:sqlite:" + path);
            Statement statement = connection.createStatement()){
            statement.execute("CREATE TABLE IF NOT EXISTS player_data (" +
                    "uuid TEXT PRIMARY KEY, " +
                    "days INTEGER, " +
                    "weeks INTEGER, " +
                    "already_claimed BOOLEAN," +
                    "last_claim DATETIME)");
        } catch (SQLException e){
            e.printStackTrace();
        }

        data = new ConcurrentHashMap<>();
    }

    public static void fetchLoginData(UUID playerID){
        try (Connection connection = DriverManager.getConnection("jdbc:sqlite:" + path);
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM player_data WHERE uuid = ?")){
            statement.setString(1, playerID.toString());
            ResultSet resultSet = statement.executeQuery();
            if (!resultSet.next()){
                LoginData loginData = new LoginData(playerID);
                data.put(playerID, loginData);
            } else{
                int days = resultSet.getInt("days");
                int weeks = resultSet.getInt("weeks");
                boolean already_claimed = resultSet.getBoolean("already_claimed");
                Date lastClaim = resultSet.getDate("last_claim");
                LoginData loginData = new LoginData(playerID, days, weeks, already_claimed, lastClaim);
                data.put(playerID, loginData);
            }
        } catch (SQLException e){
            e.printStackTrace();
        }
    }

    public static void updateLoginData(UUID playerID){
        try (Connection connection = DriverManager.getConnection("jdbc:sqlite:" + path);
            PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO player_data (uuid, days, weeks, already_claimed, last_claim) " +
                    "VALUES (?, ?, ?, ?, ?) " +
                    "ON CONFLICT(uuid) DO UPDATE SET " +
                    "days = excluded.days, " +
                    "weeks = excluded.weeks, " +
                    "already_claimed = excluded.already_claimed, " +
                    "last_claim = excluded.last_claim")){
            preparedStatement.setString(1, playerID.toString());
            preparedStatement.setInt(2, data.get(playerID).getDays());
            preparedStatement.setInt(3, data.get(playerID).getWeeks());
            preparedStatement.setBoolean(4, data.get(playerID).alreadyClaimed());
            if (data.get(playerID).getLastClaim() != null) {
                preparedStatement.setTimestamp(5, new java.sql.Timestamp(data.get(playerID).getLastClaim().getTime()));
            } else {
                preparedStatement.setTimestamp(5, null);
            }

            preparedStatement.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static LoginData getData(UUID playerID){
        return data.get(playerID);
    }
}
