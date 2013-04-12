package com.dd.test.catpaw.platform.config;

import java.util.ArrayList;
import java.util.HashMap;

import org.testng.ITestNGListener;

import com.dd.test.catpaw.platform.asserts.SoftAssertsListener;
import com.dd.test.catpaw.platform.grid.SeleniumGridListener;
import com.dd.test.catpaw.reports.reporter.html.HtmlReporterListener;
import com.dd.test.catpaw.reports.reporter.runtimereport.RuntimeReporterListener;

/**
 * TestNG 6.1 provides a capability wherein you can hook in Listeners via
 * ServiceLoading. In order for you to leverage this capability, and also be
 * able to dynamically enable/disable it from within your project , you would
 * need to first register the listener with {@link ServiceLoaderManager}. <br>
 * <ul>
 * <li>Use {@link ServiceLoaderManager#registerListener(ListenerInfo)} to
 * register your listener and then use
 * <li>{@link #executeCurrentMethod(ITestNGListener)} - to find if a current
 * method within your listener should be invoked or skipped.
 * </ul>
 * 
 * For more details about ServiceLoader capabilities in TestNG refer <a
 * href=https
 * ://groups.google.com/forum/?fromgroups#!topic/testng-users/ZVloM26gEoI
 * >here</a>
 */
public class ServiceLoaderManager {

    public final static String THREAD_EXCLUSION_MSG = "Encountered either a duplicate or an Extended instance for CatPaw Mandatory Listener. Skipping execution...";

    /**
     * An enum internally used to identify the type of the listener.
     * 
     */
    enum ListenerType {
        HTMLREPORTER(HtmlReporterListener.class.getName()), GRIDLISTENER(SeleniumGridListener.class.getName()), SOFTASSERTSLISTENER(
                SoftAssertsListener.class.getName()), RUNTIMEREPORTER(RuntimeReporterListener.class.getName());

        private ListenerType(String name) {
            this.name = name;
        }

        private String name;

        public String toString() {
            return name;
        }

        public String getName() {
            return name;
        }

        public ArrayList<String> getElementsAsList() {
            ArrayList<String> clawsListeners = new ArrayList<String>();
            for (ListenerType type : ListenerType.values()) {
                clawsListeners.add(type.toString());
            }
            return clawsListeners;
        }
    }

    /**
     * An enum class, that represents the list of VM Arguments that can be used
     * to do the following:
     * <ul>
     * <li>Enable/Disable Service Loader
     * <li>Enable/Disable {@link SeleniumGridListener} listener.
     * <li>Enable/Disable {@link HtmlReporterListener} listener.
     * <li>Enable/Disable {@link SoftAssertsListener} listener.
     * <li>Enable/Disable {@link RuntimeReporterListener} listener.
     * </ul>
     * 
     * <pre>
     * Note: For CatPaw projects it is not advisable to disable any of the Listeners. Doing so can have adverse effects
     * on the automation code.
     * </pre>
     */
    public enum VMArgs {
        /**
         * <pre>
         * Pass -Denable.service.loader=false if Listener Invocation via Service Loaders is to be disabled.
         * </pre>
         * 
         * Default value is assumed to be <b>true</b>
         */
        ENABLE_SERVICE_LOADER("enable.service.loader"),
        /**
         * <pre>
         * Pass -Denable.grid.listener=false if the Listener {@link SeleniumGridListener} is to be disabled.
         * </pre>
         * 
         * Default value is assumed to be <b>true</b>
         */
        ENABLE_GRID_LISTENER("enable.grid.listener"),
        /**
         * <pre>
         * Pass -Denable.html.reporter.listener=false if the Listener {@link HtmlReporterListener} is to be disabled.
         * </pre>
         * 
         * Default value is assumed to be <b>true</b>
         */
        ENABLE_HTML_REPORTER_LISTENER("enable.html.reporter.listener"),
        /**
         * <pre>
         * Pass -Denable.runtime.reporter.listener=false if the Listener {@link RuntimeReporterListener} is to be disabled.
         * </pre>
         * 
         * Default value is assumed to be <b>true</b>
         */
        ENABLE_RUNTIME_REPORTER_LISTENER("enable.runtime.reporter.listener"),
        /**
         * <pre>
         * Pass -Denable.soft.asserts.listener=false if the Listener {@link SoftAssertsListener} is to be disabled.
         * </pre>
         * 
         * Default value is assumed to be <b>true</b>
         */
        ENABLE_SOFT_ASSERTS_LISTENER("enable.soft.asserts.listener");

        private String vmArgValue = null;

        private VMArgs(String vmArgValue) {
            this.vmArgValue = vmArgValue;
        }

        /**
         * @return - A String that represents the actual VM argument.
         */
        public String getValue() {
            return this.vmArgValue;
        }
    }

    private static HashMap<String, ListenerInfo> listenerMap = new HashMap<String, ListenerInfo>();

    private static volatile boolean serviceLoaderEnabled = true;

//    final static SimpleLogger logger = CatPawLogger.getLogger();

    /**
     * Register your Listener using this method.
     * 
     * @param information
     *            - A {@link ListenerInfo} object that contains information
     *            pertaining to your listener.
     */
    public static void registerListener(ListenerInfo information) {
        listenerMap.put(information.getListenerClassName(), information);
    }

    /**
     * This method decides if a TestNG interface implementation should be
     * executed or skipped. The decision is made after checking if the current
     * Listener enabled/disabled as determined by
     * {@link ListenerInfo#isEnabled()}.
     * 
     * @param listener
     *            - A {@link ITestNGListener} object which represents your
     *            listener.
     * 
     * @return - A flag which states if the current method is to be skipped or
     *         executed.
     */
    public static boolean executeCurrentMethod(ITestNGListener listener) {
        String className = listener.getClass().getName();
////        // logger.entering("Listener Class : " + className);
        
        if (serviceLoaderEnabled == false) {
    ////        // logger.exiting("All CatPaw Listeners will be disabled");
            return false;
            
        }



        boolean runCurrentMethod = false;
        if ((identifyListenerType(className) == null) || (listenerMap.get(className) == null)) {
            // We would need to skip filtering for
            // a. Classes that extend CatPaw Listeners.
            // b. Listeners residing outside CatPaw framework and not
            // registered with us.
            runCurrentMethod = true;
        } else {
            runCurrentMethod = listenerMap.get(className).isEnabled();
        }

        // below lines of code are only intended to pretty print the exiting
        // message.
        StringBuffer message = new StringBuffer();
        String instanceName = listener.toString();
        String methodName = listener.getClass().getName() + "." + findInvokedMethodFromStack();
        if (instanceName.indexOf("@") >= 0) {
            instanceName = instanceName.split("@")[1];
        }
        message.append("Execute ").append(methodName).append(" on instance :").append(instanceName).append(" ? ")
                .append(runCurrentMethod);
////        // logger.exiting(message.toString());
        return runCurrentMethod;
    }

    private static String findInvokedMethodFromStack() {
        ListenerType listeners = ListenerType.GRIDLISTENER;
        ArrayList<String> clawsListeners = listeners.getElementsAsList();
        StackTraceElement[] allElements = Thread.currentThread().getStackTrace();
        StackTraceElement element = null;
        for (int i = 1; i < allElements.length; i++) {
            StackTraceElement temp = allElements[i];
            String tempClassName = temp.getClassName();
            String tempBaseClassName = temp.getClass().getSuperclass().getName();

            if (clawsListeners.contains(tempClassName)) {
                element = temp;
                break;
            }
            if (clawsListeners.contains(tempBaseClassName)) {
                element = allElements[i - 1];
                break;
            }
        }
        return element.getMethodName() + "()";
    }

    /**
     * An internal helper method that identifies if a given class name is a
     * CatPaw mandatory listener.
     * 
     * @param className
     *            - Fully qualified class Name.
     * @return - An object of type {@link ListenerType} if it is a CatPaw
     *         Mandatory Listener. If the class name that was passed in, is not
     *         a CatPaw Mandatory listener, <code>null</code> value is
     *         returned.
     */
    private static ListenerType identifyListenerType(String className) {
        for (ListenerType eachType : ListenerType.values()) {
            if (eachType.getName().equals(className)) {
                return eachType;
            }
        }
        return null;
    }

    static {
        init();
    }

    /**
     * A internal method which initializes the configuration based on the VM
     * Arguments. For a list of VM arguments that can be used please refer to
     * {@link VMArgs}
     */
    private static void init() {
        serviceLoaderEnabled = getBooleanValFromVMArg(VMArgs.ENABLE_SERVICE_LOADER.getValue());
        if (serviceLoaderEnabled) {
            // Lets register all of our CatPaw Listeners to the
            // ServiceLoaderManager.
            // Any new Listeners which are deemed mandatory listeners within
            // CatPaw should be registered here.
            boolean state = getBooleanValFromVMArg(VMArgs.ENABLE_GRID_LISTENER.getValue());
            registerListener(new ListenerInfo(ListenerType.GRIDLISTENER.getName(), state));
            state = getBooleanValFromVMArg(VMArgs.ENABLE_HTML_REPORTER_LISTENER.getValue());
            registerListener(new ListenerInfo(ListenerType.HTMLREPORTER.getName(), state));
            state = getBooleanValFromVMArg(VMArgs.ENABLE_SOFT_ASSERTS_LISTENER.getValue());
            registerListener(new ListenerInfo(ListenerType.SOFTASSERTSLISTENER.getName(), state));
            state = getBooleanValFromVMArg(VMArgs.ENABLE_RUNTIME_REPORTER_LISTENER.getValue());
            registerListener(new ListenerInfo(ListenerType.RUNTIMEREPORTER.getName(), state));
        }
    }

    /**
     * A helper method which internally parses the VM arguments and converts
     * them into boolean flags.
     * 
     * @param vmArgValue
     *            - The actual VM argument as a String.
     * @return - A boolean version of the VM Argument
     */
    private static boolean getBooleanValFromVMArg(String vmArgValue) {
        boolean flag = true;
        String sysProperty = System.getProperty(vmArgValue);
        if ((sysProperty != null) && (!sysProperty.isEmpty())) {
            flag = Boolean.parseBoolean(sysProperty);
        }
        return flag;
    }

    private ServiceLoaderManager() {
    }

}