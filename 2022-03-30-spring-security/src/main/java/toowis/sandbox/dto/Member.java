package toowis.sandbox.dto;

import java.io.Serializable;

import lombok.Data;

@Data
public class Member implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private String mbId;
    private String mbPassword;
    private String mbEmail;
    private String mbName;
    private String mbRole;
}
