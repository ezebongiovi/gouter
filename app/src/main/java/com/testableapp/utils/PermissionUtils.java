package com.testableapp.utils;

import android.content.Context;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;

import java.util.ArrayList;
import java.util.List;

public class PermissionUtils {

    private PermissionUtils() {
        throw new AssertionError("Utility classes shouldn't be instantiated");
    }

    /**
     * Checks permissions and returns an array containing denied permissions
     *
     * @param context     the application's context
     * @param permissions the list of permissions wanted to check
     * @return the permissions from the received array which are denied
     */
    public static List<String> checkForPermissions(@NonNull final Context context,
                                                   @NonNull final String... permissions) {
        final List<String> requiredPermissions = new ArrayList<>();

        for (final String permission : permissions) {
            if (ContextCompat.checkSelfPermission(context, permission)
                    == PackageManager.PERMISSION_DENIED) {
                requiredPermissions.add(permission);
            }
        }

        return requiredPermissions;
    }
}
