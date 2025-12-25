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
        return TTLockClient.getDefault().isBLEEnabled(context);
    }

    /** 2. Request to turn on Bluetooth */
    public void requestBleEnable(Activity activity) {
        TTLockClient.getDefault().requestBleEnable(activity);
    }

    /** 3. Start Bluetooth Service */
    public void startBleService(Context context) {
        // CHANGED: match demo exactly
        TTLockClient.getDefault().prepareBTService(context.getApplicationContext());
    }

    /** 4. Stop Bluetooth Service */
    public void stopBleService() {
        TTLockClient.getDefault().stopBTService();
    }

    /** 5. Start Bluetooth Scan */
    @RequiresPermission("android.permission.BLUETOOTH")
    public void startBTDeviceScan(ScanLockCallback callback) {
        // CHANGED: demo directly calls startScanLock
        TTLockClient.getDefault().startScanLock(callback);
    }

    /** 6. Stop Bluetooth Scan */
    public void stopBTDeviceScan() {
        TTLockClient.getDefault().stopScanLock();
    }

    /** 7. Connect Device by lockData */
    public void connectLock(String lockData, ConnectLockCallback callback) {
        TTLockClient.getDefault().connectLock(lockData, callback);
    }

    /** 8. Lock Initialize */
    public void lockInitialize(ExtendedBluetoothDevice device, InitLockCallback callback) {
        TTLockClient.getDefault().initLock(device, callback);
    }

    /** 9. Reset Lock */
    public void resetLock(String lockData, String lockMac, ResetLockCallback callback) {
        TTLockClient.getDefault().resetLock(lockData, lockMac, callback);
    }

    /** 10. Reset EKey */
    public void resetEKey(String lockData, String lockMac, ResetKeyCallback callback) {
        TTLockClient.getDefault().resetEkey(lockData, lockMac, callback);
    }

    /** 11. Set UID for operations */
    public void setUid(int uid) {
        TTLockClient.getDefault().setUid(uid);
    }

    /** 14. Disconnect */
    public void disconnect() {
        TTLockClient.getDefault().disconnect();
    }
}
