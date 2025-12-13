package plugin.ttlock;

import com.ansca.corona.CoronaRuntime;
import com.ansca.corona.CoronaRuntimeTask;
import com.ansca.corona.CoronaRuntimeTaskDispatcher;

public class LuaStub {

    private final CoronaRuntimeTaskDispatcher dispatcher;

    public LuaStub(CoronaRuntimeTaskDispatcher dispatcher) {
        this.dispatcher = dispatcher;

        dispatcher.send(new CoronaRuntimeTask() {
            @Override
            public void executeUsing(CoronaRuntime runtime) {
                System.out.println("LuaStub: Plugin successfully initialized");
            }
        });
    }

    public void ping() {
        dispatcher.send(new CoronaRuntimeTask() {
            @Override
            public void executeUsing(CoronaRuntime runtime) {
                System.out.println("LuaStub: ping() called from Lua");
            }
        });
    }
}
