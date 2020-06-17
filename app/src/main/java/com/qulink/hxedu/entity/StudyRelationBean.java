package com.qulink.hxedu.entity;

import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;


@NoArgsConstructor
@Data
public class StudyRelationBean {

    /**
     * invitationCode : Z6mcMO
     * seniorName : kkkppp
     * seniorPhone : 15209237027
     * juniorNums : 3
     * list : [{"juniorNums":0,"sex":"男","name":"泽元","id":20,"status":1},{"juniorNums":1,"sex":"男","name":"456","id":1,"status":1}]
     */

    private String invitationCode;
    private String seniorName;
    private String seniorPhone;
    private int juniorNums;
    private List<ListBean> list;

    @NoArgsConstructor
    @Data
    public static class ListBean {
        /**
         * juniorNums : 0
         * sex : 男
         * name : 泽元
         * id : 20
         * status : 1
         */

        private int juniorNums;
        private String sex;
        private String name;
        private int id;
        private int status;
    }
}
