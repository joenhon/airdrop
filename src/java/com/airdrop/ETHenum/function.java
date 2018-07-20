package com.airdrop.ETHenum;



public enum function {
    pairdrop("0x1d833aae");
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
