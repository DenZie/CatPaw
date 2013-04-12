package com.dd.test.catpaw.platform.config;

/**
 * A utility class that stores some information pertaining to a Listener.
 * This object is referred used by {@link ServiceLoaderManager#registerListener(ListenerInfo)}.
 *
 */
public class ListenerInfo {
	
	private String listenerClassName;
	private boolean enabled = true;
	
	public ListenerInfo(){
		
	}
	/**
	 * 
	 * @param className - Fully qualified class name.
	 * @param globallyEnabled - Is the listener enabled or disabled.
	 */
	public ListenerInfo(String className, boolean globallyEnabled){
		this.listenerClassName = className;
		this.enabled = globallyEnabled;
	}
	public String getListenerClassName() {
		return listenerClassName;
	}
	public boolean isEnabled() {
		return enabled;
	}
	
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}
	
	public void setListenerClassName(String listenerClassName) {
		this.listenerClassName = listenerClassName;
	}

}
