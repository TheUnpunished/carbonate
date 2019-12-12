package ru.icmit.Yakovlev.Model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public abstract class Dict extends Entity{
    private String name;
    private String fullName;
    private String code;
    @Override
    public String toString(){
        return id + ": " + name + ", " + fullName + ", " + code;
    }
}
