package com.ch.permission.manage;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.Fragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by CH on 2016/12/14.
 */

public class PermissionUtil {





    /**
     * 请求获得权限
     *
     * @param object
     * @param requestCode
     * @param permissions
     */
    @TargetApi(value = Build.VERSION_CODES.M)
    public static void requestPermissions(Object object, int requestCode, String... permissions) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            doExecuteSuccess(object, requestCode);
            return;
        }
        List<String> deniedPermissions = findDeniedPermissions(object, permissions);

        if (deniedPermissions.size() > 0) {
            if (object instanceof Activity) {
                ((Activity) object).requestPermissions(deniedPermissions.toArray(new String[deniedPermissions.size()]), requestCode);
            } else if (object instanceof Fragment) {
                ((Fragment) object).requestPermissions(deniedPermissions.toArray(new String[deniedPermissions.size()]), requestCode);
            } else {
                throw new IllegalArgumentException(object.getClass().getName() + " is not supported!");
            }
        } else {
//            doExecuteSuccess(object, requestCode);
        }


    }

    /**
     * 请求权限结果回调
     *
     * @param obj
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    public static void requestPermissionsResult(Object obj, int requestCode, String[] permissions,
                                                int[] grantResults) {
        List<String> deniedPermissions = new ArrayList<>();
        for (int i = 0; i < grantResults.length; i++) {
            if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                deniedPermissions.add(permissions[i]);
            }
        }
        if (deniedPermissions.size() > 0) {
//            doExecuteFail(obj, requestCode);
        } else {
//            doExecuteSuccess(obj, requestCode);
        }
    }


    /**
     * 获得需要的权限
     *
     * @param object
     * @param permission
     * @return
     */
    @TargetApi(value = Build.VERSION_CODES.M)
    private static List<String> findDeniedPermissions(Object object, String... permission) {

        List<String> denyPermissions = new ArrayList<>();
        for (String value : permission) {
            if (object instanceof Activity) {
                if (((Activity) object).checkSelfPermission(value) != PackageManager.PERMISSION_GRANTED) {
                    denyPermissions.add(value);
                }
            } else if (object instanceof Fragment) {
                if (((Fragment) object).getActivity().checkSelfPermission(value) != PackageManager.PERMISSION_GRANTED) {
                    denyPermissions.add(value);
                }
            }
        }
        return denyPermissions;
    }
}
