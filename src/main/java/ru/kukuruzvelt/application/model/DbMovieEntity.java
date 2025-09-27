package ru.kukuruzvelt.application.model;

import jakarta.persistence.*;

@Entity
@Table(name = "Movies")
public class DbMovieEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id")
    private int ID;
    @Column(name = "WebMapping")
    private String WebMapping;
    @Column(name = "VideoFileName")
    private String FilePath;
    @Column(name = "PosterFileName")
    private String PosterPath;
    @Column(name = "Year")
    private Integer Year;
    @Column(name = "Countries")
    private String[] Countries;
    @Column(name = "Genres")
    private String[] Genres;
    @Column(name = "Duration")
    private Integer Duration;
    @Column(name = "RussianTitle")
    private String TitleRussian;
    @Column(name = "EnglishTitle")
    private String TitleOriginal;
}
