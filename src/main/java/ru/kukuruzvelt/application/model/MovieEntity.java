package ru.kukuruzvelt.application.model;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@Builder


public class MovieEntity {

    private String WebMapping;
    private String FilePath;
    private String PosterFileName;
    private Integer Year;
    private String[] Countries;
    private String[] Genres;
    private Integer Duration;
    private String TitleRussian;
    private String TitleOriginal;

  public String getGenresAsString(){
      StringBuilder builder = new StringBuilder();
      for(int i = 0; i < this.Genres.length; i++){
          if(i > 0) builder.append(", ");
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


}
