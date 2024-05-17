package beside2.ten039.common;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@ToString
public class ApiResponses {
    private String msg;
    private Object data;

}
