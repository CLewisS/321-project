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
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.doesNotExist;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;


@RunWith(AndroidJUnit4.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@LargeTest
public class SignInTest {
    @Rule
    public ActivityScenarioRule<MainActivity> activityRule =
            new ActivityScenarioRule<>(MainActivity.class);

    @Test
    public void a_LoginWithNoEntry() {
        onView(withId(R.id.loginButtIntro)).perform(click());
        onView(withId(R.id.loginButt)).perform(click());
        onView(withId(R.id.userErrLogin)).check(matches(isDisplayed()));
        onView(withId(R.id.passErrLogin)).check(matches(isDisplayed()));
    }


    @Test
    public void b_LoginWithWrongEntry(){
        onView(withId(R.id.loginButtIntro)).perform(click());
        onView(withId(R.id.usernameLogin)).perform(typeText("AlexChen"));
        onView(withId(R.id.passwordLogin)).perform(typeText("Aha"));
        onView(withId(R.id.loginButt)).perform(click());
        onView(withId(R.id.loginButt)).check(matches(isDisplayed()));
    }

    @Test
    public void c_LoginWithCorrectEntry(){
        onView(withId(R.id.loginButtIntro)).perform(click());
        onView(withId(R.id.usernameLogin)).perform(typeText("AlexChen"));
        onView(withId(R.id.passwordLogin)).perform(typeText("111111"));
        onView(withId(R.id.loginButtIntro)).check(doesNotExist());
    }
}