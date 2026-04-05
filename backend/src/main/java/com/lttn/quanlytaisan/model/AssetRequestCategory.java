package com.lttn.quanlytaisan.model;

/**
 * Canonical category codes for allocation requests (must match frontend option values).
 */
public enum AssetRequestCategory {
    LAPTOP,
    DESKTOP,
    MONITOR,
    KEYBOARD,
    MOUSE,
    HEADPHONE,
    PRINTER,
    PHONE,
    TABLET,
    OTHER;

    public static AssetRequestCategory fromString(String raw) {
        if (raw == null || raw.isBlank()) {
            return OTHER;
        }
        String s = raw.trim().toUpperCase().replace('-', '_');
        try {
            return AssetRequestCategory.valueOf(s);
        } catch (IllegalArgumentException e) {
            return OTHER;
        }
    }

    /**
     * Whether an asset's free-text category is compatible with this request category.
     */
    public boolean matchesAssetCategory(String assetCategory) {
        if (assetCategory == null || assetCategory.isBlank()) {
            return false;
        }
        String ac = assetCategory.trim().toLowerCase();
        return switch (this) {
            case LAPTOP -> ac.contains("laptop");
            case DESKTOP -> ac.contains("desktop") || ac.contains("máy bàn") || ac.contains("may ban");
            case MONITOR -> ac.contains("monitor") || ac.contains("màn hình") || ac.contains("man hinh");
            case KEYBOARD -> ac.contains("keyboard") || ac.contains("bàn phím") || ac.contains("ban phim");
            case MOUSE -> ac.contains("mouse") || ac.contains("chuột") || ac.contains("chuot");
            case HEADPHONE -> ac.contains("headphone") || ac.contains("tai nghe") || ac.contains("tainghe");
            case PRINTER -> ac.contains("printer") || ac.contains("máy in") || ac.contains("may in");
            case PHONE -> ac.contains("phone") || ac.contains("điện thoại") || ac.contains("dien thoai");
            case TABLET -> ac.contains("tablet") || ac.contains("máy tính bảng") || ac.contains("may tinh bang");
            case OTHER -> true;
        };
    }
}
