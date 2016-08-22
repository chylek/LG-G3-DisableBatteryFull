package eu.chylek.adam.g3disablebatteryfull;

import android.content.Intent;

import static de.robv.android.xposed.XposedHelpers.findAndHookMethod;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XC_MethodReplacement;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.callbacks.XC_LoadPackage.LoadPackageParam;
import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

/**
 * Created by chylek on 19.8.16.
 */
public class XposedHooks implements IXposedHookLoadPackage {

    class MyHook extends XC_MethodHook {
        @Override
        protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
            Intent i = (Intent) param.args[0];
            if (i.getIntExtra("level", 0) == 100) {
                i.putExtra("level", 99);
                XposedBridge.log("Battery Full - Extra replaced");
            }
        }
    }

    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam lpparam) throws Throwable {
        if (!lpparam.packageName.equals("com.android.systemui"))
            return;

        findAndHookMethod("com.lge.power.LGPowerUI", lpparam.classLoader, "doActionBatteryChanged", Intent.class, new MyHook());
    }
}
