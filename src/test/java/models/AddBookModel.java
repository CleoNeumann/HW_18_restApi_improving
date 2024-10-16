package models;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class AddBookModel {
    private String userId;
    private List<Isbn> collectionOfIsbns;

    public AddBookModel(String userId, String[] collectionOfIsbns) {
        this.userId = userId;
        this.collectionOfIsbns = new ArrayList<>();
        for (String i : collectionOfIsbns) {
            Isbn isbn = new Isbn();
            isbn.setIsbn(i);
            this.collectionOfIsbns.add(isbn);
        }
    }

    @Data
    public class Isbn {
        private String isbn;
    }

}

