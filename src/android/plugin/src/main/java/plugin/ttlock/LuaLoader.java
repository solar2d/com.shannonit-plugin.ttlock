package plugin.ttlock;

import android.app.Activity;
import com.ansca.corona.*;
import com.naef.jnlua.*;
import com.ttlock.bl.sdk.api.TTLockClient;
import com.ttlock.bl.sdk.api.ExtendedBluetoothDevice;
import com.ttlock.bl.sdk.callback.*;
import com.ttlock.bl.sdk.entity.LockError;

/**
 * Implements the Lua interface for the TTLock Corona plugin.
 */
@SuppressWarnings({"WeakerAccess", "unused"})
public class LuaLoader implements JavaFunction, CoronaRuntimeListener {
    /** Lua registry ID to the Lua function to be called for events. */
    private int fListener;

    /** This corresponds to the event name. */
    private static final String EVENT_NAME = "ttlock";

    TTLockUtils ttlockUtils;

    @SuppressWarnings("unused")
    public LuaLoader() {
        fListener = CoronaLua.REFNIL;
        ttlockUtils = new TTLockUtils();
        CoronaEnvironment.addRuntimeListener(this);
    }

    @Override
    public int invoke(LuaState L) {
        NamedJavaFunction[] luaFunctions = new NamedJavaFunction[]{
                new InitWrapper(),          // <--- This exposes ttlock.init()
                new StartScanLockWrapper(this),
                new StopScanLockWrapper(this),
                new InitLockWrapper(this),
                new ResetEkeyWrapper(this),
                new ResetLockWrapper(this)
        };
        String libName = L.toString(1);
        L.register(libName, luaFunctions);
        return 1;
    }

    @Override public void onLoaded(CoronaRuntime runtime) { }
    @Override public void onStarted(CoronaRuntime runtime) { }
    @Override public void onSuspended(CoronaRuntime runtime) { }
    @Override public void onResumed(CoronaRuntime runtime) { }
    @Override public void onExiting(CoronaRuntime runtime) {
        CoronaLua.deleteRef(runtime.getLuaState(), fListener);
        fListener = CoronaLua.REFNIL;
    }

    /** Init event to Lua */
    public class InitWrapper implements NamedJavaFunction {
        @Override
        public String getName() { return "init"; }

        @Override
        public int invoke(LuaState L) {
            int listenerIndex = 1;
            if (CoronaLua.isListener(L, listenerIndex, EVENT_NAME)) {
                fListener = CoronaLua.newRef(L, listenerIndex);
            }
            return 0;
        }
    }

    /** Dispatch event to Lua */
    public void dispatchEvent(final String message) {
        CoronaEnvironment.getCoronaActivity().getRuntimeTaskDispatcher().send(new CoronaRuntimeTask() {
            @Override
            public void executeUsing(CoronaRuntime runtime) {
                LuaState L = runtime.getLuaState();
                CoronaLua.newEvent(L, EVENT_NAME);
                L.pushString(message);
                L.setField(-2, "message");
                try {
                    CoronaLua.dispatchEvent(L, fListener, 0);
                } catch (Exception ignored) { }
            }
        });
    }

    /** Lua function: init listener */
    public int init(LuaState L) {
        int listenerIndex = 1;
        if (CoronaLua.isListener(L, listenerIndex, EVENT_NAME)) {
            fListener = CoronaLua.newRef(L, listenerIndex);
        }
        return 0;
    }

    // ---------------------------
    // Lua function wrappers
    // ---------------------------

    private static class StartScanLockWrapper implements NamedJavaFunction {
        private LuaLoader loader;
        private TTLockUtils ttlockUtils;

        public StartScanLockWrapper(LuaLoader loader) {
            this.loader = loader;
            this.ttlockUtils = loader.ttlockUtils;
        }

        @Override public String getName() { return "startScanLock"; }

        @Override
        public int invoke(final LuaState L) {
            final int listener = CoronaLua.newRef(L, 1);
            ttlockUtils.startScanLock(new ScanLockCallback() {
                @Override
                public void onScanLockSuccess(ExtendedBluetoothDevice device) {
                    loader.dispatchDeviceEvent(device, listener);
                }

                @Override
                public void onFail(LockError error) {
                    loader.dispatchErrorEvent(error, listener);
                }
            });
            return 0;
        }
    }

    private static class StopScanLockWrapper implements NamedJavaFunction {
        private LuaLoader loader;
        private TTLockUtils ttlockUtils;

        public StopScanLockWrapper(LuaLoader loader) {
            this.loader = loader;
            this.ttlockUtils = loader.ttlockUtils;
        }

        @Override public String getName() { return "stopScanLock"; }

        @Override
        public int invoke(LuaState L) {
            ttlockUtils.stopScanLock();
            return 0;
        }
    }

    private static class InitLockWrapper implements NamedJavaFunction {
        private LuaLoader loader;
        private TTLockUtils ttlockUtils;

        public InitLockWrapper(LuaLoader loader) {
            this.loader = loader;
            this.ttlockUtils = loader.ttlockUtils;
        }

        @Override public String getName() { return "initLock"; }

        @Override
        public int invoke(final LuaState L) {
            String mac = L.checkString(1);
            ExtendedBluetoothDevice device;
            final int listener = CoronaLua.newRef(L, 2);

            // TODO: ttlockUtils.initLock(device, callback)

            return 0;
        }
    }

    private static class ResetEkeyWrapper implements NamedJavaFunction {
        private LuaLoader loader;
        private TTLockUtils ttlockUtils;

        public ResetEkeyWrapper(LuaLoader loader) {
            this.loader = loader;
            this.ttlockUtils = loader.ttlockUtils;
        }

        @Override public String getName() { return "resetEkey"; }

        @Override
        public int invoke(final LuaState L) {
            String lockData = L.checkString(1);
            String lockMac = L.checkString(2);
            final int listener = CoronaLua.newRef(L, 3);

            // TODO: ttlockUtils.resetEkey(lockData, lockMac, callback)

            return 0;
        }
    }

    private static class ResetLockWrapper implements NamedJavaFunction {
        private LuaLoader loader;
        private TTLockUtils ttlockUtils;

        public ResetLockWrapper(LuaLoader loader) {
            this.loader = loader;
            this.ttlockUtils = loader.ttlockUtils;
        }

        @Override public String getName() { return "resetLock"; }

        @Override
        public int invoke(final LuaState L) {
            String lockData = L.checkString(1);
            String lockMac = L.checkString(2);
            final int listener = CoronaLua.newRef(L, 3);

            // TODO: ttlockUtils.resetLock(lockData, lockMac, callback)

            return 0;
        }
    }

    // ---------------------------
    // Helpers for events
    // ---------------------------

    private void dispatchDeviceEvent(final ExtendedBluetoothDevice device, final int listener) {
        CoronaEnvironment.getCoronaActivity().getRuntimeTaskDispatcher().send(new CoronaRuntimeTask() {
            @Override
            public void executeUsing(CoronaRuntime runtime) {
                LuaState L = runtime.getLuaState();
                CoronaLua.newEvent(L, EVENT_NAME);
                L.pushString(device.getAddress());
                L.setField(-2, "mac");
                L.pushString(device.getName());
                L.setField(-2, "name");
                // TODO: CoronaLua.dispatchEvent(L, listener, 0);
            }
        });
    }

    private void dispatchErrorEvent(final LockError error, final int listener) {
        CoronaEnvironment.getCoronaActivity().getRuntimeTaskDispatcher().send(new CoronaRuntimeTask() {
            @Override
            public void executeUsing(CoronaRuntime runtime) {
                LuaState L = runtime.getLuaState();
                CoronaLua.newEvent(L, EVENT_NAME);
                L.pushString(error.getDescription());
                L.setField(-2, "error");
                // TODO: CoronaLua.dispatchEvent(L, listener, 0);
            }
        });
    }
}
