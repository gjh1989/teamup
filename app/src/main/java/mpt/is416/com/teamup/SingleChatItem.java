package mpt.is416.com.teamup;

/**
 * Created by User on 8/10/2015.
 */
public class SingleChatItem {
    String groupChatTitle, groupChatSection;
    int chatImg;

    SingleChatItem(String groupChatTitle, String groupChatSection, int chatImg){
        this.groupChatSection = groupChatSection;
        this.groupChatTitle = groupChatTitle;
        this.chatImg = chatImg;
    }

}
