package plugin.ttlock;

import com.ansca.corona.CoronaRuntime;
import com.ansca.corona.CoronaRuntimeListener;
import com.ansca.corona.CoronaRuntimeTask;
import com.ansca.corona.CoronaRuntimeTaskDispatcher;
import com.naef.jnlua.JavaFunction;
import com.naef.jnlua.LuaState;

public class LuaTTLock implements CoronaRuntimeListener {

    // Cached runtime (set by Corona)
    private static CoronaRuntime runtime;
    private static CoronaRuntimeTaskDispatcher dispatcher;

    // Called by Corona when runtime starts
    @Override
    public void onLoaded(CoronaRuntime coronaRuntime) {
        runtime = coronaRuntime;
        dispatcher = new CoronaRuntimeTaskDispatcher(runtime);
        System.out.println("LuaTTLock: runtime loaded");
    }

    @Override
    public void onStarted(CoronaRuntime runtime) {}

    @Override
    public void onSuspended(CoronaRuntime runtime) {}

    @Override
    public void onResumed(CoronaRuntime runtime) {}

    @Override
    public void onExiting(CoronaRuntime runtime) {
        LuaTTLock.runtime = null;
        dispatcher = null;
    }

    // ---- Lua function ----
    public static class PingFunction implements JavaFunction {
        @Override
        public int invoke(LuaState L) {
            if (dispatcher == null) {
                System.out.println("LuaTTLock: runtime not ready");
                return 0;
            }

            dispatcher.send(new CoronaRuntimeTask() {
                @Override
                public void executeUsing(CoronaRuntime runtime) {
                    System.out.println("LuaTTLock: ping() called from Lua");
                }
            });

            return 0;
        }
    }
}
