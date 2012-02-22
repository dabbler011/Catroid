/**
 *  Catroid: An on-device graphical programming language for Android devices
 *  Copyright (C) 2010-2011 The Catroid Team
 *  (<http://code.google.com/p/catroid/wiki/Credits>)
 *  
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU Affero General Public License as
 *  published by the Free Software Foundation, either version 3 of the
 *  License, or (at your option) any later version.
 *  
 *  An additional term exception under section 7 of the GNU Affero
 *  General Public License, version 3, is available at
 *  http://www.catroid.org/catroid_license_additional_term
 *  
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU Affero General Public License for more details.
 *   
 *  You should have received a copy of the GNU Affero General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package at.tugraz.ist.catroid.uitest.ui;

import java.util.ArrayList;

import android.graphics.Bitmap;
import android.test.ActivityInstrumentationTestCase2;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import at.tugraz.ist.catroid.R;
import at.tugraz.ist.catroid.ui.CostumeActivity;
import at.tugraz.ist.catroid.ui.MainMenuActivity;
import at.tugraz.ist.catroid.ui.ScriptActivity;
import at.tugraz.ist.catroid.ui.ScriptTabActivity;
import at.tugraz.ist.catroid.ui.SoundActivity;
import at.tugraz.ist.catroid.uitest.util.UiTestUtils;
import at.tugraz.ist.catroid.utils.Utils;

import com.jayway.android.robotium.solo.Solo;

public class ScriptTabActivityTest extends ActivityInstrumentationTestCase2<ScriptTabActivity> {
	private Solo solo;

	public ScriptTabActivityTest() {
		super("at.tugraz.ist.catroid", ScriptTabActivity.class);
	}

	@Override
	public void setUp() throws Exception {
		super.setUp();
		UiTestUtils.createTestProject();
		solo = new Solo(getInstrumentation(), getActivity());
	}

	@Override
	public void tearDown() throws Exception {
		try {
			solo.finalize();
		} catch (Throwable e) {
			e.printStackTrace();
		}
		getActivity().finish();
		UiTestUtils.clearAllUtilTestProjects();
		super.tearDown();
	}

	public void testMainMenuButton() {
		UiTestUtils.clickOnLinearLayout(solo, R.id.btn_action_home);
		assertTrue("Clicking on main menu button did not cause main menu to be displayed",
				solo.getCurrentActivity() instanceof MainMenuActivity);
	}

	public void testScriptTab() {
		solo.clickOnText(solo.getCurrentActivity().getString(R.string.backgrounds));
		solo.clickOnText("Script");
		solo.sleep(100);
		assertTrue("Clicking on Script Tab did not cause ScriptActivity to be displayed",
				solo.getCurrentActivity() instanceof ScriptActivity);
	}

	public void testCostumesTab() {
		solo.clickOnText(solo.getCurrentActivity().getString(R.string.backgrounds));
		solo.sleep(100);
		assertTrue("Clicking on Costumes Tab did not cause CostumeActivity to be displayed",
				solo.getCurrentActivity() instanceof CostumeActivity);
	}

	public void testSoundsTab() {
		solo.clickOnText("Sounds");
		solo.sleep(100);
		assertTrue("Clicking on Sounds Tab did not cause SoundActivity to be displayed",
				solo.getCurrentActivity() instanceof SoundActivity);
	}

	public void testTabCostumeOrBackgroundLabel() {
		String spriteDog = "dog";
		String spriteBear = "bear";
		String spriteFrog = "frog";
		String spriteToTest = "";

		UiTestUtils.clickOnLinearLayout(solo, R.id.btn_action_home);
		solo.sleep(200);
		solo.clickOnText(getActivity().getString(R.string.current_project_button));
		addNewSprite(spriteDog);
		addNewSprite(spriteBear);
		addNewSprite(spriteFrog);

		ArrayList<ListView> listViews = solo.getCurrentListViews();
		ListView spriteList = listViews.get(0);
		spriteToTest = spriteList.getItemAtPosition(0).toString();
		solo.clickOnText(spriteToTest);
		solo.sleep(100);
		assertTrue("Wrong label - Tab should be named \"Backgrounds\"",
				solo.searchText(getActivity().getString(R.string.backgrounds)));
		solo.clickOnText(getActivity().getString(R.string.backgrounds));
		solo.sleep(100);
		assertTrue("Wrong label - Tab should be named \"Backgrounds\"",
				solo.searchText(getActivity().getString(R.string.backgrounds)));

		for (int i = 1; i < 3; i++) {
			solo.goBack();
			solo.sleep(100);
			spriteToTest = spriteList.getItemAtPosition(i).toString();
			solo.clickOnText(spriteToTest);
			solo.sleep(100);
			assertTrue("Wrong label - Tab should be named \"Costumes\"",
					solo.searchText(getActivity().getString(R.string.costumes)));
		}
	}

	public void testTabImagesAndLabelColor() {
		UiTestUtils.clickOnLinearLayout(solo, R.id.btn_action_home);
		solo.sleep(100);
		solo.clickOnText(getActivity().getString(R.string.current_project_button));
		solo.sleep(100);
		addNewSprite("Sprite1");
		solo.clickInList(0);
		solo.sleep(100);

		String scriptsLabel = getActivity().getString(R.string.scripts);
		String backgroundsLabel = getActivity().getString(R.string.backgrounds);
		String soundsLabel = getActivity().getString(R.string.sounds);
		String costumesLabel = getActivity().getString(R.string.costumes);

		String[] scriptsTabHexValues = { "ff1a1a1a", "ff090909", "ff2f2f2f", "ffe5e5e5", "fff6f6f6", "ffd0d0d0" };
		String[] backgroundsTabHexValues = { "ff101010", "ff222222", "ef101010", "ffefefef", "ffdddddd", "efefefef" };
		String[] soundsTabHexValues = { "ff141414", "ff2a2a2a", "bf282828", "ffebebeb", "ffd5d5d5", "bfd7d7d7" };
		String[] costumesTabHexValues = { "ff000000", "ff505050", "ff5d5d5d", "ffffffff", "ffafafaf", "ffa2a2a2" };

		int scriptsSelector = R.drawable.ic_tab_scripts_selector;
		int backgroundsSelector = R.drawable.ic_tab_background_selector;
		int soundsSelector = R.drawable.ic_tab_sounds_selector;
		int costumesSelector = R.drawable.ic_tab_costumes_selector;

		testTabText(new String[] { scriptsLabel, backgroundsLabel, soundsLabel });
		String[][] referenceHexValues = { scriptsTabHexValues, backgroundsTabHexValues, soundsTabHexValues };
		testTabIcons(new int[] { scriptsSelector, backgroundsSelector, soundsSelector }, referenceHexValues);

		solo.sleep(100);
		solo.clickOnText(getActivity().getString(R.string.backgrounds));
		testTabText(new String[] { backgroundsLabel, scriptsLabel, soundsLabel });
		referenceHexValues[0] = backgroundsTabHexValues;
		referenceHexValues[1] = scriptsTabHexValues;
		referenceHexValues[2] = soundsTabHexValues;
		testTabIcons(new int[] { backgroundsSelector, scriptsSelector, soundsSelector }, referenceHexValues);

		solo.sleep(100);
		solo.clickOnText(getActivity().getString(R.string.sounds));
		testTabText(new String[] { soundsLabel, scriptsLabel, backgroundsLabel });
		referenceHexValues[0] = soundsTabHexValues;
		referenceHexValues[1] = scriptsTabHexValues;
		referenceHexValues[2] = backgroundsTabHexValues;
		testTabIcons(new int[] { soundsSelector, scriptsSelector, backgroundsSelector }, referenceHexValues);

		solo.sleep(100);
		solo.goBack();
		solo.clickInList(2);
		testTabText(new String[] { scriptsLabel, costumesLabel, soundsLabel });
		referenceHexValues[0] = scriptsTabHexValues;
		referenceHexValues[1] = costumesTabHexValues;
		referenceHexValues[2] = soundsTabHexValues;
		testTabIcons(new int[] { scriptsSelector, costumesSelector, soundsSelector }, referenceHexValues);

		solo.sleep(100);
		solo.clickOnText(getActivity().getString(R.string.costumes));
		testTabText(new String[] { costumesLabel, scriptsLabel, soundsLabel });
		referenceHexValues[0] = costumesTabHexValues;
		referenceHexValues[1] = scriptsTabHexValues;
		referenceHexValues[2] = soundsTabHexValues;
		testTabIcons(new int[] { costumesSelector, scriptsSelector, soundsSelector }, referenceHexValues);
	}

	private void addNewSprite(String spriteName) {
		solo.sleep(300);
		UiTestUtils.clickOnLinearLayout(solo, R.id.btn_action_add_sprite);
		solo.sleep(200);
		solo.enterText(0, spriteName);
		solo.goBack();
		solo.clickOnButton(0);
		solo.sleep(100);
	}

	private void testTabText(String[] tabLabels) {
		TextView textViewToTest;
		String tabSelectedLabel;
		String textViewLabel;
		int textViewColor;
		int tabSelectedColor = getActivity().getResources().getColor(android.R.color.black);
		int colorNotSelected = getActivity().getResources().getColor(android.R.color.white);

		tabSelectedLabel = solo.getText(tabLabels[0]).getText().toString();

		for (int i = 0; i < tabLabels.length; i++) {
			textViewToTest = solo.getText(tabLabels[i]);
			textViewColor = textViewToTest.getCurrentTextColor();
			textViewLabel = textViewToTest.getText().toString();
			if (tabSelectedLabel.equals(textViewLabel)) {
				assertTrue(tabSelectedLabel + " Tab Active - " + textViewLabel + " Text should be black",
						textViewColor == tabSelectedColor);
			} else {
				assertTrue(tabSelectedLabel + " Tab Active - " + textViewLabel + " Text should be white",
						textViewColor == colorNotSelected);
			}
		}
	}

	private void testTabIcons(int[] tabIDs, String[][] referenceHexValues) {
		assertEquals("Wrong number of tabs - should be 3 tabs", 3, tabIDs.length);
		assertEquals("Wrong number of reference arrays - should be 3 arrays", 3, referenceHexValues.length);
		for (ImageView imageViewToTest : solo.getCurrentImageViews()) {
			if (imageViewToTest.getId() == R.id.tabsIcon) {
				boolean iconFound = false;
				assertEquals("Padding right of ImageView should be 5 dip",
						Utils.getPhysicalPixels(5, solo.getCurrentActivity().getBaseContext()),
						imageViewToTest.getPaddingRight());
				int iconTag = ((Integer) imageViewToTest.getTag()).intValue();
				if (iconTag == tabIDs[0]) {
					iconFound = true;
					testTabIconColor(imageViewToTest, iconTag, true, referenceHexValues[0]);
				} else if (iconTag == tabIDs[1]) {
					iconFound = true;
					testTabIconColor(imageViewToTest, iconTag, false, referenceHexValues[1]);
				} else if (iconTag == tabIDs[2]) {
					iconFound = true;
					testTabIconColor(imageViewToTest, iconTag, false, referenceHexValues[2]);
				}
				assertTrue("Icon not found", iconFound);
			}
		}
	}

	private void testTabIconColor(ImageView icon, int iconTag, boolean activeTab, String[] referenceHexValues) {
		assertEquals("Wrong amount of values to compare - should be 6", 6, referenceHexValues.length);
		int[] x = new int[3];
		int[] y = new int[3];
		if (iconTag == R.drawable.ic_tab_scripts_selector) {
			x[0] = 12;
			x[1] = 6;
			x[2] = 9;
			y[0] = 12;
			y[1] = 19;
			y[2] = 3;
		} else if (iconTag == R.drawable.ic_tab_background_selector) {
			x[0] = 15;
			x[1] = 5;
			x[2] = 5;
			y[0] = 15;
			y[1] = 7;
			y[2] = 15;
		} else if (iconTag == R.drawable.ic_tab_sounds_selector) {
			x[0] = 15;
			x[1] = 5;
			x[2] = 10;
			y[0] = 15;
			y[1] = 5;
			y[2] = 6;
		} else if (iconTag == R.drawable.ic_tab_costumes_selector) {
			x[0] = 7;
			x[1] = 4;
			x[2] = 23;
			y[0] = 21;
			y[1] = 5;
			y[2] = 8;
		}
		testPixelsOfTabIcon(icon, x, y, referenceHexValues, activeTab);
	}

	private void testPixelsOfTabIcon(ImageView icon, int[] x, int[] y, String[] expectedHexColors, boolean activeTab) {
		assertNotNull("Icon not found", icon);
		assertEquals("X Coord Array must contain 3 integers", 3, x.length);
		assertEquals("Y Coord Array must contain 3 integers", 3, y.length);
		assertEquals("There should be 6 values to compare", 6, expectedHexColors.length);

		Bitmap iconBitmap;
		int pixelColor;
		String[] pixelColorHex = new String[3];

		icon.buildDrawingCache();
		iconBitmap = icon.getDrawingCache();
		for (int i = 0; i < y.length; i++) {
			pixelColor = iconBitmap.getPixel(x[i], y[i]);
			pixelColorHex[i] = Integer.toHexString(pixelColor);
		}
		icon.destroyDrawingCache();

		for (int j = 0; j < pixelColorHex.length; j++) {
			if (activeTab) {
				assertEquals("Pixel of active tab icon has wrong color", expectedHexColors[j], pixelColorHex[j]);
			} else {
				assertEquals("Pixel of inactive tab icon has wrong color", expectedHexColors[j + 3], pixelColorHex[j]);
			}
		}
	}
}
