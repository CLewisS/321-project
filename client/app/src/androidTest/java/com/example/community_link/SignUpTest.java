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
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;


@RunWith(AndroidJUnit4.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@LargeTest
public class SignUpTest {
    @Rule
    public ActivityScenarioRule<MainActivity> activityRule =
            new ActivityScenarioRule<>(MainActivity.class);

    @Test
    public void a_SignUpWithNoEntry() {
        onView(withId(R.id.signupButtIntro)).perform(click());
        onView(withId(R.id.menuSignupButt)).perform(click());
        onView(withId(R.id.userErrSignup)).check(matches(isDisplayed()));
    }


    @Test
    public void b_SignUpWithWrongEntry(){
        onView(withId(R.id.signupButtIntro)).perform(click());
        onView(withId(R.id.usernameSignup)).perform(typeText("AlexA11"));
        onView(withId(R.id.passwordSignup)).perform(typeText("111233"));
        onView(withId(R.id.passwordSignup2)).perform(typeText("233111"));
        onView(withId(R.id.menuSignupButt)).perform(click());
        onView(withId(R.id.passErrSignup)).check(matches(isDisplayed()));
    }
    /*
    @Test
    public void B_SignUpWithCorrectEntry(){
        onView(withId(R.id.signupButtIntro)).perform(click());
        onView(withId(R.id.usernameSignup)).perform(typeText("AlexA11"));
        onView(withId(R.id.passwordSignup)).perform(typeText("111233"));
        onView(withId(R.id.passwordSignup2)).perform(typeText("111233"));
        onView(withId(R.id.menuSignupButt)).perform(click());
        onView(withId(R.id.loginButtIntro)).check(doesNotExist());
    }*/
}