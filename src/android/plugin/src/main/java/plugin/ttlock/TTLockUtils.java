package plugin.ttlock;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import com.ttlock.bl.sdk.api.TTLockClient;
import com.ttlock.bl.sdk.callback.ScanLockCallback;
import com.ttlock.bl.sdk.callback.InitLockCallback;
import com.ttlock.bl.sdk.callback.ResetKeyCallback;
import com.ttlock.bl.sdk.callback.ResetLockCallback;
import com.ttlock.bl.sdk.api.ExtendedBluetoothDevice;

public class TTLockUtils {

    public static TTLockUtils instance;

    public TTLockUtils() { }

    public static TTLockUtils getInstance() {
        if (instance == null) {
            instance = new TTLockUtils();
        }
        return instance;
    }

    /** 1. Check if BLE is enabled */
    public boolean isBLEEnabled(Context context) {
        BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
        return adapter != null && adapter.isEnabled();
    }

    /** 2. Request user to turn on Bluetooth */
    public void requestBleEnable(Activity activity) {
        BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
        if (adapter != null && !adapter.isEnabled()) {
            adapter.enable(); // Note: this may require permissions; for a full UX, use Intent to ask user
        }
    }

    /** 3. Init the Bluetooth configuration / TTLock service */
    public void prepareBTService(Context context) {
        TTLockClient.getDefault().prepareBTService(context);
    }

    /** 4. Stop the service to release Bluetooth resource */
    public void stopBTService() {
        TTLockClient.getDefault().stopBTService();
    }

    /** 5. Start scan for Bluetooth locks */
    public void startScanLock(ScanLockCallback callback) {
        TTLockClient.getDefault().startScanLock(callback);
    }

    /** 6. Stop scanning */
    public void stopScanLock() {
        TTLockClient.getDefault().stopScanLock();
    }

    /** 7. Init the lock */
    public void initLock(ExtendedBluetoothDevice device, InitLockCallback callback) {
        TTLockClient.getDefault().initLock(device, callback);
    }

    /** 8. Reset the eKey (lockFlagPos will change) */
    public void resetEkey(String lockData, String lockMac, ResetKeyCallback callback) {
        //public void resetEkey(String lockData,String lockMac, ResetKeyCallback callback)
        //TTLockClient.resetEKey(lockData, lockMac, callback);
    }

    /** 9. Reset the lock to factory mode */
    public void resetLock(String lockData, String lockMac, ResetLockCallback callback) {
        TTLockClient.getDefault().resetLock(lockData, lockMac, callback);
    }
}
