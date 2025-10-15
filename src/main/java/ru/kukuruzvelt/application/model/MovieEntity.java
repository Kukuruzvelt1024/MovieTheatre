package ru.kukuruzvelt.application.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

@Entity
@Table(name = "public.movies")
@Getter
@Setter
@AllArgsConstructor
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@Builder


public class MovieEntity {

    @Column(name = "webmapping")
    @Id
    private String WebMapping;
    @Column(name = "videofilename")
    private String VideoFileName;
    @Column(name = "posterfilename")
    private String PosterFileName;
    @Column(name = "yearproduction")
    private Integer Year;
    @Column(name = "countries")
    private String[] Countries;
    @Column(name = "genres")
    private String[] Genres;
    @Column(name = "duration")
    private Integer Duration;
    @Column(name = "titlerussian")
    private String TitleRussian;
    @Column(name = "titleoriginal")
    private String TitleOriginal;
    @Column(name = "directors")
    private String[] Directors;

    private static MovieEntity nullEntity;

  public String getGenresAsString(){
      StringBuilder builder = new StringBuilder();
      for(int i = 0; i < this.Genres.length; i++){
          builder.append(this.Genres[i]);
          if(i < this.Genres.length - 1) builder.append(", ");
      }
      return builder.toString();
  }

  public String getCountriesAsString(){
      StringBuilder builder = new StringBuilder();
      for(int i = 0; i < this.Countries.length; i++){
          builder.append(this.Countries[i]);
          if(i < this.Countries.length - 1) builder.append(", ");
      }
      return builder.toString();
  }

  public String getDirectorsAsString(){
        StringBuilder builder = new StringBuilder();
        for(int i = 0; i < this.Directors.length; i++){
            builder.append(this.Directors[i]);
            if(i < this.Directors.length - 1) builder.append(", ");
        }
        return builder.toString();
    }

  public static MovieEntity nullEntity(){
      if (nullEntity == null) {
          MovieEntity me = MovieEntity
                  .builder()
                  .Duration(0)
                  .TitleRussian("Не Существует")
                  .TitleOriginal("Does Not Exist")
                  .VideoFileName("no name")
                  .PosterFileName("no name")
                  .Year(0)
                  .Genres(new String[1])
                  .Countries(new String[1])
                  .WebMapping("no name")
                  .Directors(new String[1])
                  .build();
          nullEntity = me;
          return me;
      }
      else{
          return nullEntity;
      }
  }


}
