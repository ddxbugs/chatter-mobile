package edu.uw.tcss450.team6_client.ui.chat.chatlist;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import edu.uw.tcss450.team6_client.R;
import edu.uw.tcss450.team6_client.databinding.FragmentChatListBinding;
import edu.uw.tcss450.team6_client.model.UserInfoViewModel;

/**
 * A simple {@link Fragment} subclass.
 */
public class ChatListFragment extends Fragment {

    private ChatListViewModel mModel;
    private UserInfoViewModel mUserModel;
    public ChatListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ViewModelProvider provider = new ViewModelProvider(getActivity());
        mUserModel = provider.get(UserInfoViewModel.class);
        mModel = provider.get(ChatListViewModel.class);
        mModel.getChatRooms(mUserModel.getmJwt(), mUserModel.getEmail());

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_chat_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Render list
        FragmentChatListBinding chat_list_binding = FragmentChatListBinding.bind(getView());
        mModel.addChatListObserver(getViewLifecycleOwner(), chatList -> {
            if (!chatList.isEmpty()) {
                chat_list_binding.listRoot.setAdapter(new ChatListRecyclerViewAdapter(chatList)
                );

                chat_list_binding.layoutWait.setVisibility(View.GONE);
            }
        });

        chat_list_binding.addChatButton.setOnClickListener(button ->
                Navigation.findNavController(getView()).navigate(ChatListFragmentDirections.actionNavigationChatListToChatAddFragment()
                ));

    }
}
