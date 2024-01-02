package edu.uw.tcss450.team6_client.ui.chat.chatlist;

import android.graphics.drawable.Icon;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import edu.uw.tcss450.team6_client.R;
import edu.uw.tcss450.team6_client.databinding.FragmentChatCardBinding;

public class ChatListRecyclerViewAdapter extends RecyclerView.Adapter<ChatListRecyclerViewAdapter.ChatListViewHolder> {

    //Store all of the blogs to present
    private final List<ChatRoom> mChatrooms;

    public ChatListRecyclerViewAdapter(List<ChatRoom> items) {

        this.mChatrooms = items;
    }

    @Override
    public int getItemCount() {

        return mChatrooms.size();
    }

    @NonNull
    @Override
    public ChatListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ChatListViewHolder(LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.fragment_chat_card, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ChatListViewHolder holder, int position) {
        holder.setRoom(mChatrooms.get(position));
    }

    /**
     * Objects from this class represent an Individual row View from the List
     * of rows in the Blog Recycler View.
     */
    public class ChatListViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public FragmentChatCardBinding binding;

        public ChatListViewHolder(View view) {
            super(view);
            mView = view;
            binding = FragmentChatCardBinding.bind(view);
//            binding.buittonMore.setOnClickListener(this::handleMoreOrLess);
        }

//        /**
//         * When the button is clicked in the more state, expand the card to display * the blog preview and switch the icon to the less state. When the button * is clicked in the less state, shrink the card and switch the icon to the
//         * more state.
//         *
//         * @param button the button that was clicked
//         */
//        private void handleMoreOrLess(final View button) {
//            if (binding.textPreview.getVisibility() == View.GONE) {
//                binding.textPreview.setVisibility(View.VISIBLE);
//                binding.buittonMore.setImageIcon(
//                        Icon.createWithResource(mView.getContext(), R.drawable.ic_less_grey_24dp));
//            } else {
//                binding.textPreview.setVisibility(View.GONE);
//                binding.buittonMore.setImageIcon(
//                        Icon.createWithResource(mView.getContext(), R.drawable.ic_more_grey_24dp));
//            }
//        }

        void setRoom(final ChatRoom room) {
            binding.buttonGoMessage.setOnClickListener(view ->
                    Navigation.findNavController(mView).navigate(
                            ChatListFragmentDirections.actionNavigationChatListToNavigationChat(room)));

            binding.textChatroomName.setText(room.getRoomName());
            binding.textMember.setText(room.getMembers());

//            binding.textPreview.setText(room.getMessagePreview());
//            binding.textTimestamp.setText(room.getTimestamp());

//Use methods in the HTML class to format the HTML found in the text
//            final String preview = Html.fromHtml(
//                    blog.getTeaser(),
//                    Html.FROM_HTML_MODE_COMPACT).
//                    toString().substring(0, 100) + "...";
//            binding.textPreview.setText(preview);
        }


    }
}
