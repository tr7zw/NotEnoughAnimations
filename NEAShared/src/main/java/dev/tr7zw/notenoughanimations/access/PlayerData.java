package dev.tr7zw.notenoughanimations.access;

public interface PlayerData {

    public boolean isUpdated(int frameId);
    
    public void setUpdated(int frameId);
    
    public long lastUpdate();
    
    public float[] getLastRotations();
    
}
