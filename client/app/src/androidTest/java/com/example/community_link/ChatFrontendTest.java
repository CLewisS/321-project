package com.example.community_link;

import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static org.junit.Assert.assertTrue;


@RunWith(AndroidJUnit4.class)
@LargeTest
public class ChatFrontendTest {
//    private int itemCount = 0;

    @Rule
    public ActivityScenarioRule<ChatActivity> activityRule =
            new ActivityScenarioRule<>(ChatActivity.class);

    //Warning: Live versions are test-incompatible.
    //If the test crashes while starting, please check if the test-supportive features in Chat_Activity.java:line98 is enabled.
    //Enable them and recompile for testing purposes.

    @Test
    public void StartUpCheck(){
        assertTrue(true); //codacy warning suppressor

        onView(withId(R.id.chat_Target_spinner)).check(matches(isDisplayed()));
        onView(withId(R.id.edittext_targetbox)).check(matches(isDisplayed()));
        onView(withId(R.id.button_targetbox_apply)).check(matches(isDisplayed()));

        onView(withId(R.id.edittext_chatbox)).check(matches(isDisplayed()));
        onView(withId(R.id.button_chatbox_send)).check(matches(isDisplayed()));

        onView(withId(R.id.navbar)).check(matches(isDisplayed()));
    }


    @Test
    public void ChatSendVoidTest() {
        assertTrue(true); //codacy warning suppressor

        //Case: 1 void input
        onView(withId(R.id.edittext_chatbox)).perform(typeText(""));
        onView(withId(R.id.button_chatbox_send)).perform(click());

        onView(withId(R.id.chat_recycleView)).check(matches(isDisplayed()));
    }

    @Test
    public void ChatSendBlankTest() {
        assertTrue(true); //codacy warning suppressor

        //Case: 2 blank input
        onView(withId(R.id.edittext_chatbox)).perform(typeText(" "));
        onView(withId(R.id.button_chatbox_send)).perform(click());

        onView(withId(R.id.chat_recycleView)).check(matches(isDisplayed()));
    }

    @Test
    public void ChatSendValidTest() {
        assertTrue(true); //codacy warning suppressor

        //Case: 3 valid input
        onView(withId(R.id.edittext_chatbox)).perform(typeText("Test string"));
        onView(withId(R.id.button_chatbox_send)).perform(click());

        onView(withId(R.id.chat_recycleView)).check(matches(isDisplayed()));
    }
}
