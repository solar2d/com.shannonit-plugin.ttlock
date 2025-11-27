package plugin.ttlock;

import com.naef.jnlua.*;
import com.shannonit.ttlock.MyTTLock;

public class LuaLoader implements JavaFunction {

    @Override
    public int invoke(LuaState L) {
        L.newTable();

        L.pushJavaFunction(new JavaFunction() {
            @Override
            public int invoke(LuaState L) {
                L.pushString(MyTTLock.helloWorld());
                return 1;
            }
        });
        L.setField(-2, "helloWorld");

        L.pushJavaFunction(new JavaFunction() {
            @Override
            public int invoke(LuaState L) {
                String[] methods = MyTTLock.listMethods();
                L.newTable();
                for (int i = 0; i < methods.length; i++) {
                    L.pushString(methods[i]);
                    L.rawSeti(-2, i + 1);
                }
                return 1;
            }
        });
        L.setField(-2, "listMethods");

        return 1;
    }
}
