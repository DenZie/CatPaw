package com.dd.test.claws.platform.html;

import org.testng.annotations.Test;

import com.dd.test.claws.platform.grid.Grid;
import com.dd.test.claws.platform.grid.WebTest;

public class ClawsHtmlElementsTest {
	
	private ButtonTest btn = new ButtonTest();
	private CheckBoxTest chk = new CheckBoxTest();
	//TODO Fix the failures with form test and then enable it
	//private FormTest form = new FormTest();
	private ImageTest image = new ImageTest();
	private LabelTest label = new LabelTest();
	private LinkTest link = new LinkTest();
	private RadioButtonTest radioBtn = new RadioButtonTest();
	private SelectListTest select = new SelectListTest();
	private TextFieldTest txt = new TextFieldTest();
	private TextAreaTest txtArea = new TextAreaTest();
	private TableTest tableTest = new TableTest();
	
	@Test(enabled=true)
	@WebTest
	public void runTestsOnHtmlElements(){
		Grid.driver().get(TestObjectRepository.URL.getValueToUse());
		btn.btnTestClick();
		btn.btnTestClickAndWaitNegativeTest();
		btn.btnTestClickonly();
		chk.chkboxTestCheck();
		chk.chkboxTestCheckAndWait();
		chk.chkboxTestClick();
		chk.chkboxTestClickAndWait();
		chk.chkboxTestIsEnabled();
		chk.chkboxTestUnCheck();
		chk.chkboxTestUnCheckAndWait();
		//form.testSubmit();
		image.imageTestClick();
		image.imageTestClickAndWait();
		image.imageTestGetHeight();
		image.imageTestGetWidth();
		label.testLabel();
		link.linkTestClick();
		link.linkTestClickAndWait();
		link.linkTestClickOnly();
		radioBtn.radioBtnTestCheck();
		radioBtn.radioBtnTestClick();
		radioBtn.radioBtnTestClickAndWait();
		radioBtn.radioBtnTestIsChecked();
		select.selectListTestSelecByIndex();
		select.selectListTestSelecByLabel();
		select.selectListTestSelectByValue();
		txt.txtFieldTestClearText();
		txt.txtFieldTestGetText();
		txt.txtFieldTestisEditable();
		txt.txtFieldTestTypeText();
		//Text Area
		txtArea.textAreaTestTypeText();
		txtArea.textAreaTestClearText();
		txtArea.textAreaTestGetText();
		txtArea.textAreaTestisEditable();
		txtArea.textAreaTestTypeTextNewLine();
		tableTest.tableTestGetRowCounts();
		tableTest.tableTestGetColumnCounts();
		tableTest.tableTestGetRowIndex();
		tableTest.tableTestGetRowText();
		tableTest.tableTestGetCellVlaue();
		tableTest.tableTestClickLink();
		tableTest.tableTestCheckCheckBox();
		tableTest.tableTestUnCheckCheckBox();
	}
}
