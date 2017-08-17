package com.framgia.ishipper.util;

import java.util.Locale;

/**
 * Created by vuduychuong1994 on 8/24/16.
 */
public class TextFormatUtils {

    public static String formatDistance(double distance) {
        return String.format(Locale.US, "%1$,.1f Km", distance);
    }

    public static String formatPrice(double orderPrice) {
        return String.format(Locale.US, "%,d", (int) orderPrice);
    }
}
