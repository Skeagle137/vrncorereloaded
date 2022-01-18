package net.skeagle.vrncore.playerdata;

public class PlayerStates {

    private boolean vanished;
    private boolean godmode;
    private boolean tpDisabled;

    public boolean isVanished() {
        return vanished;
    }

    public void setVanished(boolean vanished) {
        this.vanished = vanished;
    }

    public boolean hasGodmode() {
        return godmode;
    }

    public void setGodmode(boolean godmode) {
        this.godmode = godmode;
    }

    public boolean isTpDisabled() {
        return tpDisabled;
    }

    public void setTpDisabled(boolean tpDisabled) {
        this.tpDisabled = tpDisabled;
    }


}
