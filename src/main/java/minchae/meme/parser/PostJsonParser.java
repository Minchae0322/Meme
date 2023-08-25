package minchae.meme.parser;

import lombok.AllArgsConstructor;
import minchae.meme.entity.Post;
import minchae.meme.entity.User;
import minchae.meme.request.PostCreate;
import org.json.JSONObject;


public class PostJsonParser {
    private String jsonData;

    private JSONObject jsonObject;

    public PostJsonParser(String jsonData) {
        this.jsonData = jsonData;
        jsonObject = new JSONObject(jsonData);
    }

    public PostCreate parse(User user) {
        return PostCreate.builder()
                .title(jsonObject.getString("title"))
                .content(jsonObject.getString("content"))
                .user(user)
                .build();
    }
}
