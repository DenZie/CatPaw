package com.dd.test.catpaw.reports.model;




public class WebLog {
	private String type;
	private String msg;
	private String screen;
	private String src;
	private String location;
	private String href;
	private String cal;
	// private static SimpleLogger logger = CatPawLogger.getLogger();

	public WebLog() {
	}

	/**
	 * parse the string and build the object.
	 * 
	 * @param s
	 */
	public WebLog(String s) {
		// logger.entering(s);

		if (s == null) {
			// logger.exiting();
			return;
		}
		String[] parts = s.split("\\|\\|");
		for (int i = 0; i < parts.length; i++) {
			parse(parts[i]);
		}
		// logger.exiting();
	}

	private void parse(String part) {
		// logger.entering(part);
		if (part.startsWith("TYPE=")) {
			type = part.replace("TYPE=", "");
			if ("".equals(type)) {
				type = null;
			}
		} else if (part.startsWith("MSG=")) {
			msg = part.replace("MSG=", "");
		} else if (part.startsWith("SCREEN=")) {
			screen = part.replace("SCREEN=", "");
			if ("".equals(screen)) {
				screen = null;
			}
		} else if (part.startsWith("SRC=")) {
			src = part.replace("SRC=", "");
		} else if (part.startsWith("LOCATION=")) {
			location = part.replace("LOCATION=", "");
		} else if (part.startsWith("HREF=")) {
			href = part.replace("HREF=", "");
			if ("".equals(href)) {
				href = null;
			}
		} else if (part.startsWith("CAL=")) {
			cal = part.replace("CAL=", "");
			if ("".equals(cal)) {
				cal = null;
			}
		} else {
			msg = part;
		}
		// logger.exiting();

	}

	public String toString() {
		// logger.entering();
		StringBuffer buff = new StringBuffer();
		buff.append("TYPE=");
		if (type != null) {
			buff.append(type);
		}
		buff.append("||MSG=");
		if (msg != null) {
			buff.append(msg);
		}
		buff.append("||SCREEN=");
		if (screen != null) {
			buff.append(screen);
		}

		buff.append("||SRC=");
		if (src != null) {
			buff.append(src);
		}
		buff.append("||LOCATION=");
		if (location != null) {
			buff.append(location);
		}
		buff.append("||HREF=");
		if (href != null) {
			buff.append(href);
		}
		buff.append("||CAL=");
		if (cal != null) {
			buff.append(cal);
		}	
		// logger.exiting(buff.toString());
		return buff.toString();
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public String getScreen() {
		return screen;
	}

	public String getScreenURL() {
		// logger.entering();
		String returnValue = "no screen";
		if (screen != null) {
			returnValue =  screen.replaceAll("\\\\", "/");
		}
		// logger.exiting(returnValue);
		return returnValue;
	}

	public void setScreen(String screen) {
		this.screen = screen;
	}

	public String getSrc() {
		return src;
	}

	public void setSrc(String src) {
		this.src = src;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getHref() {
		return href;
	}

	public void setHref(String href) {
		this.href = href;
	}
	
	public String getCal() {
		return cal;
	}

	public void setCal(String cal) {
		this.cal = cal;
	}

}
