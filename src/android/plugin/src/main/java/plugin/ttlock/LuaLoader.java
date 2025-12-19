package plugin.ttlock;

import com.ansca.corona.*;
import com.naef.jnlua.*;

import com.ttlock.bl.sdk.api.ExtendedBluetoothDevice;
import com.ttlock.bl.sdk.callback.InitLockCallback;
import com.ttlock.bl.sdk.callback.ScanLockCallback;
import com.ttlock.bl.sdk.entity.LockError;

@SuppressWarnings({"unused", "WeakerAccess"})
public class LuaLoader implements JavaFunction, CoronaRuntimeListener {

    private static final String EVENT_NAME = "ttlock";
    private int fListener = CoronaLua.REFNIL;
    private TTLockUtils ttlockUtils;
    private ExtendedBluetoothDevice lastScannedDevice;

    public LuaLoader() {
        ttlockUtils = TTLockUtils.getInstance();
        CoronaEnvironment.addRuntimeListener(this);
    }

    @Override
    public int invoke(LuaState L) {
        NamedJavaFunction[] luaFunctions = new NamedJavaFunction[]{
                new InitWrapper(),  
                new IsBLEEnabledWrapper(),
                new RequestBleEnableWrapper(),
                new StartBleServiceWrapper(),
                new StopBleServiceWrapper(),
                new StartScanWrapper(),
                new StopScanWrapper(),
                new ConnectLockWrapper(),
                new LockInitWrapper(),
                new ResetLockWrapper(),
                new ResetEKeyWrapper(),
                new UnlockByUserWrapper()
        };
        L.register(L.toString(1), luaFunctions);
        return 1;
    }

    @Override public void onLoaded(CoronaRuntime runtime) {}
    @Override public void onStarted(CoronaRuntime runtime) {}
    @Override public void onSuspended(CoronaRuntime runtime) {}
    @Override public void onResumed(CoronaRuntime runtime) {}

    @Override
    public void onExiting(CoronaRuntime runtime) {
        CoronaLua.deleteRef(runtime.getLuaState(), fListener);
        fListener = CoronaLua.REFNIL;
    }

    private class InitWrapper implements NamedJavaFunction {
    @Override public String getName() { return "init"; }
        @Override public int invoke(LuaState L) {
            // Example: store the listener reference
            if (L.isFunction(1)) {
                fListener = CoronaLua.newRef(L, 1);
            }
            return 0;
        }
    }


    // ----------------------------
    // Lua Wrappers
    // ----------------------------

    private class IsBLEEnabledWrapper implements NamedJavaFunction {
        @Override public String getName() { return "isBLEEnabled"; }
        @Override public int invoke(LuaState L) {
            L.pushBoolean(ttlockUtils.isBLEEnabled(
                    CoronaEnvironment.getCoronaActivity()));
            return 1;
        }
    }

    private class RequestBleEnableWrapper implements NamedJavaFunction {
        @Override public String getName() { return "requestBleEnable"; }
        @Override public int invoke(LuaState L) {
            ttlockUtils.requestBleEnable(
                    CoronaEnvironment.getCoronaActivity());
            return 0;
        }
    }

    private class StartBleServiceWrapper implements NamedJavaFunction {
        @Override public String getName() { return "startBleService"; }
        @Override public int invoke(LuaState L) {
            ttlockUtils.startBleService(
                    CoronaEnvironment.getCoronaActivity());
            return 0;
        }
    }

    private class StopBleServiceWrapper implements NamedJavaFunction {
        @Override public String getName() { return "stopBleService"; }
        @Override public int invoke(LuaState L) {
            ttlockUtils.stopBleService();
            return 0;
        }
    }

    private class StartScanWrapper implements NamedJavaFunction {
        @Override public String getName() { return "startBTDeviceScan"; }
        @Override public int invoke(LuaState L) {

            ttlockUtils.startBTDeviceScan(new ScanLockCallback() {
                @Override
                public void onScanLockSuccess(ExtendedBluetoothDevice device) {
                    lastScannedDevice = device;  // Save the device
                    dispatchDeviceEvent(device, "found");
                }

                @Override
                public void onFail(LockError error) {
                    dispatchEvent(error.getErrorMsg());
                }
            });
            return 0;
        }
    }

    private class StopScanWrapper implements NamedJavaFunction {
        @Override public String getName() { return "stopBTDeviceScan"; }
        @Override public int invoke(LuaState L) {
            ttlockUtils.stopBTDeviceScan();
            return 0;
        }
    }

    private class ConnectLockWrapper implements NamedJavaFunction {
        @Override public String getName() { return "connectLock"; }
        @Override public int invoke(LuaState L) {
            String lockData = L.checkString(1);
            ttlockUtils.connectLock(lockData, null);
            return 0;
        }
    }

    private class LockInitWrapper implements NamedJavaFunction {
        @Override public String getName() { return "lockInitialize"; }

        @Override
        public int invoke(LuaState L) {

            if (lastScannedDevice == null) {
                dispatchEvent("no_device_scanned");
                return 0;
            }

            ttlockUtils.lockInitialize(
                    lastScannedDevice,
                    new InitLockCallback() {

                        @Override
                        public void onInitLockSuccess(String lockData) {
                            dispatchEvent("lock_initialized");
                        }

                        @Override
                        public void onFail(LockError error) {
                            dispatchEvent(error.getErrorMsg());
                        }
                    }
            );

            return 0;
        }
    }
    private class ResetLockWrapper implements NamedJavaFunction {
        @Override public String getName() { return "resetLock"; }
        @Override public int invoke(LuaState L) {
            ttlockUtils.resetLock(
                    L.checkString(1),
                    L.checkString(2),
                    null);
            return 0;
        }
    }

    private class ResetEKeyWrapper implements NamedJavaFunction {
        @Override public String getName() { return "resetEKey"; }
        @Override public int invoke(LuaState L) {
            ttlockUtils.resetEKey(
                    L.checkString(1),
                    L.checkString(2),
                    null);
            return 0;
        }
    }

    private class UnlockByUserWrapper implements NamedJavaFunction {
        @Override public String getName() { return "unlockByUser"; }
        @Override public int invoke(LuaState L) {
            // SDK v3+ unlock is handled internally (no callback)
            return 0;
        }
    }

    // ----------------------------
    // Event dispatch helpers
    // ----------------------------

    private void dispatchEvent(final String message) {
        if (fListener == CoronaLua.REFNIL) return;
        CoronaEnvironment.getCoronaActivity()
                .getRuntimeTaskDispatcher()
                .send(runtime -> {
                    LuaState L = runtime.getLuaState();
                    CoronaLua.newEvent(L, EVENT_NAME);
                    L.pushString(message);
                    L.setField(-2, "message");
                    try {
                        CoronaLua.dispatchEvent(L, fListener, 0);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                });
    }

    private void dispatchDeviceEvent(
            final ExtendedBluetoothDevice device,
            final String type) {

        if (fListener == CoronaLua.REFNIL) return;
        CoronaEnvironment.getCoronaActivity()
                .getRuntimeTaskDispatcher()
                .send(runtime -> {
                    LuaState L = runtime.getLuaState();
                    CoronaLua.newEvent(L, EVENT_NAME);
                    L.pushString(type);
                    L.setField(-2, "type");
                    L.pushString(device.getName());
                    L.setField(-2, "name");
                    L.pushString(device.getAddress());
                    L.setField(-2, "mac");
                    try {
                        CoronaLua.dispatchEvent(L, fListener, 0);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
    }
}
