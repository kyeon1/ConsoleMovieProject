package view;

import service.MovieService;

/**
 * 시작하는 클래스
 * @author indi
 *
 */
public class Client {
	
	/**
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		MovieService movieService=new MovieService();
		movieService.start();
	}
}