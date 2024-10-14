package com.xk.upms.model.enums;

/**
 * PermissionAction 枚舉
 * Created by yuan on 2024/09/11
 * @author yuan
 */
public enum PermissionAction {
    READ("READ"),
    WRITE("WRITE"),
    CREATE("CREATE");

    private final String displayName;

    PermissionAction(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    /**
     * Convert a string value to PermissionAction enum.
     * @param value the string value
     * @return PermissionAction enum or null if not found
     */
    public static PermissionAction fromString(String value) {
        for (PermissionAction action : PermissionAction.values()) {
            if (action.name().equalsIgnoreCase(value)) {
                return action;
            }
        }
        throw new IllegalArgumentException("Invalid permission action: " + value);
    }
    
    /**
     * Get the number of PermissionAction enums.
     * @return the size of PermissionAction values
     */
    public static int size() {
        return PermissionAction.values().length;
    }
}
