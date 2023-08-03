package minchae.meme.request;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
public class FreePostPage extends Page{

    public FreePostPage(int page, int size) {
        super(page, size);
    }


}
