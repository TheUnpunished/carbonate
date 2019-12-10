package Model;

import lombok.*;


@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class Contact extends Entity {
    private String fullName;
    private String lastName;
    private String firstName;
    private Boolean inBlackList;
    @Override
    public String toString(){
        return fullName;
    }

}
