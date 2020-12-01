package com.example.community_link;

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
import static androidx.test.espresso.action.ViewActions.scrollTo;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;


@RunWith(AndroidJUnit4.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@LargeTest
public class AddServiceTest {
    @Rule
    public ActivityScenarioRule<AddServiceActivity> activityRule =
            new ActivityScenarioRule<>(AddServiceActivity.class);

    @Test
    public void a_CheckExistence(){
        onView(withId(R.id.tvProjectName)).check(matches(isDisplayed()));
        onView(withId(R.id.etProjectName)).check(matches(isDisplayed()));
        onView(withId(R.id.tvType)).check(matches(isDisplayed()));
        onView(withId(R.id.type_spinner)).check(matches(isDisplayed()));
        onView(withId(R.id.tvDesc)).check(matches(isDisplayed()));
        onView(withId(R.id.etDesc)).check(matches(isDisplayed()));
        onView(withId(R.id.tvDate)).check(matches(isDisplayed()));
        onView(withId(R.id.datePicker)).check(matches(isDisplayed()));
        onView(withId(R.id.tvTime)).check(matches(isDisplayed()));
        onView(withId(R.id.hourAdd)).check(matches(isDisplayed()));
        onView(withId(R.id.minAdd)).check(matches(isDisplayed()));
        onView(withId(R.id.addServiceButt)).check(matches(isDisplayed()));
        onView(withId(R.id.cancelButt)).check(matches(isDisplayed()));
    }

    @Test
    public void b_NotEnoughInformation(){
        onView(withId(R.id.addServiceButt)).perform(scrollTo(), click());
        onView(withId(R.id.titleErr)).check(matches(isDisplayed()));
        onView(withId(R.id.descriptionErr)).check(matches(isDisplayed()));
        onView(withId(R.id.timeErr)).check(matches(isDisplayed()));
    }
}
