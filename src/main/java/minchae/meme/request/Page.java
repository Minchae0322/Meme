package minchae.meme.request;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
public class Page {
    int page;
    int size;

    public Page(int page, int size) {
        this.page = page;
        this.size = size;
    }
}
