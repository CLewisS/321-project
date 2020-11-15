package com.example.community_link;

import android.view.View;

import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;


import org.junit.FixMethodOrder;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.doesNotExist;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;


@RunWith(AndroidJUnit4.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@LargeTest
public class BrowseServiceTest {
    @Rule
    public ActivityScenarioRule<BrowseActivity> activityRule =
            new ActivityScenarioRule<>(BrowseActivity.class);


    @Test
    public void A_CheckButtonDisplay() {
        onView(withId(R.id.searchButt)).check(matches(isDisplayed()));
        onView(withId(R.id.suggestionsButt)).check(matches(isDisplayed()));
        onView(withId(R.id.filtersButt)).check(matches(isDisplayed()));
        onView(withId(R.id.serviceSearch)).check(matches(isDisplayed()));
    }


    @Test
    public void B_UseFilterToBrowseService(){
        onView(withId(R.id.serviceSearch)).perform(click());
        //onView(withId(R.id.serviceResultTitle)).check(matches(isDisplayed()));
    }

    @Test
    public void C_UseNameToBrowseService(){
        onView(withId(R.id.serviceSearch)).perform(typeText("Clothing Giveaway"));
        onView(withId(R.id.serviceSearch)).perform(click());
        //onView(withId(R.id.serviceResultTitle)).check(matches(isDisplayed()));
    }
}