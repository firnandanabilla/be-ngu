package com.ngu.demo.model.entity;


import lombok.Data;

import javax.persistence.*;

@Entity
@Data
@Table(name = Datanya.TABLE_NAME)
public class Datanya {
    public static final String TABLE_NAME = "t_data";

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = TABLE_NAME)
    @SequenceGenerator(name = TABLE_NAME , sequenceName = "t_data_seq")
    private Integer id;
    private String title;
    private String location;
    private String participant;
    private String date;
    private String note;
    private String file;

}
