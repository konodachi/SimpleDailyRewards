package me.konodachi.simpleDailyRewards;

import me.konodachi.simpleDailyRewards.data.LoginData;
import org.jspecify.annotations.Nullable;

import java.io.File;
import java.sql.*;
import java.time.LocalDate;
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
                Date sqlDate = resultSet.getDate("last_claim");
                LocalDate lastClaim = (sqlDate != null) ? sqlDate.toLocalDate() : LocalDate.now().minusDays(1);
                LoginData loginData = new LoginData(playerID, days, weeks, already_claimed, lastClaim);

                data.put(playerID, loginData);
            }
        } catch (SQLException e){
            e.printStackTrace();
        }
    }

    public static void updateLoginData(LoginData loginData){
        try (Connection connection = DriverManager.getConnection("jdbc:sqlite:" + path);
            PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO player_data (uuid, days, weeks, already_claimed, last_claim) " +
                    "VALUES (?, ?, ?, ?, ?) " +
                    "ON CONFLICT(uuid) DO UPDATE SET " +
                    "days = excluded.days, " +
                    "weeks = excluded.weeks, " +
                    "already_claimed = excluded.already_claimed, " +
                    "last_claim = excluded.last_claim")){
            preparedStatement.setString(1, loginData.getPlayerID().toString());
            preparedStatement.setInt(2, loginData.getDays());
            preparedStatement.setInt(3, loginData.getWeeks());
            preparedStatement.setBoolean(4, loginData.alreadyClaimed());
            if (loginData.getLastClaim() != null) preparedStatement.setDate(5, java.sql.Date.valueOf(loginData.getLastClaim()));
            else preparedStatement.setDate(5, java.sql.Date.valueOf(LocalDate.now()));

            preparedStatement.executeUpdate();
            dumpPlayerData(loginData.getPlayerID());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static @Nullable LoginData dumpPlayerData(UUID playerID){
        return data.containsKey(playerID) ? data.remove(playerID) : null;
    }

    public static @Nullable LoginData getData(UUID playerID){
        return data.getOrDefault(playerID, null);
    }

    public static void resetStreak(UUID playerID){
        LoginData data = getData(playerID);
        if (data == null) return;
        data.setDays(1);
        data.setWeeks(0);
        data.setAlreadyClaimed(false);
        data.setLastClaim(LocalDate.now().minusDays(1));
    }
}
