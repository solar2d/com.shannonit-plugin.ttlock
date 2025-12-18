package plugin.ttlock;

import com.ansca.corona.CoronaRuntime;
import com.ansca.corona.CoronaRuntimeTask;
import com.ansca.corona.CoronaRuntimeTaskDispatcher;

import com.ansca.corona.CoronaActivity;
import com.ansca.corona.CoronaEnvironment;
import com.ansca.corona.CoronaLua;
import com.ansca.corona.CoronaRuntime;
import com.ansca.corona.CoronaRuntimeListener;
import com.ansca.corona.CoronaRuntimeTask;
import com.ansca.corona.permissions.PermissionState;
import com.ansca.corona.permissions.PermissionsServices;
import com.naef.jnlua.JavaFunction;
import com.naef.jnlua.LuaState;
import com.naef.jnlua.NamedJavaFunction;

public class LuaTTLock {

    private final CoronaRuntimeTaskDispatcher dispatcher;

    public LuaTTLock(CoronaRuntimeTaskDispatcher dispatcher) {
        this.dispatcher = dispatcher;

        // Plugin initialization log
        dispatcher.send(new CoronaRuntimeTask() {
            @Override
            public void executeUsing(CoronaRuntime runtime) {
                System.out.println("LuaTTLock: Plugin successfully initialized (BLE-free stub)");
            }
        });
    }

    // Example test method callable from Lua
    public void ping() {
        dispatcher.send(new CoronaRuntimeTask() {
            @Override
            public void executeUsing(CoronaRuntime runtime) {
                System.out.println("LuaTTLock: ping() called from Lua");
            }
        });
    }
}
