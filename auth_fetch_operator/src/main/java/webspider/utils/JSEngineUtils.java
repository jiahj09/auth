package webspider.utils;

import org.apache.commons.io.IOUtils;
import org.springframework.core.io.ClassPathResource;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

public class JSEngineUtils {

    public static String encryption(String jsPath,String methodName, String... strings) {
        String file;
        String enPassword = "";
        try {
            ClassPathResource classPathResource = new ClassPathResource(jsPath);
            file = IOUtils.toString(classPathResource.getInputStream(), "UTF-8");
            ScriptEngineManager engineManager = new ScriptEngineManager();
            ScriptEngine engine = engineManager.getEngineByName("JavaScript");
            engine.eval(file);
            Invocable invocable = (Invocable) engine;
            Object enPasswordObj = invocable.invokeFunction(methodName, strings);
            enPassword = enPasswordObj.toString();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return enPassword;
    }
}
