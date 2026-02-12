package me.dawey.friends.database.entities;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "friends_playerdata")
public class FriendPlayer {

    @DatabaseField(columnName = "PlayerName", id = true, canBeNull = false)
    private String playerName;

    @DatabaseField(columnName = "LastOnline")
    private long lastOnline;

    @DatabaseField(columnName = "RequestEnabled")
    private boolean requestEnabled;

    public FriendPlayer() {}

    public FriendPlayer(String playerName, long lastOnline, boolean requestEnabled) {
        this.playerName = playerName;
        this.lastOnline = lastOnline;
        this.requestEnabled = requestEnabled;
    }

    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public long getLastOnline() {
        return lastOnline;
    }

    public void setLastOnline(long lastOnline) {
        this.lastOnline = lastOnline;
    }

    public boolean isRequestEnabled() {
        return requestEnabled;
    }

    public void setRequestEnabled(boolean requestEnabled) {
        this.requestEnabled = requestEnabled;
    }
}
