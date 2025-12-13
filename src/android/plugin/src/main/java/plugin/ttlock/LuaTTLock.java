package plugin.ttlock;

import com.ansca.corona.CoronaRuntime;
import com.ansca.corona.CoronaRuntimeTask;
import com.ansca.corona.CoronaRuntimeTaskDispatcher;

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
