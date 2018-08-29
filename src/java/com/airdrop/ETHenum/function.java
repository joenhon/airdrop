package com.airdrop.ETHenum;



public enum function {
    drop("0x1d833aae"),
    airdrop("0x1d833aae"),
    airdropValues("0x7d488464"),
    dataMigration("0x5bb2d425");
    private String name;

    function(String name) {
        this.name = name;
    }

    public String getValue() {
        return name;
    }

    public static function fromString(String name) {
        if (name != null) {
            for (function function:
                    function.values()) {
                if (name.equalsIgnoreCase(function.name)) {
                    return function;
                }
            }
        }
        return valueOf(name);
    }
}
