package edu.uw.tcss450.team6_client.ui.chat.chatadd;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.function.IntFunction;

import edu.uw.tcss450.team6_client.R;
import edu.uw.tcss450.team6_client.databinding.FragmentChatAddBinding;
import edu.uw.tcss450.team6_client.model.UserInfoViewModel;
import edu.uw.tcss450.team6_client.ui.chat.chatlist.ChatListViewModel;

/**
 * A simple {@link Fragment} subclass.
 */
public class ChatAddFragment extends Fragment {

    private FragmentChatAddBinding binding;
    private ChatAddViewModel mModel;
    private UserInfoViewModel mUserModel;

    public ChatAddFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ViewModelProvider provider = new ViewModelProvider(getActivity());
        mUserModel = provider.get(UserInfoViewModel.class);
        mModel = provider.get(ChatAddViewModel.class);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentChatAddBinding.inflate(inflater,container,false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.addChatRoomButton.setOnClickListener(this::attempAddRoom);

        binding.addMemberButton.setOnClickListener(this::addMember);

        //when we get the response back from the server, clear the edittext
        mModel.addResponseObserver(getViewLifecycleOwner(), add_chatroom_response -> {
            try {
                binding.testOutput.setText("Room " + add_chatroom_response.getString("chatID") + " is created");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        });

        mModel.addMemberResponseObserver(getViewLifecycleOwner(), add_member_response -> {
            try {
                binding.testOutput.setText(add_member_response.getString("success"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        });
    }

    private void attempAddRoom(View Button) {
        mModel.connectChatAdd(mUserModel.getmJwt(), binding.roomNameText.getText().toString());
    }

    private void addMember(View Button) {
        mModel.connectMemberAdd(mUserModel.getmJwt(), Integer.parseInt(binding.chatIdText.getText().toString()) , binding.memberEmailText.getText().toString());
    }
}
