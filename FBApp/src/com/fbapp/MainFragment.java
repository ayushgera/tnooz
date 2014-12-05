package com.fbapp;

import java.util.Arrays;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.facebook.HttpMethod;
import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.facebook.widget.LoginButton;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class MainFragment extends Fragment {
	private static final String TAG = "MainFragment";
	private static final List<String> PERMISSIONS = Arrays.asList("user_likes");
	private UiLifecycleHelper uiHelper;
	private TextView userInfoTextView;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.activity_main, container, false);

		LoginButton authButton = (LoginButton) view
				.findViewById(R.id.authButton);
		authButton.setFragment(this);
		authButton.setReadPermissions(PERMISSIONS);
		userInfoTextView = (TextView) view.findViewById(R.id.userInfoTextView);
		return view;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		uiHelper = new UiLifecycleHelper(getActivity(), callback);
		uiHelper.onCreate(savedInstanceState);
	}

	private void onSessionStateChange(Session session, SessionState state,
			Exception exception) {
		if (state.isOpened()) {
			new Request(session, "/me/likes", null, HttpMethod.GET,
					new Request.Callback() {
						@Override
						public void onCompleted(Response response) {
							StringBuilder joinedString = buildCategories(response);
							userInfoTextView.setText(joinedString);

						}
					}).executeAsync();
		} else if (state.isClosed()) {
			Log.i(TAG, "Logged out...");
		}
	}

	private StringBuilder buildCategories(Response response) {
		JsonParser parser1 = new JsonParser();

		JsonObject element = (JsonObject) parser1.parse(response
				.getRawResponse().toString());
		JsonArray dataArray = element.get("data").getAsJsonArray();
		String[] myStringArray = new String[dataArray.size()];
		for (int i = 0; i < dataArray.size(); i++) {
			JsonObject element1 = dataArray.get(i).getAsJsonObject();
			myStringArray[i] = element1.get("category").getAsString();
		}

		StringBuilder joinedString = new StringBuilder();
		for (String value : myStringArray) {
			if (joinedString.length() > 0)
				joinedString.append(',');
			joinedString.append("'").append(value).append("'");
		}
		return joinedString;
	}

	private Session.StatusCallback callback = new Session.StatusCallback() {
		@Override
		public void call(Session session, SessionState state,
				Exception exception) {
			onSessionStateChange(session, state, exception);
		}
	};

	@Override
	public void onResume() {
		super.onResume();
		Session session = Session.getActiveSession();
		if (session != null && (session.isOpened() || session.isClosed())) {
			onSessionStateChange(session, session.getState(), null);
		}

		uiHelper.onResume();
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		// uiHelper.onActivityResult(requestCode, resultCode, data);
		if (Session.getActiveSession() != null) {
			Session.getActiveSession().onActivityResult(getActivity(),
					requestCode, resultCode, data);
		}

	}

	@Override
	public void onPause() {
		super.onPause();
		uiHelper.onPause();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		uiHelper.onDestroy();
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		uiHelper.onSaveInstanceState(outState);
	}
}
