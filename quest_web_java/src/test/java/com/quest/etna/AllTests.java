package com.quest.etna;

import org.junit.platform.runner.JUnitPlatform;
import org.junit.platform.suite.api.SelectClasses;
import org.junit.runner.RunWith;

@SuppressWarnings("deprecation")
@RunWith(JUnitPlatform.class)
@SelectClasses({UserTests.class, CommentTests.class, CharacterTests.class, VideoTest.class})

public class AllTests {
	
}