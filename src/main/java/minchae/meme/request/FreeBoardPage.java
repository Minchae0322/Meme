package minchae.meme.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FreeBoardPage extends Page{

    public FreeBoardPage(int page, int size) {
        super(page, size);
    }


}
