package com.oscar.releasetool.app;

import java.util.List;

import org.junit.Test;

import com.oscar.releasetool.app.release.Release;
import com.oscar.releasetool.app.release.ReleaseItem;

public class ReleaseTest {
	@Test
	public void test() {
		Release release = Release.getInstance();
		List<ReleaseItem> itemsByPersonInCharge = release.getItemsByPersonInCharge("杜怀宇");
		for (ReleaseItem releaseItem : itemsByPersonInCharge) {
			System.out.println(releaseItem);
		}
	}
}
