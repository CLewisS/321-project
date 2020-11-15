package com.example.community_link;

import android.view.View;

import androidx.recyclerview.widget.RecyclerView;
import androidx.test.espresso.contrib.RecyclerViewActions;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.doesNotExist;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.hasChildCount;
import static androidx.test.espresso.matcher.ViewMatchers.hasDescendant;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.core.AllOf.allOf;
import static org.junit.Assert.assertTrue;


@RunWith(AndroidJUnit4.class)
@LargeTest
public class ChatFrontendTest {
//    private int itemCount = 0;

    @Rule
    public ActivityScenarioRule<ChatActivity> activityRule =
            new ActivityScenarioRule<>(ChatActivity.class);

//    @Before
//    public void setUp(){
//
//    }
//
//    @After
//    public void verify(){
//
//    }


    @Test
    public void ChatSendVoidTest() {
        //Case: 1 void input
        onView(withId(R.id.edittext_chatbox)).perform(typeText(""));
        onView(withId(R.id.button_chatbox_send)).perform(click());

        //TODO: verify that nothing is sent
        assertTrue(true);
    }

    @Test
    public void ChatSendBlankTest() {
        //Case: 2 blank input
        onView(withId(R.id.edittext_chatbox)).perform(typeText(" "));
        onView(withId(R.id.button_chatbox_send)).perform(click());

        //TODO: verify that nothing is sent
        assertTrue(true);
    }

    @Test
    public void ChatSendValidTest() {
        //Case: 3 valid input
        onView(withId(R.id.edittext_chatbox)).perform(typeText("Test string"));
        onView(withId(R.id.button_chatbox_send)).perform(click());

        //TODO: verify that the Text is sent
        assertTrue(true);
    }
}
