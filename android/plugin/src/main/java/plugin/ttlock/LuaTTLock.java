package plugin.ttlock;

import android.content.Context;

import com.ansca.corona.CoronaRuntime;
import com.ansca.corona.CoronaRuntimeTask;
import com.ansca.corona.CoronaRuntimeTaskDispatcher;

import com.ttlock.bl.sdk.api.ExtendedBluetoothDevice;
import com.ttlock.bl.sdk.api.TTLockClient;
import com.ttlock.bl.sdk.callback.ScanLockCallback;
import com.ttlock.bl.sdk.callback.InitLockCallback;
import com.ttlock.bl.sdk.callback.ControlLockCallback;
import com.ttlock.bl.sdk.constant.ControlAction;
import com.ttlock.bl.sdk.entity.ControlLockResult;
import com.ttlock.bl.sdk.entity.LockError;

public class LuaTTLock {

    private final Context context;
    private final TTLockClient ttlock;
    private final CoronaRuntimeTaskDispatcher dispatcher;

    public LuaTTLock(Context context, CoronaRuntimeTaskDispatcher dispatcher) {
        this.context = context;
        this.dispatcher = dispatcher;
        ttlock = TTLockClient.getDefault();
        ttlock.prepareBTService(context);
    }

    public void startScan() {
        ttlock.startScanLock(new ScanLockCallback() {
            @Override
            public void onScanLockSuccess(ExtendedBluetoothDevice device) {
                final String name = device.getName();
                final String mac = device.getAddress();

                dispatcher.send(new CoronaRuntimeTask() {
                    @Override
                    public void executeUsing(CoronaRuntime runtime) {
                        System.out.println("TTLock Scan: " + name + " / " + mac);
                    }
                });
            }

            @Override
            public void onFail(LockError error) {
                dispatcher.send(new CoronaRuntimeTask() {
                    @Override
                    public void executeUsing(CoronaRuntime runtime) {
                        System.err.println("TTLock Scan failed: " + error.getErrorMsg());
                    }
                });
            }
        });
    }

    public void stopScan() {
        ttlock.stopScanLock();
    }

    public void initLock(ExtendedBluetoothDevice device) {
        ttlock.initLock(device, new InitLockCallback() {
            @Override
            public void onInitLockSuccess(String lockData) {
                dispatcher.send(new CoronaRuntimeTask() {
                    @Override
                    public void executeUsing(CoronaRuntime runtime) {
                        System.out.println("TTLock init success: " + lockData);
                    }
                });
            }

            @Override
            public void onFail(LockError error) {
                dispatcher.send(new CoronaRuntimeTask() {
                    @Override
                    public void executeUsing(CoronaRuntime runtime) {
                        System.err.println("TTLock init failed: " + error.getErrorMsg());
                    }
                });
            }
        });
    }

    public void unlock(String lockData, String lockMac) {
        ttlock.controlLock(ControlAction.UNLOCK, lockData, lockMac, new ControlLockCallback() {
            @Override
            public void onControlLockSuccess(ControlLockResult result) {
                dispatcher.send(new CoronaRuntimeTask() {
                    @Override
                    public void executeUsing(CoronaRuntime runtime) {
                        System.out.println("TTLock unlocked. Battery: " + result.getBattery());
                    }
                });
            }

            @Override
            public void onFail(LockError error) {
                dispatcher.send(new CoronaRuntimeTask() {
                    @Override
                    public void executeUsing(CoronaRuntime runtime) {
                        System.err.println("TTLock unlock failed: " + error.getErrorMsg());
                    }
                });
            }
        });
    }

    public void release() {
        ttlock.stopBTService();
    }
}
