package plugin.ttlock;

import android.app.Activity;
import com.ansca.corona.*;
import com.naef.jnlua.*;
import com.ttlock.bl.sdk.api.TTLockClient;
import com.ttlock.bl.sdk.api.ExtendedBluetoothDevice;
import com.ttlock.bl.sdk.callback.*;
import com.ttlock.bl.sdk.entity.LockError;

@SuppressWarnings({"WeakerAccess", "unused"})
public class LuaLoader implements JavaFunction, CoronaRuntimeListener {

    private TTLockUtils ttlockUtils;
    private int listener;

    public LuaLoader() {
        ttlockUtils = new TTLockUtils();
        CoronaEnvironment.addRuntimeListener(this);
    }

    @Override
    public int invoke(LuaState L) {
        NamedJavaFunction[] luaFunctions = new NamedJavaFunction[]{
                new StartScanLock(),
                new StopScanLock(),
                new InitLock(),
                new ResetEkey(),
                new ResetLock()
        };
        String libName = L.toString(1);
        L.register(libName, luaFunctions);
        return 1;
    }

    @Override public void onLoaded(CoronaRuntime runtime) { }
    @Override public void onStarted(CoronaRuntime runtime) { }
    @Override public void onSuspended(CoronaRuntime runtime) { }
    @Override public void onResumed(CoronaRuntime runtime) { }
    @Override public void onExiting(CoronaRuntime runtime) { }

    private class StartScanLock implements NamedJavaFunction {
        @Override public String getName() { return "startScanLock"; }

        @Override
        public int invoke(final LuaState L) {
            listener = CoronaLua.newRef(L, 1);

            ttlockUtils.startScanLock(new ScanLockCallback() {
                @Override
                public void onScanLockSuccess(ExtendedBluetoothDevice device) {
                    CoronaEnvironment.getCoronaActivity().getRuntimeTaskDispatcher().send(new CoronaRuntimeTask() {
                        @Override
                        public void executeUsing(CoronaRuntime runtime) {
                            LuaState L = runtime.getLuaState();
                            CoronaLua.newEvent(L, "ttlock");
                            L.pushString(device.getAddress());
                            L.setField(-2, "mac");
                            L.pushString(device.getName());
                            L.setField(-2, "name");
                            // TODO: CoronaLua.dispatchEvent(L, listener, 0);
                        }
                    });
                }

                @Override
                public void onFail(LockError error) {
                    CoronaEnvironment.getCoronaActivity().getRuntimeTaskDispatcher().send(new CoronaRuntimeTask() {
                        @Override
                        public void executeUsing(CoronaRuntime runtime) {
                            LuaState L = runtime.getLuaState();
                            CoronaLua.newEvent(L, "ttlock");
                            L.pushString(error.getDescription());
                            L.setField(-2, "error");
                            // TODO: CoronaLua.dispatchEvent(L, listener, 0);
                        }
                    });
                }
            });
            return 0;
        }
    }

    private class StopScanLock implements NamedJavaFunction {
        @Override public String getName() { return "stopScanLock"; }
        @Override public int invoke(LuaState L) {
            ttlockUtils.stopScanLock();
            return 0;
        }
    }

    private class InitLock implements NamedJavaFunction {
        @Override public String getName() { return "initLock"; }

        @Override
        public int invoke(final LuaState L) {
            String mac = L.checkString(1);
            // TODO: ExtendedBluetoothDevice device = ttlockUtils.getDeviceByMac(mac);
            ExtendedBluetoothDevice device;
            listener = CoronaLua.newRef(L, 2);
            // TODO: 
            // ttlockUtils.initLock(device, new InitLockCallback() {
            //     @Override
            //     public void onInitLockSuccess(String lockData) {
            //         CoronaEnvironment.getCoronaActivity().getRuntimeTaskDispatcher().send(new CoronaRuntimeTask() {
            //             @Override
            //             public void executeUsing(CoronaRuntime runtime) {
            //                 LuaState L = runtime.getLuaState();
            //                 CoronaLua.newEvent(L, "ttlock");
            //                 L.pushString("success");
            //                 L.setField(-2, "status");
            //                 // TODO: CoronaLua.dispatchEvent(L, listener, 0);
            //             }
            //         });
            //     }

            //     @Override
            //     public void onFail(LockError error) {
            //         CoronaEnvironment.getCoronaActivity().getRuntimeTaskDispatcher().send(new CoronaRuntimeTask() {
            //             @Override
            //             public void executeUsing(CoronaRuntime runtime) {
            //                 LuaState L = runtime.getLuaState();
            //                 CoronaLua.newEvent(L, "ttlock");
            //                 L.pushString(error.getDescription());
            //                 L.setField(-2, "error");
            //                 // TODO: CoronaLua.dispatchEvent(L, listener, 0);
            //             }
            //         });
            //     }
            // });

            return 0;
        }
    }

    private class ResetEkey implements NamedJavaFunction {
        @Override public String getName() { return "resetEkey"; }

        @Override
        public int invoke(final LuaState L) {
            String lockData = L.checkString(1);
            String lockMac = L.checkString(2);
            listener = CoronaLua.newRef(L, 3);

            ttlockUtils.resetEkey(lockData, lockMac, new ResetKeyCallback() {
                @Override
                public void onResetKeySuccess(String lockData) {
                    CoronaEnvironment.getCoronaActivity().getRuntimeTaskDispatcher().send(new CoronaRuntimeTask() {
                        @Override
                        public void executeUsing(CoronaRuntime runtime) {
                            LuaState L = runtime.getLuaState();
                            CoronaLua.newEvent(L, "ttlock");
                            L.pushString("success");
                            L.setField(-2, "status");
                            // TODO: CoronaLua.dispatchEvent(L, listener, 0);
                        }
                    });
                }

                @Override
                public void onFail(LockError error) {
                    CoronaEnvironment.getCoronaActivity().getRuntimeTaskDispatcher().send(new CoronaRuntimeTask() {
                        @Override
                        public void executeUsing(CoronaRuntime runtime) {
                            LuaState L = runtime.getLuaState();
                            CoronaLua.newEvent(L, "ttlock");
                            L.pushString(error.getDescription());
                            L.setField(-2, "error");
                            // TODO: CoronaLua.dispatchEvent(L, listener, 0);
                        }
                    });
                }
            });

            return 0;
        }
    }

    private class ResetLock implements NamedJavaFunction {
        @Override public String getName() { return "resetLock"; }

        @Override
        public int invoke(final LuaState L) {
            String lockData = L.checkString(1);
            String lockMac = L.checkString(2);
            listener = CoronaLua.newRef(L, 3);
            // TODO: 
            // ttlockUtils.resetLock(lockData, lockMac, new ResetLockCallback() {
            //     //TODO: @Override
            //     // public void onResetLockSuccess(String lockData) {
            //     //     CoronaEnvironment.getCoronaActivity().getRuntimeTaskDispatcher().send(new CoronaRuntimeTask() {
            //     //         @Override
            //     //         public void executeUsing(CoronaRuntime runtime) {
            //     //             LuaState L = runtime.getLuaState();
            //     //             CoronaLua.newEvent(L, "ttlock");
            //     //             L.pushString("success");
            //     //             L.setField(-2, "status");
            //     //             CoronaLua.dispatchEvent(L, listener, 0);
            //     //         }
            //     //     });
            //     // }

            //     @Override
            //     public void onFail(LockError error) {
            //         CoronaEnvironment.getCoronaActivity().getRuntimeTaskDispatcher().send(new CoronaRuntimeTask() {
            //             @Override
            //             public void executeUsing(CoronaRuntime runtime) {
            //                 LuaState L = runtime.getLuaState();
            //                 CoronaLua.newEvent(L, "ttlock");
            //                 L.pushString(error.getDescription());
            //                 L.setField(-2, "error");
            //                 CoronaLua.dispatchEvent(L, listener, 0);
            //             }
            //         });
            //     }
            // });

            return 0;
        }
    }
}
