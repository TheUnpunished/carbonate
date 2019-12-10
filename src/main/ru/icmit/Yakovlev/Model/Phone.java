package Model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class Phone extends Entity {

    private Contact contact;
    private PhoneType phoneType;
    private String phoneNumber;
    @Override
    public String toString(){
        return contact.toString();
    }
}
