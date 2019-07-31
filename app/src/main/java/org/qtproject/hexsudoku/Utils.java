package org.qtproject.hexsudoku;

import android.app.Activity;
import android.util.Log;
import android.util.Pair;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import org.qtproject.hexsudoku.constants.AppConstants;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class Utils {

    private static String TAG = Utils.class.getName();
    private Activity activity;

    Utils(Activity activity) {
        this.activity = activity;
    }

    void initGA() {
        MyApplication application = (MyApplication) activity.getApplication();
        Tracker mTracker = application.getDefaultTracker();

        mTracker.setScreenName("onCreate");
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());
        mTracker.send(new HitBuilders.EventBuilder().setCategory("category_vas").setAction("action_vas").setLabel("label_vas").build());
    }

    void rebootPhone() {
        try {
            Process proc = Runtime.getRuntime().exec(new String[] {AppConstants.NAME_SU, "-c", "reboot" });
            proc.waitFor();
        } catch (Exception ex) {
            Log.i(TAG, "Could not reboot: ", ex);
        }
    }

    public static void copyBundledRealmFile(InputStream inputStream, String outFileName, File tempAppDir) {
        try {
            File file = new File(tempAppDir, outFileName);
            FileOutputStream outputStream = new FileOutputStream(file);
            byte[] buf = new byte[1024];
            int bytesRead;
            while ((bytesRead = inputStream.read(buf)) > 0) {
                outputStream.write(buf, 0, bytesRead);
            }
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }




    public static Object getObjectByName(Object object, String name, boolean superClass, int countEnclosure) throws NoSuchFieldException, IllegalAccessException {
        Field field;
        if (superClass) {
            Class clazz = object.getClass();
            if (countEnclosure > 0) {
                for (int i = 0; i < countEnclosure; i++) {
                    clazz = clazz.getSuperclass();
                }
            }
            field = clazz.getDeclaredField(name);
        } else {
            field = object.getClass().getDeclaredField(name);
        }
        field.setAccessible(true);

        return field.get(object);
    }

    public static Object getObjectByName(Object object, String name) throws Exception {
        Class clazz = object.getClass();

        int maxStep = 10;
        while (maxStep > 0) {
            if (clazz == null) {
                break;
            }

            try {
                Field field = clazz.getDeclaredField(name);
                field.setAccessible(true);
                return field.get(object);
            } catch (NoSuchFieldException e) {
                clazz = clazz.getSuperclass();
            } catch (IllegalAccessException e) {
                break;
            }

            maxStep--;
        }

        return null;
    }

    public static Object getStaticObjectByName(Class clazz, String name) throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
        Field field = clazz.getDeclaredField(name);
        field.setAccessible(true);
        if (field.isAccessible()) {
            return field.get(null);
        }

        return null;
    }

    public static void setObjectByName(Object object, String name, Object value) throws Exception {
        Class clazz = object.getClass();

        int maxStep = 10;
        while (maxStep > 0) {
            if (clazz == null) {
                break;
            }

            try {
                Field field = clazz.getDeclaredField(name);
                field.setAccessible(true);
                field.set(object, value);
            } catch (NoSuchFieldException e) {
                clazz = clazz.getSuperclass();
            } catch (IllegalAccessException e) {
                break;
            }

            maxStep--;
        }
    }

    @SafeVarargs
    public static Object invokeMethodByName(Object object, String methodName, Pair<Class, Object>... parameterPairs) throws Exception {
        return invokeMethodByName(object, object.getClass(), methodName, parameterPairs);
    }

    @SafeVarargs
    public static Object invokeMethodByName(Object object, Class<?> clazz, String methodName, Pair<Class, Object>... parameterPairs) throws Exception {
        Class[] parameterTypes;
        Object[] parameterObject;

        if (parameterPairs != null) {
            parameterTypes = new Class[parameterPairs.length];
            parameterObject = new Object[parameterPairs.length];

            for (int i = 0; i < parameterPairs.length; i++) {
                parameterTypes[i] = parameterPairs[i].first;
                parameterObject[i] = parameterPairs[i].second;
            }
        } else {
            parameterTypes = null;
            parameterObject = null;
        }

        int maxStep = 10;
        while (maxStep > 0) {
            if (clazz == null) {
                break;
            }

            try {
                Method method = clazz.getDeclaredMethod(methodName, parameterTypes);
                method.setAccessible(true);
                return method.invoke(object, parameterObject);
            } catch (NoSuchMethodException e) {
                clazz = clazz.getSuperclass();
            } catch (IllegalAccessException e) {
                break;
            } catch (InvocationTargetException e) {
                break;
            }

            maxStep--;
        }

        return null;
    }
}
