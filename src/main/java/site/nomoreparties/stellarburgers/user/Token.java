package site.nomoreparties.stellarburgers.user;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Token {

    private String token;

}