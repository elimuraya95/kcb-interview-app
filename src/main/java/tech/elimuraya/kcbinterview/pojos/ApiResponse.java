package tech.elimuraya.kcbinterview.pojos;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * @author eli.muraya
 * @date 28/04/2025/04/2025
 */
@ToString
@Getter
@Setter
@NoArgsConstructor
public class ApiResponse<T> {
	private int code;
	private String message;
	private T data;
}
