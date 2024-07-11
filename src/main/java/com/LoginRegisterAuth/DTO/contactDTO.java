package com.LoginRegisterAuth.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class contactDTO {
    private int contactId;
    private String contactName;
    private String contactMob;
    private String contactEmail;
    private String contactDesc;
}
