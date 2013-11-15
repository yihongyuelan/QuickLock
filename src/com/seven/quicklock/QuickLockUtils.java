
package com.seven.quicklock;

import java.util.List;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.Intent.ShortcutIconResource;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.text.TextUtils;

public class QuickLockUtils {

    private static QuickLockUtils mUtils;

    public static QuickLockUtils getInstance() {
        if (mUtils == null) {
            mUtils = new QuickLockUtils();
        }
        return mUtils;
    }

    private boolean addShortCut(Context context, String pkg) {
        // shortcut name
        String name = "unknown";
        String mainAct = null;
        // shortcut icon id
        int iconIdentifier = -1;
        PackageManager pkManager = context.getPackageManager();
        // create intent to query activities
        Intent intent = new Intent(Intent.ACTION_MAIN, null);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        // get the activities list through PackageManager
        List<ResolveInfo> list = pkManager.queryIntentActivities(intent,
                PackageManager.GET_ACTIVITIES);
        for (ResolveInfo info : list) {
            // compare the package name
            if (TextUtils.equals(info.activityInfo.packageName, pkg)) {
                // get the APP name
                name = info.loadLabel(pkManager).toString();
                // get the APP ICON id
                iconIdentifier = info.activityInfo.applicationInfo.icon;
                // get the entry activity name
                mainAct = info.activityInfo.name;
                break;
            }
        }
        if (TextUtils.isEmpty(mainAct)) {
            // if there's no entry activity or something wrong, return false
            return false;
        }
        // create a intent preparing for shortcut
        Intent shortcut = new Intent("com.android.launcher.action.INSTALL_SHORTCUT");
        shortcut.putExtra(Intent.EXTRA_SHORTCUT_NAME, name);
        // duplicates are not allowed to create a shortcut
        shortcut.putExtra("duplicate", false);
        ComponentName comp = new ComponentName(pkg, mainAct);
        shortcut.putExtra(Intent.EXTRA_SHORTCUT_INTENT,
                new Intent(Intent.ACTION_MAIN).setComponent(comp));
        Context pkgContext = null;
        if (TextUtils.equals(pkg, context.getPackageName())) {
            pkgContext = context;
        } else {
            try {
                // create a context by the package
                pkgContext = context.createPackageContext(pkg, Context.CONTEXT_IGNORE_SECURITY
                        | Context.CONTEXT_INCLUDE_CODE);
            } catch (NameNotFoundException e) {
                e.printStackTrace();
            }
        }
        // add the icon
        if (pkgContext != null) {
            ShortcutIconResource iconResource = Intent.ShortcutIconResource.fromContext(pkgContext,
                    iconIdentifier);
            shortcut.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE, iconResource);
        }
        // send the broadcast
        // we need to add the permission to AndroidManifest.xml
        // <uses-permission
        // android:name="com.android.launcher.permission.INSTALL_SHORTCUT" />
        context.sendBroadcast(shortcut);
        return true;
    }
}
