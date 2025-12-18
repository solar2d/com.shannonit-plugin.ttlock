package plugin.ttlock;

import com.ansca.corona.CoronaEnvironment;
import com.ansca.corona.CoronaRuntimeListener;
import com.naef.jnlua.LuaState;

public class LuaLoader {

    // REQUIRED entry point
    public static int luaopen_plugin_ttlock(LuaState L) {

        // Register runtime listener ONCE
        CoronaEnvironment.addRuntimeListener(new LuaTTLock());

        // Create Lua table
        L.newTable();

        // Expose functions
        L.pushJavaFunction(new LuaTTLock.PingFunction());
        L.setField(-2, "ping");

        return 1;
    }
}
