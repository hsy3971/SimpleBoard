package boardExample.simpleBoard.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum DeleteStatus {
    YES("Y"),
    NO("N");

    private final String value;
}
