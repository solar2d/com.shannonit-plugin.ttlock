package plugin.ttlock;

import android.app.Activity;
import android.content.Context;

import androidx.annotation.RequiresPermission;

import com.ttlock.bl.sdk.api.TTLockClient;
import com.ttlock.bl.sdk.api.ExtendedBluetoothDevice;
import com.ttlock.bl.sdk.callback.ConnectLockCallback;
import com.ttlock.bl.sdk.callback.InitLockCallback;
import com.ttlock.bl.sdk.callback.ResetKeyCallback;
import com.ttlock.bl.sdk.callback.ResetLockCallback;
import com.ttlock.bl.sdk.callback.ScanLockCallback;

public class TTLockUtils {

    private static TTLockUtils instance;

    private TTLockUtils() { }

    public static TTLockUtils getInstance() {
        if (instance == null) {
            instance = new TTLockUtils();
        }
        return instance;
    }

    /** 1. Check if BLE is enabled */
    public boolean isBLEEnabled(Context context) {
        try {
            return TTLockClient.getDefault().isBLEEnabled(context);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /** 2. Request to turn on Bluetooth */
    public void requestBleEnable(Activity activity) {
        try {
            TTLockClient.getDefault().requestBleEnable(activity);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /** 3. Start Bluetooth Service */
    public void startBleService(Context context) {
        try {
            TTLockClient.getDefault().prepareBTService(context);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /** 4. Stop Bluetooth Service */
    public void stopBleService() {
        try {
            TTLockClient.getDefault().stopBTService();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /** 5. Start Bluetooth Scan */
    @RequiresPermission("android.permission.BLUETOOTH")
    public void startBTDeviceScan(ScanLockCallback callback) {
        try {
            TTLockClient.getDefault().startScanLock(callback);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /** 6. Stop Bluetooth Scan */
    public void stopBTDeviceScan() {
        try {
            TTLockClient.getDefault().stopScanLock();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /** 7. Connect Device by lockData */
    public void connectLock(String lockData, ConnectLockCallback callback) {
        try {
            TTLockClient.getDefault().connectLock(lockData, callback);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /** 8. Lock Initialize */
    public void lockInitialize(ExtendedBluetoothDevice device, InitLockCallback callback) {
        try {
            TTLockClient.getDefault().initLock(device, callback);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /** 9. Reset Lock */
    public void resetLock(String lockData, String lockMac, ResetLockCallback callback) {
        try {
            TTLockClient.getDefault().resetLock(lockData, lockMac, callback);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /** 10. Reset EKey */
    public void resetEKey(String lockData, String lockMac, ResetKeyCallback callback) {
        try {
            TTLockClient.getDefault().resetEkey(lockData, lockMac, callback);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /** 11. Set UID for operations */
    public void setUid(int uid) {
        try {
            TTLockClient.getDefault().setUid(uid);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /** 12. Unlock by user (callback optional for Lua) */
    public void unlockByUser(ExtendedBluetoothDevice device,
                             int uid,
                             String lockVersion,
                             long startDate,
                             long endDate,
                             String unlockKey,
                             int lockFlagPos,
                             String aesKeyStr,
                             long timezoneOffset) {
        try {
            // TTLockClient.getDefault().unlockByUser(device, uid, lockVersion, startDate, endDate,
            //         unlockKey, lockFlagPos, aesKeyStr, timezoneOffset, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /** 13. Unlock by administrator (callback optional for Lua) */
    public void unlockByAdministrator(ExtendedBluetoothDevice device,
                                      int uid,
                                      String lockVersion,
                                      String adminPs,
                                      String unlockKey,
                                      int lockFlagPos,
                                      long unlockDate,
                                      String aesKeyStr,
                                      long timezoneOffset) {
        try {
            // TTLockClient.getDefault().unlockByAdministrator(device, uid, lockVersion, adminPs, unlockKey,
            //         lockFlagPos, unlockDate, aesKeyStr, timezoneOffset, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /** 14. Disconnect */
    public void disconnect() {
        try {
            TTLockClient.getDefault().disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
