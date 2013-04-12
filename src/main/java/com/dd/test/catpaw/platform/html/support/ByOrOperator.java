package com.dd.test.catpaw.platform.html.support;

import org.openqa.selenium.By;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.NoSuchElementException;

import java.util.ArrayList;
import java.util.List;
//TODO: Add unit test.
public class ByOrOperator extends By {

	private List<By> bys;

	public ByOrOperator(List<By> bys) {
		this.bys = bys;
	}

	@Override
	public WebElement findElement(SearchContext context) {
		List<WebElement> elements = findElements(context);
		if (elements.size() == 0)
			throw new NoSuchElementException("Cannot locate an element using "
					+ toString());
		return elements.get(0);
	}

	@Override
	public List<WebElement> findElements(SearchContext context) {
		List<WebElement> result = null;
		for (By by : bys) {
			try {
				result = by.findElements(context);
				if(result!=null && result.size()!= 0){
					return result;
				}
			} catch (RuntimeException e) {
				// do nothing, lets check for the next element.
			}
		}
		return new ArrayList<WebElement>();
	}

	@Override
	public String toString() {
		StringBuilder stringBuilder = new StringBuilder("By.OrOperator(");
		stringBuilder.append("{");

		boolean first = true;
		for (By by : bys) {
			stringBuilder.append((first ? "" : ",")).append(by);
			first = false;
		}
		stringBuilder.append("})");
		return stringBuilder.toString();
	}
}
