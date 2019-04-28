package io.renren.modules.sport.constants;


public interface SportConstants{
    /**
     * 年龄段
     */
    public enum AgeRangeEnum {

        AGE_35(1,"3-5"),AGE_68(2,"6-8"),AGE_911(3,"9-11");
        AgeRangeEnum(Integer age,String desc){
            this.age = age;
            this.desc = desc;
        }
        private Integer age;
        private String desc;

        public Integer getAge() {
            return age;
        }

        public void setAge(Integer age) {
            this.age = age;
        }

        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }}
}

