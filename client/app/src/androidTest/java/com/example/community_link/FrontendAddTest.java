package com.example.community_link;

import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.doesNotExist;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class FrontendAddTest {

    @Rule
    public ActivityScenarioRule<MainActivity> activityRule =
            new ActivityScenarioRule<>(MainActivity.class);

    @Test
    public void mainUIChatEntryButtonTest() {
        onView(withId(R.id.browseServiceButt)).perform(click());
        onView(withId(R.id.browseServiceButt)).check(doesNotExist());
    }

}