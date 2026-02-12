package me.dawey.friends.database.entities;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "friends_playerdata")
public class FriendPlayer {

    @DatabaseField(generatedId = true)
    private int id;

    @DatabaseField(columnName = "player_uuid", canBeNull = false, unique = true)
    private String playerUuid;

    @DatabaseField(columnName = "player_name", canBeNull = false)
    private String playerName;

    @DatabaseField(columnName = "last_online")
    private long lastOnline;

    @DatabaseField(columnName = "requests_enabled")
    private boolean requestEnabled;

    public FriendPlayer() {}

    public FriendPlayer(String playerUuid, String playerName, long lastOnline, boolean requestEnabled) {
        this.playerUuid = playerUuid;
        this.playerName = playerName;
        this.lastOnline = lastOnline;
        this.requestEnabled = requestEnabled;
    }

    public int getId() {
        return id;
    }

    public String getPlayerUuid() {
        return playerUuid;
    }

    public void setPlayerUuid(String playerUuid) {
        this.playerUuid = playerUuid;
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
