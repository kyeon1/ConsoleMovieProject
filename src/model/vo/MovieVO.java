package model.vo;

import java.time.LocalDate;

/**
 * MovieVO 클래스
 * @author indi
 *
 */
public class MovieVO {
	private int movieNum;//영화 PK
	private String movieTitle;//영화 이름
	private LocalDate movieDate;//영화 개봉 날짜
	private String movieContent;//영화 줄거리
	private double movieRate;//영화 평점 (java 저장객체)
	private String searchTitle;//영화 검색 (java 저장객체)
	public int getMovieNum() {
		return movieNum;
	}
	public void setMovieNum(int movieNum) {
		this.movieNum = movieNum;
	}
	public String getMovieTitle() {
		return movieTitle;
	}
	public void setMovieTitle(String movieTitle) {
		this.movieTitle = movieTitle;
	}
	public LocalDate getMovieDate() {
		return movieDate;
	}
	public void setMovieDate(LocalDate movieDate) {
		this.movieDate = movieDate;
	}
	public String getMovieContent() {
		return movieContent;
	}
	public void setMovieContent(String movieContent) {
		this.movieContent = movieContent;
	}
	public double getMovieRate() {
		return movieRate;
	}
	public void setMovieRate(double movieRate) {
		this.movieRate = movieRate;
	}
	public String getSearchTitle() {
		return searchTitle;
	}
	public void setSearchTitle(String searchTitle) {
		this.searchTitle = searchTitle;
	}
	@Override
	public String toString() {
		return "MovieVO [movieNum=" + movieNum + ", movieTitle=" + movieTitle + ", movieDate=" + movieDate
				+ ", movieContent=" + movieContent + ", movieRate=" + movieRate + ", searchTitle=" + searchTitle + "]";
	}
	
}
