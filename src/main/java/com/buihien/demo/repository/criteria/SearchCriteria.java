package com.buihien.demo.repository.criteria;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SearchCriteria { //keyEntityJoin
    private String key;// firstName, lastName, ....
    private String operation;//=, <, > ..
    private Object value;//gia tri tim kiem
}
