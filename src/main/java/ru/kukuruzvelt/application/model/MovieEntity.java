package ru.kukuruzvelt.application.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

import java.util.Map;

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
    @Id
    private long ID;
    @Column(name = "webmapping")
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

  public String getGenresAsString(){
      StringBuilder builder = new StringBuilder();
      for(int i = 0; i < this.Genres.length; i++){
          if(i > 0) builder.append(",   ");
          builder.append(this.Genres[i]);
      }
      return builder.toString();
  }

  public String getCountriesAsString(){
      StringBuilder builder = new StringBuilder();
      for(int i = 0; i < this.Countries.length; i++){
          if(i > 0) builder.append(", ");
          builder.append(this.Countries[i]);
      }
      return builder.toString();
  }

  public boolean containsCountry(String countryRequired){
      for(int i = 0; i < Countries.length; i++){
          if (countryRequired.contentEquals(Countries[i])) return true;
      }
      return false;
  }

  public boolean containsGenre(String genreRequired){
      for(int i = 0; i < Genres.length; i++){
          if (genreRequired.contentEquals(Genres[i])) return true;
      }
      return false;
  }

  public boolean belongsDecade(int decade){
      return ((this.Year - decade <= 9) && (this.Year - decade >= 0));
  }

  public boolean matchesRequirement(Map<String, String> paramsMap){
      String genreRequired = paramsMap.get("genre") == null ? "null" : paramsMap.get("genre");
      String countryRequired = paramsMap.get("country") == null ? "null" :paramsMap.get("country");
      String searchRequest = paramsMap.get("search") == null ? "null" : paramsMap.get("search");
      String decadeRequired = paramsMap.get("decade") == null ? "null" : paramsMap.get("decade");
      if (    (this.containsGenre(genreRequired) || genreRequired.contentEquals("null") || genreRequired.contentEquals("all")) &
              (this.containsCountry(countryRequired) || countryRequired.contentEquals("null") || countryRequired.contentEquals("all")) &
              (this.getTitleRussian().contains(searchRequest) || searchRequest.contentEquals("null") || searchRequest.contentEquals("all")) &
              (this.belongsDecade(Integer.parseInt(decadeRequired)) || decadeRequired.contentEquals("null") || decadeRequired.contentEquals("all")))
          return true;
      return false;
  }

  class NullEntity{

  }


}
