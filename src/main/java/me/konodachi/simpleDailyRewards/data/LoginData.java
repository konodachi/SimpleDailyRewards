package me.konodachi.simpleDailyRewards.data;

import java.time.LocalDate;
import java.util.UUID;

public class LoginData {
    private UUID playerID;
    private int days;
    private int weeks;
    private boolean alreadyClaimed;
    private LocalDate lastClaim;

    public LoginData(UUID playerID){
        this.playerID = playerID;
        this.days = 0;
        this.weeks = 0;
        this.alreadyClaimed = false;
        this.lastClaim = LocalDate.now();
    }

    public LoginData(UUID playerID, int days, int weeks, boolean alreadyClaimed, LocalDate lastClaim) {
        this.playerID = playerID;
        this.days = days;
        this.weeks = weeks;
        this.alreadyClaimed = alreadyClaimed;
        this.lastClaim = lastClaim;
    }

    public UUID getPlayerID() {
        return playerID;
    }

    public void setPlayerID(UUID playerID) {
        this.playerID = playerID;
    }


    public int getDays() {
        return days;
    }

    public void setDays(int days) {
        this.days = days;
    }

    public int getWeeks() {
        return weeks;
    }

    public void setWeeks(int weeks) {
        this.weeks = weeks;
    }

    public boolean alreadyClaimed() {
        return alreadyClaimed;
    }

    public void setAlreadyClaimed(boolean alreadyClaimed) {
        this.alreadyClaimed = alreadyClaimed;
    }

    public LocalDate getLastClaim(){
        return lastClaim;
    }

    public void setLastClaim(LocalDate date){
        lastClaim = date;
    }

}
